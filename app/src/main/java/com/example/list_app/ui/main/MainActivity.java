package com.example.list_app.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.list_app.R;
import com.example.list_app.data.entities.Item;
import com.example.list_app.data.network.Status;
import com.example.list_app.data.repository.RepositoriesListRepository;
import com.example.list_app.databinding.ActivityMainBinding;
import com.example.list_app.utils.DialogUtils;
import com.example.list_app.utils.ObjectUtils;

import java.util.ArrayList;


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
                if(mViewModel.dataLoading.get())
                    mBinding.listServicosLoading.setVisibility(View.VISIBLE);
                else
                    mBinding.listServicosLoading.setVisibility(View.GONE);
            }
        });
    }

    private void setupAdapter() {
        mAdapter = new MainAdapter(new ArrayList<>());

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

    }

    private void subscribeItems() {
        mViewModel.getItems().observe(this, resource -> {
            mViewModel.dataLoading.set(resource.status == Status.LOADING);

            if (resource.status == Status.SUCCESS) {
                mAdapter.updateDataSet(resource.data, true);
            } else if (resource.status == Status.ERROR) {
                DialogUtils.showDialog(this, resource.message.header, resource.message.body);
            }
        });
    }
}