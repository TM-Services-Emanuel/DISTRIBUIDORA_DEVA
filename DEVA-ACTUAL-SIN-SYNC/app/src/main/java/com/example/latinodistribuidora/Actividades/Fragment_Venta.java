package com.example.latinodistribuidora.Actividades;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latinodistribuidora.Adaptador_Productos_venta;
import com.example.latinodistribuidora.CRUD.Access_Productos;
import com.example.latinodistribuidora.CRUD.Access_UnicadMedida;
import com.example.latinodistribuidora.Modelos.Productos;
import com.example.latinodistribuidora.Modelos.UnidadMedida;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.latinodistribuidora.Actividades.Editar_Producto.getIndexSpinnerUM;

public class Fragment_Venta extends Fragment {

    public View vista;
    public ListView lv;
    public ArrayList<Productos> lista = new ArrayList<>();
    public Adaptador_Productos_venta adaptadorProductos;
    public static int productoseleccionado = -1;
    public EditText buscar, txtCantidad;
    public Button calcular, ocultarFormularioCantidad;
    public Spinner comboUM;
    public static ArrayList<String> listaum;
    public static ArrayList<UnidadMedida> umlist;
    public static int exenta,iva5,iva10;
    public LinearLayout formularioCantidad;
    private static final String ARG_SISUPER = "siSuper";
    private static String siSuper;
    private static String siMayorista;
    private static int cantMayorista, precioMinorista, precioMayorista;

    public TextView txtidproducto,txtcodbarra,txtproduto,txtprecio,txtumCant,txtumdescripcion,txtTotalVenta,txtiva,txttotaliva, txtsiSuper;

