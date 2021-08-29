package com.bnext.dv.controller;

import com.bnext.dv.controller.request.UserRequest;
import com.bnext.dv.model.User;
import com.bnext.dv.service.UserService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import javax.validation.Valid;

@Controller("/user")
public class UserController {

    private UserService userService;

    public UserController (UserService userService){
        this.userService = userService;
    }

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User createUser (@Valid @Body UserRequest user){
        return this.userService.saveUser( new User(user.getUser()));
    }
}
