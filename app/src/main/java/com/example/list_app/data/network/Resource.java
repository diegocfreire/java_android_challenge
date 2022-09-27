package com.example.list_app.data.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Resource<T> {

    @SerializedName("State")
    @NonNull
    public final Status status;

    @SerializedName("Data")
    @Nullable
    public final T data;

    @SerializedName("Message")
    @NonNull
    public final Message message;

    private Resource(@NonNull Status status, @Nullable T data, @NonNull Message message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data){
        return new Resource<>(Status.SUCCESS, data, Message.empty());
    }

    public static <T> Resource<T> error(Message message, @Nullable T data){
        return new Resource<>(Status.ERROR, data, message);
    }

    public static <T> Resource<T> error(Throwable throwable){
        return new Resource<>(Status.ERROR, null, Message.error(throwable.getMessage()));
    }

    public static <T> Resource<T> unexpected(){
        return new Resource<>(Status.ERROR, null, Message.error("Ocorreu um erro inesperado."));
    }

    public static <T> Resource<T> loading(@Nullable T data){
        return new Resource<>(Status.LOADING, data, Message.empty());
    }

    public static <T, R> Resource<R> refresh(@NonNull Resource<T> resourceOld, @NonNull R data){
        switch (resourceOld.status){
            case LOADING: return Resource.loading(data);
            case ERROR: return Resource.error(resourceOld.message, data);
            case SUCCESS:
            default: return Resource.success(data);
        }
    }
}
