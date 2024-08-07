package com.utp.zombiegame;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends AppCompatActivity {

    TextView MiPuntuaciontxt, Zombies, uid, correo, nombre, edad, pais, fecha, Menutxt;
    Button cerrarSesion, JugarBtn, EditarBtn, CambiarPassBtn, PuntuacionesBtn, AcercaDeBtn;
    CircleImageView imagenPerfil;

    private StorageReference ReferenciaDeAlmacenamiento;
    private String RutaAlmacenamiento = "FotosDePerfil/*";

    /* PERMISOS*/
    private static final int CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO = 200;
    private static final int CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN = 300;

    /* MATRICES */
    private String [] PermisosdeAlmacenamiento;
    private Uri imagen_uri;
    private String perfil;

    FirebaseAuth auth; //FIREBASE AUTENTICACION
    FirebaseUser user;
    private MediaPlayer mediaFondo; //INSTANCIA DEL AUDIO DE FONDO
    private MediaPlayer mediaBoton; //INSTANCIA DEL AUDIO DE FONDO

    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;

    Dialog dialog;

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

        dialog = new Dialog(Menu.this);;


        ReferenciaDeAlmacenamiento = FirebaseStorage.getInstance().getReference();
        PermisosdeAlmacenamiento = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


            }
        });

        EditarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                //Toast.makeText(Menu.this, "EDITAR", Toast.LENGTH_SHORT).show();
                EditarDatos();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        CambiarPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                mediaFondo.pause();
                startActivity(new Intent(Menu.this, CambioDeContra.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        PuntuacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mediaBoton.start();
                    Intent intent = new Intent(Menu.this, Puntajes.class);
                    startActivity(intent);
                    Toast.makeText(Menu.this, "Puntos", Toast.LENGTH_SHORT).show();
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }

        });

        AcercaDeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                AcercaDe();
                //Toast.makeText(Menu.this, "Acerca DE", Toast.LENGTH_SHORT).show();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });


        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaBoton.start();
                mediaFondo.stop();
                CerrarSesion();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private void AcercaDe() {

        //UBICACION TIPO DE LETRA
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Menu.this.getAssets(), ubicacion);

        TextView ProfesorPOTXT,ProfTXT,IntegrantesPOTXT,DevTXT1,DevTXT2,DevTXT3,DevTXT4,DevTXT5;
        Button OK;

        dialog.setContentView(R.layout.acerca_de);

        ProfesorPOTXT = dialog.findViewById(R.id.ProfesorPOTXT);
        ProfTXT = dialog.findViewById(R.id.ProfTXT);
        IntegrantesPOTXT = dialog.findViewById(R.id.IntegrantesPOTXT);
        DevTXT1 = dialog.findViewById(R.id.DevTXT1);
        DevTXT2 = dialog.findViewById(R.id.DevTXT2);
        DevTXT3 = dialog.findViewById(R.id.DevTXT3);
        DevTXT4 = dialog.findViewById(R.id.DevTXT4);
        DevTXT5 = dialog.findViewById(R.id.DevTXT5);
        OK = dialog.findViewById(R.id.OK);


        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ProfesorPOTXT.setTypeface(Tf);
        ProfTXT.setTypeface(Tf);
        IntegrantesPOTXT.setTypeface(Tf);
        DevTXT1.setTypeface(Tf);
        DevTXT2.setTypeface(Tf);
        DevTXT3.setTypeface(Tf);
        DevTXT4.setTypeface(Tf);
        DevTXT5.setTypeface(Tf);

        dialog.show();
    }

    //METODO PARA CAMBIAR LOS DATOS
    private void EditarDatos() {
        String [] Opciones = {"Foto de perfil", "Cambiar nombre", "Cambiar edad", "Cambiar pais"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(Opciones, (dialog, i) -> {

            if(i == 0){
                perfil = "Imagen";
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
        });
        builder.create().show();
    }

    //LLAMA
    private void ActualizarFotoPerfil() {
        String [] opciones = {"Galeria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    //SELECCIONO GALERIA
                    if (!ComprobarPermisosDeAlmacenamiento()){
                        //SI NO SE HABILITO EL PERMISO
                        SolicitarPermisoDeAlmacenamiento();
                    } else {
                        //SI SE HABILITO EL PERMISO
                        ElegirImagenDeGaleria();
                    }
                }
            }
        });
        builder.create().show();
    }

    //PERMISO DE ALMACENAMIENTO EN TIEMPO DE EJECUCION
    private void SolicitarPermisoDeAlmacenamiento() {
    requestPermissions(PermisosdeAlmacenamiento, CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO);
    }

    //COMPRUEBA SI LOS PERMISOS DE ALMACENAMIENTO ESTAN HABILITADOS
    private boolean ComprobarPermisosDeAlmacenamiento() {
        boolean resultado = ContextCompat.checkSelfPermission(Menu.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return resultado;
    }

    //SE LLAMA CUANDO EL USUARIO PRESIONA PERMITIR O DENEGAR
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO:{
                //SELECCION DE LA GALERIA
                if (grantResults.length>0){
                    boolean EscrituraDeAlmacenamientoAceptado = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (EscrituraDeAlmacenamientoAceptado){
                        //PERMISO FUE HABILITADO
                        ElegirImagenDeGaleria();
                    }else {
                        //SI EL USUARIO DIJO QUE NO
                        Toast.makeText(this,"HABILITE EL PERMISO DE LA GALERIA", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //DESPUES DE ELEGIR LA IMAGEN DE LA GALERIA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            //DE LA IMAGEN SE OBTIENE LA URI
            if (requestCode == CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN){
                imagen_uri = data.getData();
                SubirFoto(imagen_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //ESTE METODO SUBE LA IMAGEN Y ACTUALIZA LA FOTO DEL USUARIO
    private void SubirFoto(Uri imagenUri) {
    String RutaDeArchivoYNombre = RutaAlmacenamiento + "" + perfil + ""+user.getUid();
    StorageReference storageReference = ReferenciaDeAlmacenamiento.child(RutaDeArchivoYNombre);
    storageReference.putFile(imagenUri)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                    while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        if (uriTask.isSuccessful()){
                            HashMap<String, Object> resultado = new HashMap<>();
                            resultado.put(perfil, downloadUri.toString());
                            JUGADORES.child(user.getUid()).updateChildren(resultado)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Menu.this,"LA IMAGEN A SIDO ACTUALIZADA",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Menu.this,"ERROR AL SUBIR LA IMAGEN",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(Menu.this,"ERROR AL SUBIR LA IMAGEN",Toast.LENGTH_SHORT).show();
                        }
                    }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Menu.this,"ERROR AL SUBIR LA IMAGEN",Toast.LENGTH_SHORT).show();
                }
            });
    }

    //ESTE METODO ABRE LA GALERIA
    private void ElegirImagenDeGaleria() {
        Intent intentGaleria = new Intent(Intent.ACTION_PICK);
        intentGaleria.setType("image/*");
        startActivityForResult(intentGaleria,CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN);
    }

    private void ActualizarNombre(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: " + key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(linearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);

        EditText editText = new EditText(this);
        editText.setHint("Ingrese " + key);

        // Filtro para permitir solo letras y espacios
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setFilters(new InputFilter[] { new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i)) && source.charAt(i) != ' ') {
                        return "";
                    }
                }
                return null;
            }
        }});

        linearLayoutCompat.addView(editText);

        builder.setView(linearLayoutCompat);

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
                                Toast.makeText(Menu.this, "NOMBRE ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Menu.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        builder.setTitle("Cambiar: " + key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(linearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);

        EditText editText = new EditText(this);
        editText.setHint("Ingrese " + key);

        // Filtro para permitir solo números
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        linearLayoutCompat.addView(editText);

        builder.setView(linearLayoutCompat);

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
                                Toast.makeText(Menu.this, "EDAD ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Menu.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        builder.setTitle("Cambiar: " + key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(linearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);

        EditText editText = new EditText(this);
        editText.setHint("Ingrese " + key);

        // Filtro para permitir solo letras y espacios
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setFilters(new InputFilter[] { new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i)) && source.charAt(i) != ' ') {
                        return "";
                    }
                }
                return null;
            }
        }});

        linearLayoutCompat.addView(editText);

        builder.setView(linearLayoutCompat);

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
                                Toast.makeText(Menu.this, "PAIS ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Menu.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaFondo != null && !mediaFondo.isPlaying()) {
            mediaFondo.start();
        }
    }
}