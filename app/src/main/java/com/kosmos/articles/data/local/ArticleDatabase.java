package com.kosmos.articles.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.kosmos.articles.data.local.dao.ArticleDao;
import com.kosmos.articles.data.local.entity.Datum;

@Database(entities = {Datum.class}, version = 3)
public abstract class ArticleDatabase extends RoomDatabase {
    public abstract ArticleDao articleDao();
}