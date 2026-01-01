import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:parkplay_frontend/models/parking_spot.dart';
import 'api_service.dart';
import 'models/user.dart';
import 'models/parking_lot.dart';

class ManagerDashboard extends StatefulWidget {
  final User user;
  ManagerDashboard({required this.user});

  @override
  _ManagerDashboardState createState() => _ManagerDashboardState();
}

class _ManagerDashboardState extends State<ManagerDashboard> {
  final ApiService _apiService = ApiService();
  List<ParkingLot> _myLots = [];

  @override
  void initState() {
    super.initState();
    _loadLots();
  }

  void _loadLots() async {
    var lots = await _apiService.getManagerLots(widget.user.id);
    setState(() => _myLots = lots);
  }

  void _addParkingLot() async {
    final _formKey = GlobalKey<FormState>();
    
    TextEditingController nameCtrl = TextEditingController();
    TextEditingController locCtrl = TextEditingController();

    await showDialog(
      context: context,
      barrierDismissible: false,
      builder: (_) => AlertDialog(
        title: Text("Adaugă Parcare"),
        content: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                controller: nameCtrl,
                decoration: InputDecoration(
                  labelText: "Nume Parcare",
                  hintText: "Ex: Parcare Centru"
                ),
                inputFormatters: [
                  FilteringTextInputFormatter.allow(RegExp(r'[a-zA-Z0-9\s-]')),
                ],
                validator: (value) {
                  if (value == null || value.isEmpty) return 'Introdu numele parcării';
                  if (value.length < 3) return 'Numele este prea scurt';
                  return null;
                },
              ),
              
              SizedBox(height: 10),

              TextFormField(
                controller: locCtrl,
                decoration: InputDecoration(
                  labelText: "Locație / Adresă",
                  hintText: "Ex: Str. Principala Nr. 1"
                ),
                inputFormatters: [
                  FilteringTextInputFormatter.allow(RegExp(r'[a-zA-Z0-9\s.,-]')),
                ],
                validator: (value) {
                  if (value == null || value.isEmpty) return 'Introdu locația';
                  return null;
                },
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context), 
            child: Text("Anulează")
          ),
          ElevatedButton(
            onPressed: () async {
              if (_formKey.currentState!.validate()) {
                bool success = await _apiService.createParkingLot(nameCtrl.text, locCtrl.text, widget.user.id);
                
                Navigator.pop(context);

                if (success) {
                  _loadLots();
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(backgroundColor: Colors.green, content: Text("Parcare creată!")));
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(backgroundColor: Colors.red, content: Text("Eroare la creare!")));
                }
              }
            }, 
            child: Text("Salvează")
          )
        ],
      )
    );
  }

  void _deleteParkingLot(int lotId) async {
    bool confirm = await showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text("Ștergi parcarea?"),
        content: Text("Sigur vrei să ștergi această parcare și toate locurile ei?"),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx, false), child: Text("Nu")),
          TextButton(
            onPressed: () => Navigator.pop(ctx, true),
            child: Text("Da", style: TextStyle(color: Colors.red))
          ),
        ],
      )
    ) ?? false;
    if (confirm) {
      bool success = await _apiService.deleteParkingLot(lotId);
      if (success) {
        _loadLots();
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Parcare ștearsă!")));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Eroare la ștergere!")));
      }
    }
  }

  void _manageSpots(ParkingLot lot) {
    Navigator.push(context, MaterialPageRoute(builder: (_) => ManageSpotsScreen(lot: lot, refreshParent: _loadLots)));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Manager Dashboard"),
        centerTitle: true,
        backgroundColor: Colors.blueAccent,
        foregroundColor: Colors.white,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.vertical(bottom: Radius.circular(30)),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.only(top: 20.0),
        child: ListView.builder(
          padding: EdgeInsets.all(16),
          itemCount: _myLots.length,
          itemBuilder: (context, index) {
            final lot = _myLots[index];
            
            return Container(
              margin: EdgeInsets.only(bottom: 16),
              decoration: BoxDecoration(
                color: Colors.lightBlue.withOpacity(0.15),
                borderRadius: BorderRadius.circular(20),
                border: Border.all(color: Colors.blue.withOpacity(0.3)),
              ),
              child: ListTile(
                contentPadding: EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                leading: Container(
                  padding: EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    color: Colors.white.withOpacity(0.5),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Icon(Icons.business, color: Colors.blueAccent),
                ),
                title: Text(lot.name, style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
                subtitle: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    SizedBox(height: 5),
                    Row(children: [
                      Icon(Icons.location_on, size: 14, color: Colors.grey),
                      SizedBox(width: 4),
                      Expanded(
                        child:
                          Text(lot.location, overflow: TextOverflow.ellipsis, maxLines: 1, style: TextStyle(fontSize: 14)),
                      ),
                    ]),
                    Text("Capacitate: ${lot.spots.length} locuri"),
                  ],
                ),
                
                onTap: () => _manageSpots(lot),

                trailing: IconButton(
                  icon: Icon(Icons.delete, color: Colors.redAccent),
                  onPressed: () => _deleteParkingLot(lot.id),
                ),
              ),
            );
          },
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _addParkingLot,
        backgroundColor: Colors.blueAccent,
        foregroundColor: Colors.white,
        child: Icon(Icons.add_business),
      ),
    );
  }
}

class ManageSpotsScreen extends StatefulWidget {
  final ParkingLot lot;
  final VoidCallback refreshParent;

