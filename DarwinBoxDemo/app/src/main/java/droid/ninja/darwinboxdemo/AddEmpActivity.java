package droid.ninja.darwinboxdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import droid.ninja.darwinboxdemo.pojo.Employee;
import droid.ninja.darwinboxdemo.utils.RequestURL;

public class AddEmpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AddEmpActivity.class.getSimpleName();

    private EditText etName, etDesg, etAge;
    private Button bCreate;
    private Employee employee;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        etName = (EditText) findViewById(R.id.et_name);
        etDesg = (EditText) findViewById(R.id.et_desg);
        etAge = (EditText) findViewById(R.id.et_age);

        bCreate = (Button) findViewById(R.id.b_create);
        bCreate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_create: {
                if (etName.getText().toString().trim().equalsIgnoreCase("")){
                    etName.setError("Please enter name");
                }else if(etDesg.getText().toString().trim().equalsIgnoreCase("")){
                    etDesg.setError("Please enter designation");
                }else if(etAge.getText().toString().trim().equalsIgnoreCase("")){
                    etAge.setError("Please enter age");
                }else {
                    createEmp();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void createEmp() {
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RequestURL.EMP_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "res: "+response);
                        Toast.makeText(AddEmpActivity.this, "Employee added successfully", Toast.LENGTH_SHORT).show();
                        employee = gson.fromJson(response, Employee.class);
                        activityFinish(employee);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",etName.getText().toString());
                params.put("designation",etDesg.getText().toString());
                params.put("age", etAge.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void activityFinish(Employee empVar){
        Intent intent = getIntent();
        intent.putExtra("emp", gson.toJson(empVar));
        setResult(RESULT_OK, intent);
        finish();
    }


}
