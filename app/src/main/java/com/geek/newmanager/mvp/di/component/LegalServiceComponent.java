package com.geek.newmanager.mvp.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.mvp.di.module.LegalServiceModule;
import com.geek.newmanager.mvp.contract.LegalServiceContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.newmanager.mvp.ui.activity.LegalServiceActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/01/2019 22:24
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = LegalServiceModule.class, dependencies = AppComponent.class)
public interface LegalServiceComponent {
    void inject(LegalServiceActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LegalServiceComponent.Builder view(LegalServiceContract.View view);

        LegalServiceComponent.Builder appComponent(AppComponent appComponent);

        LegalServiceComponent build();
    }
}