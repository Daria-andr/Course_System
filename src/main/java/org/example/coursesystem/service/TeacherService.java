package org.example.coursesystem.service;

import org.example.coursesystem.model.Teacher;
import org.example.coursesystem.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher findByUsername(String username) {
        return teacherRepository.findByUsername(username).orElse(null);
    }

    public Teacher findByEmail(String email) {
        return teacherRepository.findByEmail(email).orElse(null);
    }

    public Teacher findById(int id) {
        return teacherRepository.findById(id).orElse(null);
    }

    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Teacher updateTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Teacher createAdmin(String username, String password, String email) {
        Teacher admin = new Teacher(username, password, email);
        admin.setRole("ADMIN");
        return saveTeacher(admin);
    }
}