package com.emilsoft.multileveladapter.model;

import java.util.List;

/**
 * Base Interface for MultiLevelAdapter.
 * The class that implements this interface needs always to set the parent's instance.
 * The parent's instance just need to have the id, that it will be used in MultiLevelAdapter.
 * Otherwise, if the parent's instance is null, the item will be considered a top level item (1).
 * Remember to implement equals and hashCode methods.
 * @param <R> the id's class
 * @param <T> the item's class which is shown in the adapter
 */
public interface MultiLevelItem<R, T> {

    void setId(R id);

    R getId();

    /**
     * Set the item as collapsed, meaning it is visible but its children aren't, or as expanded,
     * meaning both the item and its children are visible.
     */
    void setIsCollapsed(boolean isCollapsed);

    boolean isCollapsed();

    /**
     * Set the item's depth inside the list. Level = 0 means that it's not been set. Level = 1 means
     * that the item is a top-item, it doesn't have parent but only children. From level = 2 the
     * item has always a parent and could have children.
     */
    void setLevel(int level);

    int getLevel();

    /**
     * Set the item's parent. If the item has level = 1, the parent is null.
     */
    void setParent(T parent);

    T getParent();

    /**
     * If the item's children list is not null, then the item has children.
     */
    boolean hasChildren();

    /**
     * When an item is collapsed, its children are stored in this item
     * @param children
     */
    void setChildren(List<T> children);

    List<T> getChildren();

}
