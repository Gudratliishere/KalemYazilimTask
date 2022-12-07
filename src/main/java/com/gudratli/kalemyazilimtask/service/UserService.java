package com.gudratli.kalemyazilimtask.service;

import com.gudratli.kalemyazilimtask.entity.User;
import com.gudratli.kalemyazilimtask.exception.DuplicateException;

public interface UserService
{
    User addUser (User user) throws DuplicateException;

    User getById (Long id);

    User getByUsername (String username);

    User getByEmail (String email);
}
