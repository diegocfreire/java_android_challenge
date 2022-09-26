package com.example.list_app.ui.main;

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
import com.example.list_app.data.repository.RepositoriesListRepository;
import com.example.list_app.data.entities.RepositoriesList;
import com.example.list_app.data.network.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private final RepositoriesListRepository mRepositoriesListRepository;

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableField<String> filteringText = new ObservableField<>();

    private final MutableLiveData<String> mFilterConstraint = new MutableLiveData<>();

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private LiveData<Resource<List<Item>>> mItems;

    public MainViewModel(@NonNull Application application, @NonNull RepositoriesListRepository repositoriesListRepository) {
        super(application);
        mRepositoriesListRepository = repositoriesListRepository;
        loadFundos();
        setupObservables();
    }

    public LiveData<Resource<List<Item>>> getItems() {
        return mItems;
    }

    private void loadFundos() {
        mItems = LiveDataReactiveStreams.fromPublisher(getEquipesPublisher()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()));
    }

    private Flowable<Resource<List<Item>>> getEquipesPublisher() {
        return Flowable.create(e -> {
            e.onNext(Resource.loading(null));

            Disposable disposable = mRepositoriesListRepository.getRepositoriesList()
                    .observeOn(Schedulers.computation())
                    .map(this::sortAndMapToFlexibleItem)
                    .map(Resource::success)
                    .onErrorReturn(Resource::error)
                    .subscribe(e::onNext);
            addDisposable(disposable);
        }, BackpressureStrategy.BUFFER);
    }
    private List<Item> sortAndMapToFlexibleItem(Resource<RepositoriesList> equipes){
        List<Item> sortedList =new ArrayList<>();
        if(equipes.data!=null)
            sortedList = new ArrayList<>(equipes.data.items);
       // Collections.sort(sortedList, this::sortByEquipeNameAsc);
        return sortedList;//Lists.transform(sortedList, FundoViewItem::new);
    }

  //  private int sortByEquipeNameAsc(Item e1, Item e2) {
    //    return e1.().compareTo( e2.getNomeCompleto());
  //  }

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

        public Factory(
                @NonNull Application application,
                @NonNull RepositoriesListRepository fundoRepository
        ) {
            mApplication = application;
            mRepositoriesListRepository = fundoRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MainViewModel(mApplication, mRepositoriesListRepository);
        }
    }
}
