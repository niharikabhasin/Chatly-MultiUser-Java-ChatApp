package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.util.HashMap;
import java.util.Map;

public class ChatGUI {
    private JFrame frame;
    private JPanel chatPanel;
    private JTextField inputField;
    private JButton sendButton;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private boolean isDarkMode = false;

    private final Map<String, Color> userBubbleColor = new HashMap<>();
    private final Color[] palette = {
            new Color(0xD0F0FD),   // soft blue
            new Color(0xDEF9D8),   // mint
            new Color(0xFFF4C1),   // light yellow
            new Color(0xFFD9E8),   // blush
            new Color(0xE0DCFF)    // lavender
    };

    private JButton emojiButton;
    private JPopupMenu emojiPopup;
    private JButton toggleThemeButton;
    private JLabel toggleLabel;
    private JLabel appName;
    private JScrollPane chatScroll;
    private String currentUsername;  
    private String loggedInUser = "";

    public ChatGUI() {
        frame = new JFrame("Chat App");

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(245, 245, 245));
        chatScroll = new JScrollPane(chatPanel);
        chatScroll.setBorder(null);

        inputField = new JTextField(40);
        sendButton = new JButton("Send");

        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 120, 215));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userList.setForeground(new Color(0, 102, 204));
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(120, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        appName = new JLabel("Chatly");
        appName.setFont(new Font("SansSerif", Font.BOLD, 22));
        topPanel.add(appName, BorderLayout.WEST);

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        toggleThemeButton = new JButton("🌓");
        toggleLabel = new JLabel("Dark Mode");
        toggleThemeButton.addActionListener(e -> toggleTheme());
        togglePanel.add(toggleThemeButton);
        togglePanel.add(toggleLabel);
        topPanel.add(togglePanel, BorderLayout.EAST);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        emojiButton = new JButton("😊");
        emojiButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        emojiButton.setMargin(new Insets(2, 6, 2, 6));
        emojiPopup = new JPopupMenu();
        populateEmojiPopup();
        emojiButton.addActionListener(e -> emojiPopup.show(emojiButton, 0, emojiButton.getHeight()));

        inputPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        inputPanel.add(emojiButton);
        inputPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        inputPanel.add(inputField);
        inputPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        inputPanel.add(sendButton);

        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(chatScroll, BorderLayout.CENTER);
        frame.getContentPane().add(userScrollPane, BorderLayout.EAST);
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);
        frame.setPreferredSize(new Dimension(700, 500));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setLoggedInUser(String username) { this.loggedInUser = username; }
    public String getLoggedInUser()             { return this.loggedInUser;    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        setLoggedInUser(username);
    }

    public void show() {
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(700, 500));
        frame.toFront();
    }

    public String getUserInput() { return inputField.getText(); }
    public void clearInput()     { inputField.setText("");      }

    public void setSendAction(ActionListener action) {
        sendButton.addActionListener(action);
        inputField.addActionListener(action);
    }

    public String askUsername() {
        return JOptionPane.showInputDialog(frame, "Enter a username:");
    }

    public void updateUserList(String[] users) {
        SwingUtilities.invokeLater(() -> {
            userListModel.clear();
            for (String user : users) {
                if (!user.matches(".*\\p{So}.*")) {
                    userListModel.addElement("🙂 " + user);
                } else {
                    userListModel.addElement(user);
                }
            }
        });
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {

            JTextArea messageArea = new JTextArea(message);
            messageArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
            messageArea.setLineWrap(true);
            messageArea.setWrapStyleWord(true);
            messageArea.setEditable(false);
            messageArea.setOpaque(true);
            messageArea.setBorder(new EmptyBorder(8, 10, 8, 10));

            int maxWidth = frame.getWidth() - 250;
            messageArea.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));
            messageArea.setPreferredSize(
                    new Dimension(maxWidth, messageArea.getPreferredSize().height)
            );

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);

            JPanel bubble = new JPanel(new BorderLayout());
            bubble.add(messageArea, BorderLayout.CENTER);
            bubble.setBorder(new EmptyBorder(2, 4, 2, 4));

            Color bubbleColor;
            String alignment;

            String sender = extractSender(message); 

            if (message.toLowerCase().contains("joined") || message.toLowerCase().contains("left")) {
                bubbleColor = new Color(255, 255, 180);
                alignment   = BorderLayout.CENTER;

            } else if (sender.equalsIgnoreCase(loggedInUser)) {
                bubbleColor = new Color(180, 220, 240);
                alignment   = BorderLayout.EAST;

            } else {
                bubbleColor = getBubbleColorFor(sender.isEmpty() ? "other" : sender);
                alignment   = BorderLayout.WEST;
            }

            messageArea.setBackground(bubbleColor);
            messageArea.setForeground(Color.BLACK);
            bubble.setBackground(bubbleColor);
            wrapper.add(bubble, alignment);

            chatPanel.add(wrapper);
            chatPanel.add(Box.createVerticalStrut(8));
            chatPanel.revalidate();
            chatPanel.repaint();

            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = chatScroll.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        });
    }
    
    private void toggleTheme() {
        isDarkMode = !isDarkMode;

        Color bg = isDarkMode ? new Color(40, 44, 52)    // dark charcoal
                              : new Color(245, 245, 245); // light grey
        Color fg = isDarkMode ? Color.WHITE : Color.DARK_GRAY;

        chatPanel.setBackground(bg);
        frame.getContentPane().setBackground(bg);

        inputField.setBackground(bg);
        inputField.setForeground(fg);
        inputField.setCaretColor(fg);

        userList.setBackground(bg);
        userList.setForeground(isDarkMode ? Color.WHITE : Color.DARK_GRAY);

        toggleThemeButton.setBackground(bg);
        toggleThemeButton.setForeground(fg);
        toggleLabel.setForeground(fg);
        appName.setForeground(fg);

        for (Component wrapper : chatPanel.getComponents()) {
            if (!(wrapper instanceof JPanel)) continue;
            for (Component child : ((JPanel) wrapper).getComponents()) {
                child.setBackground(bg);      
                child.setForeground(fg);      
            }
        }

        chatPanel.repaint();
    }

    private void populateEmojiPopup() {
        String[] emojis = { "😀","😂","😍","😎","😢","😡","👍","👎","🎉","❤️" };
        for (String emoji : emojis) {
            JMenuItem item = new JMenuItem(emoji);
            item.setFont(new Font("SansSerif", Font.PLAIN, 18));
            item.addActionListener(e -> {
                inputField.setText(inputField.getText() + emoji);
                emojiPopup.setVisible(false);
            });
            emojiPopup.add(item);
        }
    }

    private Color getBubbleColorFor(String username) {
        return userBubbleColor.computeIfAbsent(
                username.toLowerCase(),
                u -> palette[userBubbleColor.size() % palette.length]
        );
    }

    private String extractSender(String message) {
        int colon = message.indexOf(':');
        if (colon == -1) return "";
        String raw = message.substring(0, colon).trim();
        return raw.replaceFirst("^\\p{So}\\s*", "");  
    }
}
