package com.project.service.validator;

import com.project.entity.concretes.user.User;
import com.project.messages.ErrorMessages;
import com.project.payload.request.abstracts.AbstractUserRequest;
import com.project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePropertyValidator {

    private final UserRepository userRepository;

    public void checkUniqueProperties(User user , AbstractUserRequest abstractUserRequest){
        String updatedsUsername ="";
        String updatedSsn = "";
        String updatedPhoneNumber = "";
        String updatedEmail = "";
        boolean isChanged = false;

        if (!user.getUsername().equals(abstractUserRequest.getUsername())){
            updatedsUsername = abstractUserRequest.getUsername();
            isChanged = true;
        }
        if (!user.getSsn().equals(abstractUserRequest.getSsn())){
            updatedSsn = abstractUserRequest.getSsn();
            isChanged = true;
        }
        if (!user.getPhoneNumber().equals(abstractUserRequest.getPhoneNumber())){
            updatedPhoneNumber = abstractUserRequest.getPhoneNumber();
            isChanged = true;
        }
        if (!user.getEmail().equals(abstractUserRequest.getEmail())){
            updatedEmail = abstractUserRequest.getEmail();
            isChanged = true;
        }

        if (isChanged){
            checkDuplicate(updatedsUsername, updatedSsn, updatedPhoneNumber, updatedEmail);
        }

    }

    public void checkDuplicate(String username, String ssn, String phoneNumber, String email) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.USERNAME_ALREADY_EXISTS, username));
        }
        if (userRepository.existsBySsn(ssn)) {
            throw new IllegalArgumentException(ErrorMessages.SSN_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException(ErrorMessages.PHONE_NUMBER_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }


    }

}
