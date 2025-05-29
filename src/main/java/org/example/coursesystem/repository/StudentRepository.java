package org.example.coursesystem.repository;

import org.example.coursesystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
    Optional<Student> findById(Integer id);
}