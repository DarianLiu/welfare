package com.geek.newmanager.mvp.model;

import android.app.Application;

import com.geek.newmanager.app.api.AppService;
import com.geek.newmanager.mvp.model.entity.BaseArrayResult;
import com.geek.newmanager.mvp.model.entity.BaseResult;
import com.geek.newmanager.mvp.model.entity.ServiceBean;
import com.geek.newmanager.mvp.model.entity.Thing;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.newmanager.mvp.contract.BenefitServiceContract;

import io.reactivex.Observable;
import okhttp3.RequestBody;


@ActivityScope
public class BenefitServiceModel extends BaseModel implements BenefitServiceContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public BenefitServiceModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<BaseArrayResult<ServiceBean>>> findCmsArticlePage(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findCmsArticlePage(body);
    }
}