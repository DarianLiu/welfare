package com.geek.newmanager.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.di.module.VideoRecordModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.newmanager.mvp.ui.activity.VideoRecordActivity;

@ActivityScope
@Component(modules = VideoRecordModule.class, dependencies = AppComponent.class)
public interface VideoRecordComponent {
    void inject(VideoRecordActivity activity);
}