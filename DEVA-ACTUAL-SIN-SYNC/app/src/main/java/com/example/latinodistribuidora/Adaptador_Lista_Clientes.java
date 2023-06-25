package com.example.latinodistribuidora;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.latinodistribuidora.Modelos.Clientes;

import java.util.ArrayList;

public class Adaptador_Lista_Clientes extends BaseAdapter {
    private final Context context;
    private final ArrayList<Clientes> listItems;

    public Adaptador_Lista_Clientes(Context context, ArrayList<Clientes> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Clientes item = (Clientes) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_lista_clientes, null);
        TextView idcliente = convertView.findViewById(R.id.id_l_codcliente);
        TextView razonsocial = convertView.findViewById(R.id.id_l_razonsocial);
        TextView ruc = convertView.findViewById(R.id.id_l_ruc);
        TextView ciudad = convertView.findViewById(R.id.id_l_ciudad);
        TextView direccion = convertView.findViewById(R.id.id_l_direccion);
        TextView telefono = convertView.findViewById(R.id.id_l_telefono);
        TextView supermercado = convertView.findViewById(R.id.id_l_es_super);
        LinearLayout superMercado = convertView.findViewById(R.id.id_layout_supermercado);

        idcliente.setText(String.valueOf(item.getIdcliente()));
        razonsocial.setText(item.getRazon_social());
        ruc.setText(item.getRuc());
        direccion.setText(item.getDireccion());
        ciudad.setText(item.getCiudad());
        telefono.setText(item.getCelular());
        if(item.getSupermercado().equals("S")){
            superMercado.setVisibility(View.VISIBLE);
            supermercado.setText("SUPERMERCADO");
        }else if(item.getSupermercado().equals("N")){
            superMercado.setVisibility(View.GONE);
        }

        return convertView;
    }
}
