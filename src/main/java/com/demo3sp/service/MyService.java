package com.demo3sp.service;

import com.demo3sp.entiy.Role;
import com.demo3sp.entiy.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MyService implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    private final User user;
    Set<Role> role = null;
    Set<SimpleGrantedAuthority> authorities;

    public MyService(User user) {
        this.user = user;
        role = user.getRoles();
        authorities = Collections.singleton(new SimpleGrantedAuthority(role.toString()));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        return Collections.singleton(new SimpleGrantedAuthority(roles.toString()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

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
        return user.isEnabled();
    }
}
