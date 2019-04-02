package com.geek.newmanager.mvp.model;

import android.app.Application;

import com.geek.newmanager.app.api.AppService;
import com.geek.newmanager.mvp.contract.MainContract;
import com.geek.newmanager.mvp.model.entity.Banner;
import com.geek.newmanager.mvp.model.entity.BaseArrayResult;
import com.geek.newmanager.mvp.model.entity.BaseResult;
import com.geek.newmanager.mvp.model.entity.User;
import com.geek.newmanager.mvp.model.entity.UserInfo;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class MainModel extends BaseModel implements MainContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MainModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<List<Banner>>> findAllBannerList() {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findAllBannerList();
    }

//    @Override
//    public Observable<BaseResult<UserInfo>> findStreetById(String userId) {
//        return mRepositoryManager.obtainRetrofitService(AppService.class).findStreetById(userId);
//    }
}