package com.example.messmanager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declare TextViews for overall mess details
    TextView messTotalDeposit, messTotalMeal, messMealRate, messTotalMealCost, messMealRate2;

    // Declare TextViews for Ruhan's details
    TextView ruhanMeal, ruhanMeal2, ruhanDeposit, ruhanDeposit2, ruhanCost, ruhanCost2, ruhanBalance, ruhanBalance2;

    // Declare TextViews for Tanvir details
    TextView tanvirMeal, tanvirDeposit, tanvirCost, tanvirBalance;

    // Declare TextViews for Stanly's details
    TextView stanlyMeal, stanlyDeposit, stanlyCost, stanlyBalance;

    // Declare DatabaseHelper
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize TextViews for mess details
        messTotalDeposit = findViewById(R.id.messTotalDeposit);
        messTotalMeal = findViewById(R.id.messTotalMeal);
        messMealRate = findViewById(R.id.messMealRate);
        messTotalMealCost = findViewById(R.id.messTotalMealCost);
        messMealRate2 = findViewById(R.id.messMealRate2);

        // Initialize TextViews for Ruhan's details
        ruhanMeal = findViewById(R.id.ruhanMeal);
        ruhanMeal2 = findViewById(R.id.ruhanMeal2);
        ruhanDeposit = findViewById(R.id.ruhanDeposit);
        ruhanDeposit2 = findViewById(R.id.ruhanDeposit2);
        ruhanCost = findViewById(R.id.ruhanCost);
        ruhanCost2 = findViewById(R.id.ruhanCost2);
        ruhanBalance = findViewById(R.id.ruhanBalance);
        ruhanBalance2 = findViewById(R.id.ruhanBalance2);

        // Initialize TextViews for Tanvir details
        tanvirMeal = findViewById(R.id.tanvirMeal);
        tanvirDeposit = findViewById(R.id.tanvirDeposit);
        tanvirCost = findViewById(R.id.tanvirCost);
        tanvirBalance = findViewById(R.id.tanvirBalance);

        // Initialize TextViews for Stanly's details
        stanlyMeal = findViewById(R.id.stanlyMeal);
        stanlyDeposit = findViewById(R.id.stanlyDeposit);
        stanlyCost = findViewById(R.id.stanlyCost);
        stanlyBalance = findViewById(R.id.stanlyBalance);

        // Navigate to AddActivity when Add button is clicked
        Button addButton = findViewById(R.id.button);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivityForResult(intent, 1);
        });

        // Reset button functionality
        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> {
            // Clear all data from the database
            dbHelper.resetDatabase();

            // Update the UI to reflect the reset state
            updateUI();

            // Show a toast message to confirm reset
            Toast.makeText(MainActivity.this, "All data has been reset.", Toast.LENGTH_SHORT).show();
        });

        // Update UI with data from the database
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Update UI with new data
            updateUI();
        }
    }

    // Helper method to update the UI with data from the database
    private void updateUI() {
        // Fetch total deposits and meals from the database
        double totalMessDeposit = dbHelper.getTotalDeposits();
        int totalMessMeal = dbHelper.getTotalMeals();
        double messMealRateValue = dbHelper.calculateUnitPrice();

        // Update TextViews for mess details
        messTotalDeposit.setText(formatDouble(totalMessDeposit));
        messTotalMeal.setText(formatDouble(totalMessMeal));
        messMealRate.setText(formatDouble(messMealRateValue));
        messTotalMealCost.setText(formatDouble(totalMessDeposit)); // Total meal cost = total deposit
        messMealRate2.setText(formatDouble(messMealRateValue));

        // Fetch and update individual member details
        updateMemberDetails(1, ruhanMeal, ruhanMeal2, ruhanDeposit, ruhanDeposit2, ruhanCost, ruhanCost2, ruhanBalance, ruhanBalance2);
        updateMemberDetails(2, tanvirMeal, null, tanvirDeposit, null, tanvirCost, null, tanvirBalance, null);
        updateMemberDetails(3, stanlyMeal, null, stanlyDeposit, null, stanlyCost, null, stanlyBalance, null);
    }

    // Helper method to update member details
    private void updateMemberDetails(int userId, TextView mealView, TextView meal2View, TextView depositView, TextView deposit2View, TextView costView, TextView cost2View, TextView balanceView, TextView balance2View) {
        // Fetch total meals and deposits for the user
        double totalMeals = dbHelper.getUserTotalMeals(userId);
        double totalDeposits = dbHelper.getUserTotalDeposits(userId);

        // Calculate cost and balance
        double messMealRateValue = dbHelper.calculateUnitPrice();
        double cost = totalMeals * messMealRateValue;
        double balance = totalDeposits - cost;

        // Update TextViews
        mealView.setText(formatDouble(totalMeals));
        if (meal2View != null) meal2View.setText(formatDouble(totalMeals));
        depositView.setText(formatDouble(totalDeposits));
        if (deposit2View != null) deposit2View.setText(formatDouble(totalDeposits));
        costView.setText(formatDouble(cost));
        if (cost2View != null) cost2View.setText(formatDouble(cost));
        balanceView.setText(formatDouble(balance));
        if (balance2View != null) balance2View.setText(formatDouble(balance));
    }

    // Helper method to format double values to 2 decimal places
    private String formatDouble(double value) {
        return String.format("%.2f",value);
    }
}