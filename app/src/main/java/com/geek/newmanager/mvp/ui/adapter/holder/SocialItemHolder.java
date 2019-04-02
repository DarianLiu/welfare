package com.geek.newmanager.mvp.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.geek.newmanager.R;
import com.geek.newmanager.Utils.DateUtils;
import com.geek.newmanager.mvp.model.entity.ServiceBean;
import com.geek.newmanager.mvp.model.entity.SocialThing;
import com.jess.arms.base.BaseHolder;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 服务项目ItemHolder
 * Created by LiuLi on 2019/1/24.
 */

public class SocialItemHolder extends BaseHolder<SocialThing> {

    @BindView(R.id.tv_service_title)
    TextView tvServiceTitle;
    @BindView(R.id.tv_service_create_time)
    TextView tvServiceCreateTime;

    @BindString(R.string.create_time)
    String createTime;

    public SocialItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }

    @Override
    public void setData(SocialThing data, int position) {
        tvServiceTitle.setText(data.getIntroTitle());

        tvServiceCreateTime.setText(String.format("%s%s", createTime,
                DateUtils.timeStamp2Date(data.getCreateTime(), "yyy-MM-dd")));
    }
}
