package com.example.networkservicerx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.networkservicerx.networking.NetworkClient;
import com.example.networkservicerx.networking.NetworkRequestCode;
import com.example.networkservicerx.networking.NetworkServiceGenerator;
import com.example.networkservicerx.networking.dto.ExampleDTO;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.schedulers.IoScheduler;

public class MainActivity extends AppCompatActivity implements NetworkServiceGenerator.NetworkServiceReturns {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkServiceGenerator.setNetworkServiceReturns(this);
        NetworkClient networkClient = NetworkServiceGenerator.createNetworkClient(NetworkClient.class);
        Observable<List<ExampleDTO>> posts = networkClient.getPosts();
//        NetworkServiceGenerator.callNetworkService(NetworkRequestCode.REQUEST_CODE_GET, posts);
        Observable<ExampleDTO> singlePosts = networkClient.getSinglePosts();
//        NetworkServiceGenerator.callNetworkService(posts, singlePosts);

        List<Observable<?>> observables = new ArrayList<>();
        observables.add(posts.subscribeOn(new IoScheduler()).observeOn(AndroidSchedulers.mainThread()));
        observables.add(singlePosts.subscribeOn(new IoScheduler()).observeOn(AndroidSchedulers.mainThread()));
        NetworkServiceGenerator.callNetworkService(observables);


    }

    private void networkCallRxJava(NetworkClient networkClient) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(networkClient.getPosts()
                .subscribeOn(new IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ExampleDTO>>() {
                    @Override
                    public void accept(List<ExampleDTO> exampleDTOS) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    @Override
    public <T> void onNetworkResponse(NetworkRequestCode requestCode, T response) {
        switch (requestCode) {
            case REQUEST_CODE_GET:
                Log.i(TAG, (response != null ? "GET --> " + response.toString() : ""));
                break;
            case REQUEST_CODE_PUT:
                Log.i(TAG, (response != null ? "PUT --> " + response.toString() : ""));
                break;
            case REQUEST_CODE_POST:
                Log.i(TAG, (response != null ? "POST --> " + response.toString() : ""));
                break;
        }
    }
}
