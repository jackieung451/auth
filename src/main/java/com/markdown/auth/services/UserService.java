package com.markdown.auth.services;

import com.markdown.auth.dtos.UserInfoDTO;
import com.markdown.auth.dtos.UserLoginDTO;

public interface UserService {

//    user creation
    void createUser(UserInfoDTO userInfoDTO);

//    user fetching
    UserInfoDTO retrieveUserInfo(String userId);

//    user login
    UserInfoDTO loginUser(UserLoginDTO userLoginDTO);
}
