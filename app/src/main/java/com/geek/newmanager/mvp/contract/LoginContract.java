package com.geek.newmanager.mvp.contract;

import com.geek.newmanager.mvp.model.entity.BaseResult;
import com.geek.newmanager.mvp.model.entity.User;
import com.geek.newmanager.mvp.model.entity.UserInfo;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import io.reactivex.Observable;
import retrofit2.http.Path;


public interface LoginContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<BaseResult<User>> login(String username, String password);

        Observable<BaseResult<UserInfo>> findStreetById(String userId);
    }
}
