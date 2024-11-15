package alekssey7227.mygpt.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

/**
 * @author Aleksey Shulikov
 */
class OpenAIMessageConstructorTest {


    @Test
    void makeMessage() {
        String result = null;
        try {
            result = OpenAIMessageConstructor.makeMessage("gpt-4o", "write a haiku about ai");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // assert equals
    }
}