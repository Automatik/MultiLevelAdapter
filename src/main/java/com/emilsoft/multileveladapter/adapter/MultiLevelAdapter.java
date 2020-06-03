package com.emilsoft.multileveladapter.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emilsoft.multileveladapter.callable.CollapseItemListener;
import com.emilsoft.multileveladapter.holder.MultiLevelViewHolder;
import com.emilsoft.multileveladapter.model.MultiLevelItem;
import com.emilsoft.multileveladapter.task.AddItemTask;
import com.emilsoft.multileveladapter.task.TaskRunner;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiLevelAdapter<R, T extends MultiLevelItem<R, T>,
        VH extends MultiLevelViewHolder<R, T>>
        extends RecyclerView.Adapter<VH> {

    private List<T> items;

    public MultiLevelAdapter(List<T> recyclerViewItems) {
        if(recyclerViewItems == null)
            recyclerViewItems = new ArrayList<>();
        this.items = recyclerViewItems;
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        T item = items.get(position);
        onBindViewHolder(holder, position, item);
    }

    public abstract void onBindViewHolder(@NonNull VH holder, int position, T item);

    @Override
    public final int getItemCount() {
        if(items == null)
            return 0;
        return items.size();
    }

    protected final CollapseItemListener<R, T> collapseItemListener = new CollapseItemListener<R, T>() {

        @Override
        public void onCollapse(T item) {
            final int index = items.indexOf(item);
            final int level = item.getLevel();
            if(index == -1) return;
            int i = index + 1;

            List<T> children = new ArrayList<>();
            T temp;
            while (i < items.size() && (temp = items.get(i)).getLevel() > level) {
                children.add(temp);
                if(temp.hasChildren()) // already collapsed items, not currently contained in items
                    children.addAll(temp.getChildren());
                i++;
            }
            items.subList(index + 1, i).clear();
            notifyItemRangeRemoved(index + 1, i - (index + 1));
            item.setChildren(children);
        }

        @Override
        public void onExpand(T item) {
            int index = items.indexOf(item);
            if(index == -1) return;
            if(item.hasChildren()) {
                List<T> children = item.getChildren();
                item.setChildren(null);

                // Remove children of collapsed comments
                List<T> expandedChildren = new ArrayList<>();
                for(int i = 0; i < children.size(); i++) {
                    expandedChildren.add(children.get(i));
                    if(children.get(i).hasChildren())
                        i += children.get(i).getChildren().size();
                }

                items.addAll(index + 1, expandedChildren);
                notifyItemRangeInserted(index + 1, expandedChildren.size());
            }
        }
    };

    public final void addItem(T item) {
        TaskRunner runner = new TaskRunner();
        AddItemTask<R, T> task = new AddItemTask<>(items, item,
                new AddItemTask.ItemProcessedListener<T>() {
            @Override
            public void onItemProcessed(T multiLevelItem) {
                addCommentToList(multiLevelItem);
            }
        });
        runner.executeAsync(task);
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    private void addCommentToList(T item) {
        if(item == null) return;
        int pos = items.size();
        int index;
        T parent = item.getParent();
        if(parent != null) {
            item.setLevel(parent.getLevel() + 1);
            index = items.indexOf(parent);
        } else {
            //it's a top level comment
            item.setLevel(1);
            index = pos;
        }
        int indexToInsert = index + 1;
        if(!items.contains(item)) {
            append(items, item, indexToInsert);
            notifyItemInserted(indexToInsert);
        }
    }

    private static <R, T extends MultiLevelItem<R, T>> void update(List<T> items, T item, int index) {
        T oldItem = items.get(index);
        item.setLevel(oldItem.getLevel());
        item.setIsCollapsed(oldItem.isCollapsed());
        item.setChildren(oldItem.getChildren());
    }

    private static <R, T extends MultiLevelItem<R, T>> void append(List<T> items, T item, int index) {
        int size = items.size();
        int i = (index < 0 || index > size) ? size : index;
        items.add(i, item);
    }

}

