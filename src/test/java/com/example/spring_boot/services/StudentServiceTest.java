package com.example.spring_boot.services;

import com.example.spring_boot.exceptions.EntityNotFoundException;
import com.example.spring_boot.mappers.StudentMapper;
import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.StudentCourseEnrollment;
import com.example.spring_boot.models.StudentRequest;
import com.example.spring_boot.models.StudentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentServiceTest {

    private StudentMapper studentMapper;
    private StudentService studentService;
    private Course course;
    private StudentResponse studentResponse;
    private StudentRequest studentRequest;
    private StudentResponse actualStudentResponse;

    @BeforeEach
    void setUp() {
        studentMapper = Mockito.mock(StudentMapper.class);
        studentService = new StudentServiceImpl(studentMapper);

        course = new Course("java", "java course", 4);
        Course actualCourse = new Course("java", "java course", 4);
        studentRequest = new StudentRequest(1L, "Mick", 23, 14, 15, "java", 4);
        studentResponse = new StudentResponse(studentRequest, course);
        actualStudentResponse = new StudentResponse(studentRequest, actualCourse);
    }

    @Test
    void saveStudent() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(course);
        StudentResponse expectedStudent = studentService.saveStudent(studentRequest);
        Mockito.verify(studentMapper).insertStudent(studentRequest);
        assertEquals(expectedStudent, actualStudentResponse);
    }

    @Test
    void saveStudentException() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(null);

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.saveStudent(studentRequest));

        Mockito.verify(studentMapper, Mockito.times(0)).insertStudent(studentRequest);

        assertEquals(ex.getMessage(), "Course not found");
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(studentMapper.selectByIdStudent(studentResponse.getId())).thenReturn(studentResponse);

        StudentResponse expectedStudent = studentService.getByIdStudent(studentResponse.getId());

        Mockito.verify(studentMapper, Mockito.times(1)).selectByIdStudent(studentResponse.getId());
        Mockito.verifyNoMoreInteractions(studentMapper);

        assertEquals(expectedStudent, actualStudentResponse);
    }

    @Test
    void getByIdException() {
        Mockito.when(studentMapper.selectByIdStudent(studentResponse.getId())).thenReturn(null);

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.getByIdStudent(studentResponse.getId()));

        assertEquals(ex.getMessage(), "Student not found");
    }

    @Test
    void updateSuccess() {
        Mockito.when(studentMapper.selectByIdStudent(studentResponse.getId())).thenReturn(studentResponse);
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(course);

        StudentResponse expectedStudent = studentService.updateStudent(studentResponse.getId(), studentRequest);

        Mockito.verify(studentMapper,
                Mockito.times(1)).updateStudent(studentRequest);

        assertEquals(expectedStudent, actualStudentResponse);
    }

    @Test
    void updateException() {
        Mockito.when(studentMapper.selectByIdStudent(studentResponse.getId())).thenReturn(null);

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.updateStudent(studentResponse.getId(), studentRequest));

        Mockito.verify(studentMapper,
                Mockito.times(0)).updateStudent(studentRequest);

        assertEquals(ex.getMessage(), "Student not found");
    }

    @Test
    void updateCourseException() {
        Mockito.when(studentMapper.selectByIdStudent(studentResponse.getId())).thenReturn(studentResponse);
        studentRequest.setCourse("kek");
        Mockito.when(studentMapper.selectByNameCourse(studentRequest.getCourse())).thenReturn(null);

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.updateStudent(studentResponse.getId(), studentRequest));

        Mockito.verify(studentMapper,
                Mockito.times(0)).updateStudent(studentRequest);

        assertEquals(ex.getMessage(), "Course not found");
    }

    @Test
    void deleteByIdSuccess() {
        Mockito.when(studentMapper.selectByIdStudent(studentResponse.getId())).thenReturn(studentResponse);
        studentService.deleteByIdStudent(studentResponse.getId());
        Mockito.verify(studentMapper, Mockito.times(1)).deleteByIdStudent(studentResponse.getId());
    }

    @Test
    void getAll() {
        List<StudentResponse> list = new ArrayList<>();
        list.add(studentResponse);
        Mockito.when(studentMapper.selectAllStudent()).thenReturn(list);
        List<StudentResponse> list1 = studentService.getAllStudent();
        Mockito.verify(studentMapper, Mockito.times(1)).selectAllStudent();
        assertEquals(list, list1);
    }

    @Test
    void studentCourseEnrollmentSuccess() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(course);
        Mockito.when(studentMapper.selectByIdStudent(1L)).thenReturn(studentResponse);
        Mockito.when(studentMapper.selectByIdStudent(2L)).thenReturn(studentResponse);

        StudentCourseEnrollment studentCourseEnrollment = new StudentCourseEnrollment();
        studentCourseEnrollment.setCourse(course.getName());
        studentCourseEnrollment.setListStudent(Arrays.asList(1L, 2L));

        studentService.studentCourseEnrollment(studentCourseEnrollment);

        Mockito.verify(studentMapper,
                Mockito.times(2)).updateStudent(studentRequest);
    }

    @Test
    void studentCourseEnrollmentExceptionGrade() {
        Course courseJava = new Course("java", "java course", 4);
        Course coursePython = new Course("python", "java course", 4);

        StudentResponse studentResponse1 =
                new StudentResponse(1L, "Mick", 23, 14, 15, 4, courseJava);
        StudentResponse studentResponse2 =
                new StudentResponse(2L, "Mick", 23, 14, 15, 3, courseJava);

        Mockito.when(studentMapper.selectByNameCourse(coursePython.getName())).thenReturn(coursePython);
        Mockito.when(studentMapper.selectByIdStudent(studentResponse1.getId())).thenReturn(studentResponse1);
        Mockito.when(studentMapper.selectByIdStudent(studentResponse2.getId())).thenReturn(studentResponse2);

        StudentCourseEnrollment studentCourseEnrollment = new StudentCourseEnrollment();
        studentCourseEnrollment.setCourse(coursePython.getName());
        studentCourseEnrollment.setListStudent(Arrays.asList(studentResponse1.getId(), studentResponse2.getId()));

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.studentCourseEnrollment(studentCourseEnrollment));

        Mockito.verify(studentMapper,
                Mockito.times(0)).updateStudent(studentRequest);

        assertEquals(ex.getMessage(), "StudentResponse id: " + studentResponse2.getId() + " grade < course requiredGrade");
    }

    @Test
    void studentCourseEnrollmentExceptionCourse() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(null);

        StudentCourseEnrollment studentCourseEnrollment = new StudentCourseEnrollment();
        studentCourseEnrollment.setCourse("java");
        studentCourseEnrollment.setListStudent(Arrays.asList(1L, 2L));

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.studentCourseEnrollment(studentCourseEnrollment));

        Mockito.verify(studentMapper,
                Mockito.times(0)).updateStudent(studentRequest);

        assertEquals(ex.getMessage(), "Course not found");
    }

    @Test
    void studentCourseEnrollmentExceptionStudent() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(course);
        Mockito.when(studentMapper.selectByIdStudent(1L)).thenReturn(null);

        StudentCourseEnrollment studentCourseEnrollment = new StudentCourseEnrollment();
        studentCourseEnrollment.setCourse(course.getName());
        studentCourseEnrollment.setListStudent(Arrays.asList(1L, 2L));

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.studentCourseEnrollment(studentCourseEnrollment));

        Mockito.verify(studentMapper,
                Mockito.times(0)).updateStudent(studentRequest);

        assertEquals(ex.getMessage(), "Student not found");
    }
}