package com.markdown.auth.services.impl;

import com.markdown.auth.daos.RoleDAO;
import com.markdown.auth.daos.UserDAO;
import com.markdown.auth.dtos.UserInfoDTO;
import com.markdown.auth.dtos.UserLoginDTO;
import com.markdown.auth.models.MarkdownRoleModel;
import com.markdown.auth.models.MarkdownUserModel;
import com.markdown.auth.services.TokenService;
import com.markdown.auth.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.Mark;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;


@Service
public class UserServiceImpl implements UserService {

    private static final String UNKNOWN_USERNAME_OR_BAD_PASSWORD = "Unknown username or bad password";
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void createUser(UserInfoDTO userInfoDTO) {

        checkNotNull(userInfoDTO.getPassword());
//        transform userInfoDTO to markdownRoleModel
        MarkdownUserModel markdownUserModel = modelMapper.map(userInfoDTO, MarkdownUserModel.class);

//        hash the password first
        markdownUserModel.setPassword(bCryptPasswordEncoder.encode(userInfoDTO.getPassword()));

//        assign default role of USER
        markdownUserModel.setRoles(
                roleDAO.findAll().stream()
                        .map(MarkdownRoleModel::getRole)
                        .filter(role -> role.contains("USER"))
                        .collect(Collectors.toList())
        );

//        generate a new token for the user
        tokenService.generateToken(markdownUserModel);

//        save markdownRoleModel
        userDAO.save(markdownUserModel);

//        update the userInfoDTO after the markdownRoleModel has been saved
        userInfoDTO.setPassword("");
        modelMapper.map(markdownUserModel, userInfoDTO);
    }

    @Override
    public UserInfoDTO retrieveUserInfo(String userId) {
        Optional<MarkdownUserModel> optionalMarkdownUserModel = userDAO.findById(userId);
        if (optionalMarkdownUserModel.isPresent()) {
            return modelMapper.map(optionalMarkdownUserModel.get(), UserInfoDTO.class);
        }

        return null;
    }

    @Override
    public UserInfoDTO loginUser(UserLoginDTO userLoginDTO) {

        Optional<MarkdownUserModel> optionalMarkdownUserModel = userDAO.findByUsername(userLoginDTO.getUsername());

        if (optionalMarkdownUserModel.isPresent()) {
            MarkdownUserModel markdownUserModel = optionalMarkdownUserModel.get();

//            check if passwords match
            if (bCryptPasswordEncoder.matches(userLoginDTO.getPassword(), markdownUserModel.getPassword())) {

                return modelMapper.map(markdownUserModel, UserInfoDTO.class);
            } else {
                throw new BadCredentialsException(UNKNOWN_USERNAME_OR_BAD_PASSWORD);
            }
        } else {
            throw new BadCredentialsException(UNKNOWN_USERNAME_OR_BAD_PASSWORD);
        }
    }
}
