package client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatClient {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private ChatGUI gui;

    public ChatClient() {
        gui = new ChatGUI();
        gui.setSendAction(e -> sendMessage());
    }

    private void sendMessage() {
        String message = gui.getUserInput();
        if (!message.trim().isEmpty()) {
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            out.println("[ " + time + " ] " + message);
            gui.clearInput();
        }
    }

    private void connectToServer() throws IOException {
    	System.out.println("Connecting to server...");
        socket = new Socket("localhost", 1234);
        System.out.println("Connected!");
        
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        LoginScreen login = new LoginScreen();
        login.waitForSubmit();
        String username = login.getUsername();
        String avatar = login.getAvatar();
        out.println(username + "|" + avatar);
        
        gui.setCurrentUsername(username);
        SwingUtilities.invokeLater(gui::show);

        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("/users|")) {
                        String[] users = line.substring(7).split(",");
                        gui.updateUserList(users);
                    } else {
                        gui.appendMessage(line);
                    }
                }
            } catch (IOException e) {
                gui.appendMessage("⚠️ Disconnected from server.");
            }
        }).start();
    }
    
    public void start() {
        new Thread(() -> {
            try {
                connectToServer();
            } catch (IOException ex) {
                gui.appendMessage("⚠️ Unable to connect to server.");
                ex.printStackTrace();
            }
        }).start();
    }
    
    public static void main(String[] args) throws Exception {              
        SwingUtilities.invokeLater(() -> {   
            try {
                new ChatClient().start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
