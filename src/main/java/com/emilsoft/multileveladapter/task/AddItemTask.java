package com.emilsoft.multileveladapter.task;

import com.emilsoft.multileveladapter.callable.AdapterCallable;
import com.emilsoft.multileveladapter.model.MultiLevelItem;

import java.util.ArrayList;
import java.util.List;

public class AddItemTask<R, T extends MultiLevelItem<R, T>> implements AdapterCallable<T> {

    private final ItemProcessedListener<T> listener;
    private final List<T> items;
    private final T item;

    public AddItemTask(List<T> items, T item, ItemProcessedListener<T> listener) {
        this.items = items;
        this.item = item;
        this.listener = listener;
    }

    @Override
    public void onComplete(T result) {
        listener.onItemProcessed(result);
    }

    @Override
    public T call() throws Exception {
        T parent = item.getParent();
        int itemIndex = items.indexOf(item);
        int index = items.indexOf(parent);
        if(itemIndex != -1) {
            //The comment is visible and items contains item
            update(items, item, itemIndex);
            if(index != -1) {
                T parentInstance = items.get(index);
                item.setParent(parentInstance);
                return item;
            } else {
                //it's a top-comment
                return item;
            }
        } else {
            //This is a new fresh comment or a collapsed comment
            if(index != -1) {
                T parentInstance = items.get(index);
                item.setParent(parentInstance);
                if(parentInstance.isCollapsed()) {
                    // the comment is collapsed but the parent is not
                    if (!parentInstance.hasChildren()) {
                        parentInstance.setChildren(new ArrayList<>());
                    }
                    int i = parentInstance.getChildren().indexOf(item);
                    if(i != -1) {
                        update(parentInstance.getChildren(), item, i);
                        parentInstance.getChildren().set(i, item);
                    } else {
                        item.setLevel(parentInstance.getLevel() + 1);
                        parentInstance.getChildren().add(item);
                    }
                    return null;
                } else {
                    // the comment is not collapsed and it's child of another comment
                    return item;
                }
            } else {
                int parentIndex = -1;
                int visibleCommentIndex = 0;
                boolean parentFound = false;
                if(parent != null) {
                    while (visibleCommentIndex < items.size() && !parentFound) {
                        T visibleComment = items.get(visibleCommentIndex);
                        if (visibleComment.hasChildren() && (parentIndex = visibleComment.getChildren().indexOf(parent)) != -1)
                            parentFound = true;
                        visibleCommentIndex++;
                    }
                }
                if(parentFound) {
                    T visibleComment = items.get(visibleCommentIndex - 1);
                    T parentInstance = visibleComment.getChildren().get(parentIndex);
                    item.setParent(parentInstance);
                    if(parentInstance.isCollapsed()) {
                        // parent has collapsed children
                        if(!parentInstance.hasChildren()) {
                            parentInstance.setChildren(new ArrayList<>());
                        }
                        int i = parentInstance.getChildren().indexOf(item);
                        if(i != -1) {
                            update(parentInstance.getChildren(), item, i);
                            parentInstance.getChildren().set(i, item);
                        } else {
                            item.setLevel(parentInstance.getLevel() + 1);
                            parentInstance.getChildren().add(item);
                        }
                    }
                    // maintain coherency with onCollapse
                    int i = visibleComment.getChildren().indexOf(item);
                    if(i != -1) {
                        update(visibleComment.getChildren(), item, i);
                        visibleComment.getChildren().set(i, item);
                    } else {
                        item.setLevel(parentInstance.getLevel() + 1);
                        visibleComment.getChildren().add(parentIndex + 1, item);
                    }
                    //the comment is collapsed and so not visible
                    return null;
                } else {
                    // it's a fresh new top comment (doesn't have a parent and is not contained in items)
                    return item;
                }
            }
        }
    }

    private static <R, T extends MultiLevelItem<R, T>> void update(List<T> items, T item, int index) {
        T oldItem = items.get(index);
        item.setLevel(oldItem.getLevel());
        item.setIsCollapsed(oldItem.isCollapsed());
        item.setChildren(oldItem.getChildren());
    }


    public interface ItemProcessedListener<T> {

        void onItemProcessed(T multiLevelItem);

    }

}
