package com.kosmos.articles.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;


import com.kosmos.articles.data.local.entity.Datum;
import com.kosmos.articles.data.remote.Resource;
import com.kosmos.articles.data.remote.model.UserData;
import com.kosmos.articles.data.remote.repository.ArticleRepository;

import java.util.List;

import javax.inject.Inject;


public class ArticleListViewModel extends ViewModel {
    private final LiveData<Resource<List<Datum>>> popularArticles;
    private ArticleRepository articleRepository;

    @Inject
    public ArticleListViewModel(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        popularArticles = articleRepository.loadPopularArticles(1);
    }

    public LiveData<Resource<List<Datum>>> getPopularArticles() {
        return popularArticles;
    }
    public void saveData(UserData userData){
        articleRepository.saveData(userData);
    }
}
