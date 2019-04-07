package com.har8yun.homeworks.projectx.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.activity.MainActivity;
import com.har8yun.homeworks.projectx.adapter.NewsAdapter;
import com.har8yun.homeworks.projectx.api.ApiClient;
import com.har8yun.homeworks.projectx.api.ApiInterface;
import com.har8yun.homeworks.projectx.model.Article;
import com.har8yun.homeworks.projectx.model.News;
import com.har8yun.homeworks.projectx.util.NewsUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.common.util.CollectionUtils.setOf;


public class NewsFragment extends Fragment {

    //static vars
    public static final String API_KEY = "7999da2a619a463aa9f484f093cbffe5";
    public static final String TAG = MainActivity.class.getSimpleName();

    private List<Article> mArticles = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;

    //views
    private Toolbar mToolbarNews;

    //navigation
    private NavController mNavController;

    //constructor
    public NewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initViews(view);

        setNewsToolbar();
        setNavigationComponent();
        initRecyclerView();

        LoadJson();

        return view;
    }

    private void initRecyclerView() {
        mNewsAdapter = new NewsAdapter(mArticles, getContext());
        mRecyclerView.setAdapter(mNewsAdapter);
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
    }


    private void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.rv_news);
        mToolbarNews = view.findViewById(R.id.toolbar_news);
    }

    private void LoadJson() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<News> call = apiInterface.getNews("gb", "sports", API_KEY);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null) {
                    if (!mArticles.isEmpty()) {
                        mArticles.clear();
                    }
                    mArticles = response.body().getArticle();
                    initRecyclerView();
                    mNewsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });
    }

    private void setNewsToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbarNews);
    }

    private void setNavigationComponent() {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(setOf(R.id.news_fragment)).build();
        NavigationUI.setupWithNavController(mToolbarNews, mNavController, appBarConfiguration);
    }


}
