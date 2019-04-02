package com.geek.newmanager.mvp.model.entity.treebean;


import com.geek.newmanager.R;
import com.geek.newmanager.mvp.ui.adapter.treeview.LayoutItem;

/**
 * 节点1
 */

public class RootNode implements LayoutItem {
    private String name;

    public RootNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_root;
    }

    @Override
    public int getToggleId() {
        return R.id.ivNode;
    }

    @Override
    public int getCheckedId() {
        return 0;
    }

    @Override
    public int getClickId() {
        return R.id.tvName;
    }

}
