package org.example.coursesystem.controller;

import org.example.coursesystem.model.Course;
import org.example.coursesystem.model.Student;
import org.example.coursesystem.model.Teacher;
import org.example.coursesystem.service.CourseService;
import org.example.coursesystem.service.StudentService;
import org.example.coursesystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/coursesystem")
public class CourseSystemController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    private int currentStudentId = 0;
    private int currentTeacherId = 0;




    public int getCurrentStudentId() {
        return currentStudentId;
    }
    public int getCurrentTeacherId() {
        return currentTeacherId;
    }


    @GetMapping
    public String viewHomePage(Model model) {
        model.addAttribute("isLoggedIn", currentStudentId != 0);
        model.addAttribute("isLoggedIn", currentTeacherId != 0);
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("isLoggedIn", currentStudentId != 0);
        return "register";
    }



    @PostMapping("/register")
    public String registerStudent(@ModelAttribute Student student, Model model) {
        if (studentService.findByUsername(student.getUsername()) != null) {
            model.addAttribute("error", "Username already exists");
            model.addAttribute("isLoggedIn", currentStudentId != 0);
            return "register";
        }
        if (studentService.findByEmail(student.getEmail()) != null) {
            model.addAttribute("error", "Email already exists");
            model.addAttribute("isLoggedIn", currentStudentId != 0);
            return "register";
        }
        studentService.saveStudent(student);
        return "redirect:/coursesystem/login";
    }


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (currentStudentId != 0) {
            return "redirect:/coursesystem/profile";
        }
        model.addAttribute("isLoggedIn", false);
        return "login";
    }

    @GetMapping("/login-teacher")
    public String showLoginTeacherForm(Model model) {
        if (currentTeacherId != 0) {
            return "redirect:/coursesystem/profile";
        }
        model.addAttribute("isLoggedIn", false);
        return "login-teacher";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        Student student = studentService.findByUsername(username);
        if (student == null || !student.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        currentStudentId = student.getId();
        return "redirect:/coursesystem/profile";
    }

    @PostMapping("/login-teacher")
    public String loginTeacher(@RequestParam String username, @RequestParam String password, Model model) {
        Teacher teacher = teacherService.findByUsername(username);
        if (teacher == null || !teacher.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("isLoggedIn", false);
            return "login-teacher";
        }
        currentStudentId = teacher.getId();
        return "redirect:/coursesystem/profile";
    }

    @GetMapping("/logout")
    public String logout() {
        currentStudentId = 0;
        currentTeacherId = 0;
        return "redirect:/coursesystem/login";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        if (currentStudentId == 0 && currentTeacherId == 0) {
            model.addAttribute("error", "Please login to view your profile");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        Student student = studentService.findById(currentStudentId);
        Teacher teacher = teacherService.findById(currentTeacherId);
        if (student == null && teacher == null) {
            model.addAttribute("error", "User not found");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        List<Course> studentCourses = courseService.getCoursesByStudentEmail(student.getEmail());
        List<Course> teacherCourses = courseService.getCoursesByTeacherEmail(student.getEmail());// Обновленный метод
        model.addAttribute("student", student);
        model.addAttribute("courses", studentCourses);
        model.addAttribute("isLoggedIn", true);
        return "profile";
    }



    @GetMapping("/profile/edit")
    public String editProfileForm(Model model) {
        if (currentStudentId == 0 && currentTeacherId == 0) {
            model.addAttribute("error", "Please login to edit your profile");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        Student student = studentService.findById(currentStudentId);
        Teacher teacher = teacherService.findById(currentTeacherId);
        if (student == null && teacher == null) {
            model.addAttribute("error", "User not found");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        model.addAttribute("student", student);
        model.addAttribute("teacher", teacher);
        model.addAttribute("isLoggedIn", true);

        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute Student updatedStudent, @ModelAttribute Teacher updatedTeacher, Model model) {
        if (currentStudentId == 0 && currentTeacherId == 0) {
            model.addAttribute("error", "Please login to edit your profile");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        Student student = studentService.findById(currentStudentId);
        Teacher teacher = teacherService.findById(currentTeacherId);
        if (student == null && teacher == null) {
            model.addAttribute("error", "User not found");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        student.setEmail(updatedStudent.getEmail());
        teacher.setEmail(updatedTeacher.getEmail());
        studentService.updateStudent(student);
        teacherService.updateTeacher(teacher);
        return "redirect:/coursesystem/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String newPassword, Model model) {
        if (currentStudentId == 0 && currentTeacherId == 0) {
            model.addAttribute("error", "Please login to change your password");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        Student student = studentService.findById(currentStudentId);
        Teacher teacher = teacherService.findById(currentTeacherId);
        if (student == null && teacher == null) {
            model.addAttribute("error", "User not found");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        student.setPassword(newPassword);
        teacher.setPassword(newPassword);
        studentService.updateStudent(student);
        teacherService.updateTeacher(teacher);
        return "redirect:/coursesystem/profile";
    }

    @GetMapping("/courses")
    public String viewCourses(@RequestParam(required = false) String category, Model model) {
        // Получаем все уникальные категории
        List<String> categories = courseService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);

        // Получаем предметы (все или по категории)
        List<Course> courses = category != null ?
                courseService.getCoursesByCategory(category) :
                courseService.getCourses();
        model.addAttribute("courses", courses);

        model.addAttribute("isLoggedIn", currentStudentId != 0);
        model.addAttribute("isLoggedIn", currentTeacherId != 0);
        return "courses";
    }

    @GetMapping("/courses/{id}")
    public String viewCourseDetails(@PathVariable int id, Model model) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return "redirect:/coursesystem/courses";
        }

        model.addAttribute("course", course);
        model.addAttribute("isLoggedIn", currentStudentId != 0);

        // Проверяем, может ли текущий пользователь редактировать этот предмет
        boolean canEdit = false;
        if (currentTeacherId != 0) {
            Teacher currentTeacher = teacherService.findById(currentTeacherId);
            if (currentTeacher != null && currentTeacher.getEmail().equals(course.getTeacherEmail())) {
                canEdit = true;
            }
        }
        model.addAttribute("canEdit", canEdit);

        return "course-details";
    }
    @GetMapping("/course/new")
    public String showCourseForm(Model model) {
        if (currentStudentId == 0) {
            model.addAttribute("error", "Please login to create an course");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        Student student = studentService.findById(currentStudentId);
        if (student == null) {
            model.addAttribute("error", "User not found");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }
        Course course = new Course();
        course.setStudentEmail(student.getEmail()); // Устанавливаем email текущего пользователя
        model.addAttribute("course", course);
        model.addAttribute("isLoggedIn", true);
        return "course-form";
    }



    @GetMapping("/courses/edit/{id}")
    public String editCourseForm(@PathVariable int id, Model model) {


        Student student = studentService.findById(currentStudentId);
        if (student == null) {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }

        Course course = courseService.getCourseById(id);
        if (course == null) {
            model.addAttribute("errorMessage", "course not found");
            return "error";
        }

        if (!course.getStudentEmail().equals(student.getEmail())) {
            model.addAttribute("errorMessage", "You can only edit your own courses");
            return "error";
        }

        model.addAttribute("course", course);
        model.addAttribute("isLoggedIn", true);
        return "course-form";
    }

    @PostMapping("/course/edit/{id}")
    public String updateCourse(@PathVariable int id, @ModelAttribute Course course, Model model) {


        Student student = studentService.findById(currentStudentId);
        if (currentStudentId == 0) {
            model.addAttribute("errorMessage", "Please login to edit courses");
            return "error";
        }
        if (student == null) {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }

        Course existingCourse = courseService.getCourseById(id);
        if (existingCourse == null) {
            model.addAttribute("errorMessage", "course not found");
            return "error";
        }

        if (!existingCourse.getStudentEmail().equals(student.getEmail())) {
            model.addAttribute("errorMessage", "You can only edit your own courses");
            return "error";
        }

        course.setId(id);
        course.setStudentEmail(student.getEmail());
        courseService.saveCourse(course);

        return "redirect:/coursesystem/profile";
    }

    @GetMapping("/api/courses")
    @ResponseBody
    public List<Course> getCoursesApi(@RequestParam(required = false) String category) {
        return category != null ? courseService.getCoursesByCategory(category) : courseService.getCourses();
    }

    @PostMapping("/api/courses")
    @ResponseBody
    public Course createCourseApi(@RequestBody Course course) {
        return courseService.saveCourse(course);
    }




    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable int id, Model model) {
        if (currentStudentId == 0) {
            model.addAttribute("error", "Please login to delete an course");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }

        Student student = studentService.findById(currentStudentId);
        if (student == null) {
            model.addAttribute("error", "User not found");
            model.addAttribute("isLoggedIn", false);
            return "login";
        }

        Course course = courseService.getCourseById(id);
        if (course != null && course.getStudentEmail().equals(student.getEmail())) {
            courseService.deleteCourse(id);
        }

        return "redirect:/coursesystem/profile";
    }
}