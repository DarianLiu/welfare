package com.geek.newmanager.di.component;

import com.geek.newmanager.di.module.ChangePasswordModule;
import com.geek.newmanager.mvp.ui.activity.ChangePasswordActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = ChangePasswordModule.class, dependencies = AppComponent.class)
public interface ChangePasswordComponent {
    void inject(ChangePasswordActivity activity);
}