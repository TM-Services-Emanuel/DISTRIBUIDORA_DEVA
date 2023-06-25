package com.example.latinodistribuidora;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.latinodistribuidora.Actividades.Listar_PE;
import com.example.latinodistribuidora.Actividades.MenuPrincipal;
import com.example.latinodistribuidora.Actividades.Registrar_Usuario;
import com.example.latinodistribuidora.CRUD.Access_PE;
import com.example.latinodistribuidora.CRUD.Access_Servidor;
import com.example.latinodistribuidora.CRUD.Access_Usuarios;
import com.example.latinodistribuidora.Conexion.MySingleton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;
public class MainActivity extends AppCompatActivity {
    public EditText usuario, contrasena;
    private long insertadoUsuario=0;
    private ImageButton bajarUsuario;
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario = findViewById(R.id.id_iniciar_usuario);
        contrasena = findViewById(R.id.id_iniciar_contrasena);
        bajarUsuario = (ImageButton) findViewById(R.id.idBajarUsuarios);

        bajarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("¿Seguro que desea descargar la lista de vendedores disponibles para la aplicación?");
                alertDialog.setTitle("ACTUALIZACIÓN DE DATOS");
                alertDialog.setIcon(R.drawable.ic_descargarpcdark);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("SI, COMENZAR DESCARGA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sincronizarUsuario(MainActivity.this);
                    }
                });
                alertDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opcion_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id=menuItem.getItemId();
        if(id==R.id.item_download){
            sincronizarUsuario(MainActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void irRegistrar(View view){
        Intent i = new Intent(this, Registrar_Usuario.class);
        startActivity(i);
        finish();
    }


    public void IniciarSesion (View view){
       if(usuario.getText().toString().trim().isEmpty()){
            usuario.requestFocus();
        }else if(contrasena.getText().toString().trim().isEmpty()){
            contrasena.requestFocus();
        }else{
            try{
                Access_Usuarios db = Access_Usuarios.getInstance(getApplicationContext());
                Cursor cursor= db.getUsuario_IniciarSesion(usuario.getText().toString(), contrasena.getText().toString());
                if(cursor.moveToNext()){
                    db.close();
                    Intent i = new Intent(this, MenuPrincipal.class);
                    i.putExtra("idVendedor", cursor.getInt(0));
                    i.putExtra("Vendedor", cursor.getString(1));
                    startActivity(i);
                    this.finish();
                }else{
                    //Toast.makeText(this, "NO SE PUEDE ACCEDER A LA APLICACIÓN." +
                            //"\nUsuario y/o Contraseña incorrecta.", Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(R.id.linearLayout68), "NO SE PUEDE ACCEDER A LA APLICACIÓN." +
                                    "\nVerifique si el Usuario y la Contraseña son correctas.", Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    usuario.requestFocus();
                }
            }catch(Exception e) {
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private static boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    private void sincronizarUsuario(Context context) {
        try{
            Access_Servidor db = Access_Servidor.getInstance(getApplicationContext());
            Cursor cursor= db.getServidor();
            if(cursor.moveToNext()){
                db.close();
                IP= cursor.getString(1);
                if (checkNetworkConnection(context)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+IP+context.getResources().getString(R.string.URL_UPDATE_USUARIOS), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Access_Usuarios db = Access_Usuarios.getInstance(context.getApplicationContext());
                            db.borrarUsuarios();
                            AlertDialog mDialog= new SpotsDialog.Builder()
                                    .setContext(MainActivity.this)
                                    .setMessage("Sincronizando Vendedores")
                                    .setCancelable(false)
                                    .build();
                            new Sincronizar(MainActivity.this, mDialog).execute();
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    db.openWritable();
                                    insertadoUsuario= db.insertarUsuarioServer(object.getInt("idusuario"), object.getString("nombre"),
                                            object.getInt("ci"), object.getString("direccion"),object.getString("telefono"),
                                            object.getString("usuario"),object.getString("contrasena"),object.getString("estado"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Error Respuesta en JSON: " + error.getMessage());
                            String message = "";
                            if (error instanceof NetworkError) {
                                message = "¡Error de red!";
                            } else if (error instanceof ServerError) {
                                message = "No se pudo encontrar el servidor.\n¡Inténtalo de nuevo después de un tiempo!";
                            } else if (error instanceof ParseError) {
                                message = "¡Error de sintáxis!\n¡Inténtalo de nuevo después de un tiempo!";
                            } else if (error instanceof NoConnectionError) {
                                message = "No se puede conectar a Internet ...\n¡Compruebe su conexión!";
                            } else if (error instanceof TimeoutError) {
                                message = "¡El tiempo de conexión expiro!\nPor favor revise su conexion a internet.";
                            }
                            //Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(R.id.linearLayout68), message, Snackbar.LENGTH_SHORT)
                                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                    .setBackgroundTint(Color.parseColor("#11232E"))
                                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                            Log.d(TAG, "jsArrayRequest Error : " + message);
                        }
                    });

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(context).addToRequestQue(stringRequest);
                }else{
                    Snackbar.make(findViewById(R.id.linearLayout68), "Telefono sin red Telefonico y WIFI.\n¡Active un método de conexión!", Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                }
            }else{
               // Toast.makeText(this, "IP DEL SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout68), "IP DEL SERVIDOR NO ENCONTRADO", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        }catch(Exception e){
            //Toast.makeText(this, "TABLA DE SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.linearLayout68), "TABLA DE SERVIDOR NO ENCONTRADO", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        }
    }

    private class Sincronizar extends AsyncTask<Void,Void,Void> {
        Context context;
        AlertDialog alertDialog;
        public Sincronizar(Context context,AlertDialog alertDialog)
        {
            this.alertDialog = alertDialog;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            alertDialog.dismiss();
            if(insertadoUsuario > 0){
                //Toast.makeText(context.getApplicationContext(), "Sincronizado Correctamente", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout68), "Sincronizado correctamente!", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }else{
                //Toast.makeText(context.getApplicationContext(),"Error insertado datos del servidor",Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout68), "Error insertando datos del servidor.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        }
    }
}