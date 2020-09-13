package com.example.rural_essential.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rural_essential.LoadingDialog;
import com.example.rural_essential.MainActivity;
import com.example.rural_essential.NewsDetailActivity;
import com.example.rural_essential.R;
import com.example.rural_essential.Utils;
import com.example.rural_essential.ui.adapters.Adapter;
import com.example.rural_essential.ui.api.ApiClient;
import com.example.rural_essential.ui.api.ApiInterface;
import com.example.rural_essential.ui.model.Article;
import com.example.rural_essential.ui.model.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    private static final String API_KEY = "67083e456bd7424b9c8279ba40a1c2ba";
    private RecyclerView recyclerView;
    //MainActivity mainActivity;
    private RecyclerView.LayoutManager layoutManager;

    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        //mainActivity = (MainActivity) getActivity();
        recyclerView = root.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        loadDialog();

        LoadJson();
        return root;
    }

    // Load Json data from API and set them into adapter
    public void LoadJson(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = Utils.getCountry();

        Call<News> call;
        call = apiInterface.getNewsSearch("accident+victoria+crash","en",API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()&& response.body().getArticle() != null){
                    if(!articles.isEmpty()){
                        articles.clear();
                    }
                    articles = response.body().getArticle();
                    adapter = new Adapter(articles,getContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    initListener();
                } else{
                    Toast.makeText(getContext(),"No result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                try{
                    Toast.makeText(getContext(),"Network failure, Please Try Again", Toast.LENGTH_SHORT).show();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
    }

    // Set listener for entering the news detail activity
    private void initListener(){
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);

                Article article = articles.get(position);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img",  article.getUrlToImage());
                intent.putExtra("date",  article.getPublishedAt());
                intent.putExtra("source",  article.getSource().getName());
                intent.putExtra("author",  article.getAuthor());

                startActivity(intent);

            }
        });
    }


    private void loadDialog(){
        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
            }
        },  3000);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

