package com.tictactoe.android.tictactoe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tictactoe.android.tictactoe.Models.Party;
import com.tictactoe.android.tictactoe.Models.User;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout cl_mainActivity;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cl_mainActivity = (ConstraintLayout) findViewById(R.id.mainActivity);

        name = (EditText) findViewById(R.id.nomJoueur);
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = name.getText().toString();
                if (!nom.isEmpty() && nom != "") {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("joueurs").push();
                    User u = new User();
                    u.nom = nom;
                    myRef.setValue(u);

                    Globals.playerId = myRef.getKey();
                    Globals.playerName = nom;

                    Intent i = new Intent(MainActivity.this, InviteActivity.class);
                    startActivity(i);
                } else {
                    Snackbar snackbar1 = Snackbar.make(cl_mainActivity, "Vous devez saisir un nom d'utilisateur", Snackbar.LENGTH_LONG);
                    snackbar1.show();
                }
            }
        });

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
}
