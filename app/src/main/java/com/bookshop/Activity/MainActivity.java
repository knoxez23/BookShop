package com.bookshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Window;

import com.bookshop.Adapter.PopularAdapter;
import com.bookshop.Domain.PopularDomain;
import com.bookshop.R;
import com.bookshop.databinding.ActivityMainBinding;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        statusBarColor();
        initRecyclerView();
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_Dark));
    }

    private void initRecyclerView() {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("Oxford Dictionary", "oxford_dic", 15, 4, 1500, "test"));
        items.add(new PopularDomain("English Aid", "english_aid",10 , 4.5, 350,"English Aid 1 offers a wide range of features to support your language learning journey. From grammar rules to common phrases and expressions, this tool covers all the essential elements of English language proficiency. With clear explanations and examples, you'll quickly grasp the fundamentals of grammar and gain confidence in using the language correctly."));
        items.add(new PopularDomain("Primary Mathematics", "primary_maths", 3, 4.9, 580, "This is the first book in the new series of Primary Mathematics written specifically for the competence-based curriculum. The coursebook is intended for use by Grade 1 learners. It prepares the learner for day to day living and number work in higher levels of schooling."));

        binding.PopularView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        binding.PopularView.setAdapter(new PopularAdapter(items));
    }
}