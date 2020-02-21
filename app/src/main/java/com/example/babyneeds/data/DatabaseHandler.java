package com.example.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.babyneeds.model.Item;
import com.example.babyneeds.util.Util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, Util.DataBase_Name , null,Util.DataBase_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Create_Table= "CREATE TABLE "+Util.Table_Name+"("+Util.Item_ID+"INTEGER PRIMARY KEY,"
                +Util.Item_Name+"TEXT NOT NULL,"+Util.Item_Quantity+"DOUBLE,"+Util.Item_Colour+"TEXT,"
                +Util.Item_Size+"TEXT,"+Util.Added_Date+"Long)";
        db.execSQL(Create_Table);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ Util.Table_Name);

        onCreate(db);

    }


    //CRUD Operations

    public void addItems(Item item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Util.Item_Name,item.getItemName());
        values.put(Util.Item_Quantity,item.getItemQuantity());
        values.put(Util.Item_Colour,item.getItemColor());
        values.put(Util.Item_Size,item.getItemSize());
        values.put(Util.Added_Date,java.lang.System.currentTimeMillis());
        db.insert(Util.Table_Name,null,values);
        db.close();
    }

    public Item getItem(int id){

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c= db.query(Util.Table_Name,new String[]{
                Util.Item_Name,
                Util.Item_Size,
                Util.Item_Colour,
                Util.Item_Size,
                Util.Added_Date},
                Util.Item_ID+"=?",
                new String[]
                        {
                                String.valueOf(id)
                        },null,null,null,null);

        if (c!=null)
            c.moveToFirst();

        Item item=new Item();

        item.setId(Integer.parseInt(c.getString(c.getColumnIndex(Util.Item_ID))));
        item.setItemName(c.getString(c.getColumnIndex(Util.Item_Name)));
        item.setItemSize(Integer.parseInt(c.getString(c.getColumnIndex(Util.Item_Size))));
        item.setItemColor(c.getString(c.getColumnIndex(Util.Item_Colour)));
        item.setItemQuantity(Integer.parseInt(c.getString(c.getColumnIndex(Util.Item_Quantity))));

        DateFormat dateFormat=DateFormat.getDateInstance();
        String formattedDate=dateFormat.format(new Date(c.getLong(c.getColumnIndex(Util.Added_Date))).getTime());
        item.setDateItemAdded(formattedDate);

        return item;

    }

    public List<Item> getAll()
    {

        SQLiteDatabase db=this.getReadableDatabase();
        List<Item> itemList=new ArrayList<>();
        Cursor c= db.query(Util.Table_Name,new String[]{
                        Util.Item_Name,
                        Util.Item_Size,
                        Util.Item_Colour,
                        Util.Item_Size,
                        Util.Added_Date},
                null,null,null,null,Util.Added_Date+"DESC");

        if(c.moveToFirst())
            do {
                Item item=new Item();
                item.setId(Integer.parseInt(c.getString(c.getColumnIndex(Util.Item_ID))));
                item.setItemName(c.getString(c.getColumnIndex(Util.Item_Name)));
                item.setItemSize(Integer.parseInt(c.getString(c.getColumnIndex(Util.Item_Size))));
                item.setItemColor(c.getString(c.getColumnIndex(Util.Item_Colour)));
                item.setItemQuantity(Integer.parseInt(c.getString(c.getColumnIndex(Util.Item_Quantity))));

                DateFormat dateFormat=DateFormat.getDateInstance();
                String formattedDate=dateFormat.format(new Date(c.getLong(c.getColumnIndex(Util.Added_Date))).getTime());
                item.setDateItemAdded(formattedDate);

                itemList.add(item);
            }while(c.moveToNext());


            return itemList;
    }

    public int getItemCount()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c= db.rawQuery("SELECT * FROM "+ Util.Table_Name, null);
        return c.getCount();
    }

    public void delete(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Util.Table_Name, Util.Item_ID+"=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int update(Item item)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Util.Item_Name,item.getItemName());
        values.put(Util.Item_Quantity,item.getItemQuantity());
        values.put(Util.Item_Colour,item.getItemColor());
        values.put(Util.Item_Size,item.getItemSize());
        values.put(Util.Added_Date,java.lang.System.currentTimeMillis());

        return db.update(Util.Table_Name,values,Util.Item_ID+"=?",
                new String[]{String.valueOf(item.getId())});
    }
}
