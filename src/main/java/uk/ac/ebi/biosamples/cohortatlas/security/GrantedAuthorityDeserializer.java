package uk.ac.ebi.biosamples.cohortatlas.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class GrantedAuthorityDeserializer extends JsonDeserializer<Collection<GrantedAuthority>> {

  @Override
  public Collection<GrantedAuthority> deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    ArrayNode node = parser.getCodec().readTree(parser);
    Collection<GrantedAuthority> authorities = new ArrayList<>();

    for (int i = 0; i < node.size(); i++) {
      ObjectNode authorityNode = (ObjectNode) node.get(i);
      String authority = authorityNode.get("authority").asText();
      authorities.add(new SimpleGrantedAuthority(authority));
    }

    return authorities;
  }
}
