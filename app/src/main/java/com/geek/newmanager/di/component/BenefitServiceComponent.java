package com.geek.newmanager.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.newmanager.di.module.BenefitServiceModule;

import com.geek.newmanager.mvp.ui.activity.BenefitServiceActivity;

@ActivityScope
@Component(modules = BenefitServiceModule.class, dependencies = AppComponent.class)
public interface BenefitServiceComponent {
    void inject(BenefitServiceActivity activity);
}