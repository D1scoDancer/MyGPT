package alekssey7227.mygpt.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Aleksey Shulikov
 */
@Component
@Slf4j
public class OpenAIHttpPostConstructor {

    // this will not always be constant, depends on type of request
    private static final String URL = "https://api.openai.com/v1/chat/completions";
    private static final String CONTENT_TYPE_HDR_PARAM = "Content-Type";
    private static final String AUTHORIZATION_HDR_PARAM = "Authorization";
    private static final String CONTENT_TYPE = "application/json";
    private static final String AUTHORIZATION = "Bearer ";

    private final OpenAIConfig openAIConfig;
    private final OpenAIMessageConstructor messageConstructor;

    public OpenAIHttpPostConstructor(OpenAIConfig openAIConfig, OpenAIMessageConstructor messageConstructor) {
        this.openAIConfig = openAIConfig;
        this.messageConstructor = messageConstructor;
    }

    public String sendHttpPostMessage(String model, String content) throws JsonProcessingException {
        String responseBody = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(URL);

            post.setHeader(CONTENT_TYPE_HDR_PARAM, CONTENT_TYPE);
            post.setHeader(AUTHORIZATION_HDR_PARAM, AUTHORIZATION + openAIConfig.getOpenAIApiKey());

            String json = messageConstructor.makeMessage(model, content);

            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            responseBody = EntityUtils.toString(response.getEntity());

            log.info(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }
}
