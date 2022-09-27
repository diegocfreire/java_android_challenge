package com.example.list_app.data.network.service;

import com.example.list_app.data.entities.PullRequests;
import com.example.list_app.data.entities.RepositoriesList;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RepositoriesListService {

    @GET("search/repositories?q=language:Java&sort=stars")
    Single<Response<RepositoriesList>> getRepositoriesList(@Query("page") int page);

    @GET("repos/{login}/{name}/pulls")
    Single<Response<List<PullRequests>>> getPullRequestList(
            @Path("name") String name,
            @Path("login") String login);

}
