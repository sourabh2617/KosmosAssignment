package com.kosmos.articles.view.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kosmos.articles.R;
import com.kosmos.articles.common.Constants;
import com.kosmos.articles.data.local.entity.Datum;
import com.kosmos.articles.data.remote.Status;
import com.kosmos.articles.data.remote.model.UserData;
import com.kosmos.articles.databinding.FragmentListArticlesBinding;
import com.kosmos.articles.utils.FragmentUtils;
import com.kosmos.articles.view.adapter.ArticleListAdapter;
import com.kosmos.articles.view.base.BaseFragment;
import com.kosmos.articles.view.callbacks.ArticleListCallback;
import com.kosmos.articles.viewmodel.ArticleListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArticleListFragment extends BaseFragment<ArticleListViewModel, FragmentListArticlesBinding> implements ArticleListCallback{


    public static ArticleListFragment newInstance() {
        Bundle args = new Bundle();
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Class<ArticleListViewModel> getViewModel() {
        return ArticleListViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_list_articles;
    }

    @Override
    public void onArticleClicked(Datum articleEntity) {
        if(null != getActivity()){
            Bundle args = new Bundle();
            args.putString(Constants.BUNDLE_KEY_ARTICLE_URL, articleEntity.getEmail());
            args.putString(Constants.BUNDLE_KEY_ARTICLE_PUBLISHED_DATE, articleEntity.getFirstName());
            ArticleDetailFragment detailFragment = new ArticleDetailFragment();
            detailFragment.setArguments(args);
            FragmentUtils.replaceFragment((AppCompatActivity) getActivity(), detailFragment, R.id.fragContainer, true, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataBinding.recyclerView.setAdapter(new ArticleListAdapter(this));
        dataBinding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getActivity(), "hello");
            }
        });
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getPopularArticles()
                .observe(this, listResource -> {
                    if(null != listResource && (listResource.status == Status.ERROR || listResource.status == Status.SUCCESS)){
                        dataBinding.loginProgress.setVisibility(View.GONE);
                    }
                    dataBinding.setResource(listResource);
                    // If the cached data is already showing then no need to show the error
                    if(null != dataBinding.recyclerView.getAdapter() && dataBinding.recyclerView.getAdapter().getItemCount() > 0){
                        dataBinding.errorText.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(null == getActivity())
            return;

        SearchView searchView;
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if(null != dataBinding.recyclerView.getAdapter())
                    ((ArticleListAdapter)dataBinding.recyclerView.getAdapter()).getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if(null != dataBinding.recyclerView.getAdapter())
                    ((ArticleListAdapter)dataBinding.recyclerView.getAdapter()).getFilter().filter(query);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_no);
        EditText firstET = dialog.findViewById(R.id.firstET);
        EditText lastET = dialog.findViewById(R.id.lastET);
        EditText emailET= dialog.findViewById(R.id.emailET);

        Button yesButton = (Button) dialog.findViewById(R.id.btn_yes);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UserData hello = new UserData();
                Datum datum = new Datum();
                datum.setFirstName(firstET.getText().toString());
                datum.setLastName(lastET.getText().toString());
                datum.setEmail(emailET.getText().toString());

                List<Datum> list = new ArrayList<>();
                list.add(datum);
                hello.setData(list);

                viewModel.saveData(hello);
                Objects.requireNonNull(dataBinding.recyclerView.getAdapter()).notifyDataSetChanged();

            }
        });
        dialog.show();
    }

    private static final int SELECT_PICTURE = 1;
    private void getImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }
    Uri selectedImageUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                 selectedImageUri = data.getData();

            }
        }
    }



}