package com.utp.zombiegame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyHolder> {

    private Context context;
    private List<Usuario> usuarioList;

    //Constructor
    public Adaptador(Context context, List<Usuario> usuarioList) {
        this.context = context;
        this.usuarioList = usuarioList;
    }

    @NonNull
    @Override

    //Inflamos el diseño
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jugadores,parent,false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {

        //Obtener los datos del modelo
        String Imagen = usuarioList.get(i).getImagen();
        String Nombres = usuarioList.get(i).getNombres();
        String Correo = usuarioList.get(i).getEmail();
        int Zombies = usuarioList.get(i).getZombies();

        //Conversion a string
        String Z = String.valueOf(Zombies);

        //DATOS DEL JUGADOR
        holder.NombreJugador.setText(Nombres);
        holder.CorreoJugador.setText(Correo);
        holder.PuntajeJugador.setText(Z);

        //IMAGEN DEL JUGADOR
        try {
            //SI EL USUARIO TIENE FOTO DE PERFIL
            Picasso.get().load(Imagen).into(holder.ImagenJugador);
        }
        catch (Exception e){
            //SI NO TIENE FOTO DE PERFIL
        }

    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView ImagenJugador;
        TextView NombreJugador, CorreoJugador, PuntajeJugador;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //Inicializar

            ImagenJugador = itemView.findViewById(R.id.ImagenJugador);
            NombreJugador = itemView.findViewById(R.id.NombreJugador);
            CorreoJugador = itemView.findViewById(R.id.CorreoJugador);
            PuntajeJugador = itemView.findViewById(R.id.PuntajeJugador);
        }
    }
}
