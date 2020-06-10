package com.emilsoft.multileveladapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emilsoft.multileveladapter.model.MultiLevelItem;
import com.emilsoft.multileveladapter.callable.CollapseItemListener;

public class MultiLevelViewHolder<T> extends RecyclerView.ViewHolder {

    private CollapseItemListener<T> listener;

    public MultiLevelViewHolder(@NonNull View view, CollapseItemListener<T> listener) {
        super(view);
        this.listener = listener;
    }
}
