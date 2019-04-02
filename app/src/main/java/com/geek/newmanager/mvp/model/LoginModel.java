package com.geek.newmanager.mvp.model;

import android.app.Application;

import com.geek.newmanager.app.api.AppService;
import com.geek.newmanager.mvp.model.entity.BaseResult;
import com.geek.newmanager.mvp.model.entity.User;
import com.geek.newmanager.mvp.model.entity.UserInfo;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.newmanager.mvp.contract.LoginContract;

import io.reactivex.Observable;


@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<User>> login(String username, String password) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).login(username, password);
    }

    @Override
    public Observable<BaseResult<UserInfo>> findStreetById(String userId) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findStreetById(userId);
    }
}