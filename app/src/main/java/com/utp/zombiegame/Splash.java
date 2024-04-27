package com.utp.zombiegame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

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

        //USAMOS EL OBJETO HANDLER para que se ejecute esta interfaz en un tiempo determinado y luego pasa a la interfaz principal de MainActivity
        int DURACION_SPLAH = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //INTENT NOS LLEVA DE UNA ACTIVIDAD A OTRA (PRIMERA FORMA)
                Intent intent = new Intent(Splash.this, Menu.class);
                startActivity(intent);

                //(SEGUNDA FORMA)
                //startActivity(new Intent(Splash.this, MainActivity.class));
            };
        }, DURACION_SPLAH);
    }
}