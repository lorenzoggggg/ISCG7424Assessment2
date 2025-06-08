package com.example.iscg7424assessment2part1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }

    private Context context;
    private List<Quiz> quizList;
    private OnQuizClickListener listener;

    public QuizAdapter(Context context, List<Quiz> quizList, OnQuizClickListener listener) {
        this.context = context;
        this.quizList = quizList;
        this.listener = listener;
    }

    private static final Map<String, String> categoryNames = new HashMap<String, String>() {{
        put("9", "General Knowledge");
        put("11", "Film");
        put("12", "Music");
        put("15", "Video Games");
        put("17", "Nature & Science");
        put("18", "Computer Science");
        put("22", "Geography");
        put("23", "History");
        put("27", "Animals");
        put("28", "Vehicles");
        put("31", "Anime & Manga");
    }};

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.nameTxt.setText("Name: " + quiz.name);
        holder.catTxt.setText("Category: " + categoryNames.getOrDefault(quiz.category, "Unknown"));
        holder.diffTxt.setText("Difficulty: " + quiz.difficulty);
        holder.startTxt.setText("Start Date: " + quiz.startDate);
        holder.endTxt.setText("End Date: " + quiz.endDate);
        holder.likesTxt.setText("Likes: " + quiz.likes);

        holder.itemView.setOnClickListener(v -> listener.onQuizClick(quiz));
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, catTxt, diffTxt, startTxt, endTxt, likesTxt;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.quizNameTxt);
            catTxt = itemView.findViewById(R.id.quizCatTxt);
            diffTxt = itemView.findViewById(R.id.quizDiffTxt);
            startTxt = itemView.findViewById(R.id.startDTxt);
            endTxt = itemView.findViewById(R.id.endDTxt);
            likesTxt = itemView.findViewById(R.id.likesTxt);
        }
    }
}
