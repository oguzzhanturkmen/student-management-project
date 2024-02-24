package com.project.service.user;

import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.messages.ErrorMessages;
import com.project.messages.SuccessMessages;
import com.project.payload.mappers.UserMapper;
import com.project.payload.request.user.UserRequest;
import com.project.payload.request.user.UserRequestWithoutPassword;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final PageableHelper pageableHelper;
    private final MethodHelper methodHelper;

    public ResponseMessage<UserResponse> saveUser(String userRole, UserRequest userRequest) {
        uniquePropertyValidator.checkDuplicate(userRequest.getUsername(), userRequest.getSsn(), userRequest.getPhoneNumber(), userRequest.getEmail());
        User user = userMapper.mapUserRequestToUser(userRequest);

        if(userRole.equalsIgnoreCase(RoleType.ADMIN.name())){


            if (Objects.equals(userRequest.getUsername(), "Admin")){
                user.setBuilt_in(true);
            }
            user.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));
        }else if(userRole.equalsIgnoreCase("Dean")){
            user.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        }
        else if (userRole.equalsIgnoreCase("ViceDean")){
            user.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANT_MANAGER));
        }
        else {
            throw new IllegalArgumentException(String.format(ErrorMessages.NOT_FOUND_USER_USERROLE_MESSAGE, userRole));
        }

        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        user.setIsAdvisor(Boolean.FALSE);

        User savedUser =userRepository.save(user);

        return  ResponseMessage.<UserResponse>builder()
                .object(userMapper.mapUserToUserResponse(savedUser))
                .message(ErrorMessages.USER_CREATED)
                .build();


    }


    public Page<UserResponse> getUserByPage(String userRole, int page, int size, String sort, String direction) {
        Pageable pageable = pageableHelper.createPageableWithProperties(page, size, sort, direction);

        return userRepository.findByUserByRole(userRole, pageable).map(userMapper::mapUserToUserResponse);
    }

    public ResponseMessage<BaseUserResponse> getUserById(Long userId) {
        BaseUserResponse baseUserResponse = null;

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));

        if (user.getUserRole().getRoleType().equals(RoleType.STUDENT)) {
            baseUserResponse = userMapper.mapUserToUserResponse(user);
        } else if (user.getUserRole().getRoleType().equals(RoleType.TEACHER)) {
            baseUserResponse = userMapper.mapUserToTeacherResponse(user);
        } else {
            baseUserResponse = userMapper.mapUserToUserResponse(user);
        }

        return ResponseMessage.<BaseUserResponse>builder()
                .object(baseUserResponse)
                .message(SuccessMessages.USER_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public String deleteUserById(Long id, HttpServletRequest httpServletRequest) {
        User user =methodHelper.isUserExist(id);
        String username = (String) httpServletRequest.getAttribute("username");

        User user2 = userRepository.findByUsername(username);

        if (Boolean.TRUE.equals(user.getBuilt_in())){
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        } else if (user2.getUserRole().getRoleType() == RoleType.MANAGER) {
            if (!((user.getUserRole().getRoleType() == RoleType.TEACHER) ||
                    (user.getUserRole().getRoleType() == RoleType.STUDENT) ||
                    (user.getUserRole().getRoleType() == RoleType.ASSISTANT_MANAGER))) {
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }

        } else if (user2.getUserRole().getRoleType() == RoleType.ASSISTANT_MANAGER) {
            if (!((user.getUserRole().getRoleType() == RoleType.TEACHER) ||
                    (user.getUserRole().getRoleType() == RoleType.STUDENT))) {
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }

        }
        userRepository.deleteById(id);
        return SuccessMessages.USER_DELETED;


    }

    public ResponseMessage<BaseUserResponse> updateUser(Long userId, UserRequest userRequest) {
        User user = methodHelper.isUserExist(userId);
        methodHelper.checkBuiltInUser(user);
        uniquePropertyValidator.checkUniqueProperties(user, userRequest);

        User updatedUser = userMapper.mapUserRequestToUpdatedUser(userRequest, userId);

        updatedUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        updatedUser.setUserRole(user.getUserRole());

        User savedUser = userRepository.save(updatedUser);

        return ResponseMessage.<BaseUserResponse>builder()
                .object(userMapper.mapUserToUserResponse(savedUser))
                .message(SuccessMessages.USER_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseEntity<String> updateUserForUsers(UserRequestWithoutPassword userRequestWithoutPassword, HttpServletRequest httpServletRequest) {
        User user = userRepository.findByUsername((String) httpServletRequest.getAttribute("username"));

        methodHelper.checkBuiltInUser(user);
        uniquePropertyValidator.checkUniqueProperties(user, userRequestWithoutPassword);

        user.setUsername(userRequestWithoutPassword.getUsername());
        user.setEmail(userRequestWithoutPassword.getEmail());
        user.setName(userRequestWithoutPassword.getName());
        user.setSurname(userRequestWithoutPassword.getSurname());
        user.setPhoneNumber(userRequestWithoutPassword.getPhoneNumber());
        user.setSsn(userRequestWithoutPassword.getSsn());
        user.setBirthPlace(userRequestWithoutPassword.getBirthPlace());
        user.setBirthDate(userRequestWithoutPassword.getBirthDate());
        user.setGender(userRequestWithoutPassword.getGender());

        userRepository.save(user);

        return ResponseEntity.ok(SuccessMessages.USER_UPDATED);

    }
}
