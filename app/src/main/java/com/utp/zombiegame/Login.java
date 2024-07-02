package com.utp.zombiegame;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText correoLogin, passLogin;
    Button BtnLogin;
    private MediaPlayer mediaFondo;//INSTANCIA DEL AUDIO DE FONDO
    private MediaPlayer mediaBoton; //INSTANCIA DEL AUDIO DEL BOTON
    private MediaPlayer mediaBotonNegacion;

    FirebaseAuth auth; //FIREBASE AUTENTICACION

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        mediaFondo = MediaPlayer.create(this, R.raw.loginsound);
        mediaBoton = MediaPlayer.create(this, R.raw.mainbtnsounds);
        mediaBotonNegacion = MediaPlayer.create(this, R.raw.mainactivitybtnsoundno);

        //ACÁ ASIGNAMOS EL AUDIO Y LO COLOCAMOS EN LOOP.
        mediaFondo.start();
        mediaFondo.setLooping(true);

        correoLogin = findViewById(R.id.correoLogin);
        passLogin = findViewById(R.id.passLogin);
        BtnLogin = findViewById(R.id.BtnLogin);

        auth = FirebaseAuth.getInstance();

        //UBICACION TIPO DE LETRA
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Login.this.getAssets(), ubicacion);

        //ESTABLECER EL TIPO DE LETRA AL BOTON
        BtnLogin.setTypeface(Tf);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = correoLogin.getText().toString();
                String pass = passLogin.getText().toString();

                /*Validar correo electronico correcto*/
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    correoLogin.setError("Correo invalido");
                    correoLogin.setFocusable(true);
                    mediaBotonNegacion.start();
                    /*Validar contraseña correcta*/
                } else if (pass.length()<6) {
                    passLogin.setError("Contraseña debe ser mayor a 6");
                    passLogin.setFocusable(true);
                    mediaBotonNegacion.start();
                } else {
                    LogeoDeJugador(email, pass);
                    mediaBoton.start();
                }
            }
        });
    }

    /*METODO PARA LOGUEO DEL JUGADOR*/
    private void LogeoDeJugador(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            mediaFondo.stop();
                            startActivity(new Intent(Login.this,Menu.class));
                            assert user != null; //Afirmamos que el usuario no es nulo
                            Toast.makeText(Login.this, "Bienvenido(a) "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }
                    }
                    //SI FALLA EL LOGUEO NOS VA A MOSTRAR UN MENSAJE
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

