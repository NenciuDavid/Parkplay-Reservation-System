import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:parkplay_frontend/models/parking_spot.dart';
import 'models/user.dart';
import 'models/parking_lot.dart';
import 'models/reservation.dart';

class ApiService {
  // Pt Android Emulator foloseste 10.0.2.2, pt iOS sau Web foloseste localhost
  static const String baseUrl = "http://10.0.2.2:8080";

  Future<User?> login(String email, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/auth/login'),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({"email": email, "password": password}),
    );

    if (response.statusCode == 200) {
      return User.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<bool> register(String name, String email, String password, String role, String plate) async {
    final response = await http.post(
      Uri.parse('$baseUrl/auth/register'),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({
        "name": name,
        "email": email,
        "password": password,
        "role": role,
        "licensePlate": plate
      }),
    );
    return response.statusCode == 200;
  }

  Future<List<ParkingLot>> getAllParkingLots() async {
    final response = await http.get(Uri.parse('$baseUrl/parking-lots'));
    if (response.statusCode == 200) {
      List jsonResponse = jsonDecode(response.body);
      return jsonResponse.map((item) => ParkingLot.fromJson(item)).toList();
    }
    return [];
  }

  Future<bool> createReservation(int driverId, int spotId, DateTime start, DateTime end) async {
    final response = await http.post(
      Uri.parse('$baseUrl/reservations'),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({
        "driverId": driverId,
        "spotId": spotId,
        "startTime": start.toIso8601String(),
        "endTime": end.toIso8601String()
      }),
    );
    return response.statusCode == 200;
  }

  Future<List<Reservation>> getDriverReservations(int driverId) async {
    final response = await http.get(Uri.parse('$baseUrl/reservations/driver/$driverId'));
    if (response.statusCode == 200) {
      List jsonResponse = jsonDecode(response.body);
      return jsonResponse.map((item) => Reservation.fromJson(item)).toList();
    }
    return [];
  }

  Future<bool> deleteReservation(int id) async {
    final response = await http.delete(Uri.parse('$baseUrl/reservations/$id'));
    return response.statusCode == 200;
  }

  Future<List<ParkingLot>> getManagerLots(int managerId) async {
    final response = await http.get(Uri.parse('$baseUrl/managers/$managerId/lots'));
    if (response.statusCode == 200) {
      List jsonResponse = jsonDecode(response.body);
      return jsonResponse.map((item) => ParkingLot.fromJson(item)).toList();
    }
    return [];
  }

  Future<bool> createParkingLot(String name, String location, int managerId) async {
    final response = await http.post(
      Uri.parse('$baseUrl/parking-lots'),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({
        "name": name,
        "location": location,
        "managerId": managerId
      }),
    );
    return response.statusCode == 200;
  }

  Future<bool> addSpot(int lotId, String number, double rate) async {
    final response = await http.post(
      Uri.parse('$baseUrl/parking-lots/$lotId/spots'),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({
        "spotNumber": number,
        "hourlyRate": rate,
        "isAvailable": true
      }),
    );
    return response.statusCode == 200;
  }

  Future<List<ParkingSpot>> getSpotsForLot(int lotId) async {
    final response = await http.get(Uri.parse('$baseUrl/parking-lots/$lotId/spots'));
    
    if (response.statusCode == 200) {
      List jsonResponse = jsonDecode(response.body);
      return jsonResponse.map((item) => ParkingSpot.fromJson(item)).toList();
    }
    return [];
  }

  Future<bool> deleteSpot(int spotId) async {
    final response = await http.delete(Uri.parse('$baseUrl/parking-lots/spots/$spotId'));
    return response.statusCode == 200;
  }

  Future<bool> deleteParkingLot(int lotId) async {
    final response = await http.delete(Uri.parse('$baseUrl/parking-lots/$lotId'));
    return response.statusCode == 200;
  }
}