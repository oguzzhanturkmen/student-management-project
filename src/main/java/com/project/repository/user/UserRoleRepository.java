package com.project.repository.user;

import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.RoleType;
import org.hibernate.usertype.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> findByRoleName(RoleType userRole);
}
