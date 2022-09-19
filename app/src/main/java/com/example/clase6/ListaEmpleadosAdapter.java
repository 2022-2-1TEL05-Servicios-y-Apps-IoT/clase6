package com.example.clase6;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListaEmpleadosAdapter extends RecyclerView.Adapter<ListaEmpleadosAdapter.EmpleadoViewHolder> {

    private Empleado[] listaEmpleados;
    private Context context;

    public class EmpleadoViewHolder extends RecyclerView.ViewHolder {
        Empleado empleado;

        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
            Button button = itemView.findViewById(R.id.btnEditarEmpleado);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("data usuario","id: " + empleado.getId());
                }
            });
        }
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rv, parent, false);
        return new EmpleadoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        Empleado empleado = listaEmpleados[position];
        holder.empleado = empleado;
        TextView tvFirstName = holder.itemView.findViewById(R.id.textViewFirstName);
        TextView tvLastName = holder.itemView.findViewById(R.id.textViewLastName);
        tvFirstName.setText(empleado.getFirstName());
        tvLastName.setText(empleado.getLastName());
    }

    @Override
    public int getItemCount() {
        return listaEmpleados.length;
    }

    public Empleado[] getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(Empleado[] listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
