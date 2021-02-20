package com.kosmos.articles.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.kosmos.articles.data.local.entity.Datum;

import java.util.List;

@Dao
public interface ArticleDao {
    @Query("SELECT * FROM datum")
    LiveData<List<Datum>> loadPopularArticles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveArticles(List<Datum> articleEntities);

}
