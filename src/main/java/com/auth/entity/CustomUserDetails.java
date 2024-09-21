package com.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String email;

    private String authorities;

    public String getRawAuthorities()
    {
        return this.authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Arrays.stream(authorities.split(","))
                .sorted()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        //    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        //      String[] rawAuthorities = authorities.split(",");
        //      for(String rawAuthority: rawAuthorities)
        //    {
        //      grantedAuthorities.add(new SimpleGrantedAuthority(rawAuthority));
        //   }

        //    return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
