package com.utp.zombiegame;

import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Random;

public class EscenarioJuego extends AppCompatActivity {

    String UIDS, NOMBRES, ZOMBIES;
    TextView TvContador, TvNombre, TvTiempo;
    TextView AnchoTv, AltoTv;
    ImageView IvZombie;
    LottieAnimationView AnimacionReloj;

    int contador = 0;
    int AnchoPantalla, AltoPantalla;

    //GENERAMOS UN NUMERO RANDOM
    Random aleatorio;

    boolean GameOver = false;
    Dialog miDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_escenario_juego);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // ********************* INICIAR ANIMACION TIEMPO *************************
        // Inicializar la vista LottieAnimationView
        AnimacionReloj = findViewById(R.id.AnimacionReloj);

        // Cargar y reproducir la animación del reloj desde la carpeta assets
        AnimacionReloj.setAnimation("tiempo.json");
        AnimacionReloj.playAnimation();
        //*************************************************************************


        TvContador = findViewById(R.id.TvContador);
        TvNombre = findViewById(R.id.TvNombre);
        TvTiempo = findViewById(R.id.TvTiempo);

        IvZombie = findViewById(R.id.IvZombie);

        miDialog = new Dialog(EscenarioJuego.this);

        //RECUPERAR LOS VALORES TIPO STRING QUE HA ENVIADO LA ACTIVIDAD MENU
        Bundle intent = getIntent().getExtras();
        UIDS = intent.getString("UID");
        NOMBRES = intent.getString("NOMBRE");
        ZOMBIES = intent.getString("ZOMBIE");

        AnchoTv = findViewById(R.id.AnchoTv);
        AltoTv = findViewById(R.id.AltoTv);

        //SETEAMOS PARA ACTUALIZAR LOS VALORES
        TvNombre.setText(NOMBRES);      //EL NOMBRE DEL USUARIO LOGUEADO
        TvContador.setText(ZOMBIES);    //EL TIEMPO DE JUEGO
        //----------------------------------
        Pantalla();
        CuentaAtras();
        //-----------------------------------
        //AL HACER CLICK EN LA IMAGEN EL CONTADOR TIENE QUE AUMENTAR
        IvZombie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!GameOver){
                    contador++; //contador aumenta de uno en uno
                    TvContador.setText(String.valueOf(contador)); //seteamos y convertimos a string
                    //La imagen cambia a zombie aplastado
                    IvZombie.setImageResource(R.drawable.zombieaplastado);

                    //EJECUTAR UNA FUNCION EN UN DETERMINADO TIEMPO
                    //Despues de 500 milisegundo la imagen regresa a zombie normal
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            IvZombie.setImageResource(R.drawable.zombie);
                            Movimiento();
                        }
                    }, 500);
                }
            }
        });
    }

    //METODO PARA DEFINIR EL TAMAÑO DE LA PANTALLA DEL DISPOSITIVO
    private void Pantalla(){
        Display display = getWindowManager().getDefaultDisplay();   //Display de la pantalla
        Point point = new Point();                                  //Las coordenadas del dispositivo
        display.getSize(point);      //obtenemos las coordenadas del display

        AnchoPantalla = point.x;
        AltoPantalla = point.y;

        String ANCHOS = String.valueOf(AnchoPantalla);
        String ALTOS = String.valueOf(AltoPantalla);

        AnchoTv.setText(ANCHOS);
        AltoTv.setText(ALTOS);

        aleatorio = new Random();
    }

    //METODO PARA MOVER LA IMAGEN DEL ZOMBIE DE MANERA ALEATORIA
    private void Movimiento(){
        int min = 0;
        // -------------------------LOS PUNTOS MAXIMOS ---------------------------
        //MAXIMA COORDENADA "X" = ANCHO PANTALLA  - ANCHO DE LA IMAGEN DEL ZOMBIE
        int MaximoX = AnchoPantalla - IvZombie.getWidth();
        //MAXIMA COORDENADA "Y" = ALTO PANTALLA  - ALTO DE LA IMAGEN DEL ZOMBIE
        int MaximoY = AltoPantalla - IvZombie.getWidth();
        //-------------------------------------------------------------------------

        //MOVILIZAR AL ZOMBIE
        int randonX = aleatorio.nextInt(((MaximoX - min)+1)+min);
        int randonY = aleatorio.nextInt(((MaximoY - min)+1)+min);

        //SETEAR LOS VALORES
        IvZombie.setX(randonX);
        IvZombie.setY(randonY);
    }

    //METODO PARA CONTAR EL TIEMPO DE MANERA REGRESIVA HASTA CERO.
    private void CuentaAtras(){
            //DURA "TREINTA SEGUNDOS" Y DECREMENTA CADA "UN SEGUNDO"
        new CountDownTimer(30000, 1000) {

            //onTick SE EJECUTA CADA SEGUNDO
            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished/1000;
                TvTiempo.setText(segundosRestantes+" s");
            }
            //CUANDO SE ACABA EL TIEMPO
            public void onFinish() {
                TvTiempo.setText("0 s");
                GameOver = true;
                MensajeGameOver();
            }
        }.start();
    }

    private void MensajeGameOver(){
        LottieAnimationView AnimacionFinJuego;
        String ubicacion = "fuentes/zombie.TTF";

        Typeface typeface = Typeface.createFromAsset(EscenarioJuego.this.getAssets(),ubicacion);

        TextView SeacaboTXT, HasmatadoTXT, NumeroTXT;
        Button JUGARDENUEVO, IRMENU, PUNTAJES;

        miDialog.setContentView(R.layout.gameover);

        SeacaboTXT = miDialog.findViewById(R.id.SeacaboTXT);
        HasmatadoTXT = miDialog.findViewById(R.id.HasmatadoTXT);
        NumeroTXT = miDialog.findViewById(R.id.NumeroTXT);

        JUGARDENUEVO = miDialog.findViewById(R.id.JUGARDENUEVO);
        IRMENU = miDialog.findViewById(R.id.IRMENU);
        PUNTAJES = miDialog.findViewById(R.id.PUNTAJES);

        String zombies = String.valueOf(contador);
        NumeroTXT.setText(zombies);

        SeacaboTXT.setTypeface(typeface);
        HasmatadoTXT.setTypeface(typeface);
        NumeroTXT.setTypeface(typeface);

        JUGARDENUEVO.setTypeface(typeface);
        IRMENU.setTypeface(typeface);
        PUNTAJES.setTypeface(typeface);

        // ********************* INICIAR ANIMACION FIN JUEGO *************************
        // Inicializar la vista LottieAnimationView
        AnimacionFinJuego = miDialog.findViewById(R.id.AnimacionFinJuego);

        // Cargar y reproducir la animación del reloj desde la carpeta assets
        AnimacionFinJuego.setAnimation("finjuego.json");
        AnimacionFinJuego.playAnimation();
        //*****************************************************************************
        JUGARDENUEVO.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(EscenarioJuego.this, "JUGAR DE NUEVO", Toast.LENGTH_SHORT).show();

            }

        });

        IRMENU.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(EscenarioJuego.this, "MENU", Toast.LENGTH_SHORT).show();

            }

        });

        PUNTAJES.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(EscenarioJuego.this, "PUNTAJES", Toast.LENGTH_SHORT).show();

            }

        });

        miDialog.show();

    }

}