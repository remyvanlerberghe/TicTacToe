package com.tictactoe.android.tictactoe;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tictactoe.android.tictactoe.Models.Party;

public class InviteActivity extends AppCompatActivity {

    EditText et_nomJoueur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        et_nomJoueur = (EditText) findViewById(R.id.nomJoueur);

        String name = Globals.name;
        et_nomJoueur.setText(name);

        final EditText player1 = (EditText) findViewById(R.id.nomJoueur);
        Button inviterAmi = (Button) findViewById(R.id.inviterAmi);
        inviterAmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("parties").push();
                Party p = new Party();
                p.player1 = player1.getText().toString();
                myRef.setValue(p);

                Intent intentsms = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                intentsms.putExtra("sms_body", "Cliquez sur le lien pour rejoindre cet ami dans Tic Tac Toe : www.danielpaul.fr/tictactoe/index.php?id=" + myRef.getKey());
                startActivity(intentsms);

                final EditText rejoindrePartieId = (EditText) findViewById(R.id.rejoindrePartieId);
                rejoindrePartieId.setText(myRef.getKey());
            }
        });

        final EditText rejoindrePartieId = (EditText) findViewById(R.id.rejoindrePartieId);
        Button rejoindrePartie = (Button) findViewById(R.id.rejoindrePartie);
        rejoindrePartie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InviteActivity.this, PartyActivity.class);
                intent.putExtra("id", rejoindrePartieId.getText().toString());
                startActivity(intent);
            }
        });
    }
}
