package com.project.payload.request.user;

import com.project.payload.request.abstracts.BaseUserRequest;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TeacherRequest extends BaseUserRequest {

    @NotNull(message = "Please select lesson")
    private Set<Long> lessonsIdList;

    @NotNull(message = "Please select advisor teacher status")
    private Boolean isAdvisorTeacher;
}
