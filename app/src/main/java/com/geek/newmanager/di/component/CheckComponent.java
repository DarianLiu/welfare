package com.geek.newmanager.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.di.module.CheckModule;

import com.geek.newmanager.mvp.ui.activity.CheckActivity;

@ActivityScope
@Component(modules = CheckModule.class, dependencies = AppComponent.class)
public interface CheckComponent {
    void inject(CheckActivity activity);
}