package com.dimas.uasandroid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DBNAME = "db_todo";
    private static final String TABLENAME = "todo";

    private static String colID = "id";
    private static String colTitle = "title";
    private static String colBody = "body";

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLENAME + " (" +
                colID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colTitle + " TEXT," +
                colBody + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLENAME);
        onCreate(db);
    }

    public void insertData(String title, String body){
        String insertData = "INSERT INTO "+ TABLENAME + " ("+ colTitle +","+colBody+") VALUES ('"+title +"', '"+body+"')";
        this.getWritableDatabase().execSQL(insertData);
    }

    public void updateData(int id, String title, String body){
        String updateData = "UPDATE "+TABLENAME+ " SET "+ colTitle + "= '"+title +"', "+colBody + "= '"+body + "' WHERE "+colID +" ="+id;
        this.getWritableDatabase().execSQL(updateData);
    }

    public void deleteData(int id){
        String deleteData = "DELETE FROM "+TABLENAME +" WHERE id="+id;
        this.getWritableDatabase().execSQL(deleteData);
    }

    public Model getData(int id){
        Model model = null;
        String selectData = "SELECT * FROM "+TABLENAME + " WHERE id="+String.valueOf(id);
        Cursor data = this.getWritableDatabase().rawQuery(selectData, null);
        if(data.moveToFirst()){
            model = new Model(Integer.parseInt(data.getString(data.getColumnIndex(colID))),
                    data.getString(data.getColumnIndex(colTitle)), data.getString(data.getColumnIndex(colBody)));
        }
        return model;
    }

    public List<Model> getAll(){
        List<Model> model = new ArrayList<>();
        String selectData = "SELECT * FROM "+TABLENAME;
        Cursor data = this.getWritableDatabase().rawQuery(selectData, null);
        if(data.moveToFirst()){
            do{
                model.add(new Model(Integer.parseInt(data.getString(data.getColumnIndex(colID))),
                        data.getString(data.getColumnIndex(colTitle)), data.getString(data.getColumnIndex(colBody))));
            }while (data.moveToNext());
        }
        return model;
    }
}
