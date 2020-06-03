package com.emilsoft.multileveladapter.model;

import java.util.List;

/**
 * Example abstract class that defines Long as the item's id class.
 * @param <T> the item's class which is shown in the adapter
 */
public abstract class AbstractMultiLevelLongItem<T> implements MultiLevelLongItem<T> {

    Long id;
    private T parent;
    private List<T> children = null;
    private boolean isCollapsed = false;
    private int level = 0;

    /**
     * The parent's instance is null so the item will be considered a top level item (1).
     */
    public AbstractMultiLevelLongItem(Long id) {
        this.id = id;
        this.parent = null;
    }

    /**
     * The parent's instance just need to have the id, that it will be used in MultiLevelAdapter.
     * Otherwise, if the parent's instance is null, the item will be considered a top level item (1).
     */
    public AbstractMultiLevelLongItem(Long id, T parent) {
        this.id = id;
        this.parent = parent;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setIsCollapsed(boolean isCollapsed) {
        this.isCollapsed = isCollapsed;
    }

    @Override
    public boolean isCollapsed() {
        return isCollapsed;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setParent(T parent) {
        this.parent = parent;
    }

    @Override
    public T getParent() {
        return parent;
    }

    @Override
    public boolean hasChildren() {
        return children != null;
    }

    @Override
    public void setChildren(List<T> children) {
        this.children = children;
    }

    @Override
    public List<T> getChildren() {
        return children;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
