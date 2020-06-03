package com.emilsoft.multileveladapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emilsoft.multileveladapter.model.MultiLevelItem;
import com.emilsoft.multileveladapter.callable.CollapseItemListener;

public class MultiLevelViewHolder<R, T extends MultiLevelItem<R, T>> extends RecyclerView.ViewHolder {

    private CollapseItemListener<R, T> listener;

    public MultiLevelViewHolder(@NonNull View view, CollapseItemListener<R, T> listener) {
        super(view);
        this.listener = listener;
    }
}
