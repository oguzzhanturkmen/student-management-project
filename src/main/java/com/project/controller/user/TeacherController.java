package com.project.controller.user;

import com.project.entity.concretes.user.User;
import com.project.payload.request.user.TeacherRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.StudentResponse;
import com.project.payload.response.user.TeacherResponse;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<TeacherResponse>> saveTeacher(@RequestBody @Valid TeacherRequest teacherRequest){
        return ResponseEntity.ok(teacherService.saveTeacher(teacherRequest));
    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse> updateTeacherForManagers(@PathVariable Long userId, @RequestBody @Valid TeacherRequest teacherRequest){
        return teacherService.updateTeacherForManagers(userId, teacherRequest);
    }

    @GetMapping("/getAllStudentByAdvisorUsername")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public List<StudentResponse> getAllStudentByAdvisorUsername(HttpServletRequest request){
        String username = request.getHeader("username");
        return teacherService.getAllStudentByAdvisorUsername(username);
    }

    @PatchMapping("/saveAdvisorTeacher/{teacherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse> saveAdvisorTeacher(@PathVariable Long teacherId){
        return teacherService.saveAdvisorTeacher(teacherId);
    }

    @DeleteMapping("/deleteAdvisorTeacherById/{teacherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public ResponseMessage<UserResponse> deleteAdvisorTeacherById(@PathVariable Long teacherId){
        return teacherService.deleteAdvisorTeacherById(teacherId);
    }

    @GetMapping("/getAllAdvisorTeacher")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public List<UserResponse> getAllAdvisorTeacher(){
        return teacherService.getAllAdvisorTeacher();
    }



}
