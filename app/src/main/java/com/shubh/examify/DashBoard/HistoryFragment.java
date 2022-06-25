package com.shubh.examify.DashBoard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shubh.examify.R;
import com.shubh.examify.databinding.FragmentHistoryBinding;
import com.shubh.examify.databinding.FragmentTeacherHomeBinding;


public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }

   FragmentHistoryBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}