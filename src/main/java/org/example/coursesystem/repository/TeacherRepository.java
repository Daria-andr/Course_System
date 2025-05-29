package org.example.coursesystem.repository;

import org.example.coursesystem.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Optional<Teacher> findByUsername(String username);
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findById(Integer id);
}