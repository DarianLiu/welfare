package com.geek.newmanager.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.di.module.PhotoActivityModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.newmanager.mvp.ui.activity.PhotoActivityActivity;

@ActivityScope
@Component(modules = PhotoActivityModule.class, dependencies = AppComponent.class)
public interface PhotoActivityComponent {
    void inject(PhotoActivityActivity activity);
}