package com.example.list_app.ui.main;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.list_app.data.entities.Item;
import com.example.list_app.data.repository.RepositoriesListRepository;
import com.example.list_app.data.entities.RepositoriesList;
import com.example.list_app.data.network.Resource;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public final ObservableField<Resource<List<Item>>> mValue = new ObservableField<>();

    private MainAdapter mAdapter;

    private int page = 0;

    public MainViewModel(@NonNull Application application, @NonNull RepositoriesListRepository repositoriesListRepository) {
        super(application);
        mRepositoriesListRepository = repositoriesListRepository;
        loadRepositoriesList(0);
        setupObservables();
    }

    public ObservableField<Resource<List<Item>>> getmValue() {
        return mValue;
    }

    private void loadRepositoriesList(int page) {
        this.page = page+1;
        dataLoading.set(true);
        Disposable disposable = mRepositoriesListRepository.getRepositoriesList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::mapToItemAndImage)
                .map(Resource::success)
                .onErrorReturn(Resource::error)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mValue::set);
        addDisposable(disposable);

    }

    public void newPageRepositoriesList(){
        loadRepositoriesList(page);
    }

    public void refreshRepositoriesList(){
        loadRepositoriesList(0);
    }

    public MainAdapter getAdapter() {
        if(mAdapter == null)
            mAdapter = new MainAdapter(new ArrayList<>());
        return mAdapter;
    }

    private Flowable<Resource<List<Item>>> getRepositoriesListPublisher(int page) {
        return Flowable.create(e -> {
            e.onNext(Resource.loading(null));

            Disposable disposable = mRepositoriesListRepository.getRepositoriesList(page)
                    .observeOn(Schedulers.computation())
                    .map(this::mapToItemAndImage)
                    .map(Resource::success)
                    .onErrorReturn(Resource::error)
                    .subscribe(e::onNext);
            addDisposable(disposable);
        }, BackpressureStrategy.BUFFER);
    }

    private List<Item> mapToItemAndImage(Resource<RepositoriesList> equipes){
        List<Item> sortedList =new ArrayList<>();
        if(equipes.data!=null)
            sortedList = new ArrayList<>(equipes.data.items);

        for (Item item : sortedList){
            item.bitmap = getImage(item.owner.avatar_url);
        }
        return sortedList;
    }

    private Bitmap getImage(String imageURL){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Once the executor parses the URL
        // and receives the image, handler will load it
        // in the ImageView
        Handler handler = new Handler(Looper.getMainLooper());

        // Initializing the image
        final Bitmap[] image = {null};

        InputStream in = null;
        try {
            in = new URL(imageURL).openStream();

        image[0] = BitmapFactory.decodeStream(in);

        Bitmap srcBitmap = image[0];
        // Select whichever of width or height is minimum
        int squareBitmapWidth = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());

        // Generate a bitmap with the above value as dimensions
        Bitmap dstBitmap = Bitmap.createBitmap(
                squareBitmapWidth,
                squareBitmapWidth,
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(dstBitmap);

        // initializing Paint
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Generate a square (rectangle with all sides same)
        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
        RectF rectF = new RectF(rect);

        // Operations to draw a circle
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Float left = Float.valueOf(((squareBitmapWidth - srcBitmap.getWidth()) / 2));
        Float top = Float.valueOf(((squareBitmapWidth - srcBitmap.getHeight()) / 2));
        canvas.drawBitmap(srcBitmap, left, top, paint);
        srcBitmap.recycle();

        return dstBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
