package com.project.messages;

public class ErrorMessages {

    public static final String PASSWORD_NOT_MATCH_MESSAGE = "Old password is incorrect";
    public static final String USERNAME_ALREADY_EXISTS = "Username is already taken : %s";
    public static final String SSN_ALREADY_EXISTS = "SSN is already taken!";
    public static final String PHONE_NUMBER_ALREADY_EXISTS = "Phone number is already taken!";
    public static final String EMAIL_ALREADY_EXISTS = "Email is already taken!";

    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String NOT_FOUND_USER_USERROLE_MESSAGE = "Error: User not found with user-role %s";

    public static final String USER_CREATED = "User is Saved Successfully";

    public static final String NOT_FOUND_USER_MESSAGE = "Error: User not found with id %s";
    public static final String NOT_FOUND_USER_WITH_ROLE_MESSAGE = "Error: User not found with this role";
    public static final String NOT_FOUND_USER_WITH_USERNAME_MESSAGE = "Error: User not found with this username";


    public static final String NOT_FOUND_ADVISOR_MESSAGE = "Error: Advisor Teacher with id %s not found";
    public static final String NOT_PERMITTED_METHOD_MESSAGE = "Method not allowed";
    private ErrorMessages() {
    }
}
