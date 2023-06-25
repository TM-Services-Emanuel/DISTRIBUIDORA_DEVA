package com.example.latinodistribuidora;

import static android.view.LayoutInflater.*;
import static com.example.latinodistribuidora.R.layout.listview_item_productos2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.latinodistribuidora.Modelos.Productos;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adaptador_Productos extends BaseAdapter {
    private final Context context;
    private final ArrayList<Productos> listItems;

    public Adaptador_Productos(Context context, ArrayList<Productos> listItems) {
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

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Productos item = (Productos) getItem(position);


        DecimalFormat nformat = new DecimalFormat("##,###,###");

        convertView = from(context).inflate(listview_item_productos2,null);

        TextView codB = (TextView) convertView.findViewById(R.id.id_barra);
        TextView descipcion = (TextView) convertView.findViewById(R.id.id_descripcion);
        TextView clasificacion = (TextView) convertView.findViewById(R.id.id_clasificacion);
        TextView precioMinorista = (TextView) convertView.findViewById(R.id.id_PrecioMinorista);
        TextView venderMayorista = (TextView) convertView.findViewById(R.id.id_mayorista);
        TextView cantMayorista = (TextView) convertView.findViewById(R.id.id_cant_mayorista);
        TextView precioVenta = (TextView) convertView.findViewById(R.id.id_precio_mayorista);
        TextView precioSupermercado = (TextView) convertView.findViewById(R.id.id_precio_supermercado);
        TextView impuestoProducto = (TextView) convertView.findViewById(R.id.id_impuesto_producto);


        codB.setText(item.getCod_barra());
        descipcion.setText(item.getDescripcion());
        clasificacion.setText(item.getDivision());
        precioMinorista.setText(nformat.format((item.getPrecio_minorista())));
        venderMayorista.setText(item.getMayorista());
        if(item.getMayorista().equals("SI")){
            cantMayorista.setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.id_titulo_cant_mayorista).setVisibility(View.VISIBLE);
            cantMayorista.setText(String.valueOf(item.getCant_mayorista()));
        }else if (item.getMayorista().equals("NO")){
            cantMayorista.setVisibility(View.GONE);
            convertView.findViewById(R.id.id_titulo_cant_mayorista).setVisibility(View.GONE);
            cantMayorista.setText(String.valueOf(item.getCant_mayorista()));
        }
        precioVenta.setText(nformat.format(Integer.parseInt(item.getPrecio())));
        precioSupermercado.setText(nformat.format((item.getPrecio_supermercado())));
        impuestoProducto.setText(("IMPUESTO APLICADO: "+item.getIva()));
        return convertView;
    }
}
