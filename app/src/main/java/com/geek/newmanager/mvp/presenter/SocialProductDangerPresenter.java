package com.geek.newmanager.mvp.presenter;

import android.app.Application;

import com.geek.newmanager.app.api.RxUtils;
import com.geek.newmanager.mvp.contract.SocialProductDangerContract;
import com.geek.newmanager.mvp.model.entity.UploadFile;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


@ActivityScope
public class SocialProductDangerPresenter extends BasePresenter<SocialProductDangerContract.Model, SocialProductDangerContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SocialProductDangerPresenter(SocialProductDangerContract.Model model, SocialProductDangerContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 上传图片 单张图片
     */
    public void uploadFile(String filePath) {
//        File file = new File(pathUrl);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
        File file = new File(filePath);//filePath 图片地址
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("fileName", file.getPath());//
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);//imgfile 后台接收图片流的参数名

        List<MultipartBody.Part> parts = builder.build().parts();

        mModel.uploadFile(parts).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResultResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<UploadFile>(mErrorHandler) {
                    @Override
                    public void onNext(UploadFile uploadPhoto) {
                        mRootView.uploadSuccess(uploadPhoto);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }
}
