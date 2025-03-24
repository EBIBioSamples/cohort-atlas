package uk.ac.ebi.biosamples.cohortatlas.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
  @Id
  private String id;
  @Indexed(unique = true)
  private String username;

  @JsonIgnore
  private String password;
  @Builder.Default
  private boolean accountNonExpired = true;
  @Builder.Default
  private boolean accountNonLocked = true;
  @Builder.Default
  private boolean credentialsNonExpired = true;
  @Builder.Default
  private boolean enabled = true;

  @JsonDeserialize(using = GrantedAuthorityDeserializer.class)
  private Collection<? extends GrantedAuthority> authorities;

  private String firstName;
  private String lastName;
  @CreatedDate
  private LocalDateTime createdDate;
  @LastModifiedDate
  private LocalDateTime lastModifiedDate;
  @CreatedBy
  private String createdBy;
  @LastModifiedBy
  private String lastModifiedBy;


  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }



}
