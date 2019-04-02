package com.geek.newmanager.mvp.ui.adapter;

import android.view.View;

import com.geek.newmanager.R;
import com.geek.newmanager.mvp.model.entity.SocialThing;
import com.geek.newmanager.mvp.ui.adapter.holder.SocialItemHolder;
import com.geek.newmanager.mvp.ui.adapter.holder.SocialSearchItemHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 社会治理Adapter
 * Created by LiuLi on 2019/1/24.
 */

public class SocialThingAdapter extends DefaultAdapter<SocialThing> {

    private int entryType;

    public SocialThingAdapter(List<SocialThing> infos, int entryType) {
        super(infos);
        this.entryType = entryType;
    }

    @Override
    public int getItemCount() {
        return mInfos.size() + 1;
    }

    @Override
    public BaseHolder<SocialThing> getHolder(View v, int viewType) {
        if (viewType == 0) {
            return new SocialSearchItemHolder(v, entryType);
        } else {
            return new SocialItemHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(BaseHolder<SocialThing> holder, int position) {
        if (position == 0) {
            holder.setData(null, position);
        } else {
            holder.setData(mInfos.get(position - 1), position);
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.item_search_key;
        } else {
            return R.layout.item_contorl_service;
        }
    }
}
