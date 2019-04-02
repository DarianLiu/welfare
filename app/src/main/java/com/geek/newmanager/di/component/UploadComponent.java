package com.geek.newmanager.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.di.module.UploadModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.newmanager.mvp.ui.activity.UploadActivity;

@ActivityScope
@Component(modules = UploadModule.class, dependencies = AppComponent.class)
public interface UploadComponent {
    void inject(UploadActivity activity);
}