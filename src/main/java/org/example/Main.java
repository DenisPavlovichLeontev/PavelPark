/*
package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONObject;
import java.sql.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Files {

    public static void uploadFile(String file_name, String file_id, String chat_id) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + "6157148037:AAERjJAeK9vwPRQjfRN3OOZ-hH9VQaC5b_o" + "/getFile?file_id=" + file_id);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = new URL("https://api.telegram.org/file/bot" + "6157148037:AAERjJAeK9vwPRQjfRN3OOZ-hH9VQaC5b_o" + "/" + file_path);
        String file_out_path = "C:\\Users\\Denis\\OneDrive\\Desktop\\files\\" + chat_id + "\\";
        File uploadDir = new File(file_out_path);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // создаем директорию, если ее нет
        }
        FileOutputStream fos = new FileOutputStream(file_out_path + file_name);
        System.out.println("Start upload");
        ReadableByteChannel rbc = Channels.newChannel(downoload.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        System.out.println("Uploaded!");
    }

    public static String Pdf(String path_file, String file_name, String chat_id) {
        try (PDDocument document = PDDocument.load(new File(path_file + file_name))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            System.out.println(text);
            document.close();
            Pattern datePattern1 = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})");
            Pattern datePattern2 = Pattern.compile("(\\d{2}\\s\\p{IsCyrillic}+\\s\\d{4})");

            Matcher dateMatcher1 = datePattern1.matcher(text);
            Matcher dateMatcher2 = datePattern2.matcher(text);
            //Matcher cashMatcher = cashPattern.matcher(text);
            if (dateMatcher1.find()) {
                String dateStr = dateMatcher1.group(1);
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                System.out.println(dateTime);
                return dateStr;
            } else if (dateMatcher2.find()) {
                String dateStr = dateMatcher2.group(1);
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
                System.out.println(dateTime);
                return dateStr;
            } else {
                System.out.println("Не удалось определить дату");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeToDatabase(String dateStr, String chatId) {
        // Задаем параметры для подключения к базе данных
        String url = "jdbc:mysql://localhost:3306/parkpavel";
        String username = "root";
        String password = "08523146";

        try {
            // Устанавливаем соединение с базой данных
            Connection connection = DriverManager.getConnection(url, username, password);

            // Создаем запрос на добавление данных
            String query = "INSERT INTO oplata (chat_id, date) VALUES (?, ?)";

            // Подготавливаем запрос и задаем значение для параметра
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, chatId);
            statement.setString(2, dateStr);

            // Выполняем запрос на добавление данных
            statement.executeUpdate();

            // Закрываем соединение
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
*/
