package com.example.drogariasutil.database;

import java.io.Serializable;

public class Produto implements Serializable {
    private int codigo;
    private String nome;
    private String preco;
    private String descricao;
    private String imagem;

    public Produto(int codigo, String nome, String preco, String descricao, String imagem) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.imagem = imagem;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getPreco() {
        return preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getImagem() {
        return imagem;
    }
}