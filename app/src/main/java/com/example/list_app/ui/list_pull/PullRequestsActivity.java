package com.example.list_app.ui.list_pull;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.list_app.R;
import com.example.list_app.data.entities.Item;
import com.example.list_app.data.entities.PullRequests;
import com.example.list_app.data.network.Resource;
import com.example.list_app.data.network.Status;
import com.example.list_app.data.repository.RepositoriesListRepository;
import com.example.list_app.databinding.ActivityMainBinding;
import com.example.list_app.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PullRequestsActivity extends AppCompatActivity {

    private PullRequestsViewModel mViewModel;

    private PullRequestsAdapter mAdapter;

    private ActivityMainBinding mBinding;

    public static final int REQUEST_DETAIL_CODE = 8;

    public static final String EXTRA_ITEM = "EXTRA_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupAdapter();
        setLoadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getStartIntent(Context context, Item item) {
        return new Intent(context, PullRequestsActivity.class)
                .putExtra(EXTRA_ITEM, item);
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


    private PullRequestsViewModel findOrCreateViewModel() {
        Item item = (Item) getIntent().getExtras().get(EXTRA_ITEM);

        Objects.requireNonNull(getSupportActionBar()).setTitle(item.name);

        PullRequestsViewModel.Factory factory = new PullRequestsViewModel.Factory(
                getApplication(),
                RepositoriesListRepository.getInstance(),
                item
        );
        return ViewModelProviders.of(this, factory).get(PullRequestsViewModel.class);
    }

    private void onClick(PullRequests body){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(body.html_url)));
    }

    private void subscribeItems() {
        mViewModel.getItems().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Resource<List<PullRequests>> resource = mViewModel.getItems().get();
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