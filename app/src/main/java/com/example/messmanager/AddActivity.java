package com.example.messmanager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    // Input fields for today's deposit and meal
    EditText ruhanTodayDeposit, ruhanTodayMeal, tanvirTodayDeposit, tanvirTodayMeal, stanlyTodayDeposit, stanlyTodayMeal;
    Button submitButton;

    // Database helper
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize input fields (EditTexts) for deposits and meals
        ruhanTodayDeposit = findViewById(R.id.ruhanTodayDeposit);
        ruhanTodayMeal = findViewById(R.id.ruhanTodayMeal);
        tanvirTodayDeposit = findViewById(R.id.tanvirTodayDeposit);
        tanvirTodayMeal = findViewById(R.id.tanvirTodayMeal);
        stanlyTodayDeposit = findViewById(R.id.stanlyTodayDeposit);
        stanlyTodayMeal = findViewById(R.id.stanlyTodayMeal);

        // Initialize the Submit button
        submitButton = findViewById(R.id.button2);

        // Handle the click event for the Submit button
        submitButton.setOnClickListener(v -> {
            try {
                // Step 1: Parse today's inputs from EditTexts
                double ruhanTodayDepositValue = parseInput(ruhanTodayDeposit);
                double ruhanTodayMealValue = parseInput(ruhanTodayMeal);
                double tanvirTodayDepositValue = parseInput(tanvirTodayDeposit);
                double tanvirTodayMealValue = parseInput(tanvirTodayMeal);
                double stanlyTodayDepositValue = parseInput(stanlyTodayDeposit);
                double stanlyTodayMealValue = parseInput(stanlyTodayMeal);

                // Step 2: Add today's date to the database
                dbHelper.addDeposit(1, ruhanTodayDepositValue, "2023-10-01"); // Assuming user ID 1 is Ruhan
                dbHelper.addMeal(1, ruhanTodayMealValue, "2023-10-01");

                dbHelper.addDeposit(2, tanvirTodayDepositValue, "2023-10-01"); // Assuming user ID 2 is Tanvir
                dbHelper.addMeal(2, tanvirTodayMealValue, "2023-10-01");

                dbHelper.addDeposit(3, stanlyTodayDepositValue, "2023-10-01"); // Assuming user ID 3 is Stanly
                dbHelper.addMeal(3,stanlyTodayMealValue, "2023-10-01");

                // Step 3: Notify MainActivity to update the UI
                setResult(RESULT_OK);
                finish();

            } catch (NumberFormatException e) {
                // Show a Toast message if inputs are invalid
                Toast.makeText(AddActivity.this, "Please correct invalid inputs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to parse input from EditText
    private double parseInput(EditText editText) throws NumberFormatException {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            editText.setError("This field is required");
            throw new NumberFormatException("Empty field");
        }
        return Double.parseDouble(text);
    }
}