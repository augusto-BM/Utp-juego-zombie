package com.utp.zombiegame;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity {

    TextView MiPuntuaciontxt, Zombies, uid, correo, nombre, fecha, Menutxt;
    Button cerrarSesion, JugarBtn, PuntuacionesBtn, AcercaDeBtn;

    FirebaseAuth auth; //FIREBASE AUTENTICACION
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("MI DATA BASE JUGADORES");

        //UBICACION TIPO DE LETRA
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Menu.this.getAssets(), ubicacion);

        MiPuntuaciontxt = findViewById(R.id.MiPuntuaciontxt);
        uid = findViewById(R.id.uid);
        correo = findViewById(R.id.correo);
        fecha = findViewById(R.id.fecha);
        nombre = findViewById(R.id.nombre);
        Menutxt = findViewById(R.id.Menutxt);
        Zombies = findViewById(R.id.Zombies);


        JugarBtn = findViewById(R.id.JugarBtn);
        PuntuacionesBtn = findViewById(R.id.PuntuacionesBtn);
        AcercaDeBtn = findViewById(R.id.AcercaDeBtn);
        cerrarSesion = findViewById(R.id.cerrarSesionBtn);

        //ESTABLECER EL TIPO DE LETRA A LOS TEXTVIEW Y BOTONES
        MiPuntuaciontxt.setTypeface(Tf);
        uid.setTypeface(Tf);
        correo.setTypeface(Tf);
        nombre.setTypeface(Tf);
        Menutxt.setTypeface(Tf);
        fecha.setTypeface(Tf);
        JugarBtn.setTypeface(Tf);
        PuntuacionesBtn.setTypeface(Tf);
        AcercaDeBtn.setTypeface(Tf);
        cerrarSesion.setTypeface(Tf);


        JugarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Menu.this, "Jugar", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Menu.this,EscenarioJuego.class);

                //CONVERTIMOS EN STRING EL VALOR DEL USUARIO LOGUQEADO
                String UidS = uid.getText().toString();
                String NombreS = nombre.getText().toString();
                String ZombieS = Zombies.getText().toString();

                //LO ENVIAMOS A ESCENARIO JUEGO CON ESOS (name:)
                intent.putExtra("UID", UidS);
                intent.putExtra("NOMBRE", NombreS);
                intent.putExtra("ZOMBIE", ZombieS);

                startActivity((intent));
                Toast.makeText(Menu.this, "ENVIANDO PARAMETROS", Toast.LENGTH_SHORT).show();


            }
        });

        PuntuacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this, "Puntuaciones", Toast.LENGTH_SHORT).show();
            }
        });

        AcercaDeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this, "Acerca DE", Toast.LENGTH_SHORT).show();
            }
        });


        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });
    }

    @Override
    //ESTE METODO SE ABRE EL MINIJUEGO
    protected void onStart() {
        UsuarioLogueado();
        super.onStart();
    }

    //METODO COMPRUEBA SI EL JUGADOR HA INICIADO SESION
    private void UsuarioLogueado(){
        if (user != null){
            Consulta();
            Toast.makeText(this, "Jugador en linea", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(Menu.this,MainActivity.class));
            finish();
        }
    }

    //METODO PARA CERRAR SESION
    private void CerrarSesion(){
        auth.signOut();
        startActivity(new Intent(Menu.this,MainActivity.class));
        Toast.makeText(this, "Sesion Cerrado exitosamente", Toast.LENGTH_SHORT).show();
    }

    //METODO PARA PARA LLAMAR LA INFORMACION DEL USUARIO LOGUEADO
    private void Consulta(){
        //METODO PARA REALIZAR LA CONSULTA
        Query query = JUGADORES.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //OBTENCION DE LOS DATOS
                    String zombiesString = ""+ds.child("Zombies").getValue();
                    String uidString = ""+ds.child("Uid").getValue();
                    String emailString = ""+ds.child("Email").getValue();
                    String nombreString = ""+ds.child("Nombres").getValue();
                    String fechaString = ""+ds.child("Fecha").getValue();

                    //SETEAR LOS DATOS DE LOS TEXTVIEW CON EL DATO DEL USUARIO LOGUEADO
                    Zombies.setText(zombiesString);
                    uid.setText(uidString);
                    correo.setText(emailString);
                    nombre.setText(nombreString);
                    fecha.setText(fechaString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}