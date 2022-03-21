package com.example.kanjigear.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "kanjiGearDB.db";
    private static final int DATABASE_VERSION = 1;
    private static String dbPath = null;

    private final Context myContext;

    private SQLiteDatabase myDb = null;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
        dbPath = "/data/data/" + context.getPackageName() + "/databases/";
    }

    public boolean checkDb() {
        SQLiteDatabase checkDb = null;

        try {
            checkDb = SQLiteDatabase.openDatabase(getFilePath(), null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (Exception ex) {
        }
        if (checkDb != null) {
            checkDb.close();
        }

        return (checkDb != null);
    }

    public void createDatabase() throws IOException {
        boolean dbExists = checkDb();
        if (!dbExists) {
            this.getReadableDatabase();
            try {
                copyDatabase();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void copyDatabase() throws IOException {
        InputStream ios = myContext.getAssets().open(DATABASE_NAME);
        OutputStream os = new FileOutputStream(getFilePath());

        byte[] buffer = new byte[1024];
        int len;
        while ((len = ios.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }

        os.flush();
        ios.close();
        os.close();

        Log.d("CopyDb", "Database copied");
    }

    public void openDatabase() throws SQLException {
        if (myDb == null) {
            myDb = SQLiteDatabase.openDatabase(getFilePath(), null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public void openDatabaseRead() throws SQLException {
        if (myDb == null) {
            myDb = SQLiteDatabase.openDatabase(getFilePath(), null, SQLiteDatabase.OPEN_READONLY);
        }
    }

    public void closeDatabase() {
        if (myDb != null) {
            myDb.close();
            myDb = null;
        }
    }

    @Override
    public synchronized void close() {
        closeDatabase();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            this.getReadableDatabase();
            myContext.deleteDatabase(DATABASE_NAME);
            copyDatabase();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Cursor handleQuery(String query) {
        return myDb.rawQuery(query, null);
    }

    public String getFilePath() {
        return dbPath + DATABASE_NAME;
    }

    public void insert(String table, ContentValues values) {
        myDb.insert(table, null, values);
    }

    public void delete(String table, String idName, String id) {
        myDb.delete(table, idName + "= '" + id + "'" , null);
    }
    public void delete(String table, String idName1, String id1, String idName2, String id2) {
        myDb.delete(table, idName1 + " = '" + id1 + "' AND " + idName2 + " = '" + id2 + "'", null);
    }

    public void update(String table, ContentValues values, String idName, String id) {
        myDb.update(table, values, idName + "= '" + id + "'", null);
    }
}
