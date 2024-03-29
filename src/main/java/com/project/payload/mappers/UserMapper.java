package com.project.payload.mappers;

import com.project.entity.concretes.user.User;
import com.project.payload.request.abstracts.BaseUserRequest;
import com.project.payload.request.user.StudentRequest;
import com.project.payload.request.user.TeacherRequest;
import com.project.payload.request.user.UserRequest;
import com.project.payload.response.user.StudentResponse;
import com.project.payload.response.user.TeacherResponse;
import com.project.payload.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse mapUserToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .birthPlace(user.getBirthPlace())
                .ssn(user.getSsn())
                .email(user.getEmail())
                .userRole(user.getUserRole().getRoleType().name())
                .build();
    }

    public User mapUserRequestToUser(BaseUserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .phoneNumber(userRequest.getPhoneNumber())
                .password(userRequest.getPassword())
                .ssn(userRequest.getSsn())
                .birthPlace(userRequest.getBirthPlace())
                .birthDate(userRequest.getBirthDate())
                .gender(userRequest.getGender())
                .built_in(userRequest.getBuiltIn())
                .build();
    }

    public StudentResponse mapUserToStudentResponse(User user) {
        return StudentResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .birthPlace(user.getBirthPlace())
                .motherName(user.getMotherName())
                .fatherName(user.getFatherName())
                .isActive(user.getIsActive())
                .studentNumber(user.getStudentNumber())
                .gender(user.getGender())
                .lessonPrograms(user.getLessonsProgramList())
                .build();
    }

    public TeacherResponse mapUserToTeacherResponse(User user) {
        return TeacherResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .birthPlace(user.getBirthPlace())
                .isAdviserTeacher(user.getIsAdvisor())
                .gender(user.getGender())
                .lessonPrograms(user.getLessonsProgramList())
                .build();

    }

    public User mapUserRequestToUpdatedUser(UserRequest userRequest, Long userId) {
        return User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .phoneNumber(userRequest.getPhoneNumber())
                .ssn(userRequest.getSsn())
                .birthPlace(userRequest.getBirthPlace())
                .birthDate(userRequest.getBirthDate())
                .id(userId)
                .gender(userRequest.getGender())
                .password(userRequest.getPassword())
                .build();
    }

    public User mapTeacherRequestToUser(TeacherRequest teacherRequest) {
        return User.builder()
                .name(teacherRequest.getName())
                .surname(teacherRequest.getSurname())
                .ssn(teacherRequest.getSsn())
                .username(teacherRequest.getUsername())
                .birthDate(teacherRequest.getBirthDate())
                .birthPlace(teacherRequest.getBirthPlace())
                .password(teacherRequest.getPassword())
                .phoneNumber(teacherRequest.getPhoneNumber())
                .email(teacherRequest.getEmail())
                .isAdvisor(teacherRequest.getIsAdvisorTeacher())
                .built_in(teacherRequest.getBuiltIn())
                .gender(teacherRequest.getGender())
                .build();
    }

    public User mapTeacherRequestToUpdatedUser(TeacherRequest userRequest, Long userId) {
        return User.builder()
                .id(userId)
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .password(userRequest.getPassword())
                .ssn(userRequest.getSsn())
                .birthDate(userRequest.getBirthDate())
                .birthPlace(userRequest.getBirthPlace())
                .phoneNumber(userRequest.getPhoneNumber())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .isAdvisor(userRequest.getIsAdvisorTeacher())
                .built_in(userRequest.getBuiltIn())
                .build();
    }

    public User mapStudentRequestToUser(StudentRequest studentRequest){
        return User.builder()
                .fatherName(studentRequest.getFatherName())
                .motherName(studentRequest.getMotherName())
                .birthDate(studentRequest.getBirthDate())
                .birthPlace(studentRequest.getBirthPlace())
                .name(studentRequest.getName())
                .surname(studentRequest.getSurname())
                .password(studentRequest.getPassword())
                .username(studentRequest.getUsername())
                .ssn(studentRequest.getSsn())
                .email(studentRequest.getEmail())
                .phoneNumber(studentRequest.getPhoneNumber())
                .gender(studentRequest.getGender())
                .built_in(studentRequest.getBuiltIn())
                .build();
    }
}