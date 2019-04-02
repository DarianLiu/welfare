package com.geek.newmanager.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.newmanager.R;
import com.geek.newmanager.app.Constant;
import com.geek.newmanager.app.EventBusTags;
import com.geek.newmanager.di.component.DaggerSocialManageComponent;
import com.geek.newmanager.di.module.SocialManageModule;
import com.geek.newmanager.mvp.contract.SocialManageContract;
import com.geek.newmanager.mvp.model.entity.SocialThing;
import com.geek.newmanager.mvp.model.entity.UserInfo;
import com.geek.newmanager.mvp.presenter.SocialManagePresenter;
import com.geek.newmanager.mvp.ui.adapter.SocialThingAdapter;
import com.geek.newmanager.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SocialManageActivity extends BaseActivity<SocialManagePresenter> implements SocialManageContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right)
    ImageView ivAdd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private LoadingProgressDialog loadingDialog;
    private long assortId;
    private int thingType;
    private UserInfo userInfo;
    private List<SocialThing> mDatas;
    private SocialThingAdapter mAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSocialManageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .socialManageModule(new SocialManageModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_social_manage; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ivAdd.setImageDrawable(getResources().getDrawable(R.drawable.icon_add));
        ivAdd.setVisibility(View.VISIBLE);
        ivAdd.setOnClickListener(v -> {
            Intent intent = new Intent(SocialManageActivity.this, SocialProductDangerActivity.class);
//            intent.putExtra("entry_type", assortId);
//            intent.putExtra("title", ((ServiceBean) data).getTitle());
//            intent.putExtra("content", ((ServiceBean) data).getContent());
            launchActivity(intent);
        });

        assortId = getIntent().getIntExtra("assortId", 0);
        thingType = getIntent().getIntExtra("thingType", 0);

        initTitle();
        initRecycleView();
        initRefreshLayout();

        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        if (mPresenter != null) {
            if (userInfo == null) {
                launchActivity(new Intent(this, LoginActivity.class));
            } else {
                mPresenter.findThingPositionList(true, assortId, userInfo.getStreetId(),
                        userInfo.getCommunityId(), userInfo.getGridId(), thingType, "");
            }
        }
    }

    /**
     * 初始化刷新
     */
    private void initRefreshLayout() {
        smartRefresh.setEnableLoadMoreWhenContentNotFull(false);
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null)
                    mPresenter.findThingPositionList(false, assortId, userInfo.getStreetId(),
                            userInfo.getCommunityId(), userInfo.getGridId(), thingType, "");
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null)
                    mPresenter.findThingPositionList(true, assortId, userInfo.getStreetId(),
                            userInfo.getCommunityId(), userInfo.getGridId(), thingType, "");
            }
        });
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatas = new ArrayList<>();
        mAdapter = new SocialThingAdapter(mDatas, thingType);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化标题
     * 药品 3
     * 餐饮 4
     * 特种设备 5
     * 在建工地 6
     * 危化品 7
     * 森森防火 8
     * 防台防汛 9
     * 文明祭祀 10
     * 冬季除雪 11
     * 网吧 12
     * 文物保护单位 13
     * 演出场所 14
     * 游艺娱乐 15
     * 娱乐场所 16
     */
    private void initTitle() {
        switch (thingType) {
            case 3://药品
                tvToolbarTitle.setText(R.string.social_fad_drug);
                break;
            case 4://餐饮
                tvToolbarTitle.setText(R.string.social_fad_foot);
                break;
            case 5://特种设备
                tvToolbarTitle.setText(R.string.social_product_device);
                break;
            case 6://在建工地
                tvToolbarTitle.setText(R.string.social_product_work_site);
                break;
            case 7://危化品
                tvToolbarTitle.setText(R.string.social_product_danger);
                break;
            case 8://森森防火
                tvToolbarTitle.setText(R.string.social_prevent_forest_fire);
                break;
            case 9://防台防汛
                tvToolbarTitle.setText(R.string.social_prevent_flood);
                break;
            case 10://文明祭祀
                tvToolbarTitle.setText(R.string.social_prevent_sacrifice);
                break;
            case 11://冬季除雪
                tvToolbarTitle.setText(R.string.social_prevent_snow_removal);
                break;
            case 12://网吧
                tvToolbarTitle.setText(R.string.social_cultura_internet_bar);
                break;
            case 13://文物保护单位
                tvToolbarTitle.setText(R.string.social_cultura_relic_protect);
                break;
            case 14://演出场所
                tvToolbarTitle.setText(R.string.social_cultura_performing_place);
                break;
            case 15://游艺娱乐
                tvToolbarTitle.setText(R.string.social_cultura_recreation);
                break;
            case 16://娱乐场所
                tvToolbarTitle.setText(R.string.social_cultura_place);
                break;
        }
    }

    @Subscriber(tag = EventBusTags.TAG_SOCIAL_SEARCH)
    public void receiveSearchKey(String searchKey) {
        if (mPresenter != null)
            mPresenter.findThingPositionList(true, assortId, userInfo.getStreetId(),
                    userInfo.getCommunityId(), userInfo.getGridId(), thingType, searchKey);
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


    @Override
    public void finishRefresh() {
        if (smartRefresh != null)
            smartRefresh.finishRefresh();
    }

    @Override
    public void finishLoadMore() {
        if (smartRefresh != null)
            smartRefresh.finishLoadMore();
    }

    @Override
    public void refreshData(List<SocialThing> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreData(List<SocialThing> datas) {
        if (datas != null && smartRefresh != null && datas.size() == 0) {
            smartRefresh.finishLoadMoreWithNoMoreData();
            return;
        }

        if (datas != null){
            int index = mDatas.size();
            mDatas.addAll(datas);
            mAdapter.notifyItemRangeInserted(index + 1, datas.size());
        }
    }

}
