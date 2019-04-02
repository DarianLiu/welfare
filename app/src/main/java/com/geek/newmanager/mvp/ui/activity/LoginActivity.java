package com.geek.newmanager.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.newmanager.R;
import com.geek.newmanager.di.component.DaggerLoginComponent;
import com.geek.newmanager.di.module.LoginModule;
import com.geek.newmanager.mvp.contract.LoginContract;
import com.geek.newmanager.mvp.presenter.LoginPresenter;
import com.geek.newmanager.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    private LoadingProgressDialog loadingDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        int width = (int) DeviceUtils.getScreenWidth(this);
        int height = width * 1280 / 1080 + 1;
        ivLogo.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        etUsername.setText("nsc_wgy1");
        etPassword.setText("111111");

        tvLogin.setOnClickListener(v -> {
            String userName = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            if (checkLogin(userName, password) && mPresenter != null) {
                mPresenter.login(userName, password);
            }
        });
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null)
            loadingDialog = new LoadingProgressDialog.Builder(this)
                    .setCancelable(true)
                    .setCancelOutside(true).create();
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    private boolean checkLogin(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            showMessage("用户名/账号不能为空");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            showMessage("密码不能为空");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

}
