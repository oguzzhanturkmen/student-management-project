package com.project.service.user;

import com.project.contactmessage.messages.Messages;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.messages.SuccessMessages;
import com.project.payload.mappers.UserMapper;
import com.project.payload.request.user.TeacherRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.StudentResponse;
import com.project.payload.response.user.TeacherResponse;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.repository.user.UserRoleRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final UserRoleRepository userRoleRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MethodHelper methodHelper;

    public ResponseMessage<TeacherResponse> saveTeacher(TeacherRequest teacherRequest) {

        //TODO : Check lesson program

        uniquePropertyValidator.checkDuplicate(teacherRequest.getUsername(), teacherRequest.getSsn(), teacherRequest.getPhoneNumber(), teacherRequest.getEmail());

        User teacher = userMapper.mapTeacherRequestToUser(teacherRequest);

        teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        //TODO : Save teacher lessons

        teacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));

        if (teacherRequest.getIsAdvisorTeacher()) {
            teacher.setIsAdvisor(Boolean.TRUE);
        }
        else {
            teacher.setIsAdvisor(Boolean.FALSE);
        }

        User savedTeacher = userRepository.save(teacher);

        return ResponseMessage.<TeacherResponse>builder()
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .message(SuccessMessages.TEACHER_SAVED_SUCCESSFULLY)
                .httpStatus(HttpStatus.CREATED)
                .build();



    }

    public ResponseMessage<TeacherResponse> updateTeacherForManagers(Long userId, TeacherRequest teacherRequest) {
        User teacher = methodHelper.isUserExist(userId);

        methodHelper.checkRole(teacher, RoleType.TEACHER);

        // TODO : Get lesson programs and update

        uniquePropertyValidator.checkUniqueProperties(teacher , teacherRequest);

        User updatedTeacher = userMapper.mapTeacherRequestToUpdatedUser(teacherRequest, userId);
        updatedTeacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        updatedTeacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        User savedTeacher = userRepository.save(updatedTeacher);

        return ResponseMessage.<TeacherResponse>builder()
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .message(SuccessMessages.TEACHER_UPDATED_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();



    }

    public List<StudentResponse> getAllStudentByAdvisorUsername(String username) {
        User teacher = methodHelper.isUserExist(username);
        methodHelper.checkIsAdvisorTeacher(teacher);
        return userRepository.findByAdvisorTeacherId(teacher.getId())
                .stream()
                .map(userMapper::mapUserToStudentResponse)
                .collect(Collectors.toList());

    }

    public ResponseMessage<TeacherResponse> saveAdvisorTeacher(Long teacherId) {
        User teacher = methodHelper.isUserExist(teacherId);
        methodHelper.checkRole(teacher, RoleType.TEACHER);
        teacher.setIsAdvisor(Boolean.TRUE);
        if (Boolean.TRUE.equals(teacher.getIsAdvisor())) {
            throw new IllegalArgumentException(String.format(Messages.ALREADY_ADVISOR_TEACHER_MESSAGE, teacherId));
        }
        User savedTeacher = userRepository.save(teacher);

        return ResponseMessage.<TeacherResponse>builder()
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .message(SuccessMessages.ADVISOR_TEACHER_SAVED_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<UserResponse> deleteAdvisorTeacherById(Long teacherId) {
        User teacher = methodHelper.isUserExist(teacherId);
        methodHelper.checkRole(teacher, RoleType.TEACHER);
        methodHelper.checkIsAdvisorTeacher(teacher);
        teacher.setIsAdvisor(Boolean.FALSE);
        User savedTeacher = userRepository.save(teacher);
        List<User> allStudents = userRepository.findByAdvisorTeacherId(teacherId);
        if (!allStudents.isEmpty()) {
            allStudents.forEach(student -> student.setAdvisorTeacherId(null));
            userRepository.saveAll(allStudents);
        }
        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.ADVISOR_TEACHER_DELETE)
                .object(userMapper.mapUserToUserResponse(savedTeacher))
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public List<UserResponse> getAllAdvisorTeacher() {
        return userRepository.findByIsAdvisor(Boolean.TRUE)
                .stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList());
    }
}
