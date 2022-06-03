package com.example.spring_boot.services;

import com.example.spring_boot.exceptions.EntityNotFoundException;
import com.example.spring_boot.mappers.StudentMapper;
import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.StudentResponse;
import com.example.spring_boot.models.StudentCourseEnrollment;
import com.example.spring_boot.models.StudentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService{
    private final StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public List<Course> getAllCourse() {
        return studentMapper.selectAllCourse();
    }

    @Override
    @Cacheable(value = "course", key = "#name")
    public Course getByNameCourse(String name) {
        log.info("------ get By name course: " + name);
        Course course = studentMapper.selectByNameCourse(name);
        if (course == null) {
            throw new EntityNotFoundException("Course not found");
        }
        return course;
    }

    @Override
    @Transactional
    @Cacheable(value = "course", key = "#course.name")
    public Course saveCourse(Course course) {
        log.info("------ save course: " + course.getName());
        if (studentMapper.selectByNameCourse(course.getName()) == null) {
            studentMapper.insertCourse(course);
        }
        return course;
    }

    @Override
    @Transactional
    @CachePut(value = "course", key = "#name")
    public Course updateCourse(String name, Course course) {
        getByNameCourse(name);
        course.setName(name);
        studentMapper.updateCourse(course);
        return course;
    }

    @Override
    @CacheEvict(value = "course", key = "#name")
    public void deleteByNameCourse(String name) {
        studentMapper.deleteByNameCourse(name);
    }

    @Override
    public List<StudentResponse> getAllStudent() {
        return studentMapper.selectAllStudent();
    }

    @Override
    public StudentResponse getByIdStudent(Long id) {
        StudentResponse studentResponse = studentMapper.selectByIdStudent(id);
        if (studentResponse == null) {
            throw new EntityNotFoundException("Student not found");
        }
        return studentResponse;
    }

    @Override
    @Transactional
    public StudentResponse saveStudent(StudentRequest studentRequest) {
        Course course = getByNameCourse(studentRequest.getCourse());
        studentMapper.insertStudent(studentRequest);
        return new StudentResponse(studentRequest, course);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest studentRequest) {
        StudentResponse studentResponse = getByIdStudent(id);
        Course course = studentResponse.getCourse();
        if (!studentRequest.getCourse().equals(studentResponse.getCourse().getName())) {
            course = getByNameCourse(studentRequest.getCourse());
        }
        studentMapper.updateStudent(studentRequest);
        studentResponse.setName(studentRequest.getName());
        studentResponse.setAge(studentRequest.getAge());
        studentResponse.setTimeFrom(studentRequest.getTimeFrom());
        studentResponse.setTimeTo(studentRequest.getTimeTo());
        studentResponse.setGrade(studentRequest.getGrade());
        studentResponse.setCourse(course);
        return studentResponse;
    }

    @Override
    public void deleteByIdStudent(Long id) {
        studentMapper.deleteByIdStudent(id);
    }

    @Override
    @Transactional
    public void studentCourseEnrollment(StudentCourseEnrollment sce) {
        Course course = getByNameCourse(sce.getCourse());
        for (Long id : sce.getListStudent()) {
            StudentResponse studentResponse = getByIdStudent(id);
            if (studentResponse.getGrade() < course.getRequiredGrade()) {
                throw new EntityNotFoundException("StudentResponse id: "
                        + studentResponse.getId() + " grade < course requiredGrade");
            }
            studentResponse.setCourse(course);
            updateStudent(id, new StudentRequest(studentResponse));
        }
    }
}
