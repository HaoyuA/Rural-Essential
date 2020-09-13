package com.example.rural_essential.ui.quiz;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.example.rural_essential.LoadingDialog;
import com.example.rural_essential.R;
import com.example.rural_essential.ui.model.Quiz;
import com.example.rural_essential.ui.model.Quizzes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuizFragment extends Fragment implements View.OnClickListener {

    private String quizzesRaw;
    private QuizCardAdapter quizCardAdapter;
    private Button choice_a;
    private Button choice_b;
    private Button choice_c;
    private Button nextQuestion;
    private Button startQuizButton;
    private Button finishAttempt;
    private TextView question;
    private TextView answer;
    private TextView nthQuestion;
    private Quiz quiz;
    private CardView startQuizCard;
    private ScrollView quizCardView;
    private TextView correctWrong;
    private LinearLayout feedback;
    private ArrayList<Quiz> quizzes;
    private ListView quizListView;
    private int nQuestion;
    private int quizFragmentStateValue;
    private LottieAnimationView animationView;
    private LoadingDialog loadingDialog;
    private ConstraintLayout quizCardConstraintLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);
        //initialize global variables
        nQuestion = 0;
        loadingDialog = new LoadingDialog(getActivity());
        startQuizCard = root.findViewById(R.id.start_title_card);
        quizCardView = root.findViewById(R.id.quiz_card);
        startQuizButton = root.findViewById(R.id.start_quiz_button);
        nextQuestion = root.findViewById(R.id.next_question_button);
        choice_a = root.findViewById(R.id.answer_a);
        choice_b = root.findViewById(R.id.answer_b);
        choice_c = root.findViewById(R.id.answer_c);
        question = root.findViewById(R.id.question_description);
        answer = root.findViewById(R.id.correct_answer);
        correctWrong = root.findViewById(R.id.correct_or_wrong);
        nthQuestion = root.findViewById(R.id.num_of_questions);
        animationView = root.findViewById(R.id.animation_view);
        feedback = root.findViewById(R.id.feedback);
        quizListView = root.findViewById(R.id.review_quizList);
        quizCardConstraintLayout = root.findViewById(R.id.constraintLayoutForQuizList);
        finishAttempt = root.findViewById(R.id.finish_this_attempt_button);
        ArrayList<CardView> quizCards = new ArrayList<>();
        //set on click listener
        startQuizButton.setOnClickListener(this);
        nextQuestion.setOnClickListener(this);
        choice_a.setOnClickListener(this);
        choice_b.setOnClickListener(this);
        choice_c.setOnClickListener(this);
        finishAttempt.setOnClickListener(this);
