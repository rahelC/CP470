package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    ListView chatView;
    EditText chatEditText;
    Button sendBtn;
    ArrayList<String> chatList = new ArrayList<>();

    private static ChatDatabaseHelper dbOpenHelper;
    public static SQLiteDatabase database;
    public static final String ACTIVITY_NAME = "ChatWindow";

    boolean isFrameLayout;

    class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        // Return the number of rows in ListView
        public int getCount() {
            return chatList.size();
        }

        // Return the item to show in the ListView at the specified position
        public String getItem(int position) {
            return chatList.get(position);
        }

        // Return the layout that will be positioned at the specified row in the ListView
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;

            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById(R.id.textView);
            message.setText(getItem(position)); // get the string at position

            return result;
        }

        public long getItemId(int position) {
            String query = "SELECT * FROM chats WHERE id = " + position;

            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToPosition(position);
            long data = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);

            return data;
        }
    }

    public void selectAll() {
        String query = "SELECT * FROM chats";

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String data = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            chatList.add(data);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + data);
            cursor.moveToNext();
        }
        Log.i(ACTIVITY_NAME, "Cursor’s column count = " + cursor.getColumnCount());

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Cursor’s column name " + i + ": " + cursor.getColumnName(i));
        }
    }

    public void open() throws SQLException {
        database = dbOpenHelper.getWritableDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatView = (ListView) findViewById(R.id.chatView);
        chatEditText = (EditText) findViewById(R.id.editChat);
        sendBtn = (Button) findViewById(R.id.sendButton);
        isFrameLayout = findViewById(R.id.frame) != null;

        if (isFrameLayout) {
            Log.i(ACTIVITY_NAME, "FrameLayout exists!");
        } else{
            Log.i(ACTIVITY_NAME, "FrameLayout doesn't exist!");
        }

        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);

        View.OnClickListener itemListener = new View.OnClickListener() {
            public void onClick(View v) {
                String currentMsg = chatEditText.getText().toString();
                chatList.add(currentMsg);
                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, currentMsg);
                database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);

                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                chatEditText.setText("");
            }
        };
        sendBtn.setOnClickListener(itemListener);

        ListView.OnItemClickListener itemClickListener = new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFrameLayout) {
                    MessageDetails.MessageFragment f = new MessageDetails.MessageFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Bundle args = new Bundle();
                    args.putLong("position", id);
                    f.setArguments(args);
                    ft.add(R.id.frame, f);
                    ft.commit();
                } else {
                    Intent mIntent = new Intent(ChatWindow.this, MessageDetails.class);
                    startActivity(mIntent);
                }
            }
        };

        chatView.setOnItemClickListener(itemClickListener);

        // Reading from a database file
        dbOpenHelper = new ChatDatabaseHelper(this);
        open();
        selectAll();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbOpenHelper.close();
    }
}