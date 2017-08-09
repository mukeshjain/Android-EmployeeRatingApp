package droid.ninja.darwinboxdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import droid.ninja.darwinboxdemo.EmployeeActivity;
import droid.ninja.darwinboxdemo.R;
import droid.ninja.darwinboxdemo.dao.UpdateableFragment;
import droid.ninja.darwinboxdemo.pojo.Employee;
import droid.ninja.darwinboxdemo.utils.RequestURL;

/**
 * Created by mukesh on 8/9/17.
 */

public class EmpDetailFragment extends Fragment implements UpdateableFragment{

    private static final String TAG = EmpDetailFragment.class.getSimpleName();

    private EditText etComment;
    private TextView tvName, tvDesg, tvAge;
    private RatingBar rbRate;
    private Button bSubmit;
    private Employee mEmployee;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "Emp Details";

    public EmpDetailFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EmpDetailFragment newInstance(int sectionNumber) {
        EmpDetailFragment fragment = new EmpDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emp_details, container, false);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        tvDesg = (TextView) rootView.findViewById(R.id.tv_desg);
        tvAge = (TextView) rootView.findViewById(R.id.tv_age);
        etComment = (EditText) rootView.findViewById(R.id.et_comment);
        rbRate = (RatingBar) rootView.findViewById(R.id.rb_rate);
        bSubmit = (Button) rootView.findViewById(R.id.b_submit);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmployee != null) {
                    submitRating();
                }else{
                    Toast.makeText(getActivity(), "Please select employee for rating process", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void update(Employee emp) {
        mEmployee = emp;
        tvName.setText(emp.name);
        tvDesg.setText(emp.desg);
        tvAge.setText(String.valueOf(emp.age));

    }

    private void submitRating() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RequestURL.EMP_RATING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getActivity(), "Rating done successfully", Toast.LENGTH_SHORT).show();
                        ((EmployeeActivity)getActivity()).jumpFragment(0);
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
                params.put("formTitle","Presentation Skills Rating");
                params.put("submitFor",String.valueOf(mEmployee.id));
                params.put("rating", String.valueOf(rbRate.getNumStars()));
                params.put("comment", etComment.getText().toString().trim());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
