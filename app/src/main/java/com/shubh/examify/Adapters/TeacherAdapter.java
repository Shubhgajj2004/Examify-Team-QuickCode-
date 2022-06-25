package com.shubh.examify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shubh.examify.DetailSimpleActivity;
import com.shubh.examify.Model.Model_Exam;
import com.shubh.examify.R;
import com.shubh.examify.TeacherExamMarksActivity;

import java.util.ArrayList;
import java.util.Locale;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherHolder> {


    ArrayList<Model_Exam> list;
    ArrayList<Model_Exam> listKey;
    Context context;

    public TeacherAdapter(ArrayList<Model_Exam> list, ArrayList<Model_Exam> listKey, Context context) {
        this.list = list;
        this.listKey = listKey;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_teacher,parent,false);
        return new TeacherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherHolder holder, int position) {

        Model_Exam adp= list.get(position);
        Model_Exam adp2= listKey.get(position);
        holder.time.setText( String.format(Locale.getDefault() , "%02d:%02d - %02d:%02d" , adp.getInHour() , adp.getInMin() ,adp.getFinHour() , adp.getFinMin() ));
        holder.date.setText(adp.getDate().toString());
        holder.title.setText(adp.getTitle());
        holder.isActive.setText(adp.getIsActive());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , TeacherExamMarksActivity.class);
                intent.putExtra("Key" , adp2.getKey());


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TeacherHolder extends RecyclerView.ViewHolder {

       TextView title , time , date, isActive;
       public TeacherHolder(@NonNull View itemView) {
           super(itemView);

           title = itemView.findViewById(R.id.titleTeacherRes);
           time = itemView.findViewById(R.id.timeTeacherRes);
           date = itemView.findViewById(R.id.dateTeacherRes);
           isActive = itemView.findViewById(R.id.statusTeacherRes);
       }
   }
}
