package com.tictactoe.android.tictactoe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tictactoe.android.tictactoe.Models.Party;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intentsms = new Intent(MainActivity.this, PartyActivity.class);
//        //intentsms.putExtra("sms_body", "Cliquez sur le lien pour rejoindre cet ami dans Tic Tac Toe : www.danielpaul.fr/tictactoe?id=" + myRef.getKey());
//        startActivity(intentsms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                intentsms.putExtra("sms_body", "Cliquez sur le lien pour rejoindre cet ami dans Tic Tac Toe : www.danielpaul.fr/tictactoe?id=" + myRef.getKey());
                startActivity(intentsms);
            }
        });

        final EditText rejoindrePartieId = (EditText) findViewById(R.id.rejoindrePartieId);
        Button rejoindrePartie = (Button) findViewById(R.id.rejoindrePartie);
        rejoindrePartie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsms = new Intent(MainActivity.this, PartyActivity.class);
                intentsms.putExtra("id", rejoindrePartieId.getText().toString());
                startActivity(intentsms);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
