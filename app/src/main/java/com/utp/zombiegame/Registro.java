package com.utp.zombiegame;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Registro extends AppCompatActivity {

    EditText correoEt, passEt, nombreEt, edadEt, paisEt;
    TextView fechaTxt;
    Button Registrar;
    FirebaseAuth auth; //FIREBASE AUTENTICACION

    private MediaPlayer mediaFondo;//INSTANCIA DEL AUDIO DE FONDO
    private MediaPlayer mediaBoton;
    private MediaPlayer mediaBotonNegacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mediaFondo = MediaPlayer.create(this, R.raw.registrosound);
        mediaBoton = MediaPlayer.create(this, R.raw.mainbtnsounds);
        mediaBotonNegacion = MediaPlayer.create(this, R.raw.mainactivitybtnsoundno);

        //ACÁ ASIGNAMOS EL AUDIO Y LO COLOCAMOS EN LOOP.
        mediaFondo.start();
        mediaFondo.setLooping(true);

        correoEt = findViewById(R.id.correoEt);
        passEt = findViewById(R.id.passEt);
        nombreEt = findViewById(R.id.nombreEt);
        edadEt = findViewById(R.id.edadEt);
        paisEt = findViewById(R.id.paisEt);
        fechaTxt = findViewById(R.id.fechaTxt);
        Registrar = findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance();

        //UBICACION TIPO DE LETRA
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Registro.this.getAssets(), ubicacion);

        //ESTABLECER EL TIPO DE LETRA AL BOTON
        Registrar.setTypeface(Tf);

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String StringFecha = fecha.format(date);
        fechaTxt.setText(StringFecha);

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = correoEt.getText().toString();
                String password = passEt.getText().toString();

                /*Validar correo electronico correcto con el formato*/
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correoEt.setError("Correo invalido");
                    correoEt.setFocusable(true);
                    mediaBotonNegacion.start();
                    /*Validar contraseña correcta con el formato*/
                }else if(password.length()<6) {
                    passEt.setError("Contraseña debe ser mayor a 6");
                    passEt.setFocusable(true);
                    mediaBotonNegacion.start();
                }else{
                    RegistrarJugador(email, password);
                    mediaBoton.start();
                }
            }
        });
    }

    /*METODO PARA REGISTRAR USUARIO*/
    private void RegistrarJugador(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /*SI  EL JUGADOR FUE REGISTRADO CORRECTAMENTE*/
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();

                            int contador = 0;
                            assert user != null; /* El usuario no es nulo*/
                            String uidString = user.getUid();
                            String correoString = correoEt.getText().toString();
                            String passString = passEt.getText().toString();
                            String nombreString = nombreEt.getText().toString();
                            String edadString = edadEt.getText().toString();
                            String paisString = paisEt.getText().toString();
                            String fechaString = fechaTxt.getText().toString();

                            /*HashMap sirve para asignarle claves a los valores y pueda ser reconocido por la BBDD*/
                            HashMap<Object, Object>  DatosJUGADOR = new HashMap<>();
                            DatosJUGADOR.put("Uid",uidString);
                            DatosJUGADOR.put("Email",correoString);
                            DatosJUGADOR.put("Password",passString);
                            DatosJUGADOR.put("Nombres",nombreString);
                            DatosJUGADOR.put("Edad",edadString);
                            DatosJUGADOR.put("Pais",paisString);
                            DatosJUGADOR.put("Imagen","");
                            DatosJUGADOR.put("Fecha",fechaString);
                            DatosJUGADOR.put("Zombies",contador);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                                                                                //Nombre de la base de datos
                            DatabaseReference reference = database.getReference("MI DATA BASE JUGADORES");
                            reference.child(uidString).setValue(DatosJUGADOR);
                            mediaFondo.stop();
                            startActivity(new Intent(Registro.this, Menu.class));
                            Toast.makeText(Registro.this, "USUARIO REGISTRADO EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(Registro.this, "HA OCURRIDO UN ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                /*SI FALLA EL REGISTRO*/
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaFondo != null && mediaFondo.isPlaying()) {
            mediaFondo.pause(); // O mediaFondo.stop() si quieres reiniciar la reproducción
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaFondo != null) {
            mediaFondo.release();
            mediaFondo = null;
        }
    }
}

