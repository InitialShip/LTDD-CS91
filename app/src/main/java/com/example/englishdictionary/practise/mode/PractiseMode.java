package com.example.englishdictionary.practise.mode;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.englishdictionary.practise.Card;
import com.example.englishdictionary.practise.Deck;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public abstract class PractiseMode {
    //TODO them dong nghia
    protected String name;
    protected String description;
    protected Deck selectedDeck;
    public static final Random rand = new Random();
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public PractiseMode(){}

    public void setSelectedDeckDeck(Deck newDeck){
        selectedDeck = newDeck;
        selectedDeck.sortDeck();
    }

    public Deck getSelectedDeck(){
        return selectedDeck;
    }

    public String getDescription(){
        return description;
    }

    public abstract void enter();

    public abstract void exit();

    public abstract String getQuestion();

    public abstract List<String> getAnswers();

    public abstract String getCorrectAnswer();

    public abstract boolean inputAnswer(String answer);

    public abstract boolean isEndOfDeck();
}
