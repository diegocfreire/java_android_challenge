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
import com.example.list_app.data.entities.RepositoriesList;
import com.example.list_app.data.network.Resource;
import com.example.list_app.data.repository.RepositoriesListRepository;

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

    private LiveData<Resource<List<PullRequests>>> mPullRequests;

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

    public LiveData<Resource<List<PullRequests>>> getItems() {
        return mPullRequests;
    }

    private void loadEquipesPublisher() {
        mPullRequests = LiveDataReactiveStreams.fromPublisher(getEquipesPublisher()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()));
    }

    private Flowable<Resource<List<PullRequests>>> getEquipesPublisher() {
        return Flowable.create(e -> {
            e.onNext(Resource.loading(null));

            Disposable disposable = mRepositoriesListRepository.getPullRequestList(mItem.name,mItem.owner.login)
                    .observeOn(Schedulers.computation())
                    .map(this::sortAndMapToFlexibleItem)
                    .map(Resource::success)
                    .onErrorReturn(Resource::error)
                    .subscribe(e::onNext);
            addDisposable(disposable);
        }, BackpressureStrategy.BUFFER);
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
