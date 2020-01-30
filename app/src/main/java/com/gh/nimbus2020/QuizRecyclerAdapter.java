package com.gh.nimbus2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuizRecyclerAdapter extends RecyclerView.Adapter<QuizRecyclerAdapter.viewholder> {
    private Context context;
    private ArrayList<Id_Value> arrayList;
    public QuizRecyclerAdapter(Context context,ArrayList<Id_Value> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v =inflater.inflate(R.layout.quiz_recycler_item,null);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.quizname.setText(arrayList.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        Button quizname;
        private viewholder(@NonNull View itemView) {
            super(itemView);
            quizname=itemView.findViewById(R.id.quizdepartmentname);
        }
    }
}
