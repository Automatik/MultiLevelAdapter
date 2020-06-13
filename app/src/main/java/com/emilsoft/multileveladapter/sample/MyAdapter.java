package com.emilsoft.multileveladapter.sample;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.emilsoft.multileveladapter.adapter.MultiLevelAdapter;
import com.emilsoft.multileveladapter.callable.CollapseItemListener;
import com.emilsoft.multileveladapter.holder.MultiLevelViewHolder;

import java.util.List;

public class MyAdapter extends MultiLevelAdapter<Long, MyItem, MyAdapter.ViewHolder> {

    private Context context;
    private int[] colors;
    private int levelStartMargin;

    public MyAdapter(List<MyItem> recyclerViewItems) {
        super(recyclerViewItems);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view, getCollapseItemListener());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, MyItem item) {
        holder.mItem = item;
        holder.mText.setText(item.getText());

        int color = getItemColor(holder.mItem.getLevel() - 1);
        if(color != 0)
            holder.mLevel.setBackgroundColor(color);

        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                holder.itemView.getLayoutParams();
        params.leftMargin = item.getLevel() * levelStartMargin;
        holder.itemView.setLayoutParams(params);

        if(item.isCollapsed()) {
            holder.mCollapseText.setText(R.string.item_expand_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mCollapseIcon.setImageDrawable(context.getDrawable(R.drawable.ic_expand_more_24dp));
            } else {
                holder.mCollapseIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_expand_more_24dp));
            }
        } else {
            holder.mCollapseText.setText(R.string.item_collapse_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mCollapseIcon.setImageDrawable(context.getDrawable(R.drawable.ic_expand_less_24dp));
            } else {
                holder.mCollapseIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_expand_less_24dp));
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
        colors = context.getResources().getIntArray(R.array.colors);
        levelStartMargin = (int) (context.getResources().getDimension(R.dimen.item_level_start_margin) /
                context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        colors = null;
    }

    private int getItemColor(int level) {
        return (colors != null) ? colors[level % colors.length] : 0;
    }

    public static class ViewHolder extends MultiLevelViewHolder<MyItem> {

        final ConstraintLayout mCollapseLayout;
        final TextView mText;
        final TextView mCollapseText;
        final ImageView mCollapseIcon;
        final View mLevel;
        MyItem mItem;


        public ViewHolder(@NonNull View view, final CollapseItemListener<MyItem> listener) {
            super(view, listener);
            mText = view.findViewById(R.id.item_text);
            mCollapseText = view.findViewById(R.id.item_expand_text);
            mCollapseIcon = view.findViewById(R.id.item_expand_imageview);
            mCollapseLayout = view.findViewById(R.id.item_expand_layout);
            mLevel = view.findViewById(R.id.item_level);
            mCollapseLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItem != null) {
                        if (listener != null) {
                            if (mItem.isCollapsed()) {
                                listener.onExpand(mItem);
                                mCollapseText.setText(R.string.item_collapse_text);
                                mCollapseIcon.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_expand_less_24dp));
                            } else {
                                listener.onCollapse(mItem);
                                mCollapseText.setText(R.string.item_expand_text);
                                mCollapseIcon.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_expand_more_24dp));
                            }
                        }
                    }
                }
            });
        }
    }
}
