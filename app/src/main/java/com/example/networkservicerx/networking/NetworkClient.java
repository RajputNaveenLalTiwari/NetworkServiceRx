package com.example.networkservicerx.networking;

import com.example.networkservicerx.networking.dto.ExampleDTO;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetworkClient {
    @GET("posts")
    Observable<List<ExampleDTO>> getPosts();

    @GET("posts/1")
    Observable<ExampleDTO> getSinglePosts();

    @GET("posts/{id}")
    Observable<ExampleDTO> getSinglePosts(@Path("id") String id);
}
