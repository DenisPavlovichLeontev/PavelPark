package org.example;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Smsc {
	public static void main(String[] args) throws IOException, SQLException {
/*		DataBase db = new DataBase("jdbc:mysql://localhost:3306/parkpavel", "root", "08523146");
		db.insertNumbersToTableBan(SmsSend.getPhoneNumb(db.getDaysPlusByChatId()));*/
		SmsSend.TimesForBan();
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(new Bot());
			System.out.println("Bot started!");
			Bot bot = new Bot();
			bot.sendMessageAtFixedTime();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ServerSocket serverSocket = new ServerSocket(7475);
		System.out.println("Server started on port 7475");

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("Client connected");
			// Создаем новый экземпляр ClientHandler и запускаем его в новом потоке
			executorService.submit(new ClientHandler(socket));
		}
	}

	private static class ClientHandler implements Runnable {
		private final Socket socket;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}
		DataBase db = new DataBase("jdbc:mysql://localhost:3306/parkpavel", "root", "08523146");

		@Override
		public void run() {
			try {
				System.out.println("Client in MultiThread");
				// Получаем JSON из сокета
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[1024];
				int length = in.read(buffer);
				// Преобразуем JSON в строку и распечатываем его содержимое
				String jsonString = new String(buffer, 0, length);
				System.out.println(jsonString);
				// Преобразуем JSON в объект и обрабатываем поле name
				JSONObject json = new JSONObject(jsonString);
				String name = json.getString("name");

				switch (name) {
					case "Denis":
						System.out.println("New Task Denis");
						sendNumberForBan(socket, db.getFirstNumberForBan());
						break;
					case "Andru":
						System.out.println("Hello!");
						String task = json.getString("task");
						break;
					default:
						System.out.println("Hello!");
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void sendNumberForBan(Socket socket, String number) throws IOException {
			JSONObject obj = new JSONObject();
			obj.put("num", number);
			obj.put("msg", SmsSend.textSmS(number));
			System.out.println(number);
			// Преобразуем JSON в байтовый массив
			byte[] responseBytes = obj.toString().getBytes();

			// Получаем выходной поток сокета клиента и записываем туда наш JSON
			OutputStream out = socket.getOutputStream();
			System.out.println(obj);
			out.write(responseBytes);
			out.flush();
			}
}
}
