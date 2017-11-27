package com.tictactoe.android.tictactoe;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PartyActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database;

    String id;
    Button c11, c12, c13, c21, c22, c23, c31, c32, c33;
    CountDownTimer countDownTimer;
    TextView tour, etat;

    Boolean round = true;
    String p1 = "0";
    String p2 = "X";
    int coups = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        final TextView decompte = (TextView) findViewById(R.id.decompte);
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                decompte.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                decompte.setText("Done !");
            }
        };

        etat = (TextView) findViewById(R.id.etatPartie);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        database = FirebaseDatabase.getInstance();
        tour = (TextView) findViewById(R.id.tour);
        flipRound(tour, countDownTimer);
        countDownTimer.start();

        c11 = (Button) findViewById(R.id.c11);
        c11.setOnClickListener(this);
        c12 = (Button) findViewById(R.id.c12);
        c12.setOnClickListener(this);
        c13 = (Button) findViewById(R.id.c13);
        c13.setOnClickListener(this);

        c21 = (Button) findViewById(R.id.c21);
        c21.setOnClickListener(this);
        c22 = (Button) findViewById(R.id.c22);
        c22.setOnClickListener(this);
        c23 = (Button) findViewById(R.id.c23);
        c23.setOnClickListener(this);

        c31 = (Button) findViewById(R.id.c31);
        c31.setOnClickListener(this);
        c32 = (Button) findViewById(R.id.c32);
        c32.setOnClickListener(this);
        c33 = (Button) findViewById(R.id.c33);
        c33.setOnClickListener(this);

        DatabaseReference myRef = database.getReference("parties").child(id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String sc11 = dataSnapshot.child("c11").getValue().toString();
                    c11.setText(sc11);
                    String sc12 = dataSnapshot.child("c12").getValue().toString();
                    c12.setText(sc12);
                    String sc13 = dataSnapshot.child("c13").getValue().toString();
                    c13.setText(sc13);

                    String sc21 = dataSnapshot.child("c21").getValue().toString();
                    c21.setText(sc21);
                    String sc22 = dataSnapshot.child("c22").getValue().toString();
                    c22.setText(sc22);
                    String sc23 = dataSnapshot.child("c23").getValue().toString();
                    c23.setText(sc23);

                    String sc31 = dataSnapshot.child("c31").getValue().toString();
                    c31.setText(sc31);
                    String sc32 = dataSnapshot.child("c32").getValue().toString();
                    c32.setText(sc32);
                    String sc33 = dataSnapshot.child("c33").getValue().toString();
                    c33.setText(sc33);

                    coups = Integer.valueOf(dataSnapshot.child("shots").getValue().toString());

                    controlGame(sc11, sc12, sc13, sc21, sc22, sc23, sc31, sc32, sc33);
                } catch (Exception e) {
                    Intent i = new Intent(PartyActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
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

    public boolean controlGame(String c11, String c12, String c13, String c21, String c22, String c23, String c31, String c32, String c33) {
        DatabaseReference myRef;
        boolean gagne = false;
        String winner = "";

        if ((c11.equals(c12)) && (c12.equals(c13)) && !c13.equals("") && !c13.equals(" ")) {
            gagne = true;
            winner = c13;
        } else if ((c21.equals(c22)) && (c22.equals(c23)) && !c23.equals("") && !c23.equals(" ")) {
            gagne = true;
            winner = c23;
        } else if ((c31.equals(c32)) && (c32.equals(c33)) && !c33.equals("") && !c33.equals(" ")) {
            gagne = true;
            winner = c33;
        } else if ((c11.equals(c21)) && (c21.equals(c31)) && !c31.equals("") && !c31.equals(" ")) {
            gagne = true;
            winner = c31;
        } else if ((c12.equals(c22)) && (c22.equals(c32)) && !c32.equals("") && !c32.equals(" ")) {
            gagne = true;
            winner = c32;
        } else if ((c13.equals(c23)) && (c23.equals(c33)) && !c33.equals("") && !c33.equals(" ")) {
            gagne = true;
            winner = c33;
        } else if ((c11.equals(c22)) && (c22.equals(c33)) && !c33.equals("") && !c33.equals(" ")) {
            gagne = true;
            winner = c33;
        } else if ((c13.equals(c22)) && (c22.equals(c31)) && !c31.equals("") && !c31.equals(" ")) {
            gagne = true;
            winner = c31;
        }
        myRef = database.getReference("parties").child(id);
        if (gagne) {
            myRef.child("finished").setValue(true);
            myRef.child("winner").setValue(winner);
            setUnClickable();
            etat.setText("Le joueur " + winner + " a gagné");
        }else{
            myRef.child("finished").setValue(false);
            myRef.child("winner").setValue("");
        }

        return gagne;
    }

    public void setClickable(){
        c11.setClickable(true);
        c12.setClickable(true);
        c13.setClickable(true);
        c21.setClickable(true);
        c22.setClickable(true);
        c23.setClickable(true);
        c31.setClickable(true);
        c32.setClickable(true);
        c33.setClickable(true);
    }

    public void setUnClickable(){
        c11.setClickable(false);
        c12.setClickable(false);
        c13.setClickable(false);
        c21.setClickable(false);
        c22.setClickable(false);
        c23.setClickable(false);
        c31.setClickable(false);
        c32.setClickable(false);
        c33.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        DatabaseReference myRef;
        String player = round ? p1 : p2;

        myRef = database.getReference("parties").child(id).child("shots");
        myRef.setValue(coups + 1);

        switch (v.getId()) {
            case R.id.c11:
                c11.setText(player);
                c11.setClickable(false);
                flipRound(tour, countDownTimer);
                myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue(player);
                break;
            case R.id.c12:
                c12.setText(player);
                c12.setClickable(false);
                flipRound(tour, countDownTimer);
                myRef = database.getReference("parties").child(id).child("c12");
                myRef.setValue(player);
                break;
            case R.id.c13:
                c13.setText(player);
                c13.setClickable(false);
                flipRound(tour, countDownTimer);
                myRef = database.getReference("parties").child(id).child("c13");
                myRef.setValue(player);
                break;

            case R.id.c21:
                c21.setText(player);
                c21.setClickable(false);
                flipRound(tour, countDownTimer);
                myRef = database.getReference("parties").child(id).child("c21");
                myRef.setValue(player);
                break;
            case R.id.c22:
                c22.setText(player);
                c22.setClickable(false);
                flipRound(tour, countDownTimer);
                myRef = database.getReference("parties").child(id).child("c22");
                myRef.setValue(player);
                break;
            case R.id.c23:
                c23.setText(player);
                c23.setClickable(false);
                flipRound(tour, countDownTimer);
                myRef = database.getReference("parties").child(id).child("c23");
                myRef.setValue(player);
                break;

            case R.id.c31:
                c31.setText(player);
                c31.setClickable(false);
                flipRound(tour, countDownTimer);
                myRef = database.getReference("parties").child(id).child("c31");
                myRef.setValue(player);
                break;
            case R.id.c32:
                c32.setText(player);
                c32.setClickable(false);
                flipRound(tour, countDownTimer);
                myRef = database.getReference("parties").child(id).child("c32");
                myRef.setValue(player);
                break;
            case R.id.c33:
                c33.setText(player);
                c33.setClickable(false);
                flipRound(tour, countDownTimer);
                myRef = database.getReference("parties").child(id).child("c33");
                myRef.setValue(player);
                break;

            default:
                break;
        }
    }
}
