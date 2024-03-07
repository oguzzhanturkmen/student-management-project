package com.project.controller.user;

import com.project.payload.request.business.ChooseLessonProgramWithId;
import com.project.payload.request.user.StudentRequest;
import com.project.payload.request.user.StudentRequestWithoutPassord;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.StudentResponse;
import com.project.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<StudentResponse>> saveStudent(@RequestBody @Valid StudentRequest studentRequest){
        return ResponseEntity.ok(studentService.saveStudent(studentRequest));
    }

    @PatchMapping("/update/")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentRequestWithoutPassord studentRequestWithoutPassord, HttpServletRequest httpServletRequest){
        return studentService.updateStudent(studentRequestWithoutPassord, httpServletRequest);

    }
    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public ResponseMessage<StudentResponse> updateStudentForManagers(@RequestBody @Valid StudentRequest studentRequest, @PathVariable Long userId){
        return studentService.updateStudentForManagers(studentRequest, userId);
    }
    @GetMapping("changeStatus")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public ResponseMessage changeStatusOfStudent(@RequestParam Long id, @RequestParam boolean status){
        return studentService.changeStatusOfStudent(id, status);
    }

    @PostMapping("/addLessonProgramToStudent")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseMessage<StudentResponse> addLessonProgram(@RequestBody @Valid ChooseLessonProgramWithId chooseLessonProgramWithId, HttpServletRequest httpServletRequest){
        String username = (String) httpServletRequest.getAttribute("username");
        return studentService.addLessonProgram(chooseLessonProgramWithId, username);
    }



}
