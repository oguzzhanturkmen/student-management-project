package com.project.entity.concretes.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.entity.concretes.business.LessonProgram;
import com.project.entity.concretes.business.Meet;
import com.project.entity.concretes.business.StudentInfo;
import com.project.entity.enums.Gender;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_user")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String ssn;

    private String name;

    private String surname;

    @JsonFormat(shape =JsonFormat.Shape.STRING ,pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String birthPlace;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private Boolean built_in;

    private String motherName;

    private String fatherName;

    private Integer studentNumber;

    private Boolean isActive;

    private Boolean isAdvisor;

    private Long advisorTeacherId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy ="teacher", cascade = CascadeType.REMOVE)
    private List<StudentInfo> studentInfos;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "user_lesson_program",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_program_id"))
    private Set<LessonProgram> lessonsProgramList;


    @JsonIgnore
    @ManyToMany

    private List<Meet> meets;







}
