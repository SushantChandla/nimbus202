package com.gh.nimbus2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DepartmentQuiz extends AppCompatActivity {
    RecyclerView departmentquiz;
    TextView textView;
    RequestQueue queue;
    final String BASE_URL="https://still-dawn-92078.herokuapp.com";
    ArrayList<Id_Value> quiztypes =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_quiz);
        departmentquiz=findViewById(R.id.departmentquiz);
        textView=findViewById(R.id.departmentname);

        queue = Volley.newRequestQueue(this);

        departmentquiz.setLayoutManager(new LinearLayoutManager(this));
        departmentquiz.setAdapter(new QuizRecyclerAdapter(this,quiztypes));
        getdata();
        departmentquiz.addOnItemTouchListener(
                new RecyclerItemClickListener(this, departmentquiz ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                    postdata(position);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }




    private  void getdata(){
        Intent j=getIntent();
        String response = j.getStringExtra("quiz");
        textView.setText(j.getStringExtra("departmentname"));
        try {
            JSONArray jsonArray=new JSONArray(response);
            for(int i=0;i<jsonArray.length();i++){
                Id_Value idValue =new Id_Value(jsonArray.getJSONObject(i).getString("quizName"),
                        jsonArray.getJSONObject(i).getString("_id"));
                quiztypes.add(idValue);
                Objects.requireNonNull(departmentquiz.getAdapter()).notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }








    private void postdata(final int position){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,BASE_URL+"/quiz/questions", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hi", "onResponse: "+response);
                Intent intent=new Intent(DepartmentQuiz.this,Quiz.class);
                intent.putExtra("questions",response);
                startActivity(intent);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("quizId",quiztypes.get(position).getId());
                return params;
            }

        };

        queue.add(stringRequest);

    }






}
