package com.geek.newmanager.di.component;

import com.geek.newmanager.di.module.InspectionProjectRegisterModule;
import com.geek.newmanager.mvp.ui.activity.InspectionProjectRegisterActivity;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

@ActivityScope
@Component(modules = InspectionProjectRegisterModule.class, dependencies = AppComponent.class)
public interface InspectionProjectRegisterComponent {
    void inject(InspectionProjectRegisterActivity activity);
}