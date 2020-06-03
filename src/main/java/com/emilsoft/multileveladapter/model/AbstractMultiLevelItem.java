package com.emilsoft.multileveladapter.model;

import java.util.List;
import java.util.Objects;

public abstract class AbstractMultiLevelItem<R, T> implements MultiLevelItem<R, T> {

    private R id;
    private T parent;
    private List<T> children = null;
    private boolean isCollapsed = false;
    private int level = 0;

    /**
     * The parent's instance just need to have the id, that it will be used in MultiLevelAdapter.
     * Otherwise, if the parent's instance is null, the item will be considered a top level item (0).
     */
    public AbstractMultiLevelItem(R id, T parent) {
        this.id = id;
        this.parent = parent;
    }

    @Override
    public void setId(R id) {
        this.id = id;
    }

    @Override
    public R getId() {
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
