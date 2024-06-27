package com.utp.zombiegame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends AppCompatActivity {

    TextView MiPuntuaciontxt, Zombies, uid, correo, nombre, edad, pais, fecha, Menutxt;
    Button cerrarSesion, JugarBtn, EditarBtn, CambiarPassBtn, PuntuacionesBtn, AcercaDeBtn;
    CircleImageView imagenPerfil;

    FirebaseAuth auth; //FIREBASE AUTENTICACION
    FirebaseUser user;
    private MediaPlayer mediaFondo; //INSTANCIA DEL AUDIO DE FONDO
    private MediaPlayer mediaBoton; //INSTANCIA DEL AUDIO DE FONDO

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

        mediaFondo = MediaPlayer.create(this, R.raw.menusound);
        mediaBoton = MediaPlayer.create(this, R.raw.menubtnsound);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user != null){
            //ACÁ ASIGNAMOS EL AUDIO Y LO COLOCAMOS EN LOOP.
            mediaFondo.start();
            mediaFondo.setLooping(true);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("MI DATA BASE JUGADORES");

        //UBICACION TIPO DE LETRA
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Menu.this.getAssets(), ubicacion);

        //PEFIL
        MiPuntuaciontxt = findViewById(R.id.MiPuntuaciontxt);
        imagenPerfil = findViewById(R.id.imagenPerfil);
        uid = findViewById(R.id.uid);
        correo = findViewById(R.id.correo);
        fecha = findViewById(R.id.fecha);
        nombre = findViewById(R.id.nombre);
        edad = findViewById(R.id.edad);
        pais = findViewById(R.id.pais);
        Menutxt = findViewById(R.id.Menutxt);
        Zombies = findViewById(R.id.Zombies);

        //OPCIONES DEL JUEGO
        JugarBtn = findViewById(R.id.JugarBtn);
        EditarBtn = findViewById(R.id.EditarBtn);
        CambiarPassBtn = findViewById(R.id.CambiarPassBtn);
        PuntuacionesBtn = findViewById(R.id.PuntuacionesBtn);
        AcercaDeBtn = findViewById(R.id.AcercaDeBtn);
        cerrarSesion = findViewById(R.id.cerrarSesionBtn);

        //ESTABLECER EL TIPO DE LETRA A LOS TEXTVIEW Y BOTONES
        MiPuntuaciontxt.setTypeface(Tf);
        uid.setTypeface(Tf);
        correo.setTypeface(Tf);
        nombre.setTypeface(Tf);
        edad.setTypeface(Tf);
        pais.setTypeface(Tf);
        Zombies.setTypeface(Tf);

        Menutxt.setTypeface(Tf);
        fecha.setTypeface(Tf);

        JugarBtn.setTypeface(Tf);
        EditarBtn.setTypeface(Tf);
        CambiarPassBtn.setTypeface(Tf);
        PuntuacionesBtn.setTypeface(Tf);
        AcercaDeBtn.setTypeface(Tf);
        cerrarSesion.setTypeface(Tf);


        JugarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                mediaFondo.stop();
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

        EditarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                //Toast.makeText(Menu.this, "EDITAR", Toast.LENGTH_SHORT).show();
                EditarDatos();
            }
        });

        CambiarPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                Toast.makeText(Menu.this, "CAMBIAR CONTRASEÑA", Toast.LENGTH_SHORT).show();
            }
        });

        PuntuacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                Toast.makeText(Menu.this, "Puntuaciones", Toast.LENGTH_SHORT).show();
            }
        });

        AcercaDeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                Toast.makeText(Menu.this, "Acerca DE", Toast.LENGTH_SHORT).show();
            }
        });


        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                mediaFondo.stop();
                CerrarSesion();
            }
        });
    }

    //METODO PARA CAMBIAR LOS DATOS
    private void EditarDatos() {
        String [] Opciones = {"Foto de perfil", "Cambiar nombre", "Cambiar edad", "Cambiar pais"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(Opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i == 0){
                    ActualizarFotoPerfil();
                }
                if(i == 1){
                    ActualizarNombre("Nombres");
                }
                if(i == 2){
                    ActualizarEdad("Edad");
                }
                if(i == 3){
                    ActualizarPais("Pais");
                }
            }
        });
        builder.create().show();
    }


    private void ActualizarFotoPerfil() {
    }
    private void ActualizarNombre(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: "+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(linearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(this);
        editText.setHint("Ingrese "+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);

        //SI EL USUARIO HACE CLICK EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String, Object> result = new HashMap<>();
                result.put(key, value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Menu.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Menu.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(Menu.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
    private void ActualizarEdad(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: "+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(linearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(this);
        editText.setHint("Ingrese "+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);

        //SI EL USUARIO HACE CLICK EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String, Object> result = new HashMap<>();
                result.put(key, value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Menu.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Menu.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(Menu.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
    private void ActualizarPais(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: "+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(linearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(this);
        editText.setHint("Ingrese "+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);

        //SI EL USUARIO HACE CLICK EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String, Object> result = new HashMap<>();
                result.put(key, value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Menu.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Menu.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(Menu.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
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
                    String edadString = ""+ds.child("Edad").getValue();
                    String paisString = ""+ds.child("Pais").getValue();
                    String fechaString = ""+ds.child("Fecha").getValue();
                    String imagen = ""+ds.child("Imagen").getValue();

                    //SETEAR LOS DATOS DE LOS TEXTVIEW CON EL DATO DEL USUARIO LOGUEADO
                    Zombies.setText(zombiesString);
                    uid.setText(uidString);
                    correo.setText("Correo: "+emailString);
                    nombre.setText("Nombre: "+nombreString);
                    edad.setText("Edad: "+edadString);
                    pais.setText("Pais: "+paisString);
                    fecha.setText("Se registro el: "+fechaString);
                    try {
                        Picasso.get().load(imagen).into(imagenPerfil);
                    } catch (Exception e){
                        Picasso.get().load(R.drawable.soldado).into(imagenPerfil);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}