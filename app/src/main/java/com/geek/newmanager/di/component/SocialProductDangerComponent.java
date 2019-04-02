package com.geek.newmanager.di.component;

import com.geek.newmanager.di.module.SocialProductDangerModule;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.mvp.ui.activity.SocialProductDangerActivity;

@ActivityScope
@Component(modules = SocialProductDangerModule.class, dependencies = AppComponent.class)
public interface SocialProductDangerComponent {
    void inject(SocialProductDangerActivity activity);
}