package com.example.kanjigear.db;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;

import com.example.kanjigear.R;
import com.example.kanjigear.db.DatabaseOpenHelper;

public class LoadDatabaseAsync extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private AlertDialog alertDialog;
    private DatabaseOpenHelper dbOpenHelper;

    public LoadDatabaseAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(context);
        try {
            dbHelper.createDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbHelper.close();

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        AlertDialog.Builder d = new AlertDialog.Builder(context, androidx.constraintlayout.widget.R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        LayoutInflater layout = LayoutInflater.from(context);
        View dialogView = layout.inflate(R.layout.alert_dialog_copy_database, null);
        d.setTitle("Loading Database");
        d.setView(dialogView);
        alertDialog = d.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        alertDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
