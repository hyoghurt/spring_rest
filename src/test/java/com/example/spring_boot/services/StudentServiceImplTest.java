package com.example.spring_boot.services;

import com.example.spring_boot.exceptions.EntityNotFoundException;
import com.example.spring_boot.mappers.StudentCourseMapper;
import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.Student;
import com.example.spring_boot.models.StudentCourseEnrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceImplTest {

    private StudentCourseMapper studentCourseMapper;
    private StudentService studentService;
    private Course course;
    private Student student;
    private Student actualStudent;

    @BeforeEach
    void setUp() {
        studentCourseMapper = Mockito.mock(StudentCourseMapper.class);
        studentService = new StudentServiceImpl(studentCourseMapper);

        course = new Course("java", "java course", 4);
        student = new Student(1L, "Mick", 23, 14, 15, course, 4);

        Course actualCourse = new Course();
        actualCourse.setName(course.getName());
        actualCourse.setDescription(course.getDescription());
        actualCourse.setRequiredGrade(course.getRequiredGrade());

        actualStudent = new Student();
        actualStudent.setId(student.getId());
        actualStudent.setName(student.getName());
        actualStudent.setAge(student.getAge());
        actualStudent.setGrade(student.getGrade());
        actualStudent.setTimeFrom(student.getTimeFrom());
        actualStudent.setTimeTo(student.getTimeTo());
        actualStudent.setCourse(actualCourse);
    }

    @Test
    void add() {
        studentService.add(student);
        Mockito.verify(studentCourseMapper).insertStudent(student);
        assertEquals(student, actualStudent);
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(studentCourseMapper.selectByIdStudent(student.getId())).thenReturn(student);

        Student expectedStudent = studentService.getById(student.getId());

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).selectByIdStudent(student.getId());
        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void getByIdException() {
        Mockito.when(studentCourseMapper.selectByIdStudent(student.getId())).thenReturn(null);

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.getById(student.getId()));

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).selectByIdStudent(student.getId());
        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(ex.getMessage(), "Student not found");
    }

    @Test
    void updateSuccess() {
        Mockito.when(studentCourseMapper.selectByIdStudent(student.getId())).thenReturn(student);

        Student expectedStudent = studentService.update(student.getId(), student);

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).selectByIdStudent(student.getId());
        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).updateStudent(student);
        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void updateException() {
        Mockito.when(studentCourseMapper.selectByIdStudent(student.getId()))
                .thenThrow(new EntityNotFoundException("Student not found"));

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.update(student.getId(), student));

        Mockito.verify(studentCourseMapper, Mockito.times(1)).selectByIdStudent(student.getId());
        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(ex.getMessage(), "Student not found");
    }

    @Test
    void deleteByIdSuccess() {
        Mockito.when(studentCourseMapper.selectByIdStudent(student.getId())).thenReturn(student);
        studentService.deleteById(student.getId());

        Mockito.verify(studentCourseMapper, Mockito.times(1)).selectByIdStudent(student.getId());
        Mockito.verify(studentCourseMapper, Mockito.times(1)).deleteByIdStudent(student.getId());
        Mockito.verifyNoMoreInteractions(studentCourseMapper);
    }

    @Test
    void deleteByIdException() {
        Mockito.when(studentCourseMapper.selectByIdStudent(student.getId()))
                .thenThrow(new EntityNotFoundException("Student not found"));

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.deleteById(student.getId()));

        Mockito.verify(studentCourseMapper, Mockito.times(1)).selectByIdStudent(student.getId());
        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(ex.getMessage(), "Student not found");
    }

    @Test
    void getAll() {
        List<Student> list = new ArrayList<>();
        list.add(student);

        Mockito.when(studentCourseMapper.selectAllStudent()).thenReturn(list);

        List<Student> list1 = studentService.getAll();

        Mockito.verify(studentCourseMapper, Mockito.times(1)).selectAllStudent();
        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(list, list1);
    }

    @Test
    void studentCourseEnrollmentSuccess() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);
        Mockito.when(studentCourseMapper.selectByIdStudent(1L)).thenReturn(student);
        Mockito.when(studentCourseMapper.selectByIdStudent(2L)).thenReturn(student);

        StudentCourseEnrollment studentCourseEnrollment = new StudentCourseEnrollment();
        studentCourseEnrollment.setCourse(course.getName());
        studentCourseEnrollment.setListStudent(Arrays.asList(1L, 2L));

        studentService.studentCourseEnrollment(studentCourseEnrollment);

        Mockito.verify(studentCourseMapper,
                Mockito.times(2)).updateStudent(student);
    }

    @Test
    void studentCourseEnrollmentExceptionGrade() {
        Course courseJava = new Course("java", "java course", 4);
        Course coursePython = new Course("python", "java course", 4);
        Student student1 = new Student(1L, "Mick", 23, 14, 15, courseJava, 4);
        Student student2 = new Student(2L, "Mick", 23, 14, 15, courseJava, 3);

        Mockito.when(studentCourseMapper.selectByNameCourse(coursePython.getName())).thenReturn(coursePython);
        Mockito.when(studentCourseMapper.selectByIdStudent(student1.getId())).thenReturn(student1);
        Mockito.when(studentCourseMapper.selectByIdStudent(student2.getId())).thenReturn(student2);

        StudentCourseEnrollment studentCourseEnrollment = new StudentCourseEnrollment();
        studentCourseEnrollment.setCourse(coursePython.getName());
        studentCourseEnrollment.setListStudent(Arrays.asList(student1.getId(), student2.getId()));

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.studentCourseEnrollment(studentCourseEnrollment));

        assertEquals(ex.getMessage(), "Student id: " + student2.getId() + " grade < course requiredGrade");
    }

    @Test
    void studentCourseEnrollmentExceptionCourse() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(null);

        StudentCourseEnrollment studentCourseEnrollment = new StudentCourseEnrollment();
        studentCourseEnrollment.setCourse("java");
        studentCourseEnrollment.setListStudent(Arrays.asList(1L, 2L));

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.studentCourseEnrollment(studentCourseEnrollment));

        Mockito.verify(studentCourseMapper, Mockito.times(1)).selectByNameCourse("java");
        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(ex.getMessage(), "Course not found");
    }

    @Test
    void studentCourseEnrollmentExceptionStudent() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);
        Mockito.when(studentCourseMapper.selectByIdStudent(1L))
                .thenThrow(new EntityNotFoundException("Student not found"));

        StudentCourseEnrollment studentCourseEnrollment = new StudentCourseEnrollment();
        studentCourseEnrollment.setCourse(course.getName());
        studentCourseEnrollment.setListStudent(Arrays.asList(1L, 2L));

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.studentCourseEnrollment(studentCourseEnrollment));

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).selectByNameCourse(course.getName());
        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).selectByIdStudent(1L);
        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(ex.getMessage(), "Student not found");
    }
}