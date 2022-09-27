package com.example.list_app.data.souce;

import androidx.annotation.NonNull;

import com.example.list_app.data.network.*;
import com.example.list_app.data.utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

public abstract class BaseRemoteDataSource {

    public static final String DEFAULT_PROTOCOL = "http";


    private final Gson mGson;

    protected BaseRemoteDataSource() {
        mGson = ServiceGenerator.getGsonInstance();
    }

    public <S> S getService(
            @NonNull Class<S> service, @NonNull String baseUrl
    ){
        return getService(service, DEFAULT_PROTOCOL, baseUrl);
    }

    public <S> S getService(
            @NonNull Class<S> service, @NonNull String protocol, @NonNull String baseUrl
    ) throws IllegalArgumentException{
        try {

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host(baseUrl)
                    .scheme(protocol)
                    .build();
            return ServiceGenerator.createService(service, url);
        }catch (Exception e){
            throw new IllegalArgumentException("O endereço do servidor é inválido.");
        }
    }

    public <S> S getMainService(@NonNull Class<S> service,String  address  ){
        return getService(service, address);
    }
    public <ResponseType> Response<ResponseType> wrapInErrorResponse(Throwable throwable){
        if(throwable instanceof SocketTimeoutException){
            return Response.error(NetworkUtils.REQUEST_TIMEOUT, getErrorResponseBody());
        }

        if(throwable instanceof IOException){
            return Response.error(NetworkUtils.CONNECTION_FAILED, getErrorResponseBody());
        }

        return Response.error(NetworkUtils.UNEXPECTED_ERROR, getErrorResponseBody());
    }

    private ResponseBody getErrorResponseBody(){
        return ResponseBody.create(MediaType.parse("application/json"), "");
    }

    public <DataType> Resource<DataType> proccessResponse(Response<DataType> response){
        if(response.isSuccessful()){
            return Resource.success(response.body());
        }else {
            return Resource.error(NetworkUtils.getErrorMessageByCode(response.code()),null);
        }
    }


    protected <T> String toJson(T obj){
        return mGson.toJson(obj);
    }
}
