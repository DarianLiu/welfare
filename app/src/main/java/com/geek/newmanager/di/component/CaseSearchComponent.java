package com.geek.newmanager.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.di.module.CaseSearchModule;

import com.geek.newmanager.mvp.ui.activity.CaseSearchActivity;

@ActivityScope
@Component(modules = CaseSearchModule.class, dependencies = AppComponent.class)
public interface CaseSearchComponent {
    void inject(CaseSearchActivity activity);
}