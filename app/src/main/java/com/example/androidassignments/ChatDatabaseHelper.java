package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Messages.db";
    private static final int VERSION_NUM = 3;

    public static final String TABLE_NAME = "chats";

    public static final String KEY_ID = "id";
    public static final String KEY_MESSAGE = "message";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_MESSAGE + " text not null);";

    public ChatDatabaseHelper (Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ChatDatabaseHelper", "Calling onCreate");
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
