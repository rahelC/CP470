package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    ListView chatView;
    EditText chatEditText;
    Button sendBtn;
    ArrayList<String> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatView = (ListView) findViewById(R.id.chatView);
        chatEditText = (EditText) findViewById(R.id.editChat);
        sendBtn = (Button) findViewById(R.id.sendButton);

        //in this case, “this” is the ChatWindow, which is-A Context object
        ChatAdapter messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);
    }

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

            TextView message = (TextView) result.findViewById(R.id.editChat);
            message.setText(getItem(position)); // get the string at position

            return result;
        }
    }

    // Whenever the user clicks it, add it to ArrayList
    public void onClicked(View v) {
        chatList.add(chatEditText.getText().toString());

        ChatAdapter messageAdapter = new ChatAdapter(this);
        messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()

        chatEditText.setText("");
    }
}