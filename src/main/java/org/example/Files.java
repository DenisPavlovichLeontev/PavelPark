package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONObject;
import java.sql.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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

    public static LocalDateTime Pdf(String path_file, String file_name, String chat_id) {
        DataBase db = new DataBase("jdbc:mysql://localhost:3306/parkpavel", "root", "08523146");
        try (PDDocument document = PDDocument.load(new File(path_file + file_name))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            //System.out.println(text);
            document.close();
            Pattern datePattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})");
            Pattern datePattern1 = Pattern.compile("(\\d{2}\\s\\p{IsCyrillic}+\\s\\d{4})");
            Pattern timePattern = Pattern.compile("(\\d{2}:\\d{2})");
            Matcher dateMatcher = datePattern.matcher(text);
            Matcher dateMatcher1 = datePattern1.matcher(text);
            Matcher timeMatcher = timePattern.matcher(text);
            if (dateMatcher.find() && timeMatcher.find()) {
                String dateStr = dateMatcher.group(1)+ " "+timeMatcher.group(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", new Locale("ru"));
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                //System.out.println(dateTime + "ДАТА ");
                db.writeToDatabase(dateTime, chat_id);
                db.addDays_plus(chat_id);
                System.out.println(dateTime + " Должно быть в БД оплата от " + chat_id);
                return null;
            } else if (dateMatcher1.find()&& timeMatcher.find()) {
                String dateStr = dateMatcher1.group(1)+ " "+timeMatcher.group(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", new Locale("ru"));
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                //System.out.println(dateTime + " ДАТА");
                db.writeToDatabase(dateTime, chat_id);
                db.addDays_plus(chat_id);
                System.out.println(dateTime + " Должно быть в БД оплата от " + chat_id);
                //getDateFromChatId(chat_id);
                return dateTime;
            }
            else {
                System.out.println("не получилось");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
