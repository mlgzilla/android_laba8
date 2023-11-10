package com.example.laba2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Users.db";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + "("
                + DBContract.UserEntry.COLUMN_NAME_KEY_ID + " INTEGER PRIMARY KEY," +
                DBContract.UserEntry.COLUMN_NAME_LOGIN + " TEXT," + DBContract.UserEntry.COLUMN_NAME_PASS + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.UserEntry.COLUMN_NAME_LOGIN, user.getLogin());
        values.put(DBContract.UserEntry.COLUMN_NAME_PASS, user.getPass());

        db.insert(DBContract.UserEntry.TABLE_NAME, null, values);
        db.close();
    }

    public Integer loginUser(String userLogin, String userPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME + " WHERE login = '" + userLogin + "' and pass = '" + userPass +"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
            return Integer.parseInt(cursor.getString(0));
        else
            return null;
    }

    public Integer authUser(String userLogin) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME + " WHERE login = '" + userLogin + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
            return Integer.parseInt(cursor.getString(0));
        else
            return null;
    }

    public User findByLogin(String userLogin) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME + " WHERE login = '" + userLogin + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            User user = new User();
            user.setID(Integer.parseInt(cursor.getString(0)));
            user.setLogin(cursor.getString(1));
            user.setPass(cursor.getString(2));
            return user;
        }
        else
            return null;
    }

    public void removeUser(String userLogin) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBContract.UserEntry.TABLE_NAME, "login = " + userLogin, null);
        db.close();
    }

    public void updatePass(User user, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.UserEntry.COLUMN_NAME_LOGIN, user.getLogin());
        values.put(DBContract.UserEntry.COLUMN_NAME_KEY_ID, user.getID());
        values.put(DBContract.UserEntry.COLUMN_NAME_PASS, newPass);
        db.update(DBContract.UserEntry.TABLE_NAME, values, "login = " + user.getLogin(), null);
        db.close();
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setLogin(cursor.getString(1));
                user.setPass(cursor.getString(2));
                usersList.add(user);
            } while (cursor.moveToNext());
        }
        return usersList;
    }
}

