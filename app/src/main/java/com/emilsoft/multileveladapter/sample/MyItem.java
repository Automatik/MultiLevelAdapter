package com.emilsoft.multileveladapter.sample;

import android.os.Build;

import com.emilsoft.multileveladapter.model.AbstractMultiLevelLongItem;

import java.util.Objects;

public class MyItem extends AbstractMultiLevelLongItem<MyItem> {

    private String text;

    public MyItem(Long id, MyItem parent) {
        super(id, parent);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(getId());
        } else {
            int prime = 31;
            int result = 1;
            result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
            return result;
        }
    }
}
