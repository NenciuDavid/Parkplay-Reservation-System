import 'parking_spot.dart';

class ParkingLot {
  final int id;
  final String name;
  final String location;
  final int? managerId;
  final List<ParkingSpot> spots;

  ParkingLot({
    required this.id, 
    required this.name, 
    required this.location, 
    this.managerId, 
    required this.spots
  });

  factory ParkingLot.fromJson(Map<String, dynamic> json) {
    var spotsList = json['spots'] as List?;
    List<ParkingSpot> parsedSpots = spotsList != null 
        ? spotsList.map((i) => ParkingSpot.fromJson(i)).toList() 
        : [];
    
    return ParkingLot(
      id: json['id'],
      name: json['name'],
      location: json['location'],
      managerId: json['managerId'],
      spots: parsedSpots,
    );
  }
}