package com.cieep.pmdm_a_09_firebase_ejerciciolista_de_la_compra;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.cieep.pmdm_a_09_firebase_ejerciciolista_de_la_compra.adapters.ProductosAdapter;
import com.cieep.pmdm_a_09_firebase_ejerciciolista_de_la_compra.modelos.Producto;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.cieep.pmdm_a_09_firebase_ejerciciolista_de_la_compra.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private List<Producto> productos;
    private ProductosAdapter adapter;
    private RecyclerView.LayoutManager lm;

    private FirebaseDatabase database;
    private DatabaseReference refUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        database = FirebaseDatabase.getInstance("https://pmdm-a-09-firebase-default-rtdb.europe-west1.firebasedatabase.app/");
        refUser = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("lista_productos");

        productos = new ArrayList<>();
        adapter = new ProductosAdapter(productos, R.layout.producto_view_holder, this, refUser);
        lm = new LinearLayoutManager(this);

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productos.clear();
                if (snapshot.exists()) {
                    GenericTypeIndicator<ArrayList<Producto>> gti = new GenericTypeIndicator<ArrayList<Producto>>() {};
                    ArrayList<Producto> temp = snapshot.getValue(gti);
                    productos.addAll(temp);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.contentMain.recycler.setAdapter(adapter);
        binding.contentMain.recycler.setLayoutManager(lm);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertCrearProducto().show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return true;
    }

    private AlertDialog alertCrearProducto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crear Producto");
        builder.setCancelable(false);
        View alertView = getLayoutInflater().inflate(R.layout.producto_alert, null);
        EditText txtNombre = alertView.findViewById(R.id.txtNombreAlert);
        EditText txtCantidad = alertView.findViewById(R.id.txtCantidadAlert);
        EditText txtPrecio = alertView.findViewById(R.id.txtPrecioAlert);
        builder.setView(alertView);

        builder.setNegativeButton("CANCELAR", null);
        builder.setPositiveButton("CREAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombre = txtNombre.getText().toString();
                String cantidad = txtCantidad.getText().toString();
                String precio = txtPrecio.getText().toString();

                if (!nombre.isEmpty() && !cantidad.isEmpty() && !precio.isEmpty()) {
                    productos.add(new Producto(nombre, Integer.parseInt(cantidad), Float.parseFloat(precio)));
                    // adapter.notifyItemInserted(productos.size()-1);
                    refUser.setValue(productos);
                }
            }
        });

        return builder.create();
    }
}