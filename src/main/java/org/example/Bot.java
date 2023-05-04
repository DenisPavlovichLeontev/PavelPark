package org.example;

import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.io.IOException;
import java.util.*;


public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotToken() {
        return "6157148037:AAERjJAeK9vwPRQjfRN3OOZ-hH9VQaC5b_o";
    }

    @Override
    public String getBotUsername() {
        return "@OplataPark_bot";
    }

    // Метод для приема сообщений.
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasDocument()) {
            // Получаем объект документа.
            Document document = update.getMessage().getDocument();
            String file_id = document.getFileId();
            String file_name = document.getFileName();
            String chat_id = String.valueOf(update.getMessage().getChatId());
            String file_out_path = "C:\\Users\\Denis\\OneDrive\\Desktop\\files\\"+chat_id+"\\";
            try {
                Files.uploadFile(file_name, file_id, chat_id);
                Files.Pdf(file_out_path, file_name, chat_id);
                SendOplataToPavel1(chat_id, file_out_path+file_name);
                //System.out.println(Files.Pdf(file_out_path, file_name));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessageAtFixedTime() {
        Timer timer = new Timer();
        // Устанавливаем дату и время первого запуска. ВНИМАНИЕ! Не пересекать с методом из SmsSend!!!
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 18); //часы
        date.set(Calendar.MINUTE, 36); //минуты
        date.set(Calendar.SECOND, 0); //секунды
        // Если первый запуск наступил до определенного времени, то откладываем на следующий день
        if (new Date().after(date.getTime())) {
            date.add(Calendar.DAY_OF_MONTH, 1);
        }
        // Устанавливаем интервал повторения в 24 часа
        long period = 24 * 60 * 60 * 1000;
        // Создаем задание, которое будет выполняться каждый день в заданное время
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Код для отправки сообщений пользователям из ArrayList
                remindUsers();
            }
        };
        // Запускаем задание
        timer.schedule(task, date.getTime(), period);
    }

    public void remindUsers() {
        DataBase db = new DataBase("jdbc:mysql://localhost:3306/parkpavel", "root", "08523146");
        Map<String, Integer> daysPlusMap = db.getDaysPlusByChatId();
        // Перебираем все записи в Map полученные из бд
        for (Map.Entry<String, Integer> entry : daysPlusMap.entrySet()) {
            String chatId = entry.getKey();
            int daysPlus = entry.getValue();
            // Если daysPlus меньше 1, отправляем сообщение
            if (daysPlus < 1) {
                SendMessage message = new SendMessage(chatId, "Внимание! У вас заканчиваются дни оплаты. Пожалуйста, внесите оплату до 10:00.");
                try {
                    execute(message); // отправка сообщения
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

/*  НЕ используется
public void SendOplataToPavel(String chat_id, String filePath) {
    String nameDriver = "";
    Cars driver = new Cars();
    driver.getMap1();
    switch (chat_id){
        case "402047998":
            nameDriver = "Denis Leontev 570 Camry";
            break;
        case "4020479981":
            nameDriver = "Denis Leontev 570 Camry";
            break;
    }

    // Создаем объект InputFile из файла
    java.io.File file = new java.io.File(filePath);
    InputFile inputFile = new InputFile(file);

    SendDocument sendDocument = new SendDocument();
    sendDocument.setChatId("402047998"); //id Того, кому дублируется оплата
    sendDocument.setDocument(inputFile);
    sendDocument.setCaption("Поступила оплата от " + nameDriver);

    try {
        execute(sendDocument); // отправка документа
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }
}*/
    public void SendOplataToPavel1(String chat_id, String filePath) {
        Cars driver = new Cars();
        HashMap<String, String> map1 = driver.getMap1();
        String nameDriver = map1.get(chat_id);

        // Создаем объект InputFile из файла
        java.io.File file = new java.io.File(filePath);
        InputFile inputFile = new InputFile(file);

        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId("402047998"); //id Того, кому дублируется оплата
        sendDocument.setDocument(inputFile);
        sendDocument.setCaption("Поступила оплата от " + nameDriver);
        SendMessage sendMessage = new SendMessage(chat_id, "Оплата принята");

        try {
            execute(sendDocument); // отправка документа
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



}
