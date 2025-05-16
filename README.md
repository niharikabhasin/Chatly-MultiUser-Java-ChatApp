# 💬 Chatly – A Multi-User Java Chat Application

### 🔍 Problem Statement
Most beginner chat apps lack advanced UI design and user personalization. Chatly addresses this gap by combining **real-time multi-user messaging** with a **modern, intuitive Java Swing interface**. It enables users to securely log in, chat in real time, choose avatars, toggle themes, and enjoy emoji-rich interactions—all backed by a MySQL database.

### 🎯 Project Objectives
- Build a scalable, socket-based chat system with Java
- Integrate user registration and authentication using MySQL
- Design a dynamic GUI with support for real-time updates, themes, and emojis
- Ensure secure communication and data handling via hashing and SQL protection
- Simulate a real-world messaging experience with multiple clients

### ⚙️ Tech Stack
| Layer             | Technology               |
|------------------|--------------------------|
| Language          | Java (JDK 11+)          |
| GUI Framework     | Java Swing + AWT        |
| Backend           | Java TCP Sockets        |
| Multithreading    | Java Threads            |
| Persistence Layer | MySQL + JDBC            |
| Security          | SHA-256 + Prepared Statements |
| IDE               | Eclipse                 |

### 🧠 Key Features
- ✅ Real-Time Messaging via socket programming
- ✅ Multi-Client Support using multithreading (ClientHandler)
- ✅ Login + Registration System with SHA-256 password hashing
- ✅ Emoji Picker and inline emoji rendering
- ✅ Light/Dark Mode Toggle with dynamic theme update
- ✅ User Avatars and sidebar presence tracking
- ✅ Message Bubble UI with sender alignment and color coding
- ✅ MySQL Integration to store users and chat logs
- ✅ Graceful Client Disconnection notifications
- ✅ Secure DB Queries using Prepared Statements

### 🧱 System Architecture
```
                   +------------------------+
                   |      MySQL Database    |
                   |  (Users + Messages)    |
                   +------------------------+
                             ▲
                             |
         +-------------------+--------------------+
         |                                        |
+------------------+                    +------------------+
|   Client 1 (GUI)  |   ← Socket →      |   Client 2 (GUI)  |
+------------------+                    +------------------+
         ▲                                        ▲
         |                                        |
         +------------------+---------------------+
                            |
                       +----------+
                       |  Server  |
                       |  Java    |
                       +----------+
```

### 📁 Folder Structure
```
Chatly/
├── client/
│   ├── ChatClient.java
│   ├── ChatGUI.java
│   └── LoginScreen.java
├── server/
│   └── Server.java
├── utils/
│   ├── DBUtil.java
│   └── PasswordUtil.java
├── sql/
│   └── chatly_dbsetup.sql
├── README.md
└── LICENSE
```

### 🚀 How to Run
#### 1. 🧩 Set Up the Database
- Install MySQL and MySQL Workbench
- Open and run `sql/chatly_dbsetup.sql`
- Update your MySQL username and password inside `utils/DBUtil.java`

#### 2. ▶️ Launch the Server
- Open `server/Server.java` in Eclipse
- Run it as a Java Application
- Wait for clients to connect

#### 3. 💬 Launch Multiple Clients
- Run `client/ChatClient.java` multiple times
- Register users with usernames, passwords, and avatars
- Login and start chatting in real time 🎉

### 🔒 Security Measures
- Passwords hashed with SHA-256
- SQL injection prevented using Prepared Statements
- No plain-text storage of sensitive data

### 🧪 Testing Scenarios
| Scenario                             | Expected Result                       |
|--------------------------------------|----------------------------------------|
| Duplicate username during signup     | Error shown                           |
| Login with wrong credentials         | Warning displayed                     |
| Chat between 2+ clients              | Real-time sync, scroll to latest      |
| Toggle dark mode                    | Chat UI instantly recolored           |
| Client disconnects unexpectedly      | Server notifies others, updates list |

### 📌 Future Work
- Media file/image sharing
- Private/direct messages
- Group chat functionality
- Push notifications
- Admin dashboard for monitoring/chat control

### 👤 Author
**Niharika Bhasin**  
📧 nb4048@nyu.edu  
🔗 [LinkedIn](https://linkedin.com/in/niharika-bhasin)

### 📄 License
Open-source under the [MIT License](LICENSE)
