import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'api_service.dart';
import 'models/user.dart';
import 'models/reservation.dart';
import 'models/parking_lot.dart';
import 'models/parking_spot.dart';

class DriverDashboard extends StatefulWidget {
  final User user;
  DriverDashboard({required this.user});

  @override
  _DriverDashboardState createState() => _DriverDashboardState();
}

class _DriverDashboardState extends State<DriverDashboard> {
  final ApiService _apiService = ApiService();
  List<Reservation> _reservations = [];

  @override
  void initState() {
    super.initState();
    _loadReservations();
  }

  void _loadReservations() async {
    var res = await _apiService.getDriverReservations(widget.user.id);
    setState(() => _reservations = res);
  }

  void _deleteReservation(int id) async {
    bool confirm = await showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text("Anulezi rezervarea?"),
        content: Text("Sigur vrei să anulezi această rezervare? Această acțiune este ireversibilă."),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx, false), 
            child: Text("Nu")
          ),
          TextButton(
            onPressed: () => Navigator.pop(ctx, true),
            child: Text("Da, Anulează", style: TextStyle(color: Colors.red)),
          ),
        ],
      )
    ) ?? false;

    if (confirm) {
      bool success = await _apiService.deleteReservation(id);
      if (success) {
        _loadReservations();
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Rezervare anulată cu succes!")));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Eroare la anulare!")));
      }
    }
  }

  void _showCreateReservationDialog() {
    showDialog(context: context, builder: (context) => CreateReservationDialog(user: widget.user, onReserved: _loadReservations));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: Text("Salut, ${widget.user.name}"),
        centerTitle: true,
        backgroundColor: Colors.blueAccent,
        foregroundColor: Colors.white,
        elevation: 5,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.vertical(bottom: Radius.circular(30)),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.only(top: 20.0),
        child: ListView.builder(
          padding: EdgeInsets.all(16),
          itemCount: _reservations.length,
          itemBuilder: (context, index) {
            final res = _reservations[index];
            
            return Container(
              margin: EdgeInsets.only(bottom: 16),
              decoration: BoxDecoration(
                color: Colors.blueAccent.withOpacity(0.15),
                borderRadius: BorderRadius.circular(20),
                border: Border.all(color: Colors.blue.withOpacity(0.3)),
              ),
              child: ListTile(
                contentPadding: EdgeInsets.all(16),
                leading: CircleAvatar(
                  backgroundColor: Colors.blueAccent,
                  child: Icon(Icons.confirmation_number, color: Colors.white),
                ),
                title: Text(
                  "Rezervare #${res.id}",
                  style: TextStyle(fontWeight: FontWeight.bold, color: Colors.blue[900]),
                ),
                subtitle: Padding(
                  padding: const EdgeInsets.only(top: 8.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(children: [
                        Icon(Icons.local_parking, size: 16, color: Colors.grey),
                        SizedBox(width: 5),
                        Text("Loc ID: ${res.spotId}"),
                      ]),
                      SizedBox(height: 4),
                      Text("Start: ${res.startTime.split('T').join(' ').substring(0, 16)}"), // Formatare simpla
                      Text("End:   ${res.endTime.split('T').join(' ').substring(0, 16)}"),
                    ],
                  ),
                ),
                trailing: IconButton(
                  icon: Icon(Icons.delete_outline, color: Colors.red, size: 28),
                  onPressed: () => _deleteReservation(res.id),
                ),
              ),
            );
          },
        ),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _showCreateReservationDialog,
        backgroundColor: Colors.blueAccent,
        foregroundColor: Colors.white,
        icon: Icon(Icons.add),
        label: Text("Rezervă Loc"),
      ),
    );
  }
}

class CreateReservationDialog extends StatefulWidget {
  final User user;
  final VoidCallback onReserved;
  CreateReservationDialog({required this.user, required this.onReserved});

  @override
  _CreateReservationDialogState createState() => _CreateReservationDialogState();
}

class _CreateReservationDialogState extends State<CreateReservationDialog> {
  final ApiService _apiService = ApiService();
  
  List<ParkingLot> _lots = [];
  ParkingLot? _selectedLot;
  ParkingSpot? _selectedSpot;

  DateTime? _selectedDate;
  TimeOfDay? _startTime;
  TimeOfDay? _endTime;

