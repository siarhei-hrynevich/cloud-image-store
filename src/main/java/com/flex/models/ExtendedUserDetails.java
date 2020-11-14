package com.flex.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class ExtendedUserDetails extends User {
    private Long id;
    public ExtendedUserDetails(Long id, String username, String password, Collection<String> authorities) {
        super(username,
                password,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        this.id = id;
    }

    public ExtendedUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
