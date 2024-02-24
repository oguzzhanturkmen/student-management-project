package com.project.service.helper;

import com.project.entity.concretes.user.User;
import com.project.messages.ErrorMessages;
import com.project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodHelper {

    private final UserRepository userRepository;

    public User isUserExist(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));
    }

    public void checkBuiltInUser(User user){
        if (Boolean.TRUE.equals(user.getBuilt_in())){
            throw new IllegalArgumentException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }
}
