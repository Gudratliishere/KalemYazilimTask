package com.gudratli.kalemyazilimtask.service.impl;

import com.gudratli.kalemyazilimtask.entity.User;
import com.gudratli.kalemyazilimtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException
    {
        User user = userService.getByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(("User doesn't exist with this username."));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }
}
