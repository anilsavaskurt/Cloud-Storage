package com.udacity.jwdnd.course1.cloudstorage.requests;

import lombok.Data;

@Data
public class CredentialFormRequest {
    private String url;
    private String userName;
    private String credentialId;
    private String password;
}
