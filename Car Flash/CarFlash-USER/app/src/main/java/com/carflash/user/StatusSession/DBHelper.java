package com.carflash.user.StatusSession;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";
    public static final String DATABASE_NAME = "Fleet.db";
    public static final String TABLE_NAME = "Ride_Table";


    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RIDE_ID = "ride_id";
    public static final String COLUMN_RIDE_STATUS = "ride_status";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NAME + " " +
                        "(" + COLUMN_ID + " integer primary key," +
                        " " + COLUMN_RIDE_ID + " text," +
                        " " + COLUMN_RIDE_STATUS + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public void insertRow(String Ride_id, String Ride_status) {
        try{
            if(checkRideExsistOrNot(Ride_id)){ // update row status
                updateRow(Ride_id , Ride_status);
            }else{ // create new row
                Log.i(""+TAG , "Inserting a new row in Database.");
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_RIDE_ID, Ride_id);
                contentValues.put(COLUMN_RIDE_STATUS, Ride_status);
                db.insert(TABLE_NAME, null, contentValues);
            }
        }catch (Exception e){
            Log.e(""+TAG , "TAX Exception "+e.getMessage());
        }

    }


    public String getStatusAccordingToId(String ride_id) {
        String data = "" ;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_RIDE_ID + "=" + ride_id + "", null);
            if (res.moveToFirst()){
                do{
                    data = res.getString(res.getColumnIndex(COLUMN_RIDE_STATUS));
                    // do what ever you want here
                }while(res.moveToNext());
            }
            res.close();
            Log.i(""+TAG , "Status According to ID "+ride_id+" = "+data);
        }catch (Exception e){
            Log.e(""+TAG , "Exception in getStatusAccordingToId()  "+e.getMessage());
        }

        return data;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }



    public Integer deleteLocation(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Location",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }


    public void updateRow(String ride_id, String ride_status) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_RIDE_ID, ride_id);
            contentValues.put(COLUMN_RIDE_STATUS, ride_status);
            db.update(TABLE_NAME, contentValues, COLUMN_RIDE_ID + " = ? ", null);
            Log.i(""+TAG , "rRow Updated with Ride_Id = "+ride_id+"  with Ride Status = "+ride_status);
        }catch (Exception e){
            Log.e(""+TAG , "TAXi EXCEPTION "+e.getMessage());
        }
    }

    public boolean checkRideExsistOrNot (String Rideid) {
        Log.i(""+TAG , "Checking Exsistance of Ride ID ="+Rideid);
        Cursor c ;
        c = this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+COLUMN_RIDE_ID+" = '" + Rideid + "'", null);
        if (c.getCount() > 0) { // This will get the number of rows
           return true;
        }else {
            return false;
        }
    }

    public ArrayList<String> getAllRideId(){
        ArrayList<String> Ride_ids  = new ArrayList<>();
        String searchQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(searchQuery, null);
        if (res.moveToFirst()){
            do{
                Ride_ids.add(""+res.getString(res.getColumnIndex(COLUMN_RIDE_ID)));
            }while(res.moveToNext());
        }
        return Ride_ids;
    }

    public  void deleteRowByid(String Ride_id){
        try {
            this.getReadableDatabase().delete(TABLE_NAME, COLUMN_RIDE_ID+"="+Ride_id, null);
            Log.i(""+TAG , "Deleting a row with ride_id = "+Ride_id);
        }
        catch(Exception e) {
            Log.e(""+TAG , "TAXI EXCEPTION "+e.getMessage());
        }
    }
}