package org.example.coursesystem.service;

import org.example.coursesystem.model.Student;
import org.example.coursesystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username).orElse(null);
    }

    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email).orElse(null);
    }

    public Student findById(int id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student createAdmin(String username, String password, String email) {
        Student admin = new Student(username, password, email);
        admin.setRole("ADMIN");
        return saveStudent(admin);
    }
}