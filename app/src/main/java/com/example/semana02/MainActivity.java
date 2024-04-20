package com.example.semana02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.semana02.adapter.ProductoAdapter;
import com.example.semana02.adapter.ProductoAdapter;
import com.example.semana02.entity.Producto;
import com.example.semana02.service.ServiceProductos;
import com.example.semana02.util.ConnectionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //ListView y Adapater
    GridView lstProductos;
    ArrayList<Producto> listaProductos = new ArrayList<Producto>();
    ProductoAdapter productoAdapter;

    Button   btnFiltrar;

    //conecta al servicio REST
    //ServiceUser serviceUser;

    ServiceProductos serviceProductos;

    private List<Producto> listaTotalProductos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lstProductos = findViewById(R.id.lstProductos);
        productoAdapter = new ProductoAdapter(this, R.layout.product_item, listaProductos);
        lstProductos.setAdapter(productoAdapter);


        //Relaciona las variables con las variables de la GUI
        btnFiltrar = findViewById(R.id.btnFiltrar);

        //Conecta al servicio REST
        serviceProductos = ConnectionRest.getConnecion().create(ServiceProductos.class);


        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargaProductos();
            }
        });

        //para el evento disparador del otro item
        lstProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Producto obj = listaProductos.get(position);
                Intent intent = new Intent(MainActivity.this,MainViewDetail.class);
                intent.putExtra("var_product", obj);
                startActivity(intent);

            }
        });


    }

    void cargaProductos(){
        Call<List<Producto>> call = serviceProductos.listaproductos();
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                   if (response.isSuccessful()){
                       listaTotalProductos = response.body();
                       listaProductos.clear();
                       listaProductos.addAll(listaTotalProductos);
                       productoAdapter.notifyDataSetChanged();
                   }
            }
            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {

            }
        });

    }

}