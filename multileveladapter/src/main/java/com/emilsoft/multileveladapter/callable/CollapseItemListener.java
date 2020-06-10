package com.emilsoft.multileveladapter.callable;

import com.emilsoft.multileveladapter.model.MultiLevelItem;

public interface CollapseItemListener<R, T extends MultiLevelItem<R, T>> {

    void onCollapse(T item);

    void onExpand(T item);
}
