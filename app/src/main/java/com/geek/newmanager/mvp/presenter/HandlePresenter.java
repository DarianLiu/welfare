package com.geek.newmanager.mvp.presenter;

import android.app.Application;

import com.geek.newmanager.app.api.RequestParamUtils;
import com.geek.newmanager.app.api.RxUtils;
import com.geek.newmanager.mvp.contract.HandleContract;
import com.geek.newmanager.mvp.model.entity.BaseArrayResult;
import com.geek.newmanager.mvp.model.entity.Case;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.RequestBody;


@ActivityScope
public class HandlePresenter extends BasePresenter<HandleContract.Model, HandleContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public HandlePresenter(HandleContract.Model model, HandleContract.View rootView) {
        super(model, rootView);
    }

    private int currPage = 1;

    public void findCaseInfoPageList(boolean isRefresh, int caseListStatus) {
        RequestBody body = RequestParamUtils.findCaseInfoPageList(isRefresh ? 1 : currPage + 1, 10, caseListStatus);
        mModel.findCaseInfoPageList(body).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (isRefresh) {
                        mRootView.finishRefresh();//隐藏刷新
                    } else {
                        mRootView.finishLoadMore();//隐藏加载更多
                    }
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .compose(RxUtils.handleBaseResult(mAppManager.getTopActivity()))
                .subscribeWith(new ErrorHandleSubscriber<BaseArrayResult<Case>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseArrayResult<Case> arrayResult) {
                        if (isRefresh) {
                            currPage = 1;
                            mRootView.refreshData(arrayResult.getRecords());
                        } else {
                            if (arrayResult.getRecords() != null && arrayResult.getRecords().size() != 0) {
                                currPage++;
                            }
                            mRootView.loadMoreData(arrayResult.getRecords());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
