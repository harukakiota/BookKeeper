package com.example.bookkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Calendar;

import static com.example.bookkeeper.DateToString.convertToUnix;

/**
 * Created by Юлия on 22.05.2017.
 */

class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "db_logs";

    public DBHelper(Context context) {
        super(context, "BookKeeperDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с полями
        db.execSQL("create table dataTable ("
                + "id integer primary key autoincrement,"
                + "amount integer,"
                + "category integer,"
                + "unixdate integer" + ");");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // метод для ExpensesActivity и HistoryActivity

    public void saveEntry(int id, int amount, int category, Calendar date) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put("amount", amount);
        values.put("category", category);
        values.put("unixdate", convertToUnix(date));

        if (id == -1) {

            Log.d(LOG_TAG, "--- Insert in dataTable: ---");
            long rowID = db.insert("dataTable", null, values);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);

        } else {
            Log.d(LOG_TAG, "--- Update dataTable: ---");
            // обновляем по id
            int updCount = db.update("dataTable", values, "id = ?",
                    new String[] { Integer.toString(id) });
            Log.d(LOG_TAG, "updated rows count = " + updCount);
        }
        db.close();
    }

     // методы для HistoryActivity

    public String[][] searchEntries(int fromUnixValue, int toUnixValue, int category, String numberOfEntries, boolean onCreateHistory) {

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;

        String selection;
        String[] selectionArgs;
        String orderBy = "unixdate desc";
        String limit = null;
        if (!numberOfEntries.equals("Все")) {
            limit = numberOfEntries;
        }

        Log.d(LOG_TAG, "--- Rows in dataTable: ---");

        selection = "unixdate >= ? AND unixdate <= ?";
        if (category!=0) {
            selection = selection.concat(" AND category = ?");
            selectionArgs = new String[]{String.valueOf(fromUnixValue), String.valueOf(toUnixValue), String.valueOf(category-1)};
        } else {
            selectionArgs = new String[]{String.valueOf(fromUnixValue), String.valueOf(toUnixValue)};
        }

        cursor = db.query("dataTable",null,selection,selectionArgs,null,null,orderBy,limit);
        String [][] searchResults = new String[cursor.getCount()][4];
        int i = 0;
        if (cursor.moveToFirst()) {
                String str;
                do {
                    str = "";
                    int j = 0;
                    for (String columnNames : cursor.getColumnNames()) {

                        str = str.concat(columnNames + " = "
                                + cursor.getString(cursor.getColumnIndex(columnNames)) + "; ");
                        searchResults[i][j] = cursor.getString(j);
                        j++;
                    }
                    Log.d(LOG_TAG, str);
                    i++;

                } while (cursor.moveToNext());
        } else
            if (onCreateHistory) {
            db.delete("dataTable",null,null); }
            Log.d(LOG_TAG, "0 rows");
        cursor.close();
        db.close();
        return searchResults;
    }

    public void deleteEntry(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d(LOG_TAG, "--- Delete from dataTable: ---");
        int delCount = db.delete("dataTable", "id = " + id, null);
        Log.d(LOG_TAG, "deleted row id = " + id);
        db.close();
    }

    // методы для MainActivity

    public int [] countMonthlyByCategories(int startingDay) {
        int [] categoriesArr = new int[10];

        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);

        if (startingDay != 0) {
            if (startingDay > today) {
                if (calendar.get(Calendar.MONTH) > 0) {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)-1, startingDay);
                } else {
                    calendar.set(calendar.get(Calendar.YEAR)-1, 11, startingDay);
                }
            } else {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), startingDay);
            }
        } else {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        }

        int unixCompare = convertToUnix(calendar);


        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;

        String [] columns = new String[] { "category", "sum(amount) as amount" };
        String selection;
        String[] selectionArgs;
        String orderBy = "amount desc";
        String groupBy = "category";

        Log.d(LOG_TAG, "--- Rows in dataTable: ---");

        selection = "unixdate >= ?";
        selectionArgs = new String[]{String.valueOf(unixCompare)};

        cursor = db.query("dataTable",columns,selection,selectionArgs,groupBy,null,orderBy);
        if (cursor.moveToFirst()) {
            String str;
            do {
                str = "";
                for (String columnNames : cursor.getColumnNames()) {

                    str = str.concat(columnNames + " = "
                            + cursor.getString(cursor.getColumnIndex(columnNames)) + "; ");

                    categoriesArr[Integer.parseInt(cursor.getString(cursor.getColumnIndex("category")))] =
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("amount")));
                }
                Log.d(LOG_TAG, str);

            } while (cursor.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        cursor.close();
        close();
        return categoriesArr;
    }

    public int sumTotal(int [] categoriesArr) {
        int sum = 0;
        for (int i :categoriesArr ) {
            sum+=i;
        }
        return  sum;
    }
}
