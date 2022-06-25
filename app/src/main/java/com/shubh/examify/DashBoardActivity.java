package com.shubh.examify;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;
import com.shubh.examify.DashBoard.HistoryFragment;
import com.shubh.examify.DashBoard.Home_Fragment;
import com.shubh.examify.DashBoard.SecureFragment;
import com.shubh.examify.DashBoard.TeacherFragment;
import com.shubh.examify.TeacherSection.TeacherHomeFragment;
import com.shubh.examify.databinding.ActivityDashBoardBinding;


//Dashboard activity to manage all fragments
public class DashBoardActivity extends AppCompatActivity {

    ActivityDashBoardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Fragment frag = new Home_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_frag, frag).commit();

        //sharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DashBoardActivity.this);


        //synchronised the fragment with specific button of navigation bar
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            Fragment fragment;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_nav:
                        fragment = new Home_Fragment();
                        break;

                    case R.id.history_nav:
                        fragment = new HistoryFragment();
                        break;

                    case R.id.teacher_nav:

                        //if the teacher is already signed in then this will show
                        //home page for teacher instead of sign in fragment
                        if (sharedPreferences.getBoolean("isVerified", false)) {
                            fragment = new TeacherHomeFragment();
                            break;
                        } else {
                            fragment = new TeacherFragment();
                            break;
                        }


                    case R.id.secure_nav:
                        fragment = new SecureFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container_frag, fragment).commit();


                return true;
            }
        });


    }
}