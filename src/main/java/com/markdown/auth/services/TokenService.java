package com.markdown.auth.services;

import com.markdown.auth.exceptions.InvalidTokenException;
import com.markdown.auth.models.MarkdownUserModel;

public interface TokenService {

//    Token validation
    void validateToken(String jwtToken) throws InvalidTokenException;

    void generateToken(MarkdownUserModel markdownUserModel);
}
