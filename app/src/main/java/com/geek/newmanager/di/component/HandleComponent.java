package com.geek.newmanager.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.di.module.HandleModule;

import com.geek.newmanager.mvp.ui.activity.HandleActivity;

@ActivityScope
@Component(modules = HandleModule.class, dependencies = AppComponent.class)
public interface HandleComponent {
    void inject(HandleActivity activity);
}