package com.example.list_app.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import com.example.list_app.R;
import com.example.list_app.data.entities.Item;
import com.example.list_app.data.network.Resource;
import com.example.list_app.data.network.Status;
import com.example.list_app.data.repository.RepositoriesListRepository;
import com.example.list_app.databinding.ActivityMainBinding;
import com.example.list_app.ui.list_pull.PullRequestsActivity;
import com.example.list_app.utils.DialogUtils;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;

    private MainAdapter mAdapter;

    private ActivityMainBinding mBinding;

    private boolean isScrollig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupAdapter();
        setLoadData();
    }

    private void setLoadData() {
        mViewModel.dataLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setVibilidadeLoad();
            }
        });
    }

    private void setVibilidadeLoad(){
        if(mViewModel.dataLoading.get())
            mBinding.listServicosLoading.setVisibility(View.VISIBLE);
        else
            mBinding.listServicosLoading.setVisibility(View.GONE);
    }

    private void setupAdapter() {
        mAdapter = mViewModel.getAdapter();

        mAdapter.dataOnClick.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                onClick(mAdapter.dataOnClick.get());
            }
        });

        LinearLayoutManager menager = new LinearLayoutManager(this);
        mBinding.fundoList.setLayoutManager(menager);
        mBinding.fundoList.setAdapter(mAdapter);

        mBinding.fundoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrollig = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentItems = menager.getChildCount();
                int totalItems = menager.getItemCount();
                int scrollOutItems = menager.findFirstVisibleItemPosition();

                if (isScrollig && (currentItems+scrollOutItems == totalItems)){
                    mViewModel.newPageRepositoriesList();
                }
            }
        });

        subscribeItems();
    }


    private MainViewModel findOrCreateViewModel() {
        MainViewModel.Factory factory = new MainViewModel.Factory(
                getApplication(),
                RepositoriesListRepository.getInstance()
        );
        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    private void onClick(Item item){
        Intent it = PullRequestsActivity.getStartIntent(this, item);
        startActivityForResult(it, PullRequestsActivity.REQUEST_DETAIL_CODE);
    }

    private void subscribeItems() {
        mViewModel.getItems().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Resource<List<Item>> resource = mViewModel.getItems().get();
                assert resource != null;
                mViewModel.dataLoading.set(resource.status == Status.LOADING);

                if (resource.status == Status.SUCCESS) {
                    mAdapter.updateDataSet(resource.data, true);
                } else if (resource.status == Status.ERROR) {
                    DialogUtils.showDialog(getApplicationContext(), resource.message.header, resource.message.body);
                }

                if (mAdapter.getItemCount()==0 && resource.status != Status.LOADING)
                    mBinding.emptyList.setVisibility(View.VISIBLE);
                else
                    mBinding.emptyList.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVibilidadeLoad();
    }
}