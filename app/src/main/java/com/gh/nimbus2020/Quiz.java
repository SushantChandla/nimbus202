package com.gh.nimbus2020;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Quiz extends AppCompatActivity {
    TextView questionView,questionnumber,timeview;
    Button option1;
    Button option2;
    Button option3;
    Button option4;
    RequestQueue requestQueue;
    List<QuestionData> questions = new ArrayList<>();
    int counter=0;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Objects.requireNonNull(getSupportActionBar()).hide();
        timer = new CountDownTimer(15000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
            timeview.setText(millisUntilFinished/1000+"s");
            }

            public void onFinish() {
                counter++;
                if(counter<questions.size()) {
                    updateQuestion();

                }
                else {

                    getscore();
                }
            }
        };

        questionnumber=findViewById(R.id.quizquestionnumber);
        timeview=findViewById(R.id.quizTimer);
        questionView = findViewById(R.id.quizquestion);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        requestQueue = Volley.newRequestQueue(this);
        getQuestions();



        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 questions.get(counter).setOption_chosen(1);
                timer.onFinish();


            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  questions.get(counter).setOption_chosen(4);
                timer.onFinish();
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questions.get(counter).setOption_chosen(2);
                timer.onFinish();
            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questions.get(counter).setOption_chosen(3);
                timer.onFinish();
            }
        });


    }

    private void updateQuestion() {
        questionView.setText(questions.get(counter).getQuestion());
        option1.setText(questions.get(counter).getOption_1());
        option2.setText(questions.get(counter).getOption_2());
        option3.setText(questions.get(counter).getOption_3());
        option4.setText(questions.get(counter).getOption_4());
        questionnumber.setText("Q"+(counter+1));
        timer.start();

    }


    private void getQuestions() {
        String response=getIntent().getStringExtra("questions");
        questionView.setText(response);
        JSONArray jsonArray=null;
        try {
            jsonArray= new JSONArray(response);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    QuestionData question = new QuestionData(
                            jsonArray.getJSONObject(i).getString("_id"),
                            jsonArray.getJSONObject(i).getString("question"),
                            jsonArray.getJSONObject(i).getString("option1"),
                            jsonArray.getJSONObject(i).getString("option2"),
                            jsonArray.getJSONObject(i).getString("option3"),
                            jsonArray.getJSONObject(i).getString("option4"));
                    questions.add(question);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        updateQuestion();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        finish();
    }


    private  void getscore(){
        option4.setClickable(false);
        option1.setClickable(false);
        option2.setClickable(false);
        option3.setClickable(false);
        Log.e("hiiii", "onResponse: ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://still-dawn-92078.herokuapp.com"+"/quiz/submit", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hiiii", "onResponse: "+response );
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(Quiz.this," score is "+jsonObject.getString("score"),Toast.LENGTH_LONG).show();
                    //TODO: show score
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //TODO: send answers as params
                Map<String, String> params = new HashMap<String, String>();
                Map<String,String> answer = new HashMap<>();

                for(int i=0; i<questions.size();i++){
                    answer.put("questionId"+questions.get(i).getQuestionid(),"answer"+questions.get(i).getOption_chosen());
                }
                params.put("answers",answer.toString());
                return params;
            }

        };

       requestQueue.add(stringRequest);




    }

}
