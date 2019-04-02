package com.geek.newmanager.mvp.model;

import android.app.Application;

import com.geek.newmanager.app.api.AppService;
import com.geek.newmanager.mvp.model.entity.BaseResult;
import com.geek.newmanager.mvp.model.entity.SocialThing;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.newmanager.mvp.contract.SocialManageContract;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;


@ActivityScope
public class SocialManageModel extends BaseModel implements SocialManageContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SocialManageModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<List<SocialThing>>> findThingPositionList(int currPage, int pageSize, long assortId, long streetId, long communityId, long gridId, int thingType, String name) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findThingPositionList(currPage, pageSize, assortId, streetId, communityId, gridId, thingType, name);
    }
}