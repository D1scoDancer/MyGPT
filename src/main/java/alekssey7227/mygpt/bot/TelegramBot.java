package alekssey7227.mygpt.bot;

import alekssey7227.mygpt.openai.OpenAIConfig;
import alekssey7227.mygpt.openai.OpenAIHttpPostConstructor;
import alekssey7227.mygpt.openai.OpenAIMessageConstructor;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

    OpenAIHttpPostConstructor httpPostConstructor;
    OpenAIMessageConstructor messageConstructor;


    public TelegramBot(OpenAIConfig openAIConfig, BotConfig botConfig,
                       OpenAIHttpPostConstructor httpPostConstructor,
                       OpenAIMessageConstructor messageConstructor) {
        super(botConfig.getBotToken());
        this.openAIConfig = openAIConfig;
        this.botConfig = botConfig;
        this.httpPostConstructor = httpPostConstructor;
        this.messageConstructor = messageConstructor;
        initializeCommands();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message.hasText()) {
            newChatCommanHandler(message);
        }
    }

    private void newChatCommanHandler(Message message) {
        String response = null;
        try {
            response = httpPostConstructor.sendHttpPostMessage("gpt-3.5-turbo", message.getText());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        sendMessage(response, message.getChatId());
    }

    private void sendMessage(String text, Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        executeCommand(message);
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
