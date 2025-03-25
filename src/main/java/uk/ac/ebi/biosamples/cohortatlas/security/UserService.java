package uk.ac.ebi.biosamples.cohortatlas.security;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsManager {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Could not find user: " + username));
  }

  @Override
  public void createUser(UserDetails user) {
    userRepository.insert((User) user);
  }

  public synchronized void createUserIfNotExists(UserDetails user) {
    if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
      createUser(user);
    }
  }

  @Override
  public synchronized void updateUser(UserDetails user) {
    User updatedUser = userRepository.findByUsername(user.getUsername())
        .map(dbUser -> {
          ((User) user).setId(dbUser.getId());
          return (User) user;
        }).orElse((User) user);
    userRepository.save(updatedUser);
  }

  @Override
  public void deleteUser(String username) {
    throw new InvalidDataAccessApiUsageException("Not supported: Use webin to manage users");
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    throw new InvalidDataAccessApiUsageException("Not supported: Use webin to manage users");
  }

  @Override
  public boolean userExists(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  public Collection<? extends GrantedAuthority> getGrantedAuthorities(String submissionAccountId) {
    try {
      return loadUserByUsername(submissionAccountId).getAuthorities();
    } catch (AuthenticationCredentialsNotFoundException e) {
      return List.of(new SimpleGrantedAuthority(Roles.USER.name()));
    }
  }

  public User findCurrentUser() {
    return userRepository.findCurrentUser();
  }
}
