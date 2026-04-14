Parkplay is a mobile software platform (Java Spring Boot Backend + Flutter Mobile Frontend) dedicated to parking space management and reservation. The application is designed to serve two primary user roles, offering specific functionalities for each:
- The Driver: Can view available parking lots, reserve a specific spot for a defined time interval, and manage or cancel their active reservations.
- The Manager: Can add or delete entire parking lots, manage individual spots, and access a live monitoring dashboard to see exactly which parking spot will become available next in any given lot.

To ensure high performance under heavy traffic, the validation of time overlaps and the monitoring of expiration times are not done through slow database queries. Instead, the system leverages advanced data structures maintained such as Red-Black Trees and Binomial Heaps in the server's memory.
The application is divided into two main components. Follow these steps to run it locally:

Prerequisites
- Java Development Kit (JDK 17 or higher) installed on your system.
- Flutter SDK installed, along with a configured emulator (Android/iOS) or a connected physical device.
- MySQL Server installed and running.

Step 1: Backend Setup (Spring Boot)
- Open your MySQL management tool and create an empty database named parkplay.
- Open the Backend project folder in IntelliJ IDEA.
- Navigate to the src/main/resources/application.properties file and ensure your database credentials (username and password) are correct. Hibernate will automatically generate the required database tables upon startup.
- Run the main class: ParkplayFinalApplication.java.

Step 2: Frontend Setup (Flutter)
- Open the frontend folder (parkplay_frontend) in Visual Studio Code or Android Studio.
- Open a terminal within this folder and run the following command to download all necessary dependencies:
flutter pub get
- Open the lib/api_service.dart file and ensure the baseUrl variable points to your backend server correctly:
- If running on an Android Emulator, use: http://10.0.2.2:8080
- If running on an iOS Simulator or Web, use: http://localhost:8080
- Start your emulator.
- Run the application from the terminal using:
flutter run
- The application will launch.
