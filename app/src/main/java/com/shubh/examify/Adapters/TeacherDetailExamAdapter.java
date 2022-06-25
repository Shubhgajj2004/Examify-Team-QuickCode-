package com.shubh.examify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shubh.examify.Model.Model_Exam;
import com.shubh.examify.Model.Model_Teacher_Exam;
import com.shubh.examify.R;
import com.shubh.examify.TeacherExamMarksActivity;
import com.shubh.examify.TeacherSection.MarksDetailActivity;

import java.util.ArrayList;

public class TeacherDetailExamAdapter extends RecyclerView.Adapter<TeacherDetailExamAdapter.TeacherHolder> {

    ArrayList<Model_Teacher_Exam> list;
    Context context;

    public TeacherDetailExamAdapter(ArrayList<Model_Teacher_Exam> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_detail_exam,parent,false);
        return new TeacherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherHolder holder, int position) {

        Model_Teacher_Exam adp= list.get(position);


        holder.Id.setText(adp.getStudentID());
        holder.isChecked.setText(adp.getMarks());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , MarksDetailActivity.class);
                intent.putExtra("Id" , adp.getStudentID());
                intent.putExtra("Marks" , adp.getMarks());
                intent.putExtra("Name" , adp.getStudentNAME());
                intent.putExtra("Ans" , adp.getAns());
                intent.putExtra("Key" , adp.getKey());


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TeacherHolder extends RecyclerView.ViewHolder{

        TextView Id , isChecked;

        public TeacherHolder(@NonNull View itemView) {
            super(itemView);

            Id = itemView.findViewById(R.id.collegeIdRes);
            isChecked = itemView.findViewById(R.id.isCheckedRes);
        }
    }
}
