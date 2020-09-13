package com.example.rural_essential.ui.quiz;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rural_essential.R;
import com.example.rural_essential.ui.model.Quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizCardAdapter extends ArrayAdapter<Quiz> {
    private Context quizContext;
    private List<Quiz> quizList= new ArrayList<>();
    public QuizCardAdapter(Context context, ArrayList<Quiz> quizList){
        super(context, R.layout.quiz_card_item, quizList);
        this.quizContext = context;
        this.quizList = quizList;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View cardView = convertView;
        //set up quiz card in review
        if(cardView == null)
            cardView = LayoutInflater.from(quizContext).inflate(R.layout.quiz_card_item,parent,false);
        Quiz quiz = quizList.get(position);
        TextView questions = cardView.findViewById(R.id.question_description_ListView);
        questions.setText(quiz.getQuestion());
        // choices
        TextView a = cardView.findViewById(R.id.answer_a_ListView);
        a.setText(" A. " + quiz.getChoices().get(0));
        a.setBackgroundColor(Color.rgb(230, 247, 255));
        TextView b = cardView.findViewById(R.id.answer_b_ListView);
        b.setText(" B. " + quiz.getChoices().get(1));
        b.setBackgroundColor(Color.rgb(230, 247, 255));
        TextView c = cardView.findViewById(R.id.answer_c_ListView);
        c.setText(" C. " + quiz.getChoices().get(2));
        c.setBackgroundColor(Color.rgb(230, 247, 255));
        TextView answer = cardView.findViewById(R.id.correct_answer_ListView);
        answer.setText("");
        TextView correctWrong  = cardView.findViewById(R.id.correct_or_wrong_ListView);
        correctWrong.setText("");
        // color correct or wrong
        if(!quiz.getChoices().get(quiz.getSelected()).equals(quiz.getAnswer())){

            answer.setText(quiz.getAnswer());
            correctWrong.setText(" Selection was wrong");
            switch (quiz.getSelected()){
                case 0:
                    a.setBackgroundColor(Color.rgb(234, 71, 74));
                    break;
                case 1:
                    b.setBackgroundColor(Color.rgb(234, 71, 74));
                    break;
                case 2:
                    c.setBackgroundColor(Color.rgb(234, 71, 74));
                    break;
            }
        }
        else {
            switch (quiz.getSelected()){
                case 0:
                    a.setBackgroundColor(Color.rgb(0, 136, 32));
                    break;
                case 1:
                    b.setBackgroundColor(Color.rgb(0, 136, 32));
                    break;
                case 2:
                    c.setBackgroundColor(Color.rgb(0, 136, 32));
                    break;
            }
        }
        return cardView;
    }

}
