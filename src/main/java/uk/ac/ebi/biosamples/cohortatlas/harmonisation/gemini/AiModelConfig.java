package uk.ac.ebi.biosamples.cohortatlas.harmonisation.gemini;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiModelConfig {
  @Value("${langchain4j.google-ai.api-key}")
  private String GEMINI_API_KEY;
  @Value("${langchain4j.google-ai.model-name:gemini-2.0-flash}")
  private String GEMINI_MODEL;

  @Bean
  public ChatLanguageModel chatLanguageModel() {
    return GoogleAiGeminiChatModel.builder()
        .apiKey(GEMINI_API_KEY)
        .modelName(GEMINI_MODEL)
        .build();
  }
}
