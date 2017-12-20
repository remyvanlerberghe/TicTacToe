package com.tictactoe.android.tictactoe;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tictactoe.android.tictactoe.Models.PartyShort;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    FirebaseDatabase database;


    ArrayList<PartyShort> liste = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_annonces);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("parties");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                liste = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    if (messageSnapshot.child("player1").getValue() != null &&
                            messageSnapshot.child("finished").getValue() != null &&
                            !messageSnapshot.child("finished").getValue().toString().equals("true") &&
                            (messageSnapshot.child("player2").getValue() == null || messageSnapshot.child("player2").getValue().toString().equals("") || messageSnapshot.child("player2").getValue().toString().equals(" "))) {
                        PartyShort p = new PartyShort();
                        p.setId(messageSnapshot.getKey().toString());
                        p.setUser(messageSnapshot.child("player1").getValue().toString());
                        liste.add(p);
                    }
                }
                mAdapter = new PartyAdapter(liste);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAdapter = new PartyAdapter(liste);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((PartyAdapter) mAdapter).setOnItemClickListener(new PartyAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(ListActivity.this, PartyActivity.class);
                intent.putExtra("id", liste.get(position).id);
                startActivity(intent);
            }
        });
    }
}
