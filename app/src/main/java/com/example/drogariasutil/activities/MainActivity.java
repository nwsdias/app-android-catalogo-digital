package com.example.drogariasutil.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.drogariasutil.R;
import com.example.drogariasutil.adapters.ProdutoAdapter;
import com.example.drogariasutil.database.Produto;
import com.example.drogariasutil.http.Singleton;
import com.example.drogariasutil.services.ItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://10.0.2.2:3000/produtos";
    private List<Produto> produtoList;
    private ProdutoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        buscarProdutos();
    }

    private void buscarProdutos() {
        produtoList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int codigo = jsonObject.getInt("codigo");
                        String nome = jsonObject.getString("nome");
                        double valor = jsonObject.getDouble("preco");
                        String preco = formatarPreco(valor);
                        String descricao = jsonObject.getString("descricao");
                        String imagem = jsonObject.getString("imagem");
                        Produto produto = new Produto(codigo, nome, preco, descricao, imagem);
                        produtoList.add(produto);
                    }
                    Toast.makeText(MainActivity.this, "Carregando...",
                            Toast.LENGTH_SHORT).show();
                    enviarDados();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        Singleton.getInstance(this).addToRequestQueue(request);
    }

    private String formatarPreco(double valor) {
        Locale localeBr = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(localeBr);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(valor);
    }

    private void enviarDados() {
        adapter = new ProdutoAdapter(this, produtoList, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int codigo = produtoList.get(position).getCodigo();
                String nome = produtoList.get(position).getNome();
                String preco = produtoList.get(position).getPreco();
                String descricao = produtoList.get(position).getDescricao();
                String imagem = produtoList.get(position).getImagem();
                Produto produto = new Produto(codigo, nome, preco, descricao, imagem);
                Intent intent = new Intent(MainActivity.this, ProdutoActivity.class);
                intent.putExtra("produto", produto);
                startActivity(intent);
            }
        });
        listarProdutos();
    }

    private void listarProdutos() {
        RecyclerView rv = findViewById(R.id.rv_produtos);
        rv.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch (id) {
            case R.id.item_local:
                intent = new Intent(this, LocalActivity.class);
                startActivity(intent);
                break;
            case R.id.item_sobre:
                intent = new Intent(this, SobreActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}