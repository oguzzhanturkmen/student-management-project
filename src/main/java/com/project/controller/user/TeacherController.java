package com.project.controller.user;

import com.project.payload.request.user.TeacherRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.TeacherResponse;
import com.project.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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


}
