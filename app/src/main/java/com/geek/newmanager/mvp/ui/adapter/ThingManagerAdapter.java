package com.geek.newmanager.mvp.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.geek.newmanager.R;
import com.geek.newmanager.mvp.model.entity.Thing;
import com.geek.newmanager.mvp.ui.adapter.holder.ThingManagerItemHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 巡查项管理列表适配器
 * Created by LiuLi on 2018/11/6.
 */

public class ThingManagerAdapter extends DefaultAdapter<Thing> {

    public ThingManagerAdapter(List<Thing> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Thing> getHolder(View v, int viewType) {
        return new ThingManagerItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_thing_manager_list;
    }

    @Override
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

}
