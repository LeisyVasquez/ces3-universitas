package co.edu.poli.ces3.universitas.servlet;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.sql.*;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.ServletException;
import model.Student;

@WebServlet(name = "studentServlet", value = "/student")
public class StudentServlet extends MyServlet {
    private Gson gson;
    private GsonBuilder gsonBuilder;
    private ArrayList<Student> students;

    public void init() {
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        students = new ArrayList<>();
        Student student1 = new Student();
        student1.id = 10;
        student1.setName("Pedro");
        student1.setDocument("45787");

        students.add(student1);

        for (int i = 0; i < students.size(); i++) {
            System.out.println(students.get(i).getName());
        }

    }

    //Hago una sobrescripción sobre este método para poder realizar un PATCH,
    //ya que por defecto no se encuentra habilitado.
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        long lastModified;
        if (method.equals("GET")) {
            lastModified = this.getLastModified(req);
            if (lastModified == -1L) {
                this.doGet(req, resp);
            } else {
                long ifModifiedSince = req.getDateHeader("If-Modified-Since");
                if (ifModifiedSince < lastModified) {
                    //Método privado
                    // this.maybeSetLastModified(resp, lastModified);
                    this.doGet(req, resp);
                } else {
                    resp.setStatus(304);
                }
            }
        } else if (method.equals("POST")) {
            this.doPost(req, resp);
        } else if (method.equals("PUT")) {
            this.doPut(req, resp);
        } else if (method.equals("DELETE")) {
            this.doDelete(req, resp);
        } else if (method.equals("PATCH")) {
            this.doPatch(req, resp);
        } else {
            //errArgs privado
            //String errMsg = lStrings.getString("http.method_not_implemented");
            Object[] errArgs = new Object[]{method};
            String errMsg = MessageFormat.format("Error", errArgs);
            resp.sendError(501, errMsg);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String studentId = request.getParameter("studentId");
        PrintWriter out = response.getWriter();

        if (studentId == null) {
            out.println(this.gson.toJson(this.students));
        } else {
            Student studentResult = null;
            for (Student student : students) {
                if (student.getId() == Integer.parseInt(studentId)) {
                    studentResult = student;
                    break;
                }
            }
            if (studentResult != null) {
                out.println(this.gson.toJson(studentResult));
            } else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setContentType("application/json");
        JsonObject body = this.getParamsFromPost(req);

        Student std = new Student(
                body.get("id").getAsInt(),
                body.get("document").getAsString(),
                body.get("name").getAsString()
        );
        this.students.add(std);
        out.println(gson.toJson(std));
        resp.setStatus(HttpServletResponse.SC_CREATED);
        out.flush(); //Libera los recursos que se usaron
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setContentType("application/json");
        String studentId = request.getParameter("studentId");
        Boolean seEliminoEstudiante = false;

        for (Iterator<Student> iterator = students.iterator(); iterator.hasNext(); ) {
            Student currentStudent = iterator.next();
            if (currentStudent.getId() == Integer.parseInt(studentId)) {
                iterator.remove();
                seEliminoEstudiante = true;
                break;
            }
        }

        if (seEliminoEstudiante) {
            out.println("Estudiante con id " + studentId + " eliminado");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("Estudiante no encontrado");
        }
        out.flush(); //Libera los recursos que se usaron
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setContentType("application/json");
        String studentId = request.getParameter("studentId");
        JsonObject body = this.getParamsFromPost(request);
        Boolean seActalizoEstudiante = false;

        Student afterStudent = new Student(Integer.parseInt(studentId),
                body.get("document").getAsString(),
                body.get("name").getAsString());

        for (int i = 0; i < students.size(); i++) {
            Student currentStudent = students.get(i);
            if (currentStudent.getId() == Integer.parseInt(studentId)) {
                students.set(i, afterStudent);
                seActalizoEstudiante = true;
            }
        }

        if (seActalizoEstudiante) {
            out.println("Estudiante con id " + studentId + " actualizado");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("Estudiante no encontrado");
        }
        out.flush(); //Libera los recursos que se usaron
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setContentType("application/json");
        String studentId = request.getParameter("studentId");
        JsonObject body = this.getParamsFromPost(request);

        // Buscar al estudiante por ID en el ArrayList
        Student studentToUpdate = null;
        for (Student student : students) {
            if (student.getId() == Integer.parseInt(studentId)) {
                studentToUpdate = student;
                break;
            }
        }
        if (studentToUpdate != null) {
            // Obtener los parámetros de actualización de la solicitud
            if (body.has("document")) {
                String newDocument = body.get("document").getAsString();
                studentToUpdate.setDocument(newDocument);
            }

            if (body.has("name")) {
                String newName = body.get("name").getAsString();
                studentToUpdate.setName(newName);
            }

            out.println("Estudiante con id " + studentId + " actualizado");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("Estudiante no encontrado");
        }
    }
}