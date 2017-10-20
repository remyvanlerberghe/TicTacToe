package com.tictactoe.android.tictactoe;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tictactoe.android.tictactoe.Model.Partie;

import org.w3c.dom.Text;

public class PartyActivity extends AppCompatActivity {

    Boolean round = true;
    String p1 = "0";
    String p2 = "X";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        final TextView decompte = (TextView) findViewById(R.id.decompte);
        final CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                decompte.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                decompte.setText("Done !");
            }
        };
        /*Intent intent = getIntent();
        final String id = intent.getStringExtra("id");*/

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final TextView tour = (TextView) findViewById(R.id.tour);
        flipRound(tour, countDownTimer);
        countDownTimer.start();
        final Button c11 = (Button) findViewById(R.id.c11);
        c11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c11.setText(round ? p1 : p2);
                c11.setClickable(false);
                flipRound(tour, countDownTimer);
          /*      DatabaseReference myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue("X");*/
            }
        });
        final Button c12 = (Button) findViewById(R.id.c12);
        c12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c12.setText(round ? p1 : p2);
                c12.setClickable(false);
                flipRound(tour, countDownTimer);
          /*      DatabaseReference myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue("X");*/
            }
        });
        final Button c13 = (Button) findViewById(R.id.c13);
        c13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c13.setText(round ? p1 : p2);
                c13.setClickable(false);
                flipRound(tour, countDownTimer);
          /*      DatabaseReference myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue("X");*/
            }
        });
        final Button c21 = (Button) findViewById(R.id.c21);
        c21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c21.setText(round ? p1 : p2);
                c21.setClickable(false);
                flipRound(tour, countDownTimer);
          /*      DatabaseReference myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue("X");*/
            }
        });
        final Button c22 = (Button) findViewById(R.id.c22);
        c22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c22.setText(round ? p1 : p2);
                c22.setClickable(false);
                flipRound(tour, countDownTimer);
          /*      DatabaseReference myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue("X");*/
            }
        });
        final Button c23 = (Button) findViewById(R.id.c23);
        c23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c23.setText(round ? p1 : p2);
                c23.setClickable(false);
                flipRound(tour, countDownTimer);
          /*      DatabaseReference myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue("X");*/
            }
        });
        final Button c31 = (Button) findViewById(R.id.c31);
        c31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c31.setText(round ? p1 : p2);
                c31.setClickable(false);
                flipRound(tour, countDownTimer);
          /*      DatabaseReference myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue("X");*/
            }
        });
        final Button c32 = (Button) findViewById(R.id.c32);
        c32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c32.setText(round ? p1 : p2);
                c32.setClickable(false);
                flipRound(tour, countDownTimer);
          /*      DatabaseReference myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue("X");*/
            }
        });
        final Button c33 = (Button) findViewById(R.id.c33);
        c33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c33.setText(round ? p1 : p2);
                c33.setClickable(false);
                flipRound(tour, countDownTimer);
          /*      DatabaseReference myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue("X");*/
            }
        });

       /* DatabaseReference myRef = database.getReference("parties").child(id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String sc11 = dataSnapshot.child("c11").getValue().toString();
                    c11.setText(sc11);
                }catch (Exception e){
                    Intent i = new Intent(PartyActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });*/
    }

    public void flipRound(TextView tour, CountDownTimer timer) {
        round = !round;
        System.out.println(round);
        timer.cancel();
        timer.start();
        if (!round)
            tour.setText("A joueur 1 de jouer");
        else
            tour.setText("A joueur 2 de jouer");
    }
}
