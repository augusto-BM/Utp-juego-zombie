package com.utp.zombiegame;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetalleJugador extends AppCompatActivity {

    ImageView ImagenDetalle;
    TextView INFORMACIONDETALLE, NombresDetalle, CorreoDetalle, PuntajeDetalle, EdadDetalle, PaisDetalle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_jugador);

        ImagenDetalle = findViewById(R.id.ImagenDetalle);

        INFORMACIONDETALLE = findViewById(R.id.INFORMACIONDETALLE);
        NombresDetalle = findViewById(R.id.NombresDetalle);
        CorreoDetalle = findViewById(R.id.CorreoDetalle);
        PuntajeDetalle = findViewById(R.id.PuntajeDetalle);
        EdadDetalle = findViewById(R.id.EdadDetalle);
        PaisDetalle = findViewById(R.id.PaisDetalle);



        String imagen = getIntent().getStringExtra("Imagen");
        String nombres = getIntent().getStringExtra("Nombres");
        String correo = getIntent().getStringExtra("Correo");
        String puntaje = getIntent().getStringExtra("Puntaje");
        String edad = getIntent().getStringExtra("Edad");
        String pais = getIntent().getStringExtra("Pais");

        NombresDetalle.setText("Nombres: "+nombres);
        CorreoDetalle.setText("Correo :"+correo);
        PuntajeDetalle.setText(puntaje);
        EdadDetalle.setText("Edad :"+edad);
        PaisDetalle.setText("Pais :"+pais);

        //GESTIONAR IMAGEN
        try {
            Picasso.get().load(imagen).into(ImagenDetalle);
        }catch (Exception e){
            Picasso.get().load(R.drawable.soldado).into(ImagenDetalle);
        }
        CambioDeFuente();

    }

        private void CambioDeFuente(){
            //CAMBIO DE LETRA
            String ubicacion = "fuentes/zombie.TTF";
            Typeface Tf = Typeface.createFromAsset(DetalleJugador.this.getAssets(),ubicacion);
        INFORMACIONDETALLE.setTypeface(Tf);
        NombresDetalle.setTypeface(Tf);
        CorreoDetalle.setTypeface(Tf);
        PuntajeDetalle.setTypeface(Tf);
        EdadDetalle.setTypeface(Tf);
        PaisDetalle.setTypeface(Tf);
        }
}
