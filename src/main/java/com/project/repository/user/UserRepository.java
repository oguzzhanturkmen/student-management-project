package com.project.repository.user;

import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.payload.request.user.UserRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);


    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userRole.roleName = ?1")
    Page<User> findByUserByRole(String roleName, Pageable pageable);

    List<User> getUserByNameContaining(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE u.userRole.roleType = ?1")
    long countAdmins(RoleType roleType);
}
