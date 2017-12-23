package com.tictactoe.android.tictactoe;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tictactoe.android.tictactoe.Models.Party;

public class InviteActivity extends AppCompatActivity {

    ConstraintLayout cl_inviteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        cl_inviteActivity = (ConstraintLayout) findViewById(R.id.inviteActivity);

        Button inviterAmi = (Button) findViewById(R.id.inviterAmi);
        inviterAmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference myRef = database.getReference("parties").push();
                Party p = new Party();
                p.player1 = Globals.playerName;
                myRef.setValue(p);

                DatabaseReference joueurs = database.getReference("joueurs").child(Globals.playerId).child("partie");
                joueurs.setValue(myRef.getKey());

                Globals.partieId = myRef.getKey();

                Intent intentsms = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                intentsms.putExtra("sms_body", "Cliquez sur le lien pour rejoindre cet ami dans Tic Tac Toe : www.danielpaul.fr/tictactoe/index.php?id=" + myRef.getKey());
                startActivity(intentsms);
            }
        });

        Button creerParite = (Button) findViewById(R.id.creerParite);
        creerParite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("parties").push();
                Party p = new Party();
                p.player1 = Globals.playerName;
                myRef.setValue(p);

                Globals.partieId = myRef.getKey().toString();

                Intent intent = new Intent(InviteActivity.this, PartyActivity.class);
                intent.putExtra("id", myRef.getKey().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                Intent i = new Intent(InviteActivity.this, ListActivity.class);
                startActivity(i);
                return true;

            case R.id.action_join:
                if(Globals.partieId == null || Globals.partieId.equals("") || Globals.partieId.equals(" ")){
                    Snackbar snackbar1 = Snackbar.make(cl_inviteActivity, "Vous devez créer une partie ou inviter un ami pour jouer", Snackbar.LENGTH_LONG);
                    snackbar1.show();
                }else{
                    Intent intentJoin = new Intent(InviteActivity.this, PartyActivity.class);
                    intentJoin.putExtra("id", Globals.partieId);
                    startActivity(intentJoin);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
