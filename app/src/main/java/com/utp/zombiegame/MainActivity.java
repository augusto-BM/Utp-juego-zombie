package com.utp.zombiegame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    Button BTNLOGIN, BTNREGISTRO;
    private MediaPlayer mediaFondo; //INSTANCIA DEL AUDIO DE FONDO
    private MediaPlayer mediaBoton; //INSTANCIA DEL AUDIO DEL BOTON

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mediaFondo = MediaPlayer.create(this, R.raw.mainactivitysound);
        mediaBoton = MediaPlayer.create(this, R.raw.mainbtnsounds);

        //ACÁ ASIGNAMOS EL AUDIO Y LO COLOCAMOS EN LOOP.
        mediaFondo.start();
        mediaFondo.setLooping(true);

        BTNLOGIN = findViewById(R.id.BTNLOGIN);
        BTNREGISTRO = findViewById(R.id.BTNREGISTRO);

        //UBICACION TIPO DE LETRA
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(MainActivity.this.getAssets(), ubicacion);

        //ESTABLECER EL TIPO DE LETRA A LOS TEXTVIEW Y BOTONES
        BTNLOGIN.setTypeface(Tf);
        BTNREGISTRO.setTypeface(Tf);

        //NOS DIRIGE ALA ACTIVIDAD LOGIN
        BTNLOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                mediaFondo.pause();
                /*MENSAJE*/
                //Toast.makeText(MainActivity.this,"Has hecho click en el boton login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        //NOS DIRIGE ALA ACTIVIDAD DE REGISTRO
        BTNREGISTRO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();//SE ACTIVA EL SONIDO DEL BOTON
                mediaFondo.pause();
                /*CUANDO HAGAMOS CLICK EN EL BOTON REGISTRO NOS REDIRECCIONA A LA INTERFAZ REGISTRO*/
                Intent intent = new Intent(MainActivity.this, Registro.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaFondo != null && !mediaFondo.isPlaying()) {
            mediaFondo.start();
        }
    }

}