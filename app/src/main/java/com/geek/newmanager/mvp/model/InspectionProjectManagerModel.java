package com.geek.newmanager.mvp.model;

import android.app.Application;

import com.geek.newmanager.app.api.AppService;
import com.geek.newmanager.mvp.model.entity.Banner;
import com.geek.newmanager.mvp.model.entity.BaseArrayResult;
import com.geek.newmanager.mvp.model.entity.BaseResult;
import com.geek.newmanager.mvp.model.entity.Thing;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.newmanager.mvp.contract.InspectionProjectManagerContract;

import java.util.List;

import io.reactivex.Observable;


@ActivityScope
public class InspectionProjectManagerModel extends BaseModel implements InspectionProjectManagerContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public InspectionProjectManagerModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<BaseArrayResult<Thing>>> findAllThingList(int currPage,int pageSize) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findAllThingList(currPage, pageSize);
    }

    @Override
    public Observable<BaseResult<Thing>> delThings(String thingIds) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).delThings(thingIds);
    }
}