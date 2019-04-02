package com.geek.newmanager.di.module;

import com.geek.newmanager.mvp.contract.WelfareLegalContract;
import com.geek.newmanager.mvp.model.WelfareLegalModel;

import dagger.Binds;
import dagger.Module;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/01/2019 23:04
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class WelfareLegalModule {

    @Binds
    abstract WelfareLegalContract.Model bindWelfareLegalModel(WelfareLegalModel model);
}