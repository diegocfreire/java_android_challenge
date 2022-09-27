package com.example.list_app.data.souce;

import com.example.list_app.data.entities.PullRequests;
import com.example.list_app.data.entities.RepositoriesList;
import com.example.list_app.data.network.Resource;
import com.example.list_app.data.network.service.RepositoriesListService;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class RepositoriesListRemoteDataSource extends BaseRemoteDataSource{
    private static RepositoriesListRemoteDataSource INSTANCE;

    private RepositoriesListRemoteDataSource( ) {}

    public static synchronized RepositoriesListRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RepositoriesListRemoteDataSource();
        }
        return INSTANCE;
    }

    public void clearInstance() {
        INSTANCE = null;
    }

    public Single<Resource<RepositoriesList>> getRepositoriesList(int page) {
        RepositoriesListService mainService;

        mainService = getMainService(RepositoriesListService.class,"api.github.com");

        return mainService.getRepositoriesList(page)
                .observeOn(Schedulers.computation())
                .onErrorReturn(this::wrapInErrorResponse)
                .map(this::proccessResponse);
    }

    public Single<Resource<List<PullRequests>>> getPullRequestList(String name,String login) {
        RepositoriesListService mainService;

        mainService = getMainService(RepositoriesListService.class,"api.github.com");

        return mainService.getPullRequestList(name,login)
                .observeOn(Schedulers.computation())
                .onErrorReturn(this::wrapInErrorResponse)
                .map(this::proccessResponse);
    }
}
