package com.markdown.auth.initialize;

import com.markdown.auth.daos.RoleDAO;
import com.markdown.auth.daos.UserDAO;
import com.markdown.auth.models.MarkdownRoleModel;
import com.markdown.auth.models.MarkdownUserModel;
import com.markdown.auth.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;


@Profile({"prod"})
@Component
public class InitializeProdData {

    @Autowired
    UserDAO userDAO;

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    TokenService tokenService;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {

        addRoles();
        addUsers();
    }

    void addRoles() {

        final Optional<MarkdownRoleModel> optionalMarkdownRoleModelAdmin = roleDAO.findByRole("ADMIN");
        if (!optionalMarkdownRoleModelAdmin.isPresent()) {
            MarkdownRoleModel markdownRoleModelAdmin = new MarkdownRoleModel();
            markdownRoleModelAdmin.setRole("ADMIN");
            roleDAO.save(markdownRoleModelAdmin);
        }

        final Optional<MarkdownRoleModel> optionalMarkdownRoleModelUser = roleDAO.findByRole("USER");
        if (!optionalMarkdownRoleModelUser.isPresent()) {
            MarkdownRoleModel markdownRoleModelUser = new MarkdownRoleModel();
            markdownRoleModelUser.setRole("ADMIN");
            roleDAO.save(markdownRoleModelUser);
        }
    }

    void addUsers() {

        final Optional<MarkdownUserModel> optionalMarkdownUserModelAdmin = userDAO.findByUsername("admin");
        if (!optionalMarkdownUserModelAdmin.isPresent()) {
            MarkdownUserModel markdownUserModelAdmin = new MarkdownUserModel();
            markdownUserModelAdmin.setUsername("admin");
            markdownUserModelAdmin.setEmail("admin@admin.com");
            markdownUserModelAdmin.setPassword(bCryptPasswordEncoder.encode("Admin123456789"));
            markdownUserModelAdmin.setRoles(Collections.singletonList("ADMIN"));
            tokenService.generateToken(markdownUserModelAdmin);
            markdownUserModelAdmin.setDisplayName("adminDisplayName");

            userDAO.save(markdownUserModelAdmin);
        }


        final Optional<MarkdownUserModel> optionalMarkdownUserModelUser = userDAO.findByUsername("admin");
        if (!optionalMarkdownUserModelUser.isPresent()) {
            MarkdownUserModel markdownUserModelUser = new MarkdownUserModel();
            markdownUserModelUser.setUsername("admin");
            markdownUserModelUser.setEmail("admin@admin.com");
            markdownUserModelUser.setPassword(bCryptPasswordEncoder.encode("User123456789"));
            markdownUserModelUser.setRoles(Collections.singletonList("ADMIN"));
            tokenService.generateToken(markdownUserModelUser);
            markdownUserModelUser.setDisplayName("adminDisplayName");

            userDAO.save(markdownUserModelUser);

        }
    }
}