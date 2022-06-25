package com.shubh.examify.TeacherSection;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayoutMediator;
import com.shubh.examify.Adapters.TypeExamAdapter;
import com.shubh.examify.Model.TypeModel;
import com.shubh.examify.TypeExam.SecureExamFragment;
import com.shubh.examify.TypeExam.SimpleExamFragment;
import com.shubh.examify.databinding.ActivityExamMakerBinding;

import java.util.ArrayList;

//It is the base activity for forming form for both secure and simple exam
//TabLayout for both simple and secure fragments
public class ExamMakerActivity extends AppCompatActivity {


    private ActivityExamMakerBinding binding;
    private TypeExamAdapter adapter;
    ArrayList<TypeModel> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExamMakerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fm = getSupportFragmentManager();
        adapter = new TypeExamAdapter(fm, getLifecycle());

        fragmentList.add(new TypeModel(new SimpleExamFragment(), "Simple"));
        fragmentList.add(new TypeModel(new SecureExamFragment(), "Secure"));

        adapter.Frag(fragmentList);


        binding.Viewpag2.setAdapter(adapter);
        binding.Viewpag2.setOffscreenPageLimit(1);


        new TabLayoutMediator(binding.tabTypeOfExam, binding.Viewpag2,
                (tab, position) -> {
                    TypeModel adp = fragmentList.get(position);
                    tab.setText(adp.getTitle());
                }).attach();

    }

}