package com.project.service.user;

import com.project.entity.concretes.user.User;
import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.RoleType;
import com.project.messages.ErrorMessages;
import com.project.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.usertype.UserType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRole getUserRole(RoleType userRole){
        return userRoleRepository.findByRoleName(userRole).orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ROLE_NOT_FOUND));
    }

    public List<UserRole> getAllUserRoles(){
        return userRoleRepository.findAll();
    }
}
