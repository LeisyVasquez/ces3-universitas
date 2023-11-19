package co.edu.poli.ces3.universitas.servlet;

import co.edu.poli.ces3.universitas.controller.StudentCtr;
import co.edu.poli.ces3.universitas.dto.StudentDto;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.ServletException;
import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;



@WebServlet(name = "studentServlet", value = "/student")
public class StudentServlet extends MyServlet {
    private Gson gson;
    private GsonBuilder gsonBuilder;
    public void init() {
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    //Hago una sobrescripción sobre este método para poder realizar un PATCH,
    //ya que por defecto no se encuentra habilitado.
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        long lastModified;
        switch (method) {
            case "GET":
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
                break;
            case "POST":
                this.doPost(req, resp);
                break;
            case "PUT":
                this.doPut(req, resp);
                break;
            case "DELETE":
                this.doDelete(req, resp);
                break;
            case "PATCH":
                this.doPatch(req, resp);
                break;
            default:
                //errArgs privado
                //String errMsg = lStrings.getString("http.method_not_implemented");
                Object[] errArgs = new Object[]{method};
                String errMsg = MessageFormat.format("Error", errArgs);
                resp.sendError(501, errMsg);
                break;
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");


        StudentCtr studentCtr = new StudentCtr();

        String studentId = req.getParameter("studentId");
        if (studentId == null) {
            ArrayList<StudentDto> studentsDto = studentCtr.getAllStudents();
            out.println(this.gson.toJson(studentsDto));
        } else {
            StudentDto studentDto = studentCtr.getStudentById(Integer.parseInt(studentId));
            if (studentDto != null) {
                out.println(this.gson.toJson(studentDto));
            } else {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setContentType("application/json");

        StudentCtr studentCtr = new StudentCtr();

        JsonObject body = this.getParamsFromPost(req);
        StudentDto studentDto = new StudentDto(body.get("document").getAsString(), body.get("name").getAsString());
        StudentDto newStudent = studentCtr.addStudent(studentDto);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        out.println(gson.toJson(newStudent));
        out.flush();
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setContentType("application/json");

        StudentCtr studentCtr = new StudentCtr();

        String studentId = req.getParameter("studentId");
        int seEliminoEstudiante = studentCtr.deleteStudentById(Integer.parseInt(studentId));
        if (seEliminoEstudiante == 1) {
            out.println("Estudiante con id " + studentId + " eliminado");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("Estudiante no encontrado");
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setContentType("application/json");
        StudentCtr studentCtr = new StudentCtr();

        String studentId = request.getParameter("studentId");
        JsonObject body = this.getParamsFromPost(request);


        // Verificar que el usuario si exista
        StudentDto studentBefore = studentCtr.getStudentById(Integer.parseInt(studentId));

        if (studentBefore != null) {
            int seActalizoEstudiante = studentCtr.updateStudent(new StudentDto(Integer.parseInt(studentId), body.get("document").getAsString(), body.get("name").getAsString()));
            if (seActalizoEstudiante == 1) {
                out.println("Estudiante con id " + studentId + " actualizado");
            } else {
                out.println("Ocurrió un error inesperado");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("Estudiante no encontrado");
        }
        out.flush();
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setContentType("application/json");
        StudentCtr studentCtr = new StudentCtr();

        String studentId = request.getParameter("studentId");
        JsonObject body = this.getParamsFromPost(request);

        StudentDto studentBefore = studentCtr.getStudentById(Integer.parseInt(studentId));

        if (studentBefore != null) {
            StudentDto studentAfter = new StudentDto(Integer.parseInt(studentId));

            if (body.has("document")) {
                String newDocument = body.get("document").getAsString();
                studentAfter.setDocument(newDocument);
            }

            if (body.has("name")) {
                String newName = body.get("name").getAsString();
                studentAfter.setName(newName);
            }
            int seActalizoEstudiante = studentCtr.patchStudentById(studentAfter);

            if(seActalizoEstudiante == 1) {
                out.println("Estudiante con id " + studentId + " actualizado");
            } else {
                out.println("Ocurrió un error inesperado");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("Estudiante no encontrado");
        }
    }
}