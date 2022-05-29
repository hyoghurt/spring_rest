package com.example.spring_boot.services;

import com.example.spring_boot.exceptions.EntityNotFoundException;
import com.example.spring_boot.mappers.StudentCourseMapper;
import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.Student;
import com.example.spring_boot.models.StudentCourseEnrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{
    private final StudentCourseMapper studentCourseMapper;

    @Autowired
    public StudentServiceImpl(StudentCourseMapper studentCourseMapper) {
        this.studentCourseMapper = studentCourseMapper;
    }

    @Override
    public Student add(Student student) {
        studentCourseMapper.insertStudent(student);
        return student;
    }

    @Override
    public Student getById(Long id) {
        Student student = studentCourseMapper.selectByIdStudent(id);
        if (student == null) {
            throw new EntityNotFoundException("Student not found: " + id);
        }
        return student;
    }

    @Override
    @Transactional
    public Student update(Long id, Student student) {
        if (studentCourseMapper.selectByIdStudent(id) == null) {
            studentCourseMapper.insertStudent(student);
        } else {
            student.setId(id);
            studentCourseMapper.updateStudent(student);
        }
        return student;
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        studentCourseMapper.deleteByIdStudent(id);
    }

    @Override
    public List<Student> getAll() {
        return studentCourseMapper.selectAllStudent();
    }

    @Override
    @Transactional
    public void studentCourseEnrollment(StudentCourseEnrollment sce) {
        Course course = studentCourseMapper.selectByNameCourse(sce.getCourse());
        if (course == null) {
            throw new EntityNotFoundException("course not found: " + sce.getCourse());
        }
        for (Long id : sce.getListStudent()) {
            Student student = getById(id);
            if (student.getGrade() < course.getRequiredGrade()) {
                throw new EntityNotFoundException("Student id: "
                        + student.getId() + " grade < course requiredGrade");
            }
            student.setCourse(course);
            update(id, student);
        }
    }
}