////        TextView textView = root.findViewById(R.id.text_quizzes);

        return root;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //start quiz
            case R.id.start_quiz_button:
                startQuizCard.setVisibility(View.INVISIBLE);
                quizCardView.setVisibility(View.VISIBLE);
                quizzes = new ArrayList<Quiz>();
                //get quizzes from server
                GetQuizzesAsync getQuizzesAsync = new GetQuizzesAsync();
                getQuizzesAsync.execute();
                break;
            //check if correct
            case R.id.answer_a:
                checkSelectedAnswer(nQuestion, 0, choice_a);
                break;
            case R.id.answer_b:
                checkSelectedAnswer(nQuestion, 1, choice_b);
                break;
            case R.id.answer_c:
                checkSelectedAnswer(nQuestion, 2, choice_c);
                break;
            //display next question
            case R.id.next_question_button:
                nQuestion += 1;
                if (nQuestion < 4) {
                    setQuizCard(nQuestion, quizzes);
                    feedback.setVisibility(View.GONE);
                    answer.setVisibility(View.GONE);
                } else if (nQuestion == 4) {
                    setQuizCard(nQuestion, quizzes);
                    nextQuestion.setText("Finish & Review");
                    feedback.setVisibility(View.GONE);
                    answer.setVisibility(View.GONE);
                } else {
                    quizCardView.setVisibility(View.INVISIBLE);
                    feedback.setVisibility(View.GONE);
                    choice_a.setText("");
                    choice_b.setText("");
                    choice_c.setText("");
                    choice_a.setBackgroundColor(Color.rgb(230, 247, 255));
                    choice_b.setBackgroundColor(Color.rgb(230, 247, 255));
                    choice_c.setBackgroundColor(Color.rgb(230, 247, 255));
                    answer.setText("");
                    question.setText("");
                    correctWrong.setText("");
                    nthQuestion.setText("");
                    quizCardAdapter = new QuizCardAdapter(getContext(), quizzes);
                    quizListView.setAdapter(quizCardAdapter);
                    quizCardConstraintLayout.setVisibility(View.VISIBLE);
                    nextQuestion.setText("Next Question");
                    nQuestion = 0;
                }
                break;
            case R.id.finish_this_attempt_button:
                startQuizCard.setVisibility(View.VISIBLE);
                quizCardConstraintLayout.setVisibility(View.INVISIBLE);
        }
    }

    private class GetQuizzesAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            loadingDialog.startLoadingDialog();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return Quizzes.getQUIZZES();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONObject(result).getJSONArray("body");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ArrayList<String> choices = new ArrayList<>();
                    choices.add(jsonObject.getString("CHOICE_1"));
                    choices.add(jsonObject.getString("CHOICE_2"));
                    choices.add(jsonObject.getString("CHOICE_3"));
                    quiz = new Quiz(jsonObject.getString("QUESTION"),
                            choices, jsonObject.getString("ANSWER"));
                    quizzes.add(quiz);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setQuizCard(nQuestion, quizzes);
            loadingDialog.dismissDialog();
        }
    }

    private void setQuizCard(int nQuestion, ArrayList<Quiz> quizzes) {
        quiz = quizzes.get(nQuestion);
        question.setText(quiz.getQuestion());
        choice_a.setText(" A. " + quiz.getChoices().get(0));
        choice_b.setText(" B. " + quiz.getChoices().get(1));
        choice_c.setText(" C. " + quiz.getChoices().get(2));
        choice_a.setEnabled(true);
        choice_b.setEnabled(true);
        choice_c.setEnabled(true);
        choice_a.setBackgroundColor(Color.rgb(230, 247, 255));
        choice_b.setBackgroundColor(Color.rgb(230, 247, 255));
        choice_c.setBackgroundColor(Color.rgb(230, 247, 255));
        correctWrong.setText("");
        answer.setText("");
        nthQuestion.setText("   " + (nQuestion + 1) + "/5");
        nextQuestion.setEnabled(false);
    }

    private void checkSelectedAnswer(int nQuestion, int selection, Button selectedButton) {
        quiz = quizzes.get(nQuestion);
        choice_a.setEnabled(false);
        choice_b.setEnabled(false);
        choice_c.setEnabled(false);
        quizzes.get(nQuestion).setSelected(selection);
        nextQuestion.setEnabled(true);
        if (quiz.getChoices().get(selection).equals(quiz.getAnswer())) {
            selectedButton.setBackgroundColor(Color.rgb(0, 136, 32));
            correctWrong.setText("The answer was correct.");
            //show right animation
            displayRight();
        } else {
            selectedButton.setBackgroundColor(Color.rgb(234, 71, 74));
            correctWrong.setText("The answer was incorrect.");
            answer.setVisibility(View.VISIBLE);
            answer.setText("The correct answer was: " + quiz.getAnswer());
            answer.setTextColor(Color.rgb(0, 136, 32));
            //show wrong animation
            displayWrong();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void displayRight() {
        LottieDrawable drawable = new LottieDrawable();
        LottieComposition.Factory.fromAssetFileName(requireActivity(), "right.json", (composition -> {
            drawable.setComposition(composition);
            drawable.playAnimation();
            drawable.setScale(1);
            animationView.setImageDrawable(drawable);
        }));
        feedback.setVisibility(View.VISIBLE);
    }

    private void displayWrong() {
        LottieDrawable drawable = new LottieDrawable();
        LottieComposition.Factory.fromAssetFileName(requireActivity(), "wrong.json", (composition -> {
            drawable.setComposition(composition);
            drawable.playAnimation();
            drawable.setScale(1);
            animationView.setImageDrawable(drawable);
        }));
        feedback.setVisibility(View.VISIBLE);
    }
}
