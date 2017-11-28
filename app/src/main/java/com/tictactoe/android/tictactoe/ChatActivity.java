package com.tictactoe.android.tictactoe;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {
    FirebaseDatabase database;
    ChatView mChatView;
    ConstraintLayout messageViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        String myName = "Michael";

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Emily";

        final User me = new User(myId, myName, myIcon);
        final User you = new User(yourId, yourName, yourIcon);

        database = FirebaseDatabase.getInstance();
        mChatView = (ChatView) findViewById(R.id.chatView);
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan900));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);
        mChatView.setOnBubbleClickListener(new Message.OnBubbleClickListener() {
            @Override
            public void onClick(Message message) {
                Toast.makeText(
                        ChatActivity.this,
                        "click : " + message.getUser().getName() + " - " + message.getMessageText(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        //Click option button
        mChatView.setOnClickOptionButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar1 = Snackbar.make(view, "Vous devez saisir un nom d'utilisateur", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }
        });

        mChatView.setOnIconClickListener(new Message.OnIconClickListener() {
            @Override
            public void onIconClick(Message message) {
                System.out.println(message.getCreatedAt());
            }
        });
        mChatView.setOnBubbleLongClickListener(new Message.OnBubbleLongClickListener() {
            @Override
            public void onLongClick(Message message) {
                System.out.println(message.getMessageText());
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getApplicationContext())
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Do you want to Delete")

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                dialog.dismiss();
                            }

                        })


                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();
                myQuittingDialogBox.show();
            }
        });
        messageViewContainer = (ConstraintLayout) findViewById(R.id.messageViewContainer);
        DatabaseReference messagesRef = database.getReference("parties").child(getIntent().getStringExtra("id")).child("chatroom").child("messages");

        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSend = mChatView.getInputText();

                DatabaseReference myRef = database.getReference("parties").child(getIntent().getStringExtra("id"));
                DatabaseReference messageRef = myRef.child("chatroom").child("messages").push();
                messageRef.setValue(new com.tictactoe.android.tictactoe.Models.Message(myRef.child("player1").getKey(), toSend, ServerValue.TIMESTAMP));
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
                    if (sender.equals("player1")) {
                        Message message = new Message.Builder().setUser(me)
                                .setRightMessage(true)
                                .setMessageText(content)
                                .hideIcon(true)
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
                    //Reset edit text
                    mChatView.setInputText("");
                }
                mChatView.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
