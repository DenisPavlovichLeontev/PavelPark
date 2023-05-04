package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DataBase {


    private final String url;
    private final String username;
    private final String password;

    public DataBase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    public void writeToDatabase(LocalDateTime dateStr, String chatId) {
        try {
            // Устанавливаем соединение с базой данных
            Connection connection = DriverManager.getConnection(url, username, password);
            // Создаем запрос на добавление данных
            String query = "INSERT INTO oplata (chat_id, date) VALUES (?, ?)";
            // Подготавливаем запрос и задаем значение для параметра
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, chatId);
            statement.setObject(2, dateStr);
            // Выполняем запрос на добавление данных
            statement.executeUpdate();
            // Закрываем соединение
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDays_plus(String chatId) {
        try {
            // Устанавливаем соединение с базой данных
            Connection connection = DriverManager.getConnection(url, username, password);
            // Создаем запрос на обновление данных
            String query = "UPDATE oplata SET days_plus = IFNULL(days_plus, 0) + 1 WHERE chat_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, chatId);
            // Выполняем запрос на обновление данных
            statement.executeUpdate();
            // Получаем текущее значение столбца days_plus для данного chat_id
            query = "SELECT days_plus FROM oplata WHERE chat_id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, chatId);
            ResultSet resultSet = statement.executeQuery();
            int daysPlus = 0;
            if (resultSet.next()) {
                daysPlus = resultSet.getInt("days_plus");
            }
            // Обновляем все записи в столбце days_plus для данного chat_id
            query = "UPDATE oplata SET days_plus = ? WHERE chat_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, daysPlus);
            statement.setString(2, chatId);
            statement.executeUpdate();
            // Закрываем соединение
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void getDateFromChatId(String chat_id) {
        try {
            // Подключение к базе данных
            Connection conn = DriverManager.getConnection(url, username, password);
            // SQL-запрос на выборку данных
            String query = "SELECT date FROM oplata WHERE chat_id = ?";
            // Подготовка SQL-запроса
            PreparedStatement pstmt = conn.prepareStatement(query);
            // Установка параметра для SQL-запроса
            pstmt.setString(1, chat_id);
            // Выполнение SQL-запроса
            ResultSet rs = pstmt.executeQuery();
            // Обработка результатов
            while (rs.next()) {
                // Получение значения столбца "date"
                String date = rs.getString("date");
                System.out.println(date);
            }
            // Закрытие ресурсов
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getDaysPlusByChatId() {
        Map<String, Integer> result = new HashMap<>();
        Cars cars = new Cars();
        HashMap<String, String> map = cars.getMap();
        Set<String> keys = map.keySet();
        try {
            // Подключение к базе данных
            Connection conn = DriverManager.getConnection(url, username, password);
            // Выполняем запрос к базе данных
            Statement statement = conn.createStatement();

            // Для каждого chat_id из списка получаем значение days_plus из базы данных
            for (String chatId : keys) {
                ResultSet resultSet = statement.executeQuery("SELECT days_plus FROM oplata WHERE chat_id = '" + chatId + "'");
                // Обрабатываем результаты запроса и заполняем список
                while (resultSet.next()) {
                    int daysPlus = resultSet.getInt("days_plus");
                    result.put(chatId, daysPlus);
                }
                resultSet.close();
            }
            // Закрываем все соединения и ресурсы
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(result);
        return result;
    }






    public void insertNumbersToTableBan(String[] numbers) {
        try {
            // Подключение к базе данных
            Connection conn = DriverManager.getConnection(url, username, password);
            // Подготовка SQL-запроса
            PreparedStatement statement = conn.prepareStatement("INSERT INTO number_for_ban (number, send) VALUES (?, 0)");
            // Добавление номеров в таблицу
            for (String number : numbers) {
                statement.setString(1, number);
                statement.executeUpdate();
            }
            // Закрытие соединений и ресурсов
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //вот тут остановился
    public String getFirstNumberForBan() {
        String firstNumber = null;
        try {
            // Подключение к базе данных
            Connection conn = DriverManager.getConnection(url, username, password);
            // Выполняем запрос к базе данных для получения первого номера с send=0
            PreparedStatement selectStmt = conn.prepareStatement("SELECT number FROM number_for_ban WHERE send = 0 LIMIT 1");
            ResultSet resultSet = selectStmt.executeQuery();
            // Если был найден хотя бы один номер, сохраняем его
            if (resultSet.next()) {
                firstNumber = resultSet.getString("number");
            }
            // Закрываем все соединения и ресурсы
            resultSet.close();
            selectStmt.close();
            // Если был найден хотя бы один номер, выполняем запрос на удаление записи
            if (firstNumber != null) {
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM number_for_ban WHERE number = ?");
                deleteStmt.setString(1, firstNumber);
                deleteStmt.executeUpdate();
                deleteStmt.close();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firstNumber;
    }

    /* не корректная работа. Ошибку выкидывает
    public void decreaseDaysPlus() {
        try {
            // Подключение к базе данных
            Connection conn = DriverManager.getConnection(url, username, password);
            // Выполняем запрос к базе данных
            Statement statement = conn.createStatement();
            // Получаем значения chat_id и days_plus из базы данных
            ResultSet resultSet = statement.executeQuery("SELECT chat_id, days_plus FROM oplata");

            // Обрабатываем результаты запроса и обновляем значения в базе данных
            while (resultSet.next()) {
                String chatId = resultSet.getString("chat_id");
                int daysPlus = resultSet.getInt("days_plus");
                if (daysPlus > 0) {
                    int newDaysPlus = daysPlus - 1;
                    String updateQuery = "UPDATE oplata SET days_plus = " + newDaysPlus + " WHERE chat_id = '" + chatId + "'";
                    statement.executeUpdate(updateQuery);
                }
            }

            // Закрываем все соединения и ресурсы
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public List<String> getChatIdsWithDaysPlus() {
        List<String> chatIds = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT chat_id FROM oplata WHERE days_plus > 0");
            while (resultSet.next()) {
                String chatId = resultSet.getString("chat_id");
                chatIds.add(chatId);
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatIds;
    }

    public void decreaseDaysPlus1() {
        List<String> chatIds = getChatIdsWithDaysPlus();
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement();
            for (String chatId : chatIds) {
                String updateQuery = "UPDATE oplata SET days_plus = days_plus - 1 WHERE chat_id = '" + chatId + "'";
                statement.executeUpdate(updateQuery);
            }
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

