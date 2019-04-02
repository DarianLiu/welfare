package com.geek.newmanager.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.di.module.SocialManageModule;

import com.geek.newmanager.mvp.ui.activity.SocialManageActivity;

@ActivityScope
@Component(modules = SocialManageModule.class, dependencies = AppComponent.class)
public interface SocialManageComponent {
    void inject(SocialManageActivity activity);
}