package com.geek.newmanager.mvp.model.entity.treebean;


import com.geek.newmanager.R;
import com.geek.newmanager.mvp.ui.adapter.treeview.LayoutItem;

/**
 *节点3
 */

public class LeafNode implements LayoutItem {
    private String name;

    public LeafNode(String name) {
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
        return R.layout.item_leaf;
    }

    @Override
    public int getToggleId() {
        return 0;
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
