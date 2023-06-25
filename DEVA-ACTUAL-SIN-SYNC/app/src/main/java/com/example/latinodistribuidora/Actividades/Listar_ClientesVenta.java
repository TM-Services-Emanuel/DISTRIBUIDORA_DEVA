package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latinodistribuidora.Adaptador_Clientes;
import com.example.latinodistribuidora.CRUD.Access_Clientes;
import com.example.latinodistribuidora.CRUD.Access_Departamentos;
import com.example.latinodistribuidora.CRUD.Access_PE;
import com.example.latinodistribuidora.CRUD.Access_Timbrado;
import com.example.latinodistribuidora.Modelos.Clientes;
import com.example.latinodistribuidora.Modelos.Departamentos;
import com.example.latinodistribuidora.Modelos.Fecha;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Listar_ClientesVenta extends AppCompatActivity {
    private ListView lv;
    private final ArrayList<Clientes> lista = new ArrayList<>();
    private Adaptador_Clientes adaptadorClientes;
    private int clienteseleccionado = -1;
    public Object mActionMode;
    private EditText buscar;
    private String idvendedor,vendedor;
    private String factactual;
    private String idTim;
    Spinner comboDias;
    ArrayList<String> listadep;
    private String dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_clientes_venta);
        buscar = findViewById(R.id.id_buscarclienteV);
        comboDias = findViewById(R.id.id_spinner_dias);
        Button botonFiltar = (Button) findViewById(R.id.btn_filtrarClientes);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            idvendedor = bundle.getString("idVendedor");
            vendedor= bundle.getString("Vendedor");
        }

        CargarDiasenCombo();
        //llenarLista();



        botonFiltar.setOnClickListener(v -> {
            bajarTeclado();
            String filtro=buscar.getText().toString();
            if(filtro.length()<=0){
                if(dia.equals("todos")){
                    lista.removeAll(lista);
                    llenarLista();
                    onClick();
                }else{
                    lista.removeAll(lista);
                    llenarListaporDia();
                    onClick();
                }

            }else{
                if(dia.equals("todos")){
                    lista.removeAll(lista);
                    llenarListaFiltrada(filtro);
                    onClick();
                }else{
                    lista.removeAll(lista);
                    llenarListaFiltradaporDia(filtro);
                    onClick();
                }

            }
        });

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, R.layout.spinner_item_dias,listadep);
        comboDias.setAdapter(adaptador);
        int index= getIndexSpinner(comboDias, (String) android.text.format.DateFormat.format("EEEE", new Date()) );
        comboDias.setSelection(index);
        comboDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    switch (position) {
                        case 0:
                            //Toast.makeText(getApplicationContext(), "todos", Toast.LENGTH_LONG).show();
                            dia="todos";
                            lista.removeAll(lista);
                            llenarLista();
                            onClick();
                            break;
                        case 1:
                            dia="lunes";
                            lista.removeAll(lista);
                            llenarListaporDia();
                            onClick();
                            break;
                        case 2:
                            dia="martes";
                            lista.removeAll(lista);
                            llenarListaporDia();
                            onClick();
                            break;
                        case 3:
                            dia="miercoles";
                            lista.removeAll(lista);
                            llenarListaporDia();
                            onClick();
                            break;
                        case 4:
                            dia="jueves";
                            lista.removeAll(lista);
                            llenarListaporDia();
                            onClick();
                            break;
                        case 5:
                            dia="viernes";
                            lista.removeAll(lista);
                            llenarListaporDia();
                            onClick();
                            break;
                        case 6:
                            dia="sabado";
                            lista.removeAll(lista);
                            llenarListaporDia();
                            onClick();
                            break;
                        case 7:
                            dia="domingo";
                            lista.removeAll(lista);
                            llenarListaporDia();
                            onClick();
                            break;
                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filtro= String.valueOf(s.toString());
                if(filtro.length()>=0){
                    Log.i("",filtro);
                    lista.removeAll(lista);
                    llenarListaFiltrada(filtro);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }
    public void onResume() {
        super.onResume();
        if(comboDias.getSelectedItemPosition() == 0){
            dia="todos";
            lista.removeAll(lista);
            llenarLista();
            onClick();
        }else if(comboDias.getSelectedItemPosition() == 1){
            dia="lunes";
            lista.removeAll(lista);
            llenarListaporDia();
            onClick();
        }else if(comboDias.getSelectedItemPosition() == 1){
            dia="martes";
            lista.removeAll(lista);
            llenarListaporDia();
            onClick();
        }else if(comboDias.getSelectedItemPosition() == 1){
            dia="miercoles";
            lista.removeAll(lista);
            llenarListaporDia();
            onClick();
        }else if(comboDias.getSelectedItemPosition() == 1){
            dia="jueves";
            lista.removeAll(lista);
            llenarListaporDia();
            onClick();
        }else if(comboDias.getSelectedItemPosition() == 1){
            dia="viernes";
            lista.removeAll(lista);
            llenarListaporDia();
            onClick();
        }else if(comboDias.getSelectedItemPosition() == 1){
            dia="sabado";
            lista.removeAll(lista);
            llenarListaporDia();
            onClick();
        }else if(comboDias.getSelectedItemPosition() == 1){
            dia="domingo";
            lista.removeAll(lista);
            llenarListaporDia();
            onClick();
        }
    }

    public void onClick() {
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            clienteseleccionado = position;
            mActionMode = Listar_ClientesVenta.this.startActionMode(amc);
            view.setSelected(true);
            return true;
        });
    }
    private final ActionMode.Callback amc = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.opciones_vender, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.item_contado) {
                Clientes clientes = lista.get(clienteseleccionado);
                String fechaF = obtenerFecha();
                String horaF = obtenerHora();
                String establecimiento = obtenerEstab();
                String idemision = obtenerIDEmision();
                String puntoexpedicion = obtenerPE();
                String idtimbrado = obtenerTimbrado();
                String factA=obtenerFactura();
                if(obtenerTimbrado().equals("-fec")){
                    Snackbar.make(findViewById(R.id.id_lista_clienteV), "LA FECHA DEL TIMBRADO HA EXPIRADO." +
                                    "\nConfigure uno nuevo para registrar nuevas ventas.", Snackbar.LENGTH_INDEFINITE)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF"))
                            .setAction("VOLVER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();
                }else if(obtenerTimbrado().equals("-tim")){
                    Snackbar.make(findViewById(R.id.id_lista_clienteV), "NO EXISTE TIMBRADO ACTIVO." +
                                    "\nConfigure uno para comenzar a registra las ventas.", Snackbar.LENGTH_INDEFINITE)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF"))
                            .setAction("VOLVER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();
                }else if(obtenerPE().equals("-pec")){
                    Snackbar.make(findViewById(R.id.id_lista_clienteV), "NO EXISTE PUNTO DE EMISIÓN ACTIVO." +
                                    "\nConfigure uno para registrar las ventas.", Snackbar.LENGTH_INDEFINITE)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF"))
                            .setAction("VOLVER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();
                }else if(obtenerPE().equals("-fac")){
                    Snackbar.make(findViewById(R.id.id_lista_clienteV), "SE HA ALCANZADO EL NÚMERO MÁXIMO DE FACTURAS.", Snackbar.LENGTH_INDEFINITE)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF"))
                            .setAction("VOLVER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();
                }else{
                    Intent in = new Intent(getApplicationContext(), Registrar_venta.class);
                    in.putExtra("condicion","CONTADO");
                    in.putExtra("idcliente", clientes.getIdcliente());
                    in.putExtra("razonsocial", clientes.getRazon_social());
                    in.putExtra("ruc", clientes.getRuc());
                    in.putExtra("fecha", fechaF);
                    in.putExtra("hora",horaF);
                    in.putExtra("idvendedor", idvendedor);
                    in.putExtra("vendedor", vendedor);
                    in.putExtra("puntoexpedicion", puntoexpedicion);
                    in.putExtra("establecimiento", establecimiento);
                    in.putExtra("facturaactual",factA);
                    in.putExtra("idemision",idemision);
                    in.putExtra("idtimbrado",idtimbrado);
                    in.putExtra("super", clientes.getSupermercado());
                    startActivity(in);
                    mode.finish();
                    finish();
                }
            }else if (item.getItemId() == R.id.item_credito) {
                Clientes clientes = lista.get(clienteseleccionado);
                String fechaF = obtenerFecha();
                String horaF = obtenerHora();
                String establecimiento = obtenerEstab();
                String idemision = obtenerIDEmision();
                String puntoexpedicion = obtenerPE();
                String idtimbrado = obtenerTimbrado();
                String factA=obtenerFactura();
                if(obtenerTimbrado().equals("-fec")){
                    Snackbar.make(findViewById(R.id.id_lista_clienteV), "LA FECHA DEL TIMBRADO HA EXPIRADO." +
                                    "\nConfigure uno nuevo para registrar nuevas ventas.", Snackbar.LENGTH_INDEFINITE)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF"))
                            .setAction("VOLVER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();
                }else if(obtenerTimbrado().equals("-tim")){
                    Snackbar.make(findViewById(R.id.id_lista_clienteV), "NO EXISTE TIMBRADO ACTIVO." +
                                    "\nConfigure uno para comenzar a registra las ventas.", Snackbar.LENGTH_INDEFINITE)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF"))
                            .setAction("VOLVER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();
                }else if(obtenerPE().equals("-pec")){
                    Snackbar.make(findViewById(R.id.id_lista_clienteV), "NO EXISTE PUNTO DE EMISIÓN ACTIVO." +
                                    "\nConfigure uno para registrar las ventas.", Snackbar.LENGTH_INDEFINITE)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF"))
                            .setAction("VOLVER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();
                }else if(obtenerPE().equals("-fac")){
                    Snackbar.make(findViewById(R.id.id_lista_clienteV), "SE HA ALCANZADO EL NÚMERO MÁXIMO DE FACTURAS.", Snackbar.LENGTH_INDEFINITE)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF"))
                            .setAction("VOLVER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();
                }else{
                    Intent in = new Intent(getApplicationContext(), Registrar_venta.class);
                    in.putExtra("condicion","CRÉDITO");
                    in.putExtra("idcliente", clientes.getIdcliente());
                    in.putExtra("razonsocial", clientes.getRazon_social());
                    in.putExtra("ruc", clientes.getRuc());
                    in.putExtra("fecha", fechaF);
                    in.putExtra("hora",horaF);
                    in.putExtra("idvendedor", idvendedor);
                    in.putExtra("vendedor", vendedor);
                    in.putExtra("puntoexpedicion", puntoexpedicion);
                    in.putExtra("establecimiento", establecimiento);
                    in.putExtra("facturaactual",factA);
                    in.putExtra("idemision",idemision);
                    in.putExtra("idtimbrado",idtimbrado);
                    in.putExtra("super", clientes.getSupermercado());
                    startActivity(in);
                    mode.finish();
                    finish();
                }
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }


    };

    private String obtenerFecha(){
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(fecha);
    }
    private String obtenerFechaT(){
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(fecha);
    }
    private String obtenerHora(){
        long ahora = System.currentTimeMillis();
        Date hora = new Date(ahora);
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(hora);
    }
    private String obtenerEstab(){
        Access_PE db = new Access_PE(getApplicationContext());
        Cursor e= db.getPEActivo();
        String estab;
        if(e.moveToNext()){
            estab =e.getString(1);
        }else{
            estab = "0";
        }
        return estab;
    }
    private String obtenerIDEmision(){
        Access_PE db = new Access_PE(getApplicationContext());
        Cursor e= db.getPEActivo();
        String idestab;
        if(e.moveToNext()){
            idestab =String.valueOf(e.getInt(0));
        }else{
            idestab ="0";
        }
        return idestab;
    }
    private String obtenerPE(){
        Access_PE pe = new Access_PE(getApplicationContext());
        Cursor pex= pe.getPEActivo();
        String puntoex;
        if(pex.moveToNext()){
            int fin=pex.getInt(5);
            int actual =pex.getInt(6);
            if(actual<fin){
                puntoex =pex.getString(2);
            }else{
                puntoex= "-fac";
            }
        }else{
            puntoex = "-pec";
        }
        return puntoex;
    }
    private String obtenerFactura(){
        String fac = null;
        Access_PE pe = new Access_PE(getApplicationContext());
        Cursor pex= pe.getPEActivo();
        if(pex.moveToNext()){
            fac= String.valueOf(pex.getInt(6)+1);
        }else{
            fac="0";
        }
        switch (fac.length()){
            case 1: factactual="000000"+fac;
                break;
            case 2: factactual="00000"+fac;
                break;
            case 3: factactual="0000"+fac;
                break;
            case 4: factactual="000"+fac;
                break;
            case 5: factactual="00"+fac;
                break;
            case 6: factactual="0"+fac;
                break;
            case 7: factactual=""+fac;
                break;
        }
        return factactual;
    }

    private String obtenerTimbrado(){
        Access_Timbrado tim = new Access_Timbrado(getApplicationContext());
        Cursor e= tim.getTimbradoActivo();
        if(e.moveToNext()){
            try{
                SimpleDateFormat sdformatf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdformatf.parse((obtenerFechaT()));
                Date date2 = sdformatf.parse(Fecha.formatoFecha(e.getString(3)));
                //Toast.makeText(this, "Hoy: "+(date1)+"\n"+"tim: "+(date2), Toast.LENGTH_LONG).show();
                /*if(date1.before(date2)){
                    idTim =String.valueOf(e.getInt(0));
                }else if(date1.after(date2)){
                    idTim ="-fec";
                }else if(date1.equals(date2)){
                    idTim =String.valueOf(e.getInt(0));
                }*/
                if(date1.compareTo(date2)>0){
                    //idTim =String.valueOf(e.getInt(0));
                    idTim ="-fec";
                }else if(date1.compareTo(date2)<0){
                    idTim =String.valueOf(e.getInt(0));
                }else if(date1.compareTo(date2)==0){
                    idTim =String.valueOf(e.getInt(0));
                }
            }catch(Exception ex){
                Toast.makeText(this, "Error consultado fecha timbrado: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            idTim ="-tim";
        }
        return idTim;
    }

    private void llenarLista(){

        try{
            lv = (ListView) findViewById(R.id.id_lista_clienteV);
            Access_Clientes db = Access_Clientes.getInstance(getApplicationContext());
            Cursor c = db.getClientes();
            if (c.moveToFirst()){
                do {
                    lista.add( new Clientes (c.getInt(0),c.getString(1),c.getString(2),c.getString(3)
                            ,c.getString(4),c.getInt(5),c.getString(6), c.getString(7)));
                }while (c.moveToNext());
            }
            adaptadorClientes = new Adaptador_Clientes(this,lista);
            lv.setAdapter(adaptadorClientes);
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void llenarListaporDia(){

        try{
            lv = (ListView) findViewById(R.id.id_lista_clienteV);
            Access_Clientes db = Access_Clientes.getInstance(getApplicationContext());
            Cursor c = db.getClientesporDia(dia);
            if (c.moveToFirst()){
                do {
                    lista.add( new Clientes (c.getInt(0),c.getString(1),c.getString(2),c.getString(3)
                            ,c.getString(4),c.getInt(5),c.getString(6), c.getString(7)));
                }while (c.moveToNext());
            }
            adaptadorClientes = new Adaptador_Clientes(this,lista);
            lv.setAdapter(adaptadorClientes);
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void llenarListaFiltrada(String filtro){
        try{
            lv = (ListView) findViewById(R.id.id_lista_clienteV);
            Access_Clientes db = Access_Clientes.getInstance(getApplicationContext());
            Cursor c = db.getFiltrarClientes(filtro);
            if (c.moveToFirst()){
                do {
                    lista.add( new Clientes (c.getInt(0),c.getString(1),c.getString(2),c.getString(3)
                            ,c.getString(4),c.getInt(5),c.getString(6), c.getString(7)));
                }while (c.moveToNext());
            }
            adaptadorClientes = new Adaptador_Clientes(this,lista);
            lv.setAdapter(adaptadorClientes);
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void llenarListaFiltradaporDia(String filtro){
        try{
            lv = (ListView) findViewById(R.id.id_lista_clienteV);
            Access_Clientes db = Access_Clientes.getInstance(getApplicationContext());
            Cursor c = db.getFiltrarClientesporDia(filtro, dia);
            if (c.moveToFirst()){
                do {
                    lista.add( new Clientes (c.getInt(0),c.getString(1),c.getString(2),c.getString(3)
                            ,c.getString(4),c.getInt(5),c.getString(6), c.getString(7)));
                }while (c.moveToNext());
            }
            adaptadorClientes = new Adaptador_Clientes(this,lista);
            lv.setAdapter(adaptadorClientes);
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bajarTeclado() {
        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager imn = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imn.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

   /* @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }*/

    private void CargarDiasenCombo(){
        listadep = new ArrayList<String>();
        listadep.add("Todos");
        listadep.add("Lunes");
        listadep.add("Martes");
        listadep.add("Miércoles");
        listadep.add("Jueves");
        listadep.add("Viernes");
        listadep.add("Sábado");
        listadep.add("Domingo");
    }

    public static int getIndexSpinner(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        return index;
    }
}