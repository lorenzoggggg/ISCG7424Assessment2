package com.example.iscg7424assessment2part1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PlayerQuizAdapter extends RecyclerView.Adapter<PlayerQuizAdapter.QuizViewHolder> {

    private Context context;
    private List<Quiz> quizList;

    public PlayerQuizAdapter(Context context, List<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.player_quiz_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.name.setText("Name: " + quiz.name);
        holder.category.setText("Category: " + quiz.category);
        holder.difficulty.setText("Difficulty: " + quiz.difficulty);
        holder.startDate.setText("Start Date: " + quiz.startDate);
        holder.endDate.setText("End Date: " + quiz.endDate);
        holder.likes.setText("Likes: " + quiz.likes);

        holder.playBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, playQuizActivity.class);
            intent.putExtra("quizId", quiz.id);
            context.startActivity(intent);
        });

        holder.likeBtn.setOnClickListener(v -> {
            quiz.likes += 1;
            FirebaseDatabase.getInstance().getReference("quizzes")
                    .child(quiz.id)
                    .child("likes")
                    .setValue(quiz.likes)
                    .addOnSuccessListener(aVoid -> {
                        holder.likes.setText("Likes: " + quiz.likes);
                        Toast.makeText(context, "You liked this quiz!", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, difficulty, startDate, endDate, likes;
        Button playBtn, likeBtn;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.quizNameTxt);
            category = itemView.findViewById(R.id.quizCatTxt);
            difficulty = itemView.findViewById(R.id.quizDiffTxt);
            startDate = itemView.findViewById(R.id.startDTxt);
            endDate = itemView.findViewById(R.id.endDTxt);
            likes = itemView.findViewById(R.id.likesTxt);
            playBtn = itemView.findViewById(R.id.PlayBtn);
            likeBtn = itemView.findViewById(R.id.LikeBtn);
        }
    }
}

