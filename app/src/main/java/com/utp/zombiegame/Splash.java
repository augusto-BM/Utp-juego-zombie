package com.utp.zombiegame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Iniciar la reproducción del sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.introzombie);
        mediaPlayer.start();

        //USAMOS EL OBJETO HANDLER para que se ejecute esta interfaz en un tiempo determinado y luego pasa a la interfaz principal de MainActivity
        int DURACION_SPLAH = 10000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Detener la reproducción del sonido
                mediaPlayer.stop();
                mediaPlayer.release();

                //INTENT NOS LLEVA DE UNA ACTIVIDAD A OTRA (PRIMERA FORMA)
                Intent intent = new Intent(Splash.this, Menu.class);
                startActivity(intent);
                finish();

                //(SEGUNDA FORMA)
                //startActivity(new Intent(Splash.this, MainActivity.class));
            };
        }, DURACION_SPLAH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}