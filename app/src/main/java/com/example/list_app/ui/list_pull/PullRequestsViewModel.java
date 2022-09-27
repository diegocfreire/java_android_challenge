package com.example.list_app.ui.list_pull;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.list_app.data.entities.Item;
import com.example.list_app.data.entities.PullRequests;
import com.example.list_app.data.network.Resource;
import com.example.list_app.data.repository.RepositoriesListRepository;
import com.example.list_app.ui.main.MainAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PullRequestsViewModel extends AndroidViewModel {

    private final RepositoriesListRepository mRepositoriesListRepository;

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableField<String> filteringText = new ObservableField<>();

    private final MutableLiveData<String> mFilterConstraint = new MutableLiveData<>();

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public final ObservableField<Resource<List<PullRequests>>> mPullRequests = new ObservableField<>();;

    private PullRequestsAdapter mAdapter;

    private final Item mItem;

    public PullRequestsViewModel(
            @NonNull Application application,
            @NonNull RepositoriesListRepository repositoriesListRepository,
            @NonNull Item item) {
        super(application);
        mRepositoriesListRepository = repositoriesListRepository;
        this.mItem = item;
        loadEquipesPublisher();
        setupObservables();
    }

    public PullRequestsAdapter getAdapter() {
        if(mAdapter == null)
            mAdapter = new PullRequestsAdapter(new ArrayList<>());
        return mAdapter;
    }

    public ObservableField<Resource<List<PullRequests>>> getItems() {
        return mPullRequests;
    }


    public void loadEquipesPublisher() {
        dataLoading.set(true);
        Disposable disposable = mRepositoriesListRepository.getPullRequestList(mItem.name,mItem.owner.login)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::sortAndMapToFlexibleItem)
                .map(Resource::success)
                .onErrorReturn(Resource::error)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mPullRequests::set);
        addDisposable(disposable);

    }

    private List<PullRequests> sortAndMapToFlexibleItem(Resource<List<PullRequests>> equipes){
        List<PullRequests> sortedList =new ArrayList<>();
        if(equipes.data!=null)
            sortedList = new ArrayList<>(equipes.data);
        return sortedList;
    }

    private void setupObservables() {
        filteringText.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setFilterConstraint(filteringText.get());
            }
        });
    }

    private void setFilterConstraint(String text) {
        mFilterConstraint.setValue(text);
    }

    public LiveData<String> getFilterConstraint() {
        return mFilterConstraint;
    }

    private void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.clear();
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final Application mApplication;

        private final RepositoriesListRepository mRepositoriesListRepository;

        private final Item mItem;

        public Factory(
                @NonNull Application application,
                @NonNull RepositoriesListRepository fundoRepository,
                @NonNull Item item
        ) {
            mApplication = application;
            mRepositoriesListRepository = fundoRepository;
            mItem = item;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new PullRequestsViewModel(mApplication, mRepositoriesListRepository,mItem);
        }
    }
}