  ManageSpotsScreen({required this.lot, required this.refreshParent});

  @override
  _ManageSpotsScreenState createState() => _ManageSpotsScreenState();
}

class _ManageSpotsScreenState extends State<ManageSpotsScreen> {
  final ApiService _apiService = ApiService();
  
  List<ParkingSpot> _spots = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _spots = widget.lot.spots; 
    _loadSpots(); 
  }

  void _loadSpots() async {
    var updatedSpots = await _apiService.getSpotsForLot(widget.lot.id);
    setState(() {
      _spots = updatedSpots;
      _isLoading = false;
    });
  }

  void _addSpot() async {
    final _formKey = GlobalKey<FormState>();
    
    TextEditingController numCtrl = TextEditingController();
    TextEditingController rateCtrl = TextEditingController();

    await showDialog(
      context: context,
      barrierDismissible: false, 
      builder: (_) => AlertDialog(
        title: Text("Adaugă Loc"),
        content: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                controller: numCtrl,
                decoration: InputDecoration(
                  labelText: "Număr Loc (ex: A-5)",
                  hintText: "Doar litere, cifre și cratime"
                ),
                inputFormatters: [
                  FilteringTextInputFormatter.allow(RegExp(r'[a-zA-Z0-9-]')),
                ],
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Introdu numărul locului';
                  }
                  if (value.length < 2) {
                    return 'Prea scurt';
                  }
                  return null;
                },
              ),
              
              SizedBox(height: 10),

              TextFormField(
                controller: rateCtrl,
                decoration: InputDecoration(
                  labelText: "Tarif/oră",
                  hintText: "Ex: 10.5",
                  suffixText: "RON"
                ),
                keyboardType: TextInputType.numberWithOptions(decimal: true),
                inputFormatters: [
                  FilteringTextInputFormatter.allow(RegExp(r'^\d+\.?\d*')),
                ],
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Introdu tariful';
                  }
                  if (double.tryParse(value) == null) {
                    return 'Număr invalid';
                  }
                  return null;
                },
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context), 
            child: Text("Anulează")
          ),
          ElevatedButton(
            onPressed: () async {
              if (_formKey.currentState!.validate()) {
                
                double rate = double.parse(rateCtrl.text);
                
                bool success = await _apiService.addSpot(widget.lot.id, numCtrl.text, rate);
                
                Navigator.pop(context);

                if (success) {
                  _loadSpots();
                  widget.refreshParent();
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(backgroundColor: Colors.green, content: Text("Loc adăugat!")));
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(backgroundColor: Colors.red, content: Text("Eroare la salvare!")));
                }
              }
            }, 
            child: Text("Salvează")
          )
        ],
      )
    );
}

  void _deleteSpot(int spotId) async {
    bool confirm = await showDialog(
      context: context, 
      builder: (ctx) => AlertDialog(
        title: Text("Ștergi locul?"),
        content: Text("Sigur vrei să ștergi acest loc de parcare?"),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx, false), child: Text("Nu")),
          TextButton(onPressed: () => Navigator.pop(ctx, true), child: Text("Da", style: TextStyle(color: Colors.red))),
        ],
      )
    ) ?? false;

    if (confirm) {
      await _apiService.deleteSpot(spotId);
      _loadSpots();
      widget.refreshParent();
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Loc șters!")));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Locuri: ${widget.lot.name}"),
        centerTitle: true,
        backgroundColor: Colors.blueAccent,
        foregroundColor: Colors.white,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.vertical(bottom: Radius.circular(30)),
        ),
      ),
      body: _isLoading 
          ? Center(child: CircularProgressIndicator())
          : ListView.builder(
              padding: EdgeInsets.all(16),
              itemCount: _spots.length,
              itemBuilder: (context, index) {
                var spot = _spots[index];
                
                return Container(
                  margin: EdgeInsets.only(bottom: 12),
                  decoration: BoxDecoration(
                    color: spot.isAvailable 
                        ? Colors.lightBlue.withOpacity(0.15)
                        : Colors.grey.withOpacity(0.15),
                    borderRadius: BorderRadius.circular(15),
                    border: Border.all(
                      color: spot.isAvailable ? Colors.blue.withOpacity(0.3) : Colors.grey.withOpacity(0.3)
                    ),
                  ),
                  child: ListTile(
                    contentPadding: EdgeInsets.symmetric(horizontal: 16, vertical: 4),
                    leading: Container(
                      padding: EdgeInsets.all(8),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        shape: BoxShape.circle,
                        boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 4)]
                      ),
                      child: Icon(
                        Icons.local_parking, 
                        color: spot.isAvailable ? Colors.green : Colors.red
                      ),
                    ),
                    title: Text(
                      "Loc ${spot.spotNumber}", 
                      style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)
                    ),
                    subtitle: Text(
                      "${spot.hourlyRate} RON/h - ${spot.isAvailable ? 'Liber' : 'Ocupat'}",
                      style: TextStyle(color: Colors.black87)
                    ),
                    trailing: IconButton(
                      icon: Icon(Icons.delete_outline, color: Colors.red),
                      onPressed: () => _deleteSpot(spot.id),
                    ),
                  ),
                );
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: _addSpot,
        backgroundColor: Colors.blueAccent,
        foregroundColor: Colors.white,
        child: Icon(Icons.add),
      ),
    );
  }
}