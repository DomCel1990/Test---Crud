package com.example.crudtest.services;

import com.example.crudtest.entities.Student;
import com.example.crudtest.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.plaf.LabelUI;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public Student createStudent(Student student){
        return studentRepository.save(student);
    }
    public List<Student> studentsGet(){
        return studentRepository.findAll();
    }
    public Student getSingleStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent())
            return student.get();
        else
            return null;
    }

    public Student updateStudent(Long id, Student student) {
        Optional<Student> student1 = studentRepository.findById(id);
        if (student1.isPresent()) {
            student.setId(id);
            return studentRepository.save(student);
        } else
            return null;
    }

    public Student upDateWorking(Long id, boolean working) {
        Student student = studentRepository.findById(id).get();
        if (student != null) {
            student.setWorking(working);
            return studentRepository.save(student);
        }else return null;

    }
    public void deleteStudent(Long id){
        studentRepository.deleteById(id);
    }
}