    public Fragment_Venta() {
        // Required empty public constructor
    }
    public static Fragment_Venta newInstance(String siSuper){
        Fragment_Venta fragment = new Fragment_Venta();
        Bundle args = new Bundle();
        args.putString(ARG_SISUPER, siSuper);

        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             siSuper = getArguments().getString(ARG_SISUPER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment__venta, container, false);
        formularioCantidad = (LinearLayout) vista.findViewById(R.id.formulario_cantidad);
        buscar = vista.findViewById(R.id.id_buscarproductosV);
        txtidproducto = vista.findViewById(R.id.id_idprodven);
        txtcodbarra = vista.findViewById(R.id.txt_idcodbarra);
        txtproduto = vista.findViewById(R.id.id_txtproducto);
        txtprecio = vista.findViewById(R.id.id_txtprecio);
        txtumCant = vista.findViewById(R.id.txtidumcant);
        txtumdescripcion = vista.findViewById(R.id.txtiddescripum);
        txtCantidad = vista.findViewById(R.id.id_txtcant);
        txtTotalVenta = vista.findViewById(R.id.id_txttotal);
        txtiva = vista.findViewById(R.id.txt_idiva);
        txttotaliva = (TextView) vista.findViewById(R.id.txt_totaliva);
        comboUM = vista.findViewById(R.id.spinner_umv);
        calcular = (Button) vista.findViewById(R.id.btnCalcular);
        ocultarFormularioCantidad = (Button) vista.findViewById(R.id.btnOcularFormularioCantidad);
        txtsiSuper = (TextView) vista.findViewById(R.id.id_si_super_en_fragmentV);

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull final View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtsiSuper.setText(siSuper);
        getParentFragmentManager().setFragmentResultListener("bloquear", this, ((requestKey, result) -> {
            if(!result.isEmpty()){
                if(result.getString("B").equals("SI")){
                    vista.findViewById(R.id.list_prodavender).setEnabled(false);
                }
            }
        }));

        formularioCantidad.setVisibility(View.GONE);
        llenarLista();
        onClick();
        ocultarFormularioCantidad.setOnClickListener(v -> {
            bajarTeclado();
            formularioCantidad.setVisibility(View.GONE);

        });

        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filtro= s.toString();
                if(filtro.length()>=0){
                    Log.i("",filtro);
                    lista.removeAll(lista);
                    llenarListaFiltrada(filtro);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        consultarlistaUM();
        ArrayAdapter<String> adaptadorUM = new ArrayAdapter(getContext(), R.layout.spinner_item_ldventa,listaum);
        comboUM.setAdapter(adaptadorUM);
        comboUM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    if(position!=0){
                        txtumCant.setText(String.valueOf(umlist.get(position-1).getCant()));
                        calcular();
                    }else{
                        txtumCant.setText("");
                        txtTotalVenta.setText("");
                    }
                }catch (Exception e){
                    //Toast.makeText(getContext(), "error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        txtCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcular();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((Button)view.findViewById(R.id.btnEnviarC)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(((TextView) view.findViewById(R.id.id_txttotal)).getText().toString().trim().isEmpty()){
                        //Toast.makeText(getActivity(),"Falta calcular el total de esta venta",Toast.LENGTH_SHORT).show();
                        Snackbar.make(view.findViewById(R.id.formulario_cantidad), "Falta calcular el total para cargar al carrito!"
                                        , Snackbar.LENGTH_SHORT)
                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                .setBackgroundTint(Color.parseColor("#11232E"))
                                .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    }else if(((TextView) view.findViewById(R.id.id_txttotal)).getText().toString().trim().equals("0")){
                        //Toast.makeText(getActivity(),"Falta calcular el total de esta venta",Toast.LENGTH_SHORT).show();
                        Snackbar.make(view.findViewById(R.id.formulario_cantidad), "El total no puede ser igual a 0!", Snackbar.LENGTH_SHORT)
                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                .setBackgroundTint(Color.parseColor("#11232E"))
                                .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    }else{
                        String id=((TextView) view.findViewById(R.id.id_idprodven)).getText().toString();
                        String codbarra=((TextView) view.findViewById(R.id.txt_idcodbarra)).getText().toString();
                        String producto=((TextView  ) view.findViewById(R.id.id_txtproducto)).getText().toString();
                        String precio = ((TextView) view.findViewById(R.id.id_txtprecio)).getText().toString().replace(".","").replace(",","");
                        String cantidad = ((EditText) view.findViewById(R.id.id_txtcant)).getText().toString();
                        String um= ((Spinner) view.findViewById(R.id.spinner_umv)).getSelectedItem().toString();
                        int total= Integer.parseInt(((TextView) view.findViewById(R.id.id_txttotal)).getText().toString().replace(".","").replace(",",""));
                        String ivadescripcion = ((TextView) view.findViewById(R.id.txt_idiva)).getText().toString();

                        Bundle enviar = new Bundle();
                        enviar.putString("id",id);
                        enviar.putString("codbarra",codbarra);
                        enviar.putString("producto",producto);
                        enviar.putString("precio",precio);
                        enviar.putString("cantidad",cantidad);
                        enviar.putString("um",um);
                        enviar.putInt("total",total);
                        enviar.putString("ivadescripcion",ivadescripcion);
                        enviar.putInt("exenta",exenta);
                        enviar.putInt("iva5",iva5);
                        enviar.putInt("iva10",iva10);

                        getParentFragmentManager().setFragmentResult("enviar",enviar);
                        limpiar();
                        buscar.requestFocus();
                        formularioCantidad.setVisibility(View.GONE);
                        bajarTeclado();
                    }

                }catch (Exception e){}

            }
        });

    }

    public void bajarTeclado() {
        View view = getActivity().getCurrentFocus();
        if(view !=null){
            InputMethodManager imn = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imn.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    public void onClick() {
        DecimalFormat nformat = new DecimalFormat("##,###,###");
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            productoseleccionado = position;
            Productos productos = lista.get(productoseleccionado);
            if(siSuper.equals("S")){
                txtidproducto.setText(String.valueOf(productos.getIdproducto()));
                txtcodbarra.setText(productos.getCod_barra());
                txtproduto.setText(productos.getDescripcion());
                txtprecio.setText(nformat.format((productos.getPrecio_supermercado())));
                txtumdescripcion.setText(productos.getUnidad());
                txtiva.setText(String.valueOf(productos.getImpuesto()));
                txtCantidad.setText("");
                txtTotalVenta.setText("");
                int indexUM= getIndexSpinnerUM(comboUM, txtumdescripcion.getText().toString());
                comboUM.setSelection(indexUM);

                txtCantidad.requestFocus();

                view.setSelected(true);

                formularioCantidad.setVisibility(View.VISIBLE);
            }else if(siSuper.equals("N")){
                txtidproducto.setText(String.valueOf(productos.getIdproducto()));
                txtcodbarra.setText(productos.getCod_barra());
                txtproduto.setText(productos.getDescripcion());
                txtumdescripcion.setText(productos.getUnidad());
                txtiva.setText(String.valueOf(productos.getImpuesto()));
                txtprecio.setText(nformat.format((productos.getPrecio_minorista())));
                siMayorista= productos.getMayorista();
                cantMayorista = productos.cant_mayorista;
                precioMinorista = productos.getPrecio_minorista();
                precioMayorista = Integer.parseInt(productos.getPrecio());
                txtCantidad.setText("");
                txtTotalVenta.setText("");

                int indexUM= getIndexSpinnerUM(comboUM, txtumdescripcion.getText().toString());
                comboUM.setSelection(indexUM);

                txtCantidad.requestFocus();

                view.setSelected(true);

                formularioCantidad.setVisibility(View.VISIBLE);
            }
            return true;
        });
    }

    public void llenarLista(){
        try{
            lv = (ListView) vista.findViewById(R.id.list_prodavender);
            Access_Productos db = Access_Productos.getInstance(getContext());
            Cursor c = db.getProductos();
            if (c.moveToFirst()){
                do {
                    lista.add( new Productos (c.getInt(0), c.getString(1), c.getString(2), c.getString(3)
                            , c.getString(4), c.getString(5), c.getString(9),
                            c.getString(10), c.getString(7), c.getInt(8),
                            c.getString(11), c.getInt(12), c.getInt(13), c.getInt(14)));
                }while (c.moveToNext());
            }
            adaptadorProductos = new Adaptador_Productos_venta(getContext(),lista);
            lv.setAdapter(adaptadorProductos);
            db.close();

        }catch (Exception e){
            Toast.makeText(getContext(), "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void llenarListaFiltrada(String filtro){
        try{
            lv = (ListView) vista.findViewById(R.id.list_prodavender);
            Access_Productos db = Access_Productos.getInstance(getContext());
            Cursor c = db.getFiltrarProductos(filtro);
            if (c.moveToFirst()){
                do {
                    lista.add( new Productos (c.getInt(0), c.getString(1), c.getString(2), c.getString(3)
                            , c.getString(4), c.getString(5), c.getString(9),
                            c.getString(10), c.getString(7), c.getInt(8),
                            c.getString(11), c.getInt(12), c.getInt(13), c.getInt(14)));
                }while (c.moveToNext());
            }
            adaptadorProductos = new Adaptador_Productos_venta(getContext(),lista);
            lv.setAdapter(adaptadorProductos);
            db.close();

        }catch (Exception e){
            Toast.makeText(getContext(), "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void consultarlistaUM() {
        Access_UnicadMedida db = Access_UnicadMedida.getInstance(getContext());
        db.openReadable();
        UnidadMedida unidadMedida=null;
        umlist = new ArrayList<UnidadMedida>();
        Cursor cursor = db.getUnidadMedida();
        while (cursor.moveToNext()){
            unidadMedida = new UnidadMedida();
            unidadMedida.setIdunidad(cursor.getInt(0));
            unidadMedida.setUm(cursor.getString(1));
            unidadMedida.setCant(cursor.getInt(2));

            umlist.add(unidadMedida);
        }
        obtenerlistaUM();
    }
    public void obtenerlistaUM(){
        listaum = new ArrayList<String>();
        listaum.add("SELEC.");
        for(int i=0; i < umlist.size();i++) {
            listaum.add(umlist.get(i).getUm().toString());
        }
    }

    private void limpiar(){
        txtidproducto.setText("");
        txtproduto.setText("");
        txtprecio.setText("");
        txtumdescripcion.setText("");
        txtCantidad.setText("");
        txtTotalVenta.setText("");
    }

    private void calcular() {
        if (txtidproducto.getText().toString().trim().isEmpty()) {
            //Toast.makeText(getContext(),"No ha seleccionado ningún producto",Toast.LENGTH_SHORT).show();
        } else if (txtCantidad.getText().toString().trim().isEmpty()) {
            //Toast.makeText(getContext(),"Ingrese cantidad",Toast.LENGTH_SHORT).show();
            txtCantidad.requestFocus();
        } else if (Float.parseFloat(txtCantidad.getText().toString()) <= 0) {
            // Toast.makeText(getContext(),"Ingrese cantidad válida",Toast.LENGTH_SHORT).show();
            txtCantidad.requestFocus();
        } else {
            if(siSuper.equals("S")){
                try {
                    DecimalFormat nformat = new DecimalFormat("##,###,###");
                    int cantum = Integer.parseInt(txtumCant.getText().toString());
                    double cantventa = Double.parseDouble(txtCantidad.getText().toString());
                    int precioventa = Integer.parseInt(txtprecio.getText().toString().replace(",", "").replace(".", ""));
                    int total = (int) ((cantum * cantventa) * precioventa);
                    txtTotalVenta.setText(String.valueOf(nformat.format(total)));
                    int imp = Integer.parseInt(((TextView) vista.findViewById(R.id.txt_idiva)).getText().toString());
                    switch (imp) {
                        case 0:
                            exenta = 0;
                            iva5 = 0;
                            iva10 = 0;
                            txttotaliva.setText(String.valueOf(exenta));
                            break;
                        case 5:
                            exenta = 0;
                            //iva5=total/21;
                            iva5 = total;
                            iva10 = 0;
                            txttotaliva.setText(String.valueOf(iva5));
                            break;
                        case 10:
                            exenta = 0;
                            iva5 = 0;
                            //iva10=total/11;
                            iva10 = total;
                            txttotaliva.setText(String.valueOf(iva10));
                            break;
                    }
                } catch (Exception e) {}

            }else if(siSuper.equals("N")){
                try {
                    DecimalFormat nformat = new DecimalFormat("##,###,###");
                    int cantum = Integer.parseInt(txtumCant.getText().toString());
                    double cantventa = Double.parseDouble(txtCantidad.getText().toString());
                    int precioventa;
                    if(Double.parseDouble(txtCantidad.getText().toString()) >= (cantMayorista)){
                        precioventa = precioMayorista;
                        txtprecio.setText(nformat.format(precioMayorista));
                    }else{
                        precioventa = precioMinorista;
                        txtprecio.setText(nformat.format(precioMinorista));
                    }
                    int total = (int) ((cantum * cantventa) * precioventa);
                    txtTotalVenta.setText(String.valueOf(nformat.format(total)));
                    int imp = Integer.parseInt(((TextView) vista.findViewById(R.id.txt_idiva)).getText().toString());
                    switch (imp) {
                        case 0:
                            exenta = 0;
                            iva5 = 0;
                            iva10 = 0;
                            txttotaliva.setText(String.valueOf(exenta));
                            break;
                        case 5:
                            exenta = 0;
                            //iva5=total/21;
                            iva5 = total;
                            iva10 = 0;
                            txttotaliva.setText(String.valueOf(iva5));
                            break;
                        case 10:
                            exenta = 0;
                            iva5 = 0;
                            //iva10=total/11;
                            iva10 = total;
                            txttotaliva.setText(String.valueOf(iva10));
                            break;
                    }
                } catch (Exception e) {}
            }

        }
    }

}