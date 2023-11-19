package co.edu.poli.ces3.universitas.dto;

public class StudentDto {
    public int id;

    protected String document;

    private String name;

    public StudentDto(int id, String document, String name){
        this.id = id;
        this.document = document;
        this.name = name;
    }

    public StudentDto(int id){
        this.id = id;
    }
    public StudentDto(String document, String name){
        this.document = document;
        this.name = name;
    }

    public StudentDto(String document){
        this.document = document;
    }

    public StudentDto() {

    }

    public int getId(){
        return this.id;
    }


    private void setId(int id){
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "El estudiante se llama: " + this.name +
                " su documento es: " + this.document;
    }
}