package com.project.service.helper;

import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.ResourceNotFoudeException;
import com.project.messages.ErrorMessages;
import com.project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodHelper {

    private final UserRepository userRepository;

    public User isUserExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));
    }

    public void checkBuiltInUser(User user) {
        if (Boolean.TRUE.equals(user.getBuilt_in())) {
            throw new IllegalArgumentException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }

    public void checkRole(User user, RoleType roleType) {
        if (!user.getUserRole().getRoleType().equals(roleType)) {
            throw new ResourceNotFoudeException(ErrorMessages.NOT_FOUND_USER_WITH_ROLE_MESSAGE);
        }
    }

    public User isUserExist(String username) {
        User user = userRepository.findByUsername(username);

        if (user.getId() == null) {
            throw new ResourceNotFoudeException(ErrorMessages.NOT_FOUND_USER_WITH_USERNAME_MESSAGE);
        }
        return user;

    }

    public void checkIsAdvisorTeacher(User user) {
        if (Boolean.FALSE.equals(user.getIsAdvisor())) {
            throw new ResourceNotFoudeException(String.format(ErrorMessages.NOT_FOUND_ADVISOR_MESSAGE, user.getId()));
        }
    }
}