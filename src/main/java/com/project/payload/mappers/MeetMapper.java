package com.project.payload.mappers;

import com.project.entity.concretes.business.Meet;
import com.project.payload.request.business.MeetRequest;
import com.project.payload.response.business.MeetResponse;
import org.springframework.stereotype.Component;

@Component
public class MeetMapper {

    public Meet mapMeetRequestToMeet(MeetRequest meetRequest){

        return Meet.builder()
                .date(meetRequest.getDate())
                .startTime(meetRequest.getStartTime())
                .endTime(meetRequest.getStopTime())
                .description(meetRequest.getDescription())
                .build();
    }

    public MeetResponse mapMeetToMeetResponse(Meet meet) {

        return MeetResponse.builder()
                .id(meet.getId())
                .date(meet.getDate())
                .startTime(meet.getStartTime())
                .stopTime(meet.getEndTime())
                .description(meet.getDescription())
                .advisoryTeacherId(meet.getAdvisoryTeacher().getId())
                .teacherSsn(meet.getAdvisoryTeacher().getSsn())
                .teacherName(meet.getAdvisoryTeacher().getName())
                .students(meet.getStudents())
                .build();
    }
}
