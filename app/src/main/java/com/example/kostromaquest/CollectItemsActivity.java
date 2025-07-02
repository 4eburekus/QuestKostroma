package com.example.kostromaquest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CollectItemsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_items); // Этот layout должен содержать FrameLayout или FragmentContainerView

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new CollectItemsFragment())
                    .commit();
        }
    }
}
