package com.flex.exeptions;

public class UserAlreadyExistException extends Exception{
    private String login;

    public UserAlreadyExistException(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
