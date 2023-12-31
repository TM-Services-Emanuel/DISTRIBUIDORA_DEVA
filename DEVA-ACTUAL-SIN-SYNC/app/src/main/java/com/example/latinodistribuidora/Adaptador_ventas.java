package com.example.latinodistribuidora;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.latinodistribuidora.Modelos.DetalleVenta;
import com.example.latinodistribuidora.Modelos.Ventas;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Adaptador_ventas extends BaseAdapter {
    private final Context context;
    private ArrayList<Ventas> listItems;



    public Adaptador_ventas(Context context, ArrayList<Ventas> listItems) {
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
        Ventas item = (Ventas) getItem(position);
        DecimalFormat nformat = new DecimalFormat("##,###,###");

        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_listarventas, null);

        TextView idventa = convertView.findViewById(R.id.tv_idventa);
        TextView timbado = convertView.findViewById(R.id.tv_timbrado);
        TextView factura = convertView.findViewById(R.id.tv_factura);
        TextView fecha = convertView.findViewById(R.id.tv_fecha);
        TextView vendedor = convertView.findViewById(R.id.tv_vendedor);
        TextView cliente = convertView.findViewById(R.id.tv_cliente);
        TextView total = convertView.findViewById(R.id.tv_total);
        TextView estado= convertView.findViewById(R.id.id_estadoV);

        idventa.setText(String.valueOf(item.getId()));
        timbado.setText("TIMBRADO N°: "+item.getTimbrado());
        factura.setText("FACTURA "+item.getCondicion()+" N°: "+item.getEst()+"-"+item.getEmision()+"-"+item.getNrofactura());
        fecha.setText("FECHA/HORA: "+item.getFecha());
        vendedor.setText("VENDEDOR: "+item.getNombreusu());
        cliente.setText("CLIENTE: "+item.getDescripcion()+" - RUC: "+item.getRuc());
        total.setText("TOTAL: "+nformat.format(item.getTotal()));
        estado.setText(estado(item.getEstado()));

        return convertView;
    }

    public String estado(String estado){
        String est;
        if(estado.equals("S")){
            est="";
        }else{
            est="ANULADO";
        }
        return est;
    }


}
