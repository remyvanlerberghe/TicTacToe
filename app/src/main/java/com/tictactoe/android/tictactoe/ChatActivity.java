package com.tictactoe.android.tictactoe;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.models.Message;

import com.github.bassaer.chatmessageview.views.ChatView;
import com.github.bassaer.chatmessageview.views.MessageView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    FirebaseDatabase database;
    ChatView mChatView;
    ConstraintLayout messageViewContainer;
    User me;
    User you;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String player1 = getIntent().getStringExtra("player1");
        TextDrawable p1 = TextDrawable.builder()
                .buildRect(player1.toUpperCase().substring(0, 1), ContextCompat.getColor(this, R.color.chatGreyDark));
        Bitmap p1icon = drawableToBitmap(p1);

        String player2 = getIntent().getStringExtra("player2");
        TextDrawable p2 = TextDrawable.builder()
                .buildRect(player2.toUpperCase().substring(0, 1), ContextCompat.getColor(this, R.color.chatGreyDark));
        Bitmap p2icon = drawableToBitmap(p2);
        if (Globals.name.equals(player1)) {
            me = new User(1, player1, p1icon);
            you = new User(2, player2, p2icon);
        } else {
            me = new User(2, player2, p2icon);
            you = new User(1, player1, p1icon);
        }
        database = FirebaseDatabase.getInstance();
        mChatView = (ChatView) findViewById(R.id.chatView);
        mChatView.setBackgroundColor(Color.WHITE);
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setInputTextHint("Message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);
        messageViewContainer = (ConstraintLayout) findViewById(R.id.messageViewContainer);
        DatabaseReference messagesRef = database.getReference("parties").child(getIntent().getStringExtra("id")).child("chatroom").child("messages");
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSend = mChatView.getInputText();
                DatabaseReference myRef = database.getReference("parties").child(getIntent().getStringExtra("id"));
                DatabaseReference messageRef = myRef.child("chatroom").child("messages").push();
                messageRef.setValue(new com.tictactoe.android.tictactoe.Models.Message(Globals.name, toSend, ServerValue.TIMESTAMP));
            }
        });
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChatView.setRefreshing(true);
                ((MessageView) (mChatView.findViewById(R.id.message_view))).init(new ArrayList());
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String sender = (String) messageSnapshot.child("sender").getValue();
                    String content = (String) messageSnapshot.child("content").getValue();
                    long time = (long) messageSnapshot.child("time").getValue();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(time);
                    if (sender.equals(Globals.name)) {
                        Message message = new Message.Builder().setUser(me)
                                .setRightMessage(true)
                                .setMessageText(content)
                                .setCreatedAt(calendar)
                                .setDateCell(true)
                                .build();
                        mChatView.send(message);

                    } else {
                        Message receivedMessage = new Message.Builder()
                                .setUser(you)
                                .setRightMessage(false)
                                .setMessageText(content)
                                .setCreatedAt(calendar)
                                .setDateCell(true)
                                .build();
                        mChatView.send(receivedMessage);
                    }
                    mChatView.setInputText("");
                }
                mChatView.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                NavUtils.navigateUpTo(this, intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }


        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 96; // Replaced the 1 by a 96
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 96; // Replaced the 1 by a 96

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
