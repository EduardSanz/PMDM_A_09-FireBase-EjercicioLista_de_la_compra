package com.cieep.pmdm_a_09_firebase_ejerciciolista_de_la_compra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cieep.pmdm_a_09_firebase_ejerciciolista_de_la_compra.R;
import com.cieep.pmdm_a_09_firebase_ejerciciolista_de_la_compra.modelos.Producto;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoVH> {

    private List<Producto> objects;
    private int resource;
    private Context context;
    private DatabaseReference refDatabase;

    public ProductosAdapter(List<Producto> objects, int resource, Context context, DatabaseReference refDatabase) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        this.refDatabase = refDatabase;
    }

    @NonNull
    @Override
    public ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productoView = LayoutInflater.from(context).inflate(resource, null);
        productoView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ProductoVH(productoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoVH holder, int position) {
        Producto producto = objects.get(position);
        holder.lblNombre.setText(producto.getNombre());
        holder.lblCantidad.setText(String.valueOf(producto.getCantidad()));
        holder.lblPrecio.setText(producto.getPrecioMoneda());

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objects.remove(producto);
                // notifyItemRemoved(holder.getAdapterPosition());
                refDatabase.setValue(objects);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ProductoVH extends RecyclerView.ViewHolder {

        ImageButton btnEliminar;
        TextView lblNombre, lblCantidad, lblPrecio;

        public ProductoVH(@NonNull View itemView) {
            super(itemView);
            btnEliminar = itemView.findViewById(R.id.btnDeleteCard);
            lblNombre = itemView.findViewById(R.id.lblNombreCard);
            lblCantidad = itemView.findViewById(R.id.lblCantidadCard);
            lblPrecio = itemView.findViewById(R.id.lblPrecioCard);
        }
    }
}
