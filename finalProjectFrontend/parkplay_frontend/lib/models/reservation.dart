class Reservation {
  final int id;
  final int driverId;
  final int spotId;
  final String startTime;
  final String endTime;

  Reservation({
    required this.id, 
    required this.driverId, 
    required this.spotId, 
    required this.startTime, 
    required this.endTime
  });

  factory Reservation.fromJson(Map<String, dynamic> json) {
    return Reservation(
      id: json['id'],
      driverId: json['driverId'],
      spotId: json['spotId'],
      startTime: json['startTime'],
      endTime: json['endTime'],
    );
  }
}