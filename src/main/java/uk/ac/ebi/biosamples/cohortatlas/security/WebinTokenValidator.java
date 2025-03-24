package uk.ac.ebi.biosamples.cohortatlas.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.biosamples.cohortatlas.config.CohortAtlasProperties;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WebinTokenValidator implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
  final RestTemplate restTemplate;
  final CohortAtlasProperties cohortAtlasProperties;
  final ObjectMapper objectMapper;
  final UserService userService;


  @Override
  public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
    String tokenString = (String) token.getPrincipal();
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.set(HttpHeaders.AUTHORIZATION, tokenString);
      HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
      String validationUrl = cohortAtlasProperties.getWebinUrl() + "/admin/submission-account";

      ResponseEntity<String> authResponse =
          restTemplate.exchange(new URI(validationUrl), HttpMethod.GET, requestEntity, String.class);
      if (authResponse.getStatusCode().is2xxSuccessful()) {
        JsonNode accountInfo = objectMapper.readTree(authResponse.getBody());
        DocumentContext accountJsonDoc = JsonPath.parse(authResponse.getBody());
        List<String> firstName = accountJsonDoc.read("$.submissionContacts[?(@.mainContact==true)].firstName");
        List<String> lastName = accountJsonDoc.read("$.submissionContacts[?(@.mainContact==true)].surname");

        String submissionAccountId = accountInfo.get("submissionAccountId").asText();
        Collection<? extends GrantedAuthority> authorities = userService.getGrantedAuthorities(submissionAccountId);
        return User.builder()
            .username(submissionAccountId)
            .password("N/A")
            .enabled("N".equals(accountInfo.get("suspended").asText()))
            .accountNonLocked("N".equals(accountInfo.get("suspended").asText()))
            .accountNonExpired("N".equals(accountInfo.get("suspended").asText()))
            .authorities(authorities)
            .firstName(firstName.get(0))
            .lastName(lastName.get(0))
            .build();
      } else {
        throw new BadCredentialsException("Invalid token: Auth response status: " + authResponse.getStatusCode());
      }
    } catch (Exception ex) {
      throw new BadCredentialsException("Invalid token: Error during validation: " + ex.getMessage(), ex);
    }
  }
}
