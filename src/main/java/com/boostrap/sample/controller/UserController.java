package com.boostrap.sample.controller;

import com.boostrap.sample.UserService;
import com.boostrap.sample.domain.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AUTHOR: 819521
 * DATE  : 2015/12/28
 * TIME  : 15:15
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    private UserService userService = UserService.getInstance();

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<User> list() {
        return userService.listUser();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map delete(@RequestBody String id) {
        System.out.println("delete id = " + id);
        userService.deleteUserById(id);
        Map result = new HashMap();
        result.put("success", true);
        result.put("data", "删除成功");
        return result;

    }
}
