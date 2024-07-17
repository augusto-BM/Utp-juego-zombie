package com.utp.zombiegame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CambioDeContra extends AppCompatActivity {

    EditText ActualPass, NuevoPass;
    Button CambiarPass;
    DatabaseReference BASEDEDATOS;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    private MediaPlayer mediaFondo;//INSTANCIA DEL AUDIO DE FONDO
    private MediaPlayer mediaBoton; //INSTANCIA DEL AUDIO DEL BOTON
    private MediaPlayer mediaBotonNegacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cambio_de_contra);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mediaFondo = MediaPlayer.create(this, R.raw.cambiarcontrasound);
        mediaBoton = MediaPlayer.create(this, R.raw.mainbtnsounds);
        mediaBotonNegacion = MediaPlayer.create(this, R.raw.mainactivitybtnsoundno);

        //ACÁ ASIGNAMOS EL AUDIO Y LO COLOCAMOS EN LOOP.
        mediaFondo.start();
        mediaFondo.setLooping(true);

        ActualPass = findViewById(R.id.ActualPass);
        NuevoPass = findViewById(R.id.NuevoPass);
        CambiarPass = findViewById(R.id.CambiarPass);

        BASEDEDATOS = FirebaseDatabase.getInstance().getReference("MI DATA BASE JUGADORES");
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        CambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ACTUAL = ActualPass.getText().toString().trim();
                String NUEVA = NuevoPass.getText().toString().trim();

                if(TextUtils.isEmpty(ACTUAL)){
                    mediaBotonNegacion.start();
                    Toast.makeText(CambioDeContra.this, "Ingrese su contraseña actual", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(NUEVA)){
                    mediaBotonNegacion.start();
                    Toast.makeText(CambioDeContra.this, "Ingrese su nueva contraseña", Toast.LENGTH_SHORT).show();
                }
                if(!TextUtils.isEmpty(ACTUAL) && !TextUtils.isEmpty(NUEVA) && ACTUAL.length()>=6 && NUEVA.length()>=6){
                    mediaBoton.start();
                    CambioDePassJugador(ACTUAL, NUEVA);
                }
            }
        });
    }

    private void CambioDePassJugador(String actual, String nueva) {
        AuthCredential authCredential = EmailAuthProvider.getCredential((user.getEmail()), actual);

        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(nueva)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        String value = NuevoPass.getText().toString().trim();
                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put("Password", value);
                                        BASEDEDATOS.child(user.getUid()).updateChildren(result)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(CambioDeContra.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                });
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(CambioDeContra.this, Login.class));
                                        Toast.makeText(CambioDeContra.this, "Se ha cerrado sesión", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CambioDeContra.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CambioDeContra.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaFondo != null && !mediaFondo.isPlaying()) {
            mediaFondo.start();
        }
    }
}