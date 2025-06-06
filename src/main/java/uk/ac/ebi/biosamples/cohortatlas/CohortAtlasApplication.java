package uk.ac.ebi.biosamples.cohortatlas;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.ac.ebi.biosamples.cohortatlas.cohort.Cohort;
import uk.ac.ebi.biosamples.cohortatlas.utils.NoSplitStringConverter;

//@SpringBootApplication
//@EnableWebSecurity
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class}
)
public class CohortAtlasApplication {

  public static void main(String[] args) {
    SpringApplication.run(CohortAtlasApplication.class, args);
  }

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean
  public WebMvcConfigurer configureWebMvC() {
    return new WebMvcConfigurer() {
      // enable cors for development
      @Override
      public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
      }

      @Override
      public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
        configurer.addPathPrefix("api", HandlerTypePredicate.forAnnotation(RestController.class));
      }

      @Override
      public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new NoSplitStringConverter());
      }
    };
  }

  @Bean
  public RepositoryRestConfigurer repositoryRestConfigurer() {

    return new RepositoryRestConfigurer() {

      @Override
      public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Cohort.class);
      }
    };
  }

  // initially db data loading for development
  @Bean
  public Jackson2RepositoryPopulatorFactoryBean populateRepoWithTestData() {
    Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
    factory.setMapper(objectMapper());
    factory.setResources(new Resource[]{new ClassPathResource("env/init_db_schema.json")});
    return factory;
  }

//  @Order(1)
//  @Bean
//  public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
//    http.authorizeHttpRequests()
//        .requestMatchers(HttpMethod.POST).authenticated()
//        .requestMatchers(HttpMethod.PUT).authenticated()
//        .requestMatchers("/login").authenticated()
//        .anyRequest().permitAll();
//
//    http.oauth2Login()
//        .and()
//        .logout()
////        .addLogoutHandler(keycloakLogoutHandler)
//        .logoutSuccessUrl("/");
//
////    http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
//
//    http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
//
//    http.sessionManagement()
//        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//    return http.build();
//  }

}
