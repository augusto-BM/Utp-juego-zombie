package com.utp.zombiegame;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Puntajes extends AppCompatActivity {

    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerViewUsuarios;
    Adaptador adaptador;
    List<Usuario> usuarioList;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntajes);

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Puntajes");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        firebaseAuth = FirebaseAuth.getInstance();
        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);

        mLayoutManager.setReverseLayout(true);//ORDENA DE Z a A
        mLayoutManager.setStackFromEnd(true);//EMPIEZA DE ARRIBA
        recyclerViewUsuarios.setHasFixedSize(true);
        recyclerViewUsuarios.setLayoutManager(mLayoutManager);
        usuarioList = new ArrayList<>();

        ObtenerTodosLosUsuarios();


    }

    private void ObtenerTodosLosUsuarios() {

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MI DATA BASE JUGADORES"); //firebaseDatabase.getReference("MI DATA BASE JUGADORES");
        ref.orderByChild("Zombies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarioList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    Usuario usuario = ds.getValue(Usuario.class);

                    /*if (!usuario.getUid().equals(fUser.getUid())){
                        usuarioList.add(usuario);
                    }*/

                    usuarioList.add(usuario);

                    adaptador = new Adaptador(Puntajes.this, usuarioList);
                    recyclerViewUsuarios.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}