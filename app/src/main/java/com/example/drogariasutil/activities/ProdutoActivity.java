package com.example.drogariasutil.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.drogariasutil.R;
import com.example.drogariasutil.database.Produto;

public class ProdutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);
        Toolbar toolbar = findViewById(R.id.toolbar_produto);
        setSupportActionBar(toolbar);

        mostrarProduto();
    }

    private void mostrarProduto() {
        Intent intent = getIntent();
        Produto produto = (Produto) intent.getSerializableExtra("produto");

        ImageView ivProduto = (ImageView) findViewById(R.id.iv_produto);
        Glide.with(this).load(produto.getImagem()).into(ivProduto);

        TextView tvNome = (TextView) findViewById(R.id.tv_nome);
        tvNome.setText(produto.getNome());

        TextView tvCodigo = (TextView) findViewById(R.id.tv_codigo);
        tvCodigo.setText("CÓDIGO: " + produto.getCodigo());

        TextView tvPreco = (TextView) findViewById(R.id.tv_preco);
        tvPreco.setText("R$ " + produto.getPreco());

        TextView tvDescricao = (TextView) findViewById(R.id.tv_descricao);
        tvDescricao.setText(produto.getDescricao());
    }
}