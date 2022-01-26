package com.cryptowatch.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cryptowatch.models.Currency;

import java.util.ArrayList;

public class CurrencyRepository {
    private final Database database;

    public CurrencyRepository(Database database) {
        this.database = database;
    }

    public void insertCurrency(Currency currency) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Currency.FIELD_ID, currency.getId());
        db.insert(Currency.TABLE_NAME, null, cv);
    }

    public int deleteCurrency(Currency currency) {
        SQLiteDatabase db = database.getReadableDatabase();
        return db.delete(Currency.TABLE_NAME, Currency.FIELD_ID + "=?", new String[] {currency.getId()});
    }

    @SuppressLint("Range")
    public boolean isCurrencyInserted(Currency currency) {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = ?", Currency.TABLE_NAME, Currency.FIELD_ID);
        Cursor cursor = db.rawQuery(query, new String[]{ String.valueOf(currency.getId())});
        return cursor.moveToFirst();
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllCurrencyId() {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = String.format("SELECT * FROM %s", Currency.TABLE_NAME);
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> list = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(Currency.FIELD_ID)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}