package co.edu.poli.ces3.universitas.model;

import co.edu.poli.ces3.universitas.dto.StudentDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CRUD {
    abstract Student create(StudentDto student) throws SQLException;

    public ArrayList<Student> all() throws SQLException;

    public Student findByPk(int id) throws SQLException;

    public int update(StudentDto studentDto) throws SQLException;

    public int delete(int id) throws SQLException;

    public int patch(StudentDto studentDto) throws SQLException;
}
