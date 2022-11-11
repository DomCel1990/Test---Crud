package com.example.crudtest.controllers;

import com.example.crudtest.entities.Student;
import com.example.crudtest.repositories.StudentRepository;
import com.example.crudtest.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    @PostMapping
    public Student createStudent(@RequestBody Student student){
        return studentRepository.save(student);
    }
    @GetMapping
    public List<Student> studentsGet(){
        return studentRepository.findAll();
    }
    @GetMapping("/{id}")
    public Student getSingleStudent(@PathVariable Long id){
        return studentService.getSingleStudent(id);
    }
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student){
        return studentService.updateStudent(id,student);
    }
    @PutMapping("/{id}/is-working")
    public Student updateStudentIsWoking(@PathVariable Long id, @RequestParam boolean working){
        return studentService.upDateWorking(id,working);
    }
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id){
        studentRepository.deleteById(id);
    }
}
