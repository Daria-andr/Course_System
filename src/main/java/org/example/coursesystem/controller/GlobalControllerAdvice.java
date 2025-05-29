package org.example.coursesystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CourseSystemController courseSystemController;

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn() {
        return courseSystemController.getCurrentStudentId() != 0
                && courseSystemController.getCurrentTeacherId() != 0;


    }
}