package com.example.list_app.data.network.service;

import com.example.list_app.data.entities.RepositoriesList;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface RepositoriesListService {

    @GET("search/repositories?q=language:Java&sort=stars&page=1")
    Single<Response<RepositoriesList>> getRepositoriesList();
}
