package com.example.drogariasutil.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drogariasutil.R;
import com.example.drogariasutil.database.Produto;
import com.example.drogariasutil.services.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>
        implements Filterable {
    private Context context;
    private List<Produto> produtoList;
    private ItemClickListener itemClickListener;
    private List<Produto> produtoListFull;

    public ProdutoAdapter(Context context, List<Produto> produtoList,
                          ItemClickListener itemClickListener) {
        this.context = context;
        this.produtoList = produtoList;
        this.itemClickListener = itemClickListener;
        produtoListFull = new ArrayList<>(produtoList);
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent,
                false);
        final ProdutoViewHolder pvh = new ProdutoViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, pvh.getAdapterPosition());
            }
        });
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        holder.tvNome.setText(produtoList.get(position).getNome());
        holder.tvPreco.setText("R$ " + produtoList.get(position).getPreco());
        Glide.with(context).load(produtoList.get(position).getImagem()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Produto> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(produtoListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Produto produto : produtoListFull) {
                    if (produto.getNome().toLowerCase().contains(filterPattern)) {
                        filteredList.add(produto);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            produtoList.clear();
            produtoList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ProdutoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNome;
        public TextView tvPreco;
        public ImageView imageView;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_nome);
            tvPreco = itemView.findViewById(R.id.tv_preco);
            imageView = itemView.findViewById(R.id.iv_produto);
        }
    }
}