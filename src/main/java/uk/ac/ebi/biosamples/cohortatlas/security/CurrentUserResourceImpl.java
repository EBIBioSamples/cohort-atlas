package uk.ac.ebi.biosamples.cohortatlas.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserResourceImpl implements CurrentUserResource {

  @Override
  public User findCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof User) {
      return (User) authentication.getPrincipal();
    }
    throw new IllegalStateException("No authenticated user found.");
  }
}
