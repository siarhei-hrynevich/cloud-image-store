package com.flex.models;

public class UserModel {
    private Long id;
    private String login;
    private String encryptedPassword;
    private String name;
    private Boolean enabled;


    public UserModel(Long id, String login, String encryptedPassword, String name, boolean enabled) {
        this.id = id;
        this.login = login;
        this.encryptedPassword = encryptedPassword;
        this.name = name;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean isEnabled() {
        return enabled;
    }
}
