package com.shubh.examify.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.shubh.examify.Model.TypeModel;

import java.util.ArrayList;

public class TypeExamAdapter extends FragmentStateAdapter {

    ArrayList<TypeModel> fragmentList ;




    public TypeExamAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    public void Frag(ArrayList<TypeModel> fragmentList )
    {
        this.fragmentList = fragmentList;

    }


//    public void addFragment(Fragment fragment , String title)
//    {
//        fragmentList.add(fragment);
//        fragmentTitleList.add(title);
//    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        TypeModel adp =fragmentList.get(position);

        return adp.getFm();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