  @override
  void initState() {
    super.initState();
    _loadLots();
  }

  void _loadLots() async {
    var lots = await _apiService.getAllParkingLots();
    setState(() => _lots = lots);
  }

  Future<void> _pickDate() async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2100),
    );
    if (picked != null && picked != _selectedDate) {
      setState(() => _selectedDate = picked);
    }
  }

  Future<void> _pickTime(bool isStart) async {
    final TimeOfDay? picked = await showTimePicker(
      context: context,
      initialTime: TimeOfDay.now(),
      builder: (BuildContext context, Widget? child) {
        return MediaQuery(
          data: MediaQuery.of(context).copyWith(alwaysUse24HourFormat: true),
          child: child!,
        );
      },
    );

    if (picked != null) {
      setState(() {
        if (isStart) {
          _startTime = picked;
        } else {
          _endTime = picked;
        }
      });
    }
  }

  void _reserve() async {
    if (_selectedSpot == null || _selectedDate == null || _startTime == null || _endTime == null) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Te rog completează toate câmpurile!")));
      return;
    }

    final startDateTime = DateTime(
      _selectedDate!.year, _selectedDate!.month, _selectedDate!.day,
      _startTime!.hour, _startTime!.minute
    );

    final endDateTime = DateTime(
      _selectedDate!.year, _selectedDate!.month, _selectedDate!.day,
      _endTime!.hour, _endTime!.minute
    );

    if (startDateTime.isAfter(endDateTime) || startDateTime.isAtSameMomentAs(endDateTime)) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Ora de sfârșit trebuie să fie după ora de start!")));
      return;
    }

    if (startDateTime.isBefore(DateTime.now())) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Nu poți face rezervări în trecut!")));
      return;
    }

    bool success = await _apiService.createReservation(widget.user.id, _selectedSpot!.id, startDateTime, endDateTime);
    
    if (success) {
      widget.onReserved();
      Navigator.pop(context);
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Rezervare reușită!")));
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Interval ocupat sau eroare server!")));
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text("Rezervare Nouă"),
      content: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            // Dropdown Parcare
            DropdownButton<ParkingLot>(
              isExpanded: true,
              hint: Text("Alege Parcare"),
              value: _selectedLot,
              items: _lots.map((l) => DropdownMenuItem(value: l, child: Text(l.name))).toList(),
              onChanged: (val) {
                setState(() {
                  _selectedLot = val;
                  _selectedSpot = null;
                });
              },
            ),
            SizedBox(height: 10),

            // Dropdown Loc (Dacă parcarea e aleasă)
            if (_selectedLot != null)
              DropdownButton<ParkingSpot>(
                isExpanded: true,
                hint: Text("Alege Loc"),
                value: _selectedSpot,
                items: _selectedLot!.spots.where((s) => s.isAvailable).map((s) => DropdownMenuItem(
                  value: s,
                  child: Text("${s.spotNumber} (${s.hourlyRate} RON/h)"),
                )).toList(),
                onChanged: (val) => setState(() => _selectedSpot = val),
              ),
            
            Divider(),
            
            // Selector Data
            ListTile(
              contentPadding: EdgeInsets.zero,
              title: Text(_selectedDate == null 
                  ? "Alege Data" 
                  : "Data: ${DateFormat('yyyy-MM-dd').format(_selectedDate!)}"),
              trailing: Icon(Icons.calendar_today),
              onTap: _pickDate,
            ),

            // Selector Ora Start
            ListTile(
              contentPadding: EdgeInsets.zero,
              title: Text(_startTime == null 
                  ? "Ora Start" 
                  : "Start: ${_startTime!.format(context)}"),
              trailing: Icon(Icons.access_time),
              onTap: () => _pickTime(true),
            ),

            // Selector Ora Sfârșit
            ListTile(
              contentPadding: EdgeInsets.zero,
              title: Text(_endTime == null 
                  ? "Ora Sfârșit" 
                  : "Sfârșit: ${_endTime!.format(context)}"),
              trailing: Icon(Icons.access_time_filled),
              onTap: () => _pickTime(false),
            ),
          ],
        ),
      ),
      actions: [
        TextButton(onPressed: () => Navigator.pop(context), child: Text("Anulează")),
        ElevatedButton(onPressed: _reserve, child: Text("Rezervă")),
      ],
    );
  }
}