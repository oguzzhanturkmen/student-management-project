package com.project.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.Day;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Enumerated(EnumType.STRING)
    private Day day;

   @JsonFormat(shape = JsonFormat.Shape.STRING ,pattern = "HH:mm", timezone = "US")
   private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING ,pattern = "HH:mm", timezone = "US")
   private LocalTime endTime;

    @ManyToMany
    @JoinTable(name = "lesson_program_lesson",
            joinColumns = @JoinColumn(name = "lesson_program_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    private Set<Lesson> lessons;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private EducationTerm educationTerm;

    @ManyToMany(mappedBy = "lessonsProgramList", fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<User> users;

    @PreRemove
    private void removeLessonProgramFromUsers(){
        users.forEach(user -> user.getLessonsProgramList().remove(this));
    }

}
