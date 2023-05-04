package org.example;

import java.util.*;

public class SmsSend {
    public static String textSmS(String number){
        switch (number){
            case "89137767475":
                return "abc";
            case "89134517628":
                return "qwe";
            case "89232237091":
                return "test_ban_for_you";
        }
        return null;
    }

    public static String[] getPhoneNumb(Map<String, Integer> id_for_ban) {
        Cars car = new Cars();
        HashMap<String, String> map = car.getMap();
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : id_for_ban.entrySet()) {
            String chatId = entry.getKey();
            int value = entry.getValue();

            if (value == 0) {
                String phoneNumber = map.get(chatId);
                result.add(phoneNumber);
            }
        }
        System.out.println(result);
        return result.toArray(new String[0]);
    }

    public static void TimesForBan(){
            Timer timer = new Timer();

            // Устанавливаем дату и время первого запуска
            Calendar date = Calendar.getInstance();
            date.set(Calendar.HOUR_OF_DAY, 18);
            date.set(Calendar.MINUTE, 37);
            date.set(Calendar.SECOND, 0);

            // Если первый запуск наступил до 10:10, то откладываем на следующий день
            if (new Date().after(date.getTime())) {
                date.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Устанавливаем интервал повторения в 24 часа
            long period = 24 * 60 * 60 * 1000;

            // Создаем задание, которое будет выполняться каждый день в 10:10
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    DataBase db = new DataBase("jdbc:mysql://localhost:3306/parkpavel", "root", "08523146");
                    db.decreaseDaysPlus1();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    db.insertNumbersToTableBan(SmsSend.getPhoneNumb(db.getDaysPlusByChatId()));
                }
            };

            // Запускаем задание
            timer.schedule(task, date.getTime(), period);

    }

}
