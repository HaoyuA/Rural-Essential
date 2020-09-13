package com.example.rural_essential.ui.model;

import java.util.ArrayList;

public class Quiz {
    private String question;
    private ArrayList<String> choices;
    private String answer;
    private int selected;
    public Quiz (){

    }
    public Quiz(String question, ArrayList<String> choices, String answer){
        this.question = question;
        this.choices = choices;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public int getSelected() {
        return selected;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
