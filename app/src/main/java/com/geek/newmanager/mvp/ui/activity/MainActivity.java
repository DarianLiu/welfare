package com.geek.newmanager.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.geek.newmanager.R;
import com.geek.newmanager.app.Constant;
import com.geek.newmanager.app.EventBusTags;
import com.geek.newmanager.di.component.DaggerMainComponent;
import com.geek.newmanager.di.module.MainModule;
import com.geek.newmanager.mvp.contract.MainContract;
import com.geek.newmanager.mvp.model.entity.Banner;
import com.geek.newmanager.mvp.model.entity.UserInfo;
import com.geek.newmanager.mvp.presenter.MainPresenter;
import com.geek.newmanager.mvp.ui.fragment.BenefitServiceFragment;
import com.geek.newmanager.mvp.ui.fragment.BusinessEnvironmentFragment;
import com.geek.newmanager.mvp.ui.fragment.CaseManagerFragment;
import com.geek.newmanager.mvp.ui.fragment.MyMessageFragment;
import com.geek.newmanager.mvp.ui.fragment.SocialManageFragment;
import com.geek.newmanager.view.FragmentTabHost;
import com.geek.newmanager.view.autoviewpager.AutoScrollViewPager;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(android.R.id.tabs)
    TabWidget tabs;
    @BindView(android.R.id.tabhost)
    FragmentTabHost tabHost;
    @BindView(R.id.auto_scroll)
    AutoScrollViewPager autoScroll;

    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_grid_name)
    TextView tvGridName;


    @BindString(R.string.tab_case)
    String str_tab_case;
    @BindString(R.string.tab_social)
    String str_tab_social;
    @BindString(R.string.tab_business)
    String str_tab_business;
    @BindString(R.string.tab_service)
    String str_tab_service;
    @BindString(R.string.tab_message)
    String str_tab_message;

    @BindDrawable(R.drawable.selector_tab_case)
    Drawable selector_tab_case;
    @BindDrawable(R.drawable.selector_tab_social)
    Drawable selector_tab_social;
    @BindDrawable(R.drawable.selector_tab_mine)
    Drawable selector_tab_mine;
    @BindDrawable(R.drawable.selector_tab_service)
    Drawable selector_tab_service;
    @BindDrawable(R.drawable.selector_tab_business)
    Drawable selector_tab_business;

    @Inject
    ImageLoader mImageLoader;

    //    //轮播图低部滑动图片红点
