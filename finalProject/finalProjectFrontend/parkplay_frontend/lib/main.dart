import 'package:flutter/material.dart';
import 'login_screen.dart';

void main() {
  runApp(ParkPlayApp());
}

class ParkPlayApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ParkPlay',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        useMaterial3: true,
      ),
      home: LoginScreen(),
    );
  }
}