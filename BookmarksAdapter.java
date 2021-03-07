package com.learnprogramming.academy.tsp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.viewholder> {
private List<QuestionModel> list;
public BookmarksAdapter(List<QuestionModel> list){
    this.list = list;
}
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarkitem,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(list.get(position).getQuestion(),list.get(position).getCorrectAns(),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder{

    private ImageButton deleteBtn;
    private TextView question,answer;
        public viewholder(@NonNull View itemview){
            super(itemview);

            question = itemview.findViewById(R.id.question);
            answer =  itemview.findViewById(R.id.answer);
            deleteBtn = itemview.findViewById(R.id.delete);
        }

        private  void setData(String question, String answer, final int position){
            this.question.setText(question);
            this.answer.setText(answer);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }
}
