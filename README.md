# MultiLevelAdapter

[![Maven Central](https://img.shields.io/maven-central/v/io.github.automatik/multileveladapter?label=Download&logo=android)](https://repo.maven.apache.org/maven2/io/github/automatik/multileveladapter/1.0.2/multileveladapter-1.0.2.aar)

MultiLevelAdapter is an Android library to allow collapsing and expanding items in RecyclerView's Adapter on multiple levels (no limits).

![Demo Gif](MultiLevelAdapter.gif)

This library lets you collapse and expand items in your `RecyclerView` by only:
* Extending your adapter with abstract class `MultiLevelAdapter`
* Implementing the interface `MultiLevelItem` or extending the abstract class `AbstractMultiLevelItem` in your `RecyclerView`'s item's class

## Dependency
```gradle
dependencies {
    implementation 'io.github.automatik:multileveladapter:1.0.1'
}
```

## Features
* The interface `MultiLevelItem<R, T>` lets you define both the type of the item's id and the item itself.
* The abstract class `AbstractMultiLevelItem<R, T>` lets you define both the type of the item's id and the item itself, but you don't need to implement all the interface's methods.
* The interface `MultiLevelLongItem<T>` is a shortcut and defines the id's type as `long` and lets you define the item's class.
* The abstract class `AbstractMultiLevelLongItem<T>` is a shortcut and defines the id's type as `long` and implements all the interface's methods, but lets you define the item's class.
* The abstract class `MultiLevelAdapter<T, VH>` already implements the logic behind collapsing and expanding items. You need only to call its listener when clicking collapse/expand.
* The adapter's method `addItem` adds an item to the adapter's list. If the item's parent is collapsed, the item will be added to its parent's children and will not be showed in the `RecyclerView`.
* The adapter's method `addItem` lets you add new items immediately, running on main UI thread, or delayed by using a Handler attached to main UI thread and adding the `addItemTask` to the message queue. This is to avoid possible heavy-blocking execution. In case `delayed` is `true` obviously the new item won't be immediately indexable in the list.
* The adapter's method `addItem` lets you avoid to add redundant items by checking the item's id.

## Basic Usage
Your item's class needs to have an `id` to uniquely identify the item in the list and to pass the parent's instance in the constructor. If the parent is null the item is considered as a top-level item (level = 1). The parent doesn't need to have all the fields complete but only the id field. The id field is used when adding the item to the list.
You don't need to manually edit or set the fields `children`, `isCollapsed` and `level`. They are used internally in the `addItemTask`.

The most straightforward way to use this library is by first extending your item's class (used in your list) with `AbstractMultiLevelItem`. The `MyItem` class has the id's type as `long`.
```java
public class MyItem extends AbstractMultiLevelItem<Long, MyItem> {

    public MyItem(Long id, MyItem parent) {
        super(id, parent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyItem item = (MyItem) o;
        return getId().equals(item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
```

And then extending your adapter's class with `MultiLevelAdapter`
```java
public class Adapter extends MultiLevelAdapter<MyItem, Adapter.ViewHolder> {


    public Adapter(List<MyItem> recyclerViewItems) {
        super(recyclerViewItems);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        //collapseItemListener is defined in MultiLevelAdapter class
        return new Adapter.ViewHolder(view, getCollapseItemListener());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, MyItem item) {
        holder.mItem = item;
        if(item.isCollapsed())
            holder.mCollapseText.setText("Expand");
        else
            holder.mCollapseText.setText("Collapse");
    }

    public static class ViewHolder extends MultiLevelViewHolder<MyItem> {

        final TextView mCollapseText;
        MyItem mItem;

        public ViewHolder(@NonNull View view, CollapseItemListener<MyItem> listener) {
            super(view, listener);
            mCollapseText = view.findViewById(R.id.item_expand_text);
            mCollapseText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItem.isCollapsed()) {
                        listener.onExpand(mItem);
                        mCollapseText.setText("Collapse");
                    } else {
                        listener.onCollapse(mItem);
                        mCollapseText.setText("Expand");
                    }
                }
            });
        }
    }
}
```

Finally, to add items to your list you need to call `addItem` in your Activity/Fragment
```java
adapter.addItem(item);
```

A sample app is also provided in this repository.

## Advanced Usage
Instead of extending your item's class, you can implement the interface `MultiLevelItem`
```java
public class MyItem implements MultiLevelItem<Long, MyItem> {

    private Long id;
    private MyItem parent;
    private List<MyItem> children = null;
    private boolean isCollapsed = false;
    private int level = 0;
    
    //The parent's instance is null so the item will be considered a top level item (level = 1).
    public MyItem(long id) {
        this.id = id;
        parent = null;
    }
    
    public MyItem(long id, MyItem parent) {
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
    public void setParent(MyItem parent) {
        this.parent = parent;
    }

    @Override
    public MyItem getParent() {
        return parent;
    }

    @Override
    public boolean hasChildren() {
        return children != null;
    }

    @Override
    public void setChildren(List<MyItem> children) {
        this.children = children;
    }

    @Override
    public List<MyItem> getChildren() {
        return children;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyItem item = (MyItem) o;
        return getId().equals(item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
```

* You can also define more hierarchies by implementing or extending the library's classes and interfaces and defining only one type.
* You can implement your own `TaskRunner` if you want to execute the `addItemTask` on another thread and pass it in the adapter's constructor.
* Yoi can implement your own `AddItemTask` and override the `addItem` method to define how you want to add your items to the adapter.

## License

```
Copyright 2020 Emil Osterhed

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
