package com.markdown.auth.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")
public class MarkdownUserModel extends GenericModel{

    @Indexed(direction = IndexDirection.DESCENDING, unique = true)
    private String username;

    @Indexed(direction = IndexDirection.DESCENDING, unique = true)
    private String displayName;

    @Indexed(direction = IndexDirection.DESCENDING, unique = true)
    private String email;
    private List<String> roles;
    private String jwtToken;
    private String password;

    public MarkdownUserModel() {
        super();
    }
}
