package com.project.repository.business;

import com.project.entity.concretes.business.Meet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetRepository extends JpaRepository<Meet, Long> {
    List<Meet> getByAdvisoryTeacher_IdEquals(Long userId);

    @Query("SELECT m FROM Meet m WHERE m.advisoryTeacher.id = ?1 AND m.date = ?2 AND ((m.startTime <= ?3 AND m.endTime >= ?3) OR (m.startTime <= ?4 AND m.endTime >= ?4))")
    List<Meet> findByStudents_IdEquals(Long userId);
}
