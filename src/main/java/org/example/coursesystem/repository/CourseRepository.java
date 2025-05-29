package org.example.coursesystem.repository;

import org.example.coursesystem.model.Course;
import org.example.coursesystem.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByCategory(String category);
    List<Course> findByStudentEmail(String studentEmail);
    List<Course> findByTeacherEmail(String teacherEmail);
    Optional<Course> findById(int id);
}