import edu.innotech.Student;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Tests {

    private String baseUrl = "http://localhost:8080";
    @Test
    public void testValidGetRequest() throws IOException {
        Student student = new Student();
        student.setName("TestStudent");
        student.setMarks(Arrays.asList(2,5,7,5,4));
        TestStudentHelper studentApp = new TestStudentHelper(student);
        studentApp.creatStudent();
        RestAssured
                .given()
                    .baseUri(baseUrl)
                    .pathParam("id",student.getId())
                .when()
                    .get("/student/{id}")
                .then()
                    .statusCode(200)
                    .body("id",equalTo(student.getId()))
                    .body("name",equalTo(student.getName()))
                    .body("marks",equalTo(student.getMarks()));
        studentApp.deleteStudent();
    }
    @Test
    public void testInValidGetRequest() throws IOException {
        RestAssured
                .given()
                    .baseUri(baseUrl)
                    .pathParam("id",0)
                .when()
                    .get("/student/{id}")
                .then()
                    .statusCode(404);
    }

    @Test
    public void testPostRequest() throws IOException {
        Student student = new Student();
        student.setId(1);
        student.setName("TestStudent");
        student.setMarks(Arrays.asList(2,5,7,5,4));
        TestStudentHelper studentApp = new TestStudentHelper(student);
        while(studentApp.selectStudent()==200){
            student.setId(student.getId()+1);
        }
        String body = "{\"Id\":"+student.getId()+",\"name\":\""+student.getName()+"\",\"marks\":"+ student.getMarks()+"}";
            RestAssured
                    .given()
                        .baseUri(baseUrl+"/student")
                        .contentType(ContentType.JSON)
                        .body(body)
                    .when()
                        .post()
                    .then()
                        .statusCode(201);
    }
    @Test
    public void testPostRequestUpdate() throws IOException {
        Student student = new Student();
        student.setId(1);
        student.setName("TestStudent");
        student.setMarks(Arrays.asList(2,5,7,5,4));
        TestStudentHelper studentApp = new TestStudentHelper(student);
        while(studentApp.selectStudent()!=200){
            studentApp.creatStudent();
        }
        String body = "{\"Id\":"+student.getId()+",\"name\":\""+student.getName()+"\",\"marks\":"+ student.getMarks()+"}";
        RestAssured
                .given()
                .baseUri(baseUrl+"/student")
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post()
                .then()
                .statusCode(201);
    }
    @Test
    public void testPostRequestAddIdNull() throws IOException {
        Student student = new Student();
        student.setName("TestStudent");
        student.setMarks(Arrays.asList(2,5,7,5,4));
        String body = "{\"name\":\""+student.getName()+"\",\"marks\":"+ student.getMarks()+"}";
        RestAssured
                .given()
                .baseUri(baseUrl+"/student")
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    @Test
    public void testPostRequestAddNameNull() throws IOException {
        Student student = new Student();
        student.setMarks(Arrays.asList(2,5,7,5,4));
        String body = "{\"Id\":"+student.getId()+",\"marks\":"+ student.getMarks()+"}";
        RestAssured
                .given()
                .baseUri(baseUrl+"/student")
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post()
                .then()
                .statusCode(400);
    }
    @Test
    public void testValidDeleteRequest() throws IOException {
        Student student = new Student();
        student.setId(1);
        student.setName("TestStudent");
        student.setMarks(Arrays.asList(2,5,7,5,4));
        TestStudentHelper studentApp = new TestStudentHelper(student);
        studentApp.creatStudent();
        RestAssured
                .given()
                    .baseUri(baseUrl)
                    .pathParam("id",student.getId())
                .when()
                    .delete("/student/{id}")
                .then()
                    .statusCode(200);
    }

    @Test
    public void testInValidDeleteRequest() throws IOException {
        Student student = new Student();
        student.setId(-1);
        RestAssured
                .given()
                    .baseUri(baseUrl)
                    .pathParam("id",student.getId())
                .when()
                    .delete("/student/{id}")
                .then()
                    .statusCode(404);
    }

    @Test
    public void testTopStudentRequest() throws IOException {
        Student student = new Student();
        student.setId(1);
        TestStudentHelper studentApp = new TestStudentHelper(student);
        while(studentApp.selectStudent()==200){
            studentApp.deleteStudent();
            student.setId(student.getId()+1);
        }
        RestAssured
                .given()
                    .baseUri(baseUrl)
                .when()
                    .get("/topStudent")
                .then()
                    .statusCode(200)
                    .body(isEmptyOrNullString());
    }

    @Test
    public void testTopStudentNullMarksRequest() throws IOException {
        Student student = new Student();
        student.setId(1);
        student.setName("TestStudent");
        TestStudentHelper studentApp = new TestStudentHelper(student);
        studentApp.creatStudent();
        RestAssured
                .given()
                    .baseUri(baseUrl)
                .when()
                    .get("/topStudent")
                .then()
                    .statusCode(200)
                    .body(isEmptyOrNullString());
    }

    @Test
    public void testTopStudentMoreMarks(){

    }





}




