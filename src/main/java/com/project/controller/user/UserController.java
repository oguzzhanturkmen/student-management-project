package com.project.controller.user;

import com.project.payload.request.user.UserRequest;
import com.project.payload.request.user.UserRequestWithoutPassword;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/save/{userRole}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<UserResponse>> saveUser(@PathVariable String userRole, @Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.saveUser(userRole, userRequest));
    }

    @GetMapping("/getAllUserByPage/{userRole}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUserByPage(@PathVariable String userRole, @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size,
                                                               @RequestParam(value = "sort", defaultValue = "createDate") String sort,
                                                               @RequestParam(value = "direction", defaultValue = "DESC") String direction){
        Page<UserResponse> adminsOrDeansOrViceDeans = userService.getUserByPage(userRole, page, size, sort, direction);
        return new ResponseEntity<>(adminsOrDeansOrViceDeans, HttpStatus.OK);
    }

    @GetMapping("/getUserById/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseMessage<BaseUserResponse> getUserById(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id, HttpServletRequest HttpServletRequest){
        return ResponseEntity.ok(userService.deleteUserById(id, HttpServletRequest));
    }

    @PutMapping("/update({userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseMessage<BaseUserResponse> updateAdminDeanViceDeanForAdmin(@PathVariable Long userId, @Valid @RequestBody UserRequest userRequest){
        return userService.updateUser(userId, userRequest);
    }

    @PatchMapping("/updateUser")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER', 'TEACHER')")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserRequestWithoutPassword userRequestWithoutPassword, HttpServletRequest httpServletRequest{
       return userService.updateUserForUsers(userRequestWithoutPassword, httpServletRequest);
    }




}
