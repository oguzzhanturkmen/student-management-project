package com.project.service.validator;

import com.project.messages.ErrorMessages;
import com.project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePropertyValidator {

    private final UserRepository userRepository;

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
