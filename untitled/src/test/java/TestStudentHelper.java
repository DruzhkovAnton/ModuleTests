import edu.innotech.Student;
import io.restassured.RestAssured;
import org.hamcrest.Matcher;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;

public class TestStudentHelper {
    private String baseUrl = "http://localhost:8080";
    private Student student;
    private int studentId = 1;

    public TestStudentHelper(Student student){
        this.student = student;
    }

    public int selectStudent()throws IOException {
        // URI для отправки запроса
        URL url = new URL(baseUrl+"/student/"+student.getId());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        // Установка заголовка Content-Type (если необходимо)
        connection.setRequestProperty("Content-Type", "application/json");
        // Получение кода ответа в результате выполнения запроса
        int statusCode = connection.getResponseCode();
        System.out.println("Status Code: " + statusCode);
        // Закрытие соединения
        connection.disconnect();
        return statusCode;
    }

    public void deleteStudent()throws IOException {
        // URI для отправки запроса
        URL url = new URL(baseUrl+"/student/"+student.getId());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        // Установка заголовка Content-Type (если необходимо)
        connection.setRequestProperty("Content-Type", "application/json");
        // Получение кода ответа в результате выполнения запроса
        int responseCode = connection.getResponseCode();
        // Обработка кода ответа
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {

            System.out.println("Deleted successfully. Response Code: " + responseCode);
        } else {
            // Ошибка во время удаления
            System.out.println("Failed to delete. Response Code: " + responseCode);
        }
        // Закрытие соединения
        connection.disconnect();
    }

    public void creatStudent(){
        String body = "{\"Id\":"+student.getId()+",\"name\":\""+student.getName()+"\",\"marks\":"+ student.getMarks()+"}";
        try {
            // Создаем URL из строки
            URL url = new URL(baseUrl+"/student");
            // Открываем соединение
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Устанавливаем метод запроса на POST
            connection.setRequestMethod("POST");
            // Устанавливаем заголовок Content-Type на application/json, указывающий на формат данных, который мы отправляем
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            // Устанавливаем что мы хотим получить ответ от сервера
            connection.setDoOutput(true);
            // Отправляем JSON данные
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            // Получаем статус код от сервера
            int statusCode = connection.getResponseCode();
            System.out.println("Status Code: " + statusCode);
            // Читаем ответ от сервера
            try (java.io.InputStream inputStream = (statusCode >= 400) ? connection.getErrorStream() : connection.getInputStream();
                 java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = reader.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // Выводим ответ от сервера в консоль
                System.out.println("Response: " + response.toString());
                student.setId(Integer.parseInt(response.toString()));
            }

            // Закрываем соединение
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestPostStudent(){
        RestAssured
                .given()
                    .baseUri(baseUrl)
                    .body(student)
                .when()
                    .post("/student")
                .then()
                    .statusCode(201)
                    .body("id", equalTo(student.getId()));

    }
    public void requestDeleteStudent(int id){
        RestAssured.given()
                .baseUri(baseUrl)
                .when().delete("/student/"+student.getId())
                .then().statusCode(200);


    }

    public void requestGetStudent(){

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

    }

}
