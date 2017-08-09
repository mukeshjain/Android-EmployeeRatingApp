package droid.ninja.darwinboxdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import droid.ninja.darwinboxdemo.utils.RequestURL;

/**
 * Created by mukesh on 8/9/17.
 */

public class EmpRequestHandler {

    private static final String TAG = EmpRequestHandler.class.getSimpleName();

    private Context mContext;
    private EmpDeletListener empDeletListener;
    private ProgressDialog mDialogPD;

    public EmpRequestHandler(Context mContext){
        this.mContext = mContext;
        init();
    }

    private void init() {
        mDialogPD = new ProgressDialog(mContext);
        mDialogPD.setCancelable(false);
        mDialogPD.setCanceledOnTouchOutside(false);
        mDialogPD.setMessage("Processing, please wait...");

    }

    public void deleteEmp(final String id, EmpDeletListener listener){
        mDialogPD.show();
        empDeletListener = listener;

        RequestQueue queue = Volley.newRequestQueue(mContext);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RequestURL.EMP_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mDialogPD.dismiss();
                        empDeletListener.onFinish(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialogPD.dismiss();
                Log.d(TAG, error.getMessage());
                empDeletListener.onError(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public interface EmpDeletListener {

        void onFinish(String res);

        void onError(String str);
    }

}
