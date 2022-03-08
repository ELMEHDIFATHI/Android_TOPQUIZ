package com.mehdi.topquiz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mehdi.topquiz.R;
import com.mehdi.topquiz.models.Question;
import com.mehdi.topquiz.models.QuestionBank;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4 ;
    Question mcurrentQuestion;
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";

    private int mRemainingQuestionCount;
    private int mscore;
     QuestionBank mQuestionBank = generateQuestionBank();
     boolean mEnableTouchEvents;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_SCORE, mscore);
        outState.putInt(BUNDLE_STATE_QUESTION, mRemainingQuestionCount);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
         return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mRemainingQuestionCount = 4;

        mTextView = findViewById(R.id.game_activity_textview_question);
        mButton1   = findViewById(R.id.game_activity_button_1);
        mButton2   = findViewById(R.id.game_activity_button_2);
        mButton3   = findViewById(R.id.game_activity_button_3);
        mButton4   = findViewById(R.id.game_activity_button_4);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);

        mEnableTouchEvents=true;
        if (savedInstanceState != null) {
            mscore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mscore = 0;
            mRemainingQuestionCount = 4;
        }

        mcurrentQuestion = mQuestionBank.getCurrentQuestion();
        displayQuestion(mcurrentQuestion);
    }

    private void displayQuestion(final Question question) {
        mTextView.setText(question.getQuestion());
        mButton1.setText(question.getChoiceList().get(0));
        mButton1.setText(question.getChoiceList().get(1));
        mButton1.setText(question.getChoiceList().get(2));
        mButton1.setText(question.getChoiceList().get(3));
    }


    private QuestionBank generateQuestionBank(){
        Question question1 = new Question(
                "Who is the creator of Android?",
                Arrays.asList(
                        "Andy Rubin",
                        "Steve Wozniak",
                        "Jake Wharton",
                        "Paul Smith"
                ),
                0
        );

        Question question2 = new Question(
                "When did the first man land on the moon?",
                Arrays.asList(
                        "1958",
                        "1962",
                        "1967",
                        "1969"
                ),
                3
        );

        Question question3 = new Question(
                "What is the house number of The Simpsons?",
                Arrays.asList(
                        "42",
                        "101",
                        "666",
                        "742"
                ),
                3
        );

        return new QuestionBank(Arrays.asList(question1, question2, question3));


    }


    @Override
    public void onClick(View v) {
        int index;

        if (v == mButton1) {
            index = 0;
        } else if (v == mButton2) {
            index = 1;
        } else if (v == mButton3) {
            index = 2;
        } else if (v == mButton4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }


        if (index == mQuestionBank.getCurrentQuestion().getAnswerIndex()) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            mscore++;
        } else {
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }
        mEnableTouchEvents=false;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // If this is the last question, ends the game.
                mRemainingQuestionCount--;

                if (mRemainingQuestionCount > 0) {
                    mcurrentQuestion = mQuestionBank.getNextQuestion();
                    displayQuestion(mcurrentQuestion);
                } else {
                    endGame();
                }
                mEnableTouchEvents=true;

            }
        }, 2_000); // LENGTH_SHORT is usually 2 second long




    }
    private void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done!")
                .setMessage("Your score is " + mscore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mscore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }
}