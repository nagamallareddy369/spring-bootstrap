package com.boostrap.sample;

import com.boostrap.sample.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * AUTHOR: 819521
 * DATE  : 2015/12/28
 * TIME  : 15:16
 */

public class UserService {
    private static UserService userService = new UserService();

    private UserService() {

    }
    public static UserService getInstance() {
        return userService;
    }
    private List<User> users = new ArrayList<User>(){{
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setId(i + "");
            user.setUsername("username_" + i);
            user.setPassword("password_" + i);
            user.setEmail("email_" + i);
            user.setPhone("phone_" + i);
            user.setAddress("address_" + i);
            add(user);
        }
    }};

    public List<User> listUser() {
        return users;
    }

    public User findById(String id) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (StringUtils.equals(id, user.getId())) {
                return user;
            }
        }
        return null;
    }

    public User updateUser(User user) {
        User editUser = findById(user.getId());
        BeanUtils.copyProperties(user, editUser);
        return editUser;
    }

    public void deleteUserById(String id) {
        User user = findById(id);
        users.remove(user);
    }

}
