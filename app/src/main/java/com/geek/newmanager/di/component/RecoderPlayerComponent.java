package com.geek.newmanager.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.di.module.RecoderPlayerModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.newmanager.mvp.ui.activity.RecoderPlayerActivity;

@ActivityScope
@Component(modules = RecoderPlayerModule.class, dependencies = AppComponent.class)
public interface RecoderPlayerComponent {
    void inject(RecoderPlayerActivity activity);
}