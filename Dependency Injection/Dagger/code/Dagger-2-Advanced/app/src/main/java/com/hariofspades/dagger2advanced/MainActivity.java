package com.hariofspades.dagger2advanced;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.hariofspades.dagger2advanced.adapter.RandomUserAdapter;
import com.hariofspades.dagger2advanced.di.component.DaggerMainActivityComponent;
import com.hariofspades.dagger2advanced.di.component.MainActivityComponent;
import com.hariofspades.dagger2advanced.di.module.MainActivityModule;
import com.hariofspades.dagger2advanced.interfaces.RandomUsersApi;
import com.hariofspades.dagger2advanced.model.RandomUsers;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Inject
    RandomUserAdapter mAdapter;

    @Inject
    RandomUsersApi randomUsersApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        MainActivityComponent mainActivityComponent = DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .randomUserComponent(RandomUserApplication.get(this).getRandomUserApplicationComponent())
                .build();

        mainActivityComponent.injectMainActivity(this);

        populateUsers();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void populateUsers() {
        Call<RandomUsers> randomUsersCall = randomUsersApi.getRandomUsers(10);
        randomUsersCall.enqueue(new Callback<RandomUsers>() {
            @Override
            public void onResponse(@NonNull Call<RandomUsers> call, @NonNull Response<RandomUsers> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    mAdapter.setItems(response.body().getResults());
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RandomUsers> call, @NonNull Throwable t) {
                Log.d("test", t.getMessage());
                Timber.i(t.getMessage());
            }
        });
    }

}
