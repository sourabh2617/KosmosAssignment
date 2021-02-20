package com.kosmos.articles.data.remote.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.kosmos.articles.data.local.dao.ArticleDao;
import com.kosmos.articles.data.local.entity.Datum;
import com.kosmos.articles.data.remote.ApiService;
import com.kosmos.articles.data.remote.NetworkBoundResource;
import com.kosmos.articles.data.remote.Resource;
import com.kosmos.articles.data.remote.model.UserData;
import com.kosmos.articles.view.callbacks.ResponseListener;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;


public class ArticleRepository {

    private final ArticleDao articleDao;
    private final ApiService apiService;

    @Inject
    ArticleRepository(ArticleDao dao, ApiService service) {
        this.articleDao = dao;
        this.apiService = service;
    }

    /**
     * This method fetches the popular articles from the service.
     * Once the fetching is done the data is cached to local db so that the app can even work offline
     * @param howfarback index indicating how far back
     * @return List of articles
     */
    public LiveData<Resource<List<Datum>>> loadPopularArticles(int howfarback) {
        return new NetworkBoundResource<List<Datum>, UserData>() {

            @Override
            protected void saveCallResult(UserData item) {
                if(null != item)
                    articleDao.saveArticles(item.getData());
            }

            @NonNull
            @Override
            protected LiveData<List<Datum>> loadFromDb() {
                return articleDao.loadPopularArticles();
            }

            @NonNull
            @Override
            protected Call<UserData> createCall() {
                return apiService.loadPopularArticles(howfarback);
            }
        }.getAsLiveData();
    }

    public void saveData(UserData item){
        if(null != item)
            saveResultAndReInit(item);
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    private void saveResultAndReInit(UserData item) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                articleDao.saveArticles(item.getData());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();
    }


    /**
     * This method fetches the HTML comntent from the url and parses it and fills the model
     * @param url url to be fetched
     * @param responseListener callback
     */
    @SuppressLint("CheckResult")
    public void loadArticleDetails(String url, ResponseListener responseListener) {
        Datum articleDetails = new Datum();
        Observable.fromCallable(() -> {
            Document document = Jsoup.connect(url).get();
            articleDetails.setFirstName(document.title());
            articleDetails.setEmail(document.select("p").text());
            return false;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> responseListener.onSuccess(articleDetails),
                 (error -> responseListener.onFailure(error.getMessage())));

    }

}
