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

    void setIsCollapsed(boolean isCollapsed);

    boolean isCollapsed();

    void setLevel(int level);

    int getLevel();

    void setParent(T parent);

    T getParent();

    boolean hasChildren();

    void setChildren(List<T> children);

    List<T> getChildren();

}
