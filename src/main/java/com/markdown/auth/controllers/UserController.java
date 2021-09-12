package com.markdown.auth.controllers;

import com.markdown.auth.dtos.UserInfoDTO;
import com.markdown.auth.dtos.UserLoginDTO;
import com.markdown.auth.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.google.common.base.Preconditions.checkNotNull;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ANONYMOUS', 'ADMIN')")
    public UserInfoDTO createUser(@RequestBody UserInfoDTO userInfoDTO){

        checkNotNull(userInfoDTO);

        userService.createUser(userInfoDTO);
        return userInfoDTO;
    }

    @GetMapping("/info/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserInfoDTO getUserInfo(@PathVariable String userId) {

        return userService.retrieveUserInfo(userId);
    }

    @PostMapping("/login")
    @PreAuthorize("hasAnyRole('ANONYMOUS')")
    public UserInfoDTO loginUser(@RequestBody UserLoginDTO userLoginDTO){

        checkNotNull(userLoginDTO);

        return userService.loginUser(userLoginDTO);
    }

//    delete a user
//    TODO: homework
//    modify a user
    //    TODO: homework
}
