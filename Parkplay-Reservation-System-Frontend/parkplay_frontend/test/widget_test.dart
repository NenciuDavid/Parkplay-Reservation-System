import 'package:flutter_test/flutter_test.dart';

import 'package:parkplay_frontend/main.dart';

void main() {
  testWidgets('Login screen smoke test', (WidgetTester tester) async {
    await tester.pumpWidget(ParkPlayApp());

    expect(find.text('ParkPlay Login'), findsOneWidget);
    
    expect(find.text('Autentificare'), findsOneWidget);
  });
}