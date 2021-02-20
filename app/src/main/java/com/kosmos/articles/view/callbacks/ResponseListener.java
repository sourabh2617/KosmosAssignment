package com.kosmos.articles.view.callbacks;

import com.kosmos.articles.data.local.entity.Datum;

public interface ResponseListener {

    void onSuccess(Datum data);
    void onFailure(String message);
}