//    private ArrayList<ImageView> mScrollImageViews = new ArrayList<>();
    //轮播图图片
    private List<Banner> mBannerList = new ArrayList<>();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText(R.string.app_name);
        UserInfo userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        if (userInfo == null) {
            launchActivity(new Intent(this, LoginActivity.class));
        } else {
            updateUserInfo(userInfo);
        }
        initTabHost();
        if (mPresenter != null) {
            mPresenter.findAllBannerList();
//            mPresenter.findStreetById();
        }

        setBannerHeight();
    }

    /**
     * 设置banner控件的高度
     */
    private void setBannerHeight() {
        int screenWidth = ArmsUtils.getScreenWidth(getApplicationContext());
        int height = screenWidth / 5 * 2;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, height);
        autoScroll.setLayoutParams(params);
    }

    /**
     * 初始化FragmentTabHost(底部菜单点击事件处理)
     */
    public void initTabHost() {
        tabHost.setup(MainActivity.this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.getTabWidget().setDividerDrawable(null); // 去掉分割线


        for (int i = 0; i < 5; i++) {
            TabHost.TabSpec tabSpec;
            switch (i) {
                case 0:
                    tabSpec = tabHost.newTabSpec(str_tab_case)
                            .setIndicator(getTabView(str_tab_case, selector_tab_case));
                    tabHost.addTab(tabSpec, CaseManagerFragment.class, null);
                    break;
                case 1:
                    tabSpec = tabHost.newTabSpec(str_tab_social)
                            .setIndicator(getTabView(str_tab_social, selector_tab_social));
                    tabHost.addTab(tabSpec, SocialManageFragment.class, null);
                    break;
                case 2:
                    tabSpec = tabHost.newTabSpec(str_tab_business)
                            .setIndicator(getTabView(str_tab_business, selector_tab_business));
                    tabHost.addTab(tabSpec, BusinessEnvironmentFragment.class, null);
                    break;
                case 3:
                    tabSpec = tabHost.newTabSpec(str_tab_service)
                            .setIndicator(getTabView(str_tab_service, selector_tab_service));
                    tabHost.addTab(tabSpec, BenefitServiceFragment.class, null);
                    break;
                case 4:
                    tabSpec = tabHost.newTabSpec(str_tab_message)
                            .setIndicator(getTabView(str_tab_message, selector_tab_mine));
                    tabHost.addTab(tabSpec, MyMessageFragment.class, null);
                    break;

            }
        }
    }


    /**
     * 获取当前tab视图
     */
    private View getTabView(String tabName, Drawable tabResource) {
        View view = getLayoutInflater().inflate(R.layout.view_tab, null);
        ImageView imageView = view.findViewById(R.id.iv_menu);
        TextView textView = view.findViewById(R.id.tv_menu);
        imageView.setImageDrawable(tabResource);
        textView.setText(tabName);
        return view;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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

    /**
     * 初始化轮播图控件
     */
    private void initAutoScrollViewPager() {
        autoScroll.setAdapter(mPagerAdapter);

        autoScroll.setScrollFactgor(10); // 控制滑动速度
        autoScroll.setOffscreenPageLimit(5); //设置缓存屏数
        autoScroll.startAutoScroll(3000);  //设置间隔时间

        autoScroll.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
//                showSelectScrollImage(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * 设置轮播图数据
     *
     * @param banners 轮播图列表
     */
    @Override
    public void setAutoBanner(List<Banner> banners) {
        mBannerList = banners;
        initAutoScrollViewPager();
    }

    @Subscriber(tag = EventBusTags.TAG_LOGIN_STATE)
    public void receiveLoginState(UserInfo userInfo) {
        if (userInfo != null)
            updateUserInfo(userInfo);
    }

    /**
     * 更新用户网格信息
     *
     * @param userInfo 用户网格信息
     */
    public void updateUserInfo(UserInfo userInfo) {
        tvUserName.setText(String.format("登录名称：%s", userInfo.getUsername()));
        tvGridName.setText(String.format("网格名称：%s", userInfo.getGridName()));
    }

//    /**
//     * 当前滑动的轮播图对应底部的标识
//     *
//     * @param position 当前位置
//     */
//    private void showSelectScrollImage(int position) {
//        if (position < 0 || position >= mScrollImageViews.size()) return;
//        if (mScrollImageViews != null) {
//            for (ImageView iv : mScrollImageViews) {
//                iv.setImageResource(R.drawable.icon_indicator_normal);
//            }
//            mScrollImageViews.get(position).setImageResource(R.drawable.icon_indicator_selected);
//        }
//    }

//    /**
//     * 轮播图底部的滑动的下划线
//     *
//     * @param size 轮播图数量
//     */
//    private void addScrollImage(int size) {
////        autoScrollIndicator.removeAllViews();
//        mScrollImageViews.clear();
//
//        for (int i = 0; i < size; i++) {
//            ImageView iv = new ImageView(getActivity());
//            iv.setPadding(10, 0, 10, 20);
//            if (i != 0) {
//                iv.setImageResource(R.drawable.icon_indicator_normal);
//            } else {
//                iv.setImageResource(R.drawable.icon_indicator_selected);
//            }
//            iv.setLayoutParams(new ViewGroup.LayoutParams(40, 40));
////            autoScrollIndicator.addView(iv);// 将图片加到一个布局里
//            mScrollImageViews.add(iv);
//        }
//    }

    /**
     * 轮播图适配器
     */
    PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mBannerList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
            View view = getLayoutInflater().inflate(R.layout.include_viewpager_banner, null);
            ImageView ivBanner = view.findViewById(R.id.bannerImg);
            mImageLoader.loadImage(MainActivity.this, ImageConfigImpl.builder()
                    .url("http://221.180.255.233:8088/" + mBannerList.get(position).getUrl())
                    .isCenterCrop(true)
                    .imageView(ivBanner)
                    .build());
            container.addView(view);

//            view.setOnClickListener(v -> {
//                if (!TextUtils.isEmpty(bannerBeans.get(position).getEvent())) {
//                    skipType(bannerBeans.get(position).getEvent(), bannerBeans.get(position).getUrl());
//                }
//
//            });
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBannerList != null) {
            mBannerList.clear();
            mBannerList = null;
        }
        mImageLoader = null;
        mPagerAdapter = null;
    }

    @Override
    public void killMyself() {
        finish();
    }

}
