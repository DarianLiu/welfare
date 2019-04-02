package com.geek.newmanager.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.geek.newmanager.app.Constant;
import com.geek.newmanager.app.api.RxUtils;
import com.geek.newmanager.mvp.model.entity.Banner;
import com.geek.newmanager.mvp.model.entity.BaseArrayResult;
import com.geek.newmanager.mvp.model.entity.User;
import com.geek.newmanager.mvp.model.entity.UserInfo;
import com.geek.newmanager.mvp.ui.activity.LoginActivity;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.geek.newmanager.mvp.contract.MainContract;
import com.jess.arms.utils.DataHelper;

import java.util.List;


@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView) {
        super(model, rootView);
    }

//    private String getUserId() {
//        return DataHelper.getStringSF(mApplication, Constant.SP_KEY_USER_ID);
//    }

    public void findAllBannerList() {
        mModel.findAllBannerList()
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<Banner>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Banner> bannerList) {
                        mRootView.setAutoBanner(bannerList);
                    }
                });
    }

//    /**
//     * 根取当前登录用户，所属街道、社区、网格
//     */
//    public void findStreetById() {
//        if (TextUtils.isEmpty(getUserId())) {
//            mRootView.launchActivity(new Intent(mApplication, LoginActivity.class));
//            return;
//        }
//        mModel.findStreetById(getUserId()).compose(RxUtils.applySchedulers(mRootView))
//                .compose(RxUtils.handleBaseResult(mApplication))
//                .subscribeWith(new ErrorHandleSubscriber<UserInfo>(mErrorHandler) {
//                    @Override
//                    public void onNext(UserInfo user) {
//                        mRootView.updateUserInfo();
//                    }
//                });
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
