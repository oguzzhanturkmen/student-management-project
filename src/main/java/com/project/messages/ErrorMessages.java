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

    public static final String EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTIRATION_DATE = "Education start date is earlier than last registration date";
    public static final String EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE = "Education start date is earlier than last registration date";
    public static final String EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE = "Education end date is earlier than start date";
    public static final String EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE = "Education term is already exist by term and year";
    public static final String EDUCATION_TERM_CONFLICT_MESSAGE = "Education term conflict";
    public static final String NOT_FOUND_EDUCATION_TERM_MESSAGE = "Error: Education term with id %s not found";
    public static final String LESSON_ALREADY_EXISTS = "Lesson is already exists : %s";
    public static final String LESSON_NOT_FOUND =   "Error: Lesson not found";

    public static final String NOT_FOUND_LESSOIN_IN_LIST = "Error: Lesson not found in list";
    public static final String TIME_NOT_VALID_MESSAGE = "Time is not valid";
    public static final String NOT_FOUND_LESSON_PROGRAM_BY_ID = "Error: Lesson program not found with id %s";
    public static final String NOT_FOUND_LESSON_PROGRAM_BY_LESSON_ID = "Error: Lesson program not found with lesson id";
    public static final String LESSON_PROGRAM_ALREADY_EXIST = "Lesson program already exist";
    public static final String STUDENT_INFO_NOT_FOUND = "Error: Student info not found";
    public static final String STUDENT_INFO_NOT_FOUND_BY_STUDENT_ID = "Error: Student info not found by student id %s";
    public static final String MEET_HOURS_CONFLICT = "Meet hours conflict";
    public static final String MEET_NOT_FOUND_MESSAGE = "Error: Meet not found";

    ;

    private ErrorMessages() {
    }
}
