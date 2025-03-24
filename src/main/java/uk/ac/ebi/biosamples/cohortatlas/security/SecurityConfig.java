package uk.ac.ebi.biosamples.cohortatlas.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final UserService userService;

  @Bean
  protected SecurityFilterChain configure(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    String basePath = "";
    http
        .authorizeHttpRequests(authorize -> authorize
            // checklists
//            .requestMatchers(HttpMethod.POST, "/**").hasAuthority("editor")
            .requestMatchers(HttpMethod.GET, basePath + "/users/search/me").authenticated()
            .requestMatchers(HttpMethod.GET, basePath + "/users/search").authenticated()
            .requestMatchers(HttpMethod.GET, basePath + "/users/**").hasAuthority("admin")
            .requestMatchers(HttpMethod.POST, basePath + "/users/**").hasAuthority("admin")
            .requestMatchers(HttpMethod.PUT, basePath + "/users/**").hasAuthority("admin")
            .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()

            .requestMatchers(HttpMethod.GET, "/**").permitAll()
            .requestMatchers( "/**").permitAll()

            .anyRequest().authenticated()
        )
        .addFilter(requestHeaderAuthenticationFilter(authenticationManager))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
    ;
    return http.build();
  }

  private RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter(AuthenticationManager authenticationManager) {
    RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
    filter.setPrincipalRequestHeader("Authorization");
    filter.setAuthenticationManager(authenticationManager);
    filter.setCheckForPrincipalChanges(true);
    filter.setExceptionIfHeaderMissing(false);
    filter.setAuthenticationSuccessHandler(
        (request, response, authentication) -> userService.updateUser((UserDetails) authentication.getPrincipal()));
    return filter;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationManagerBuilder auth, AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> uds) {
    PreAuthenticatedAuthenticationProvider authProvider = new PreAuthenticatedAuthenticationProvider();
    authProvider.setPreAuthenticatedUserDetailsService(uds);
    auth.authenticationProvider(authProvider);
    return auth.getOrBuild();
  }
}
