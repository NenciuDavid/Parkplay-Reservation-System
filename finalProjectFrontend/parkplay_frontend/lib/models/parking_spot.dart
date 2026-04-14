class ParkingSpot {
  final int id;
  final String spotNumber;
  final bool isAvailable;
  final double hourlyRate;
  final int? parkingLotId;

  ParkingSpot({
    required this.id, 
    required this.spotNumber, 
    required this.isAvailable, 
    required this.hourlyRate, 
    this.parkingLotId
  });

  factory ParkingSpot.fromJson(Map<String, dynamic> json) {
    return ParkingSpot(
      id: json['id'],
      spotNumber: json['spotNumber'],
      // Gestionăm ambele cazuri posibile de la backend
      isAvailable: json['available'] ?? json['isAvailable'] ?? true, 
      hourlyRate: (json['hourlyRate'] as num).toDouble(), // Siguranță pentru double
      parkingLotId: json['parkingLotId'],
    );
  }
}