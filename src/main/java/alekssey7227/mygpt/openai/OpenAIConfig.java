package alekssey7227.mygpt.openai;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aleksey Shulikov
 */
@Getter
@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    private String openAIApiKey;

}
