package com.project.repository.business;

import com.project.entity.concretes.business.LessonProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Repository
public interface LessonProgramRepository extends JpaRepository<LessonProgram, Long> {
    List<LessonProgram> findByUsers_IdNull();

    List<LessonProgram>  findByUsers_IdNotNull();

    @Query("SELECT l FROM LessonProgram l INNER JOIN l.users users WHERE users.username = :username")
    Set<LessonProgram> getLessonProgramByUsersUsername(String username);

    Set<LessonProgram> findByUsers_IdEquals(Long id);

    @Query("SELECT l FROM LessonProgram l WHERE l.id IN :lessonsIdList")
    Set<LessonProgram> getLessonProgramByLessonProgramIdList(Set<Long> lessonsIdList);
}
