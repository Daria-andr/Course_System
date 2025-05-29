package org.example.coursesystem.service;

import org.example.coursesystem.model.Course;
import org.example.coursesystem.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository CourseRepository;

    public List<Course> getCourses() {
        return CourseRepository.findAll();
    }

    public List<Course> getCoursesByCategory(String category) {
        return CourseRepository.findByCategory(category);
    }

    public List<Course> getCoursesByStudentEmail(String studentEmail) {
        return CourseRepository.findByStudentEmail(studentEmail);
    }
    public List<Course> getCoursesByTeacherEmail(String teacherEmail) {
        return CourseRepository.findByTeacherEmail(teacherEmail);
    }

    public Course getCourseById(int id) {
        return CourseRepository.findById(id).orElse(null);
    }
    public Course saveCourse(Course course) {
        return CourseRepository.save(course);
    }

    public void deleteCourse(int id) {
        CourseRepository.deleteById(id);
    }

    public List<String> getAllCategories() {
        return CourseRepository.findAll().stream()
                .map(Course::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}