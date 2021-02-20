package com.kosmos.articles.view.callbacks;

import com.kosmos.articles.data.local.entity.Datum;

public interface ArticleListCallback {
    void onArticleClicked(Datum articleEntity);
}

