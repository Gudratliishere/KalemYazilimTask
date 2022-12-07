package com.gudratli.kalemyazilimtask.service.impl;

import com.gudratli.kalemyazilimtask.entity.User;
import com.gudratli.kalemyazilimtask.exception.DuplicateException;
import com.gudratli.kalemyazilimtask.repository.UserRepository;
import com.gudratli.kalemyazilimtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public User addUser (User user) throws DuplicateException
    {
        checkForDuplicate(user);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User getById (Long id)
    {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByUsername (String username)
    {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getByEmail (String email)
    {
        return userRepository.findByEmail(email);
    }

    private void checkForDuplicate (User user) throws DuplicateException
    {
        if (getByUsername(user.getUsername()) != null)
            throw new DuplicateException("Username is already taken.");
        if (getByEmail(user.getEmail()) != null)
            throw new DuplicateException("Email is already taken.");
    }
}

