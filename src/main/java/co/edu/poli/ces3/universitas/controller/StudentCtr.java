package co.edu.poli.ces3.universitas.controller;

import co.edu.poli.ces3.universitas.model.Student;
import co.edu.poli.ces3.universitas.dto.StudentDto;

import java.sql.SQLException;
import java.util.ArrayList;

public class StudentCtr {

    private Student modelStudent;

    public StudentCtr() {
        modelStudent = new Student();
    }

    public StudentDto addStudent(StudentDto student) {
        try {
            Student newStudent = modelStudent.create(student);
            return new StudentDto(newStudent.getId(), newStudent.getDocument(), newStudent.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<StudentDto> getAllStudents() {
        try {
            ArrayList<Student> students = modelStudent.all();
            ArrayList<StudentDto> studentsDto = new ArrayList<>();
            for (Student student : students) {
                studentsDto.add(new StudentDto(student.getId(), student.getDocument(), student.getName()));
            }
            return studentsDto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public StudentDto getStudentById(int id) {
        try {
            Student student = modelStudent.findByPk(id);
            if(student != null) {
                StudentDto studentDto = new StudentDto(student.getId(), student.getName(), student.getDocument());
                return studentDto;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateStudent(StudentDto beforeStudent) {
        try {
            return modelStudent.update(beforeStudent);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteStudentById(int id) {
        try {
            return modelStudent.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int patchStudentById(StudentDto beforeStudent) {
        try {
            return modelStudent.patch(beforeStudent);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
