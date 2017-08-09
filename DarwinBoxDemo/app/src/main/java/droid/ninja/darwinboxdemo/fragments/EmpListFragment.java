package droid.ninja.darwinboxdemo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import droid.ninja.darwinboxdemo.EmpRequestHandler;
import droid.ninja.darwinboxdemo.EmployeeActivity;
import droid.ninja.darwinboxdemo.R;
import droid.ninja.darwinboxdemo.adapters.EmployeeAdapter;
import droid.ninja.darwinboxdemo.dao.UpdateableFragment;
import droid.ninja.darwinboxdemo.listeners.MyItemClickListener;
import droid.ninja.darwinboxdemo.pojo.Employee;
import droid.ninja.darwinboxdemo.utils.RequestURL;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by mukesh on 8/9/17.
 */

public class EmpListFragment extends Fragment implements SearchView.OnQueryTextListener, UpdateableFragment {

    private RecyclerView mRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Gson gson;
    private List<Employee> employees = new ArrayList<>();
    private ProgressDialog mDialogPD;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "Emp List";

    public EmpListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EmpListFragment newInstance(int sectionNumber) {
        EmpListFragment fragment = new EmpListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emp_list, container, false);
        init();
        setHasOptionsMenu(true);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_emp_list);
        mRecyclerView.setHasFixedSize(true);
        mEmployeeAdapter = new EmployeeAdapter(employees);
        mEmployeeAdapter.setOnItemClickListener(viewClickListener);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mEmployeeAdapter);
        loadEmpList();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView
                    .ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    //item removed from recylcerview
                    //employees.remove(position);
                    EmpRequestHandler empRequestHandler = new EmpRequestHandler(getActivity());
                    empRequestHandler.deleteEmp(String.valueOf(employees.get(position).id), new EmpRequestHandler.EmpDeletListener() {

                        @Override
                        public void onFinish(String res) {
                            mEmployeeAdapter.notifyItemRemoved(position);
                            Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(String str) {
                            Log.d(TAG, "Error: "+str);

                        }
                    });
                }

            }
        });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        return rootView;
    }

    private void init() {
        mDialogPD = new ProgressDialog(getActivity());
        mDialogPD.setCancelable(false);
        mDialogPD.setCanceledOnTouchOutside(false);
        mDialogPD.setMessage("Loading Emp, please wait...");

    }

    private void loadEmpList(){

        mDialogPD.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestURL.EMP_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        employees = new LinkedList<Employee>(Arrays.asList(gson.fromJson(response, Employee[].class)));
                        if (employees != null && !employees.isEmpty()) {
                            mEmployeeAdapter.updateItems(employees);
                        }
                        mDialogPD.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialogPD.dismiss();
                Log.d(TAG, error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_employee, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        mEmployeeAdapter.setFilter(employees);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Employee> filteredModelList = filter(employees, newText);

        mEmployeeAdapter.setFilter(filteredModelList);
        return true;
    }

    private List<Employee> filter(List<Employee> models, String query) {
        query = query.toLowerCase();
        final List<Employee> filteredModelList = new ArrayList<>();
        for (Employee model : models) {
            final String textName = model.name.toLowerCase();
            final String textId = String.valueOf(model.id).toLowerCase();
            final String textdesg = model.desg.toLowerCase();
            final String textAge = String.valueOf(model.age).toLowerCase();
            if (textName.contains(query) || textId.contains(query) || textdesg.contains(query) || textAge.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private MyItemClickListener.OnItemClickListener viewClickListener = new MyItemClickListener.OnItemClickListener() {
        @Override
        public void OnItemClick(View view, int position) {

            Employee empVar = employees.get(position);
            ((EmployeeActivity)getActivity()).jumpFragment(empVar, 1);


        }
    };

    @Override
    public void update(Employee emp) {
        if (emp != null) {
            employees.add(emp);
            mEmployeeAdapter.updateItems(employees);
        }

    }


}
