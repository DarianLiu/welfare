package com.geek.newmanager.mvp.model;

import android.app.Application;

import com.geek.newmanager.app.api.AppService;
import com.geek.newmanager.app.api.RequestParamUtils;
import com.geek.newmanager.mvp.contract.ReportContract;
import com.geek.newmanager.mvp.model.entity.BaseResult;
import com.geek.newmanager.mvp.model.entity.CaseInfo;
import com.geek.newmanager.mvp.model.entity.CaseAttribute;
import com.geek.newmanager.mvp.model.entity.Grid;
import com.geek.newmanager.mvp.model.entity.Street;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.RequestBody;


@ActivityScope
public class ReportModel extends BaseModel implements ReportContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ReportModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<List<CaseAttribute>>> findCaseCategoryListByAttribute(String caseCategory) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findCaseCategoryListByAttribute(caseCategory);
    }

    @Override
    public Observable<BaseResult<List<Street>>> findAllStreetCommunity() {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findAllStreetCommunity();
    }

    @Override
    public Observable<BaseResult<List<Grid>>> findGridListByCommunityId(String communityId) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findGridListByCommunityId(communityId);
    }

    @Override
    public Observable<BaseResult<CaseInfo>> addOrUpdateCaseInfo(String acceptDate, String streetId, String communityId,
                                                                String gridId, String lat, String lng, String source,
                                                                String address, String description, String caseAttribute,
                                                                String casePrimaryCategory, String caseSecondaryCategory,
                                                                String caseChildCategory, String handleType, String whenType,
                                                                String caseProcessRecordID) {
        RequestBody requestBody = RequestParamUtils.addOrUpdateCaseInfo(acceptDate, streetId,
                communityId, gridId, lat, lng, source, address, description, caseAttribute,
                casePrimaryCategory, caseSecondaryCategory, caseChildCategory, handleType, whenType, caseProcessRecordID);
        return mRepositoryManager.obtainRetrofitService(AppService.class).addOrUpdateCaseInfo(requestBody);
    }
}