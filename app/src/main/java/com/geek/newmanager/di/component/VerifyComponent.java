package com.geek.newmanager.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.di.module.VerifyModule;

import com.geek.newmanager.mvp.ui.activity.VerifyActivity;

@ActivityScope
@Component(modules = VerifyModule.class, dependencies = AppComponent.class)
public interface VerifyComponent {
    void inject(VerifyActivity activity);
}