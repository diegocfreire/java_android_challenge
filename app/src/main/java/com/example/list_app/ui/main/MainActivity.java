package com.example.list_app.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

        mBinding.fundoList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.fundoList.setAdapter(mAdapter);

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
        mViewModel.getmValue().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Resource<List<Item>> resource = mViewModel.getmValue().get();
                mViewModel.dataLoading.set(resource.status == Status.LOADING);

                if (resource.status == Status.SUCCESS) {
                    mAdapter.updateDataSet(resource.data, true);
                } else if (resource.status == Status.ERROR) {
                    DialogUtils.showDialog(getApplicationContext(), resource.message.header, resource.message.body);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVibilidadeLoad();
    }
}