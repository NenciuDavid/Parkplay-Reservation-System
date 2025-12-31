import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'api_service.dart';

class RegisterScreen extends StatefulWidget {
  @override
  _RegisterScreenState createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final ApiService _apiService = ApiService();
  final _formKey = GlobalKey<FormState>();

  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _plateController = TextEditingController();

  bool _isLoading = false;
  String _selectedRole = 'DRIVER';

  void _register() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isLoading = true);

    String licensePlate = _selectedRole == 'DRIVER' ? _plateController.text : "-";

    bool success = await _apiService.register(
      _nameController.text,
      _emailController.text,
      _passwordController.text,
      _selectedRole,
      licensePlate,
    );

    setState(() => _isLoading = false);

    if (success) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(backgroundColor: Colors.green, content: Text("Cont creat! Te poți autentifica."))
      );
      Navigator.pop(context); 
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(backgroundColor: Colors.red, content: Text("Eroare la înregistrare. Email-ul există deja?"))
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Înregistrare Nouă")),
      body: Padding(
        padding: EdgeInsets.all(16),
        child: SingleChildScrollView(
          child: Form(
            key: _formKey,
            child: Column(
              children: [
                TextFormField(
                  controller: _nameController,
                  decoration: InputDecoration(labelText: "Nume Complet", prefixIcon: Icon(Icons.person)),
                  inputFormatters: [
                    FilteringTextInputFormatter.allow(RegExp(r'[a-zA-Z0-9\s]')),
                  ],
                  validator: (v) => v!.isEmpty ? "Introdu numele" : null,
                ),
                SizedBox(height: 10),

                TextFormField(
                  controller: _emailController,
                  decoration: InputDecoration(labelText: "Email", prefixIcon: Icon(Icons.email)),
                  keyboardType: TextInputType.emailAddress,
                  validator: (v) => !v!.contains("@") ? "Email invalid" : null,
                ),
                SizedBox(height: 10),

                TextFormField(
                  controller: _passwordController,
                  decoration: InputDecoration(labelText: "Parolă", prefixIcon: Icon(Icons.lock)),
                  obscureText: true,
                  validator: (v) => v!.length < 4 ? "Parola prea scurtă" : null,
                ),
                SizedBox(height: 20),

                DropdownButtonFormField<String>(
                  value: _selectedRole,
                  decoration: InputDecoration(labelText: "Tip Cont", prefixIcon: Icon(Icons.badge)),
                  items: [
                    DropdownMenuItem(value: 'DRIVER', child: Text("Șofer (Driver)")),
                    DropdownMenuItem(value: 'MANAGER', child: Text("Manager Parcare")),
                  ],
                  onChanged: (val) {
                    setState(() {
                      _selectedRole = val!;
                    });
                  },
                ),
                SizedBox(height: 10),

                if (_selectedRole == 'DRIVER')
                  TextFormField(
                    controller: _plateController,
                    decoration: InputDecoration(
                      labelText: "Număr Înmatriculare", 
                      prefixIcon: Icon(Icons.directions_car),
                      hintText: "ex: B-100-ABC"
                    ),
                    inputFormatters: [
                      FilteringTextInputFormatter.allow(RegExp(r'[a-zA-Z0-9-]')),
                      UpperCaseTextFormatter(),
                    ],
                    validator: (v) {
                      if (_selectedRole == 'DRIVER' && (v == null || v.isEmpty)) {
                        return "Introdu numărul mașinii";
                      }
                      return null;
                    },
                  ),

                SizedBox(height: 30),

                _isLoading 
                  ? CircularProgressIndicator()
                  : SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: _register,
                        style: ElevatedButton.styleFrom(padding: EdgeInsets.symmetric(vertical: 15)),
                        child: Text("Creează Cont", style: TextStyle(fontSize: 18)),
                      ),
                    ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class UpperCaseTextFormatter extends TextInputFormatter {
  @override
  TextEditingValue formatEditUpdate(TextEditingValue oldValue, TextEditingValue newValue) {
    return TextEditingValue(
      text: newValue.text.toUpperCase(),
      selection: newValue.selection,
    );
  }
}