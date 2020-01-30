package com.gh.nimbus2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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


public class MainActivity extends AppCompatActivity {
    RecyclerView quizrecyclerView;
    RequestQueue queue;
    final String BASE_URL="https://still-dawn-92078.herokuapp.com";
    ArrayList<Id_Value> quiztypes =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quizrecyclerView=findViewById(R.id.quizrecyclerview);
        queue = Volley.newRequestQueue(MainActivity.this);

        quizrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizrecyclerView.setAdapter(new QuizRecyclerAdapter(MainActivity.this,quiztypes));
        getdata();
        quizrecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, quizrecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
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

        StringRequest stringRequest = new StringRequest(Request.Method.GET,BASE_URL+"/quiz/departments", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        Id_Value idValue =new Id_Value(jsonArray.getJSONObject(i).getString("departmentName"),
                                jsonArray.getJSONObject(i).getString("departmentId"));
                        quiztypes.add(idValue);
                        Objects.requireNonNull(quizrecyclerView.getAdapter()).notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Loggerreer",error.getMessage());

            }
        });


        queue.add(stringRequest);


    }





    private void postdata(final int position){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,BASE_URL+"/quiz/departments", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Intent i=new Intent(MainActivity.this,DepartmentQuiz.class);
                i.putExtra("quiz",response);
                i.putExtra("departmentname",quiztypes.get(position).getValue());
                startActivity(i);

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
                params.put("deptId",quiztypes.get(position).getId());
                return params;
            }

        };

        queue.add(stringRequest);

    }




}
