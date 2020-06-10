package com.emilsoft.multileveladapter.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyAdapter adapter;
    private List<MyItem> items;

    static String lorem = "\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new MyAdapter(items);
        recyclerView.setAdapter(adapter);

        createItems();

    }

    private void createItems() {
        int top_n = 4;

        // create top-level items (level = 1)
        for(int i = 1; i < top_n; i++) {
            String text = "This is a top-level comment with id: " + i;
            // The top-level items don't have a parent, so we pass null
            MyItem item = new MyItem((long) i, null);
            item.setText(text + lorem);
            adapter.addItem(item);
        }

        for(int i = 1; i < top_n; i++) {
            // create second-level items (level = 2)
            int num_children = 2;
            for(int j = 0; j < num_children; j++) {
                long id = i * 10 + j + 1;
                String text = "This is a second-level comment with id: " + id;

                //Only the parent's id is needed for MultiLevelAdapter
                MyItem top_parent = new MyItem((long) i, null);

                //Otherwise you can get the full instance like this
                //top_parent = items.get(items.indexOf(top_parent));

                MyItem item = new MyItem(id, top_parent);
                item.setText(text + lorem);
                adapter.addItem(item);

                // create third-level items (level = 3)
                long id2 = i * 100 + j + 1;
                text = "This is a third-level comment with id: " + id2;
                MyItem kidItem = new MyItem(id2, item);
                kidItem.setText(text + lorem);
                adapter.addItem(kidItem);
            }
        }

    }
}