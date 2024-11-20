package alekssey7227.mygpt.openai;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author Aleksey Shulikov
 */
@Component
public class OpenAIMessageConstructor {

    private static final String MODEL_PARAM = "model";

    private static final String MESSAGES_PARAM = "messages";

    private static final String ROLE_PARAM = "role";

    private static final String CONTENT_PARAM = "content";

    private static final String ROLE = "user"; // roles

    public String makeMessage(String model, String content) throws JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put(MODEL_PARAM, model);

        ArrayNode messagesNode = mapper.createArrayNode();
        ObjectNode messageNode = mapper.createObjectNode();
        messageNode.put(ROLE_PARAM, ROLE);

        messageNode.put(CONTENT_PARAM, content);

        messagesNode.add(messageNode);
        rootNode.set(MESSAGES_PARAM, messagesNode);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
}
