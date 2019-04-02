package com.geek.newmanager.di.component;

import com.geek.newmanager.di.module.WordGuildModule;
import com.geek.newmanager.mvp.ui.activity.WordGuildActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = WordGuildModule.class, dependencies = AppComponent.class)
public interface WordGuildComponent {
    void inject(WordGuildActivity activity);
}