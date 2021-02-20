package com.kosmos.articles.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.kosmos.articles.data.local.entity.Datum;
import com.kosmos.articles.databinding.ItemArticleListBinding;
import com.kosmos.articles.utils.DataBindAdapter;
import com.kosmos.articles.view.base.BaseAdapter;
import com.kosmos.articles.view.callbacks.ArticleListCallback;

import java.util.ArrayList;
import java.util.List;

public class ArticleListAdapter extends  BaseAdapter<ArticleListAdapter.ArticleViewHolder, Datum>
implements Filterable{

    private List<Datum> articleEntities;
    private List<Datum> articleEntitiesFiltered;
    private final ArticleListCallback articleListCallback;
    private DataBindAdapter databindAdapter;

    public ArticleListAdapter(@NonNull ArticleListCallback articleListCallback) {
        articleEntities = new ArrayList<>();
        articleEntitiesFiltered = new ArrayList<>();
        this.articleListCallback = articleListCallback;
        databindAdapter = new DataBindAdapter();
    }

    @Override
    public void setData(List<Datum> entities) {
        this.articleEntities = entities;
        this.articleEntitiesFiltered = entities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return ArticleViewHolder.create(LayoutInflater.from(viewGroup.getContext()), viewGroup, articleListCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder viewHolder, int i) {
        viewHolder.onBind(articleEntitiesFiltered.get(i),databindAdapter);
    }

    @Override
    public int getItemCount() {
        return articleEntitiesFiltered.size();
    }

    @Override
    public Filter getFilter() {
         return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    articleEntitiesFiltered = articleEntities;
                } else {
                    List<Datum> filteredList = new ArrayList<>();
                    for (Datum row : articleEntities) {

                        // name match condition. this might differ depending on your requirement
                        if (row.getFirstName().toLowerCase().contains(charString.toLowerCase())
                                || row.getEmail().toLowerCase().contains(charString.toLowerCase())
                                || row.getLastName().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    articleEntitiesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = articleEntitiesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                articleEntitiesFiltered = (ArrayList<Datum>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {

        private static ArticleViewHolder create(LayoutInflater inflater, ViewGroup parent, ArticleListCallback callback) {
            ItemArticleListBinding itemMovieListBinding = ItemArticleListBinding.inflate(inflater, parent, false);
            return new ArticleViewHolder(itemMovieListBinding, callback);
        }

        final ItemArticleListBinding binding;

        private ArticleViewHolder(ItemArticleListBinding binding, ArticleListCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v ->
                    callback.onArticleClicked(binding.getArticle()));
        }

        private void onBind(Datum articleEntity, DataBindAdapter databindAdapter) {
            binding.setArticle(articleEntity);
            binding.setDatabindAdapter(databindAdapter);
            binding.executePendingBindings();

        }
    }
}
