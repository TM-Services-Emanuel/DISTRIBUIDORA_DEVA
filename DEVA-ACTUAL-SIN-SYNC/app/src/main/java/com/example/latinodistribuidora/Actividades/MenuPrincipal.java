package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.latinodistribuidora.CRUD.Access_Venta;
import com.example.latinodistribuidora.MainActivity;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MenuPrincipal extends AppCompatActivity {
    private int idvendedor;
    private String vendedor;
    private TextView txtbienvenida,txtidvendedor, txtvendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        txtidvendedor = findViewById(R.id.id_idvendedor);
        txtvendedor = findViewById(R.id.id_vendedor);
        txtbienvenida = findViewById(R.id.id_bienvenido);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            idvendedor = bundle.getInt("idVendedor");
            vendedor= bundle.getString("Vendedor");
        }
        txtidvendedor.setText(String.valueOf(idvendedor));
        txtvendedor.setText(vendedor);
        txtbienvenida.setText("Bienvenido "+vendedor+", comencemos a trabajar!");

        findViewById(R.id.btnVender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), Listar_ClientesVenta.class);
                i.putExtra("idVendedor",txtidvendedor.getText());
                i.putExtra("Vendedor",txtvendedor.getText());
                startActivity(i);
            }
        });

        findViewById(R.id.btnVenderCosto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), Listar_ClientesVentaCosto.class);
                i.putExtra("idVendedor",txtidvendedor.getText());
                i.putExtra("Vendedor",txtvendedor.getText());
                startActivity(i);
            }
        });

        findViewById(R.id.id_btnMant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), Opciones_Mantenimiento.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btnListarVentas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent( getApplicationContext(), Listar_ventas.class);
                    startActivity(i);
            }
        });

        findViewById(R.id.btnProducto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent( getApplicationContext(), Listar_Productos.class);
                    startActivity(i);
            }
        });

        findViewById(R.id.id_btnClient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent( getApplicationContext(), Listar_Clientes.class);
                    startActivity(i);
            }
        });

    }

    public void CerrarSesion(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Esta seguro que desea salir de esta aplicación cerrando sesión por completo?");
        alertDialog.setTitle("CERRAR SESIÓN");
        alertDialog.setIcon(R.drawable.ic_salirlight);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("SI, SALIR", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Intent i = new Intent( getApplicationContext(), MainActivity.class);
                startActivity(i);
                txtidvendedor.setText("");
                txtvendedor.setText("");
                txtbienvenida.setText("");
                finish();
            }
        });
        alertDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, "Una sesión se encuentra iniciada.\n\n", Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.linearLayout26), "UNA SESIÓN SE ENCUETRA INICADA." +
                        "\nCierre Sesión si desea finalizar las operaciones.", Snackbar.LENGTH_SHORT)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(Color.parseColor("#11232E"))
                .setActionTextColor(Color.parseColor("#FFFFFF")).show();
    }
}