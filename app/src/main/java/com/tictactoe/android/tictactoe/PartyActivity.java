package com.tictactoe.android.tictactoe;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.deeplinkdispatch.DeepLink;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;

@DeepLink("ttt://{id}")
public class PartyActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout cl_partyActivity;

    FirebaseDatabase database;

    String id;
    Button c11, c12, c13, c21, c22, c23, c31, c32, c33;

    String player, currentPlayer, player1_name, player2_name;

    TextView etat, tv_match;

    int coups = 0;

    Boolean enabledChat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        Intent intent = getIntent();
        database = FirebaseDatabase.getInstance();
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle parameters = intent.getExtras();
            id = Uri.parse(parameters.getString(DeepLink.URI)).getHost();
            final DatabaseReference deeplinkRef = database.getReference("parties").child(id);
            deeplinkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("player2").getValue().toString().isEmpty()) {
                        new MaterialDialog.Builder(PartyActivity.this)
                                .title("Entrez votre nom d'utilisateur")
                                .content("Pour rejoindre la partie, veuillez renseigner un nom")
                                .inputType(InputType.TYPE_CLASS_TEXT)
                                .input("Nom d'utilisateur", "", new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                        deeplinkRef.child("player2").setValue(input.toString());
                                        Globals.name = input.toString();
                                        deeplinkRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                try {
                                                    currentPlayer = "0";
                                                    enabledChat = dataSnapshot.child("player2").getValue().toString() != null && !dataSnapshot.child("player2").getValue().toString().equals("") ? true : false;

                                                    tv_match.setText(dataSnapshot.child("player1").getValue().toString() + " vs " + dataSnapshot.child("player2").getValue().toString());

                                                    String nextPlayer = dataSnapshot.child("next").getValue().toString();
                                                    player = nextPlayer;

                                                    player1_name = dataSnapshot.child("player1").getValue().toString();
                                                    player2_name = dataSnapshot.child("player2").getValue().toString();

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

                                                    if (currentPlayer.equals(dataSnapshot.child("next").getValue().toString())) {
                                                        setClickable();
                                                    } else {
                                                        setUnClickable();
                                                    }

                                                    String remaining = dataSnapshot.child("remaining").getValue().toString();

                                                    coups = Integer.valueOf(dataSnapshot.child("shots").getValue().toString());

                                                    if (!dataSnapshot.child("next").getValue().toString().equals("0")) {
                                                        etat.setText("Au tour de : " + player1_name + " Symbole : 0");
                                                    } else {
                                                        etat.setText("Au tour de : " + player2_name + " Symbole : X");
                                                    }

                                                    controlGame(sc11, sc12, sc13, sc21, sc22, sc23, sc31, sc32, sc33);
                                                } catch (Exception e) {
                                                    Intent i = new Intent(PartyActivity.this, InviteActivity.class);
                                                    startActivity(i);
                                                }
                                            }


                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                            }
                                        });
                                    }
                                }).show();
                    } else {
                        new MaterialStyledDialog.Builder(PartyActivity.this)
                                .setIcon(R.drawable.ic_remove_circle_outline)
                                .setTitle("Partie pleine")
                                .setDescription("Cette partie a déjà deux joueurs, voulez vous trouver une partie disponible ?")
                                .setPositiveText("Oui")
                                .setNegativeText("Non")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent i = new Intent(PartyActivity.this, ListActivity.class);
                                        startActivity(i);
                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent i = new Intent(PartyActivity.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                })
                                .show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            id = intent.getStringExtra("id");
        }

        cl_partyActivity = (ConstraintLayout) findViewById(R.id.partyActivity);

        tv_match = (TextView) findViewById(R.id.match);

        etat = (TextView) findViewById(R.id.etatPartie);

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

        if (!intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            DatabaseReference myRef = database.getReference("parties").child(id);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (Globals.name.equals(dataSnapshot.child("player1").getValue().toString())) {
                            currentPlayer = "X";
                        } else {
                            database.getReference("parties").child(id).child("player2").setValue(Globals.name);
                            currentPlayer = "0";
                        }

                        enabledChat = dataSnapshot.child("player2").getValue().toString() != null && !dataSnapshot.child("player2").getValue().toString().equals("") ? true : false;

                        tv_match.setText(dataSnapshot.child("player1").getValue().toString() + " vs " + dataSnapshot.child("player2").getValue().toString());

                        String nextPlayer = dataSnapshot.child("next").getValue().toString();
                        player = nextPlayer;

                        player1_name = dataSnapshot.child("player1").getValue().toString();
                        player2_name = dataSnapshot.child("player2").getValue().toString();

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

                        if (currentPlayer.equals(dataSnapshot.child("next").getValue().toString())) {
                            setClickable();
                        } else {
                            setUnClickable();
                        }

                        String remaining = dataSnapshot.child("remaining").getValue().toString();

                        coups = Integer.valueOf(dataSnapshot.child("shots").getValue().toString());

                        if (!dataSnapshot.child("next").getValue().toString().equals("0")) {
                            etat.setText("Au tour de : " + player1_name);
                        } else {
                            etat.setText("Au tour de : " + player2_name);
                        }

                        controlGame(sc11, sc12, sc13, sc21, sc22, sc23, sc31, sc32, sc33);
                    } catch (Exception e) {
                        Intent i = new Intent(PartyActivity.this, InviteActivity.class);
                        startActivity(i);
                    }
                }


                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
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
            if (winner.equals("0")) {
                etat.setText("Le joueur " + player1_name + " a gagné");
                showDialog("Le joueur " + player1_name + " a gagné", false);
            } else {
                etat.setText("Le joueur " + player2_name + " a gagné");
                showDialog("Le joueur " + player2_name + " a gagné", false);
            }
        } else {
            if (coups == 9) {
                myRef.child("finished").setValue(true);
                myRef.child("winner").setValue("");
                setUnClickable();
                etat.setText("Match nul");
                showDialog("Match nul", true);
            } else {
                myRef.child("finished").setValue(false);
                myRef.child("winner").setValue("");
            }
        }

        return gagne;
    }

    public void setClickable() {
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

    public void setUnClickable() {
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

    public void showDialog(String message, boolean isNul) {
        try {
            new MaterialStyledDialog.Builder(this)
                    .setIcon(isNul ? R.drawable.ic_remove_circle_outline : R.drawable.ic_action_achievement)
                    .setTitle("Partie terminée")
                    .setDescription(message)
                    .setPositiveText("Fermer")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent i = new Intent(PartyActivity.this, InviteActivity.class);
                            startActivity(i);
                        }
                    })
                    .show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View v) {
        DatabaseReference myRef;

        myRef = database.getReference("parties").child(id).child("shots");
        coups++;
        myRef.setValue(coups);

        String currentPlayer = coups % 2 != 0 ? "X" : "0";
        myRef = database.getReference("parties").child(id).child("next");
        myRef.setValue(currentPlayer);

        switch (v.getId()) {
            case R.id.c11:
                c11.setText(currentPlayer);
                c11.setClickable(false);
                myRef = database.getReference("parties").child(id).child("c11");
                myRef.setValue(currentPlayer);
                break;
            case R.id.c12:
                c12.setText(currentPlayer);
                c12.setClickable(false);
                myRef = database.getReference("parties").child(id).child("c12");
                myRef.setValue(currentPlayer);
                break;
            case R.id.c13:
                c13.setText(currentPlayer);
                c13.setClickable(false);
                myRef = database.getReference("parties").child(id).child("c13");
                myRef.setValue(currentPlayer);
                break;
            case R.id.c21:
                c21.setText(currentPlayer);
                c21.setClickable(false);
                myRef = database.getReference("parties").child(id).child("c21");
                myRef.setValue(currentPlayer);
                break;
            case R.id.c22:
                c22.setText(currentPlayer);
                c22.setClickable(false);
                myRef = database.getReference("parties").child(id).child("c22");
                myRef.setValue(currentPlayer);
                break;
            case R.id.c23:
                c23.setText(currentPlayer);
                c23.setClickable(false);
                myRef = database.getReference("parties").child(id).child("c23");
                myRef.setValue(currentPlayer);
                break;
            case R.id.c31:
                c31.setText(currentPlayer);
                c31.setClickable(false);
                myRef = database.getReference("parties").child(id).child("c31");
                myRef.setValue(currentPlayer);
                break;
            case R.id.c32:
                c32.setText(currentPlayer);
                c32.setClickable(false);
                myRef = database.getReference("parties").child(id).child("c32");
                myRef.setValue(currentPlayer);
                break;
            case R.id.c33:
                c33.setText(currentPlayer);
                c33.setClickable(false);
                myRef = database.getReference("parties").child(id).child("c33");
                myRef.setValue(currentPlayer);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_party, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_message:
                if (enabledChat) {
                    Intent intentChat = new Intent(PartyActivity.this, ChatActivity.class);
                    intentChat.putExtra("id", getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)
                            ? Uri.parse(getIntent().getExtras().getString(DeepLink.URI)).getHost()
                            : getIntent().getStringExtra("id"));
                    intentChat.putExtra("player1", player1_name);
                    intentChat.putExtra("player2", player2_name);
                    startActivity(intentChat);
                } else {
                    Snackbar snackbar1 = Snackbar.make(cl_partyActivity, "Chat indisponible, aucun player 2", Snackbar.LENGTH_LONG);
                    snackbar1.show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
