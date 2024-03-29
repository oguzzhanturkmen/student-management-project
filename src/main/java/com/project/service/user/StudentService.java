package com.project.service.user;

import com.project.entity.concretes.business.LessonProgram;
import com.project.entity.concretes.user.User;
import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.RoleType;
import com.project.messages.SuccessMessages;
import com.project.payload.mappers.UserMapper;
import com.project.payload.request.business.ChooseLessonProgramWithId;
import com.project.payload.request.user.StudentRequest;
import com.project.payload.request.user.StudentRequestWithoutPassord;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.StudentResponse;
import com.project.repository.user.UserRepository;
import com.project.service.business.LessonProgramService;
import com.project.service.helper.MethodHelper;
import com.project.service.validator.DateTimeValidator;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final LessonProgramService lessonProgramService;
    private final DateTimeValidator dateTimeValidator;


    public ResponseMessage<StudentResponse> saveStudent(StudentRequest studentRequest) {

        User advisorTeacher = methodHelper.isUserExist(studentRequest.getAdvisorTeacherId());

        methodHelper.checkIsAdvisorTeacher(advisorTeacher);
        uniquePropertyValidator.checkDuplicate(studentRequest.getUsername(), studentRequest.getSsn(), studentRequest.getPhoneNumber(), studentRequest.getEmail());

        User student = userMapper.mapStudentRequestToUser(studentRequest);
        student.setAdvisorTeacherId(advisorTeacher.getId());
        student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        student.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
        student.setIsActive(true);
        student.setIsAdvisor(Boolean.FALSE);
        student.setStudentNumber(getLastNumber());

        return ResponseMessage.<StudentResponse>builder()
                .object(userMapper.mapUserToStudentResponse(userRepository.save(student)))
                .message(SuccessMessages.STUDENT_SAVED)
                .build();


    }

    private int getLastNumber() {
        if (!userRepository.findStudent(RoleType.STUDENT)){
            return 1000;
        }
        return userRepository.getMaxStudentNumber() + 1;

    }

    public ResponseEntity<String> updateStudent(StudentRequestWithoutPassord studentRequest, HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        User student = userRepository.findByUsername(username);
        uniquePropertyValidator.checkUniqueProperties(student, studentRequest);

        student.setMotherName(studentRequest.getMotherName());
        student.setFatherName(studentRequest.getFatherName());
        student.setBirthDate(studentRequest.getBirthDate());
        student.setEmail(studentRequest.getEmail());
        student.setPhoneNumber(studentRequest.getPhoneNumber());
        student.setBirthPlace(studentRequest.getBirthPlace());
        student.setGender(studentRequest.getGender());
        student.setName(studentRequest.getName());
        student.setSurname(studentRequest.getSurname());
        student.setSsn(student.getSsn());

        userRepository.save(student);
        String message = SuccessMessages.USER_UPDATED;

        return ResponseEntity.ok(message);

    }

    public ResponseMessage<StudentResponse> updateStudentForManagers(StudentRequest studentRequest, Long userId) {
        User user = methodHelper.isUserExist(userId);

        methodHelper.checkRole(user, RoleType.STUDENT);

        uniquePropertyValidator.checkUniqueProperties(user, studentRequest);

        user.setName(studentRequest.getName());
        user.setSurname(studentRequest.getSurname());
        user.setBirthDate(studentRequest.getBirthDate());
        user.setBirthPlace(studentRequest.getBirthPlace());
        user.setSsn(studentRequest.getSsn());
        user.setEmail(studentRequest.getEmail());
        user.setPhoneNumber(studentRequest.getPhoneNumber());
        user.setGender(studentRequest.getGender());
        user.setMotherName(studentRequest.getMotherName());
        user.setFatherName(studentRequest.getFatherName());
        user.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        user.setAdvisorTeacherId(studentRequest.getAdvisorTeacherId());

        return ResponseMessage.<StudentResponse>builder()
                .message(SuccessMessages.STUDENT_UPDATE)
                .object(userMapper.mapUserToStudentResponse(userRepository.save(user)))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage changeStatusOfStudent(Long studentId, boolean status) {
        User student = methodHelper.isUserExist(studentId);
        methodHelper.checkRole(student, RoleType.STUDENT);

        student.setIsActive(status);
        userRepository.save(student);

        return ResponseMessage.builder()
                .message("Student is " + (status ? "active" : "passive"))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<StudentResponse> addLessonProgram(ChooseLessonProgramWithId chooseLessonProgramWithId, String username) {
        User student = methodHelper.isUserExist(username);

        Set<LessonProgram> lessonProgramSet = lessonProgramService.getLessonProgramById(chooseLessonProgramWithId.getLessonProgramId());

        Set<LessonProgram> studentCurrentLessonProgram = student.getLessonsProgramList();
        dateTimeValidator.checkLessonPrograms(lessonProgramSet , studentCurrentLessonProgram);

        student.setLessonsProgramList(lessonProgramSet);

        User savedStudent = userRepository.save(student);

        return ResponseMessage.<StudentResponse>builder()
                .message(SuccessMessages.LESSON_PROGRAM_ADDED)
                .object(userMapper.mapUserToStudentResponse(savedStudent))
                .httpStatus(HttpStatus.OK)
                .build();

    }
}
