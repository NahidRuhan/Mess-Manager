package com.example.messmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "MealManager.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_DEPOSITS = "deposits";
    private static final String TABLE_MEALS = "meals";

    // User Table Columns
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "name";

    // Deposit Table Columns
    private static final String COLUMN_DEPOSIT_ID = "deposit_id";
    private static final String COLUMN_DEPOSIT_AMOUNT = "amount";
    private static final String COLUMN_DEPOSIT_DATE = "date";

    // Meal Table Columns
    private static final String COLUMN_MEAL_ID = "meal_id";
    private static final String COLUMN_MEAL_DATE = "date";
    private static final String COLUMN_MEAL_AMOUNT = "meal_amount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Deposits Table
        String CREATE_DEPOSITS_TABLE = "CREATE TABLE " + TABLE_DEPOSITS + "("
                + COLUMN_DEPOSIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_DEPOSIT_AMOUNT + " REAL,"
                + COLUMN_DEPOSIT_DATE + " TEXT)";
        db.execSQL(CREATE_DEPOSITS_TABLE);

        // Create Meals Table
        String CREATE_MEALS_TABLE = "CREATE TABLE " + TABLE_MEALS + "("
                + COLUMN_MEAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_MEAL_AMOUNT + " REAL,"
                + COLUMN_MEAL_DATE + " TEXT)";
        db.execSQL(CREATE_MEALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPOSITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        onCreate(db);
    }

    // Add a Deposit
    public long addDeposit(int userId, double amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_DEPOSIT_AMOUNT, amount);
        values.put(COLUMN_DEPOSIT_DATE, date);
        long result = db.insert(TABLE_DEPOSITS, null, values);
        db.close(); // Close the database connection
        return result;
    }

    // Add a Meal
    public long addMeal(int userId,double amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_MEAL_DATE, date);
        values.put(COLUMN_MEAL_AMOUNT, amount);
        long result = db.insert(TABLE_MEALS, null, values);
        if (result == -1) {
            System.out.println("Failed to add meal for user ID: " + userId);
        } else {
            System.out.println("Meal added successfully for user ID: " + userId);
        }
        db.close(); // Close the database connection
        return result;
    }

    // Calculate Total Deposits
    public double getTotalDeposits() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_DEPOSIT_AMOUNT + ") AS total FROM " + TABLE_DEPOSITS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        cursor.close();
        return 0;
    }

    // Calculate Total Meals for all users
    public int getTotalMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_MEAL_AMOUNT + ") AS total FROM " + TABLE_MEALS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int totalMeals = cursor.getInt(0);
            System.out.println("Total meals in database: " + totalMeals);
            cursor.close(); // Close the cursor to avoid memory leaks
            return totalMeals;
        }
        cursor.close(); // Close the cursor to avoid memory leaks
        System.out.println("No meals found in the database.");
        return 0; // Return 0 if no meals are found
    }

    // Calculate Total Meals for a specific user
    public int getUserTotalMeals(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_MEAL_AMOUNT + ") AS total FROM " + TABLE_MEALS + " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        cursor.close();
        return 0;
    }

    // Get total deposits for a specific user
    public double getUserTotalDeposits(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_DEPOSIT_AMOUNT + ") AS total FROM " + TABLE_DEPOSITS + " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            double totalDeposits = cursor.getDouble(0);
            cursor.close(); // Close the cursor to avoid memory leaks
            return totalDeposits;
        }
        cursor.close(); // Close the cursor to avoid memory leaks
        return 0; // Return 0 if no deposits are found
    }

    // Calculate Unit Price
    public double calculateUnitPrice() {
        double totalDeposits = getTotalDeposits();
        int totalMeals = getTotalMeals();
        if (totalMeals == 0) return 0;
        return totalDeposits / totalMeals;
    }

    // Reset the database by deleting all rows from deposits and meals tables
    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEPOSITS, null, null); // Delete all rows from deposits table
        db.delete(TABLE_MEALS, null, null);    // Delete all rows from meals table
        db.close();
    }
}