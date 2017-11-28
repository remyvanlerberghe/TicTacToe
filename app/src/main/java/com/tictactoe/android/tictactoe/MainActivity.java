package com.tictactoe.android.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    CoordinatorLayout cl_mainActivity;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cl_mainActivity = (CoordinatorLayout) findViewById(R.id.mainActivity);

        name = (EditText) findViewById(R.id.nomJoueur);
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = name.getText().toString();
                if(!nom.isEmpty() && nom != ""){
                    Globals.name = nom;
                    Intent i = new Intent(MainActivity.this, InviteActivity.class);
                    startActivity(i);
                }else{
                    Snackbar snackbar1 = Snackbar.make(cl_mainActivity, "Vous devez saisir un nom d'utilisateur", Snackbar.LENGTH_LONG);
                    snackbar1.show();
                }
            }
        });
    }
}
