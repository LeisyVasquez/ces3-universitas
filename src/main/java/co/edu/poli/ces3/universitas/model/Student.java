package co.edu.poli.ces3.universitas.model;

import co.edu.poli.ces3.universitas.dto.StudentDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Student extends ConnectionMySQL implements CRUD {
    int id;
    String document;
    String name;

    public Student() {
    }

    public Student(int id, String document, String name) {
        this.id = id;
        this.document = document;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "El estudiante se llama: " + this.name + " su documento es: " + this.document;
    }

    @Override
    public Student create(StudentDto studentDto) throws SQLException {
        Connection cnn = this.getConnectionMySQL();
        if (cnn != null) {
            String sql = "INSERT INTO usuarios(document, name) VALUES(?,?);";
            try {
                PreparedStatement stmt = cnn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, studentDto.getDocument());
                stmt.setString(2, studentDto.getName());
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                this.id = rs.getInt(1);
                this.document = studentDto.getDocument();
                this.name = studentDto.getName();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                cnn.close();
            }
            return this;
        }
        return null;
    }

    @Override
    public ArrayList<Student> all() throws SQLException {
        Connection cnn = this.getConnectionMySQL();
        if (cnn != null) {
            try {
                ArrayList<Student> list = new ArrayList<>();
                String sql = "SELECT * FROM usuarios ORDER BY name;";
                PreparedStatement stmt = cnn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    list.add(new Student(rs.getInt("id"), rs.getString("document"), rs.getString("name")));
                }
                return list;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                cnn.close();
            }
        }
        return null;
    }

    @Override
    public Student findByPk(int id) throws SQLException {
        Connection cnn = this.getConnectionMySQL();
        if (cnn != null) {
            try {
                String sql = "SELECT * FROM usuarios WHERE id = ?;";
                PreparedStatement stmt = cnn.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    this.id = rs.getInt("id");
                    this.name =   rs.getString("name");
                    this.document = rs.getString("document");
                    return this;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                cnn.close();
            }

        }
        return null;
    }

    @Override
    public int update(StudentDto studentDto) throws SQLException{
        Connection cnn = this.getConnectionMySQL();
        if (cnn != null) {
            try {
                String sql = "UPDATE usuarios SET document = ?, name = ? WHERE id = ?;";
                PreparedStatement stmt = cnn.prepareStatement(sql);
                stmt.setString(1, studentDto.getDocument());
                stmt.setString(2, studentDto.getName());
                stmt.setInt(3, studentDto.getId());
                return stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                cnn.close();
            }
        }
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException  {
        Connection cnn = this.getConnectionMySQL();
        if (cnn != null) {
            try {
                String sql = "DELETE FROM usuarios WHERE id = ?;";
                PreparedStatement stmt = cnn.prepareStatement(sql);
                stmt.setInt(1, id);
                return stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                cnn.close();
            }
        }
        return 0;
    }

    @Override
    public int patch(StudentDto studentDto) throws SQLException {
        Connection cnn = this.getConnectionMySQL();
        if(cnn != null) {
            try {
                if(studentDto.getDocument() != null & studentDto.getName() != null) {
                    String sql = "UPDATE usuarios SET document = ?, name = ? WHERE id = ?;";
                    PreparedStatement stmt = cnn.prepareStatement(sql);
                    stmt.setString(1, studentDto.getDocument());
                    stmt.setString(2, studentDto.getName());
                    stmt.setInt(3, studentDto.getId());
                    return stmt.executeUpdate();
                } else if(studentDto.getName() != null) {
                    String sql = "UPDATE usuarios SET name = ? WHERE id = ?;";
                    PreparedStatement stmt = cnn.prepareStatement(sql);
                    stmt.setString(1, studentDto.getName());
                    stmt.setInt(2, studentDto.getId());
                    return stmt.executeUpdate();
                } else {
                    String sql = "UPDATE usuarios SET document = ? WHERE id = ?;";
                    PreparedStatement stmt = cnn.prepareStatement(sql);
                    stmt.setString(1, studentDto.getDocument());
                    stmt.setInt(2, studentDto.getId());
                    return stmt.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                cnn.close();
            }
        }
        return 0;
    }
}
