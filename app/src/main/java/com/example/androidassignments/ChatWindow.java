package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);

        View.OnClickListener itemListener = new View.OnClickListener() {
            public void onClick(View v) {
                chatList.add(chatEditText.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                chatEditText.setText("");
            }
        };
        sendBtn.setOnClickListener(itemListener);
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

            TextView message = (TextView) result.findViewById(R.id.textView);
            message.setText(getItem(position)); // get the string at position

            return result;
        }
    }
}