package com.kosmos.articles.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.kosmos.articles.R;

import com.kosmos.articles.databinding.ActivityMainBinding;
import com.kosmos.articles.utils.FragmentUtils;
import com.kosmos.articles.view.base.BaseActivity;
import com.kosmos.articles.view.fragment.ArticleListFragment;

import static com.kosmos.articles.utils.FragmentUtils.TRANSITION_NONE;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.replaceFragment(this, ArticleListFragment.newInstance(), R.id.fragContainer, false, TRANSITION_NONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
