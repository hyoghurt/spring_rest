package com.example.spring_boot.services;

import com.example.spring_boot.exceptions.EntityNotFoundException;
import com.example.spring_boot.mappers.UserMapper;
import com.example.spring_boot.models.MyUserDetails;
import com.example.spring_boot.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService, UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserDetailsService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getByNameUser(username);
        if (user == null) {
            throw new EntityNotFoundException("");
        }
        return new MyUserDetails(user);
    }

    @Override
    public void addUser(User user) {
        user.setRoles("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.saveUser(user);
    }
}
