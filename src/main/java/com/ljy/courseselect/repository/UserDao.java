package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.StudentAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserDao extends JpaRepository<StudentAccountEntity,Long> {
    StudentAccountEntity findByStudentId(String studentId);
}
