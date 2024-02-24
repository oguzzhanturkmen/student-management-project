package com.project.service.user;

import com.project.payload.request.user.TeacherRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.TeacherResponse;
import com.project.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final UserRoleRepository userRoleRepository;

    public ResponseMessage<TeacherResponse> saveTeacher(TeacherRequest teacherRequest) {
        return null;
    }
}
