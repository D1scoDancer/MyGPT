package alekssey7227.mygpt.bot;

import alekssey7227.mygpt.openai.OpenAIConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aleksey Shulikov
 */
@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    OpenAIConfig openAIConfig;
    BotConfig botConfig;

    public TelegramBot(OpenAIConfig openAIConfig, BotConfig botConfig) {
        super(botConfig.getBotToken());
        this.openAIConfig = openAIConfig;
        this.botConfig = botConfig;
        initializeCommands();
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message.hasText()) {
            switch (message.getText()) {
                case "/newchat":
                    newChatCommanHandler(message);
                    break;
                default:
                    break;
            }
        }
    }

    private void newChatCommanHandler(Message message) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String url = "https://api.openai.com/v1/chat/completions";
            HttpPost post = new HttpPost(url);

            // Set headers
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + openAIConfig.getOpenAIApiKey()); // Replace with your API key

            // Create JSON body
            String json = "{\n" +
                    "    \"model\": \"gpt-4o\",\n" +
                    "    \"messages\": [\n" +
                    "        {\"role\": \"user\", \"content\": \"write a haiku about ai\"}\n" +
                    "    ]\n" +
                    "}";

            // Set the request body
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);

            // Execute the request
            HttpResponse response = client.execute(post);
            String responseBody = EntityUtils.toString(response.getEntity());

            // Print the response
            System.out.println(responseBody);
            log.info(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCommands() {
        List<BotCommand> commands = List.of(
                new BotCommand("/newchat", "Новый чат"),
                new BotCommand("/chmod", "Выбрать модель"),
                new BotCommand("/help", "Руководство пользователя")
        );
        executeCommand(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
    }

    private void executeCommand(BotApiMethod<? extends Serializable> command) {
        try {
            execute(command);
        } catch (TelegramApiException e) {
            log.error("Ошибка при выполнении команды: {}", e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }
}
