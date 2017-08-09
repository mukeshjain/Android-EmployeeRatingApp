package droid.ninja.darwinboxdemo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import droid.ninja.darwinboxdemo.R;
import droid.ninja.darwinboxdemo.listeners.MyItemClickListener;
import droid.ninja.darwinboxdemo.pojo.Employee;

/**
 * Created by mukesh on 8/8/17.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> implements MyItemClickListener{

    private List<Employee> mEmployees;
    private String COLON = ": ";
    private MyItemClickListener.OnItemClickListener viewClickListener;

    public EmployeeAdapter(List<Employee> mEmployees){
        this.mEmployees = mEmployees;
    }

    public void updateItems(List<Employee> employees){
        mEmployees = employees;
        notifyDataSetChanged();
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_employee_view, parent, false);
        EmployeeViewHolder viewHolder = new EmployeeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, final int position) {
        Employee employee = mEmployees.get(position);

        holder.tvId.setText(COLON+String.valueOf(employee.id));
        holder.tvName.setText(COLON+employee.name);
        holder.tvAge.setText(COLON+String.valueOf(String.valueOf(employee.age)));
        holder.tvDesg.setText(COLON+employee.desg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClickListener.OnItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEmployees.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder{

        private TextView tvId, tvName, tvAge, tvDesg;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAge = (TextView) itemView.findViewById(R.id.tv_age);
            tvDesg = (TextView) itemView.findViewById(R.id.tv_desg);
        }
    }

    public void setFilter(List<Employee> empModels) {
        mEmployees = new ArrayList<>();
        mEmployees.addAll(empModels);
        notifyDataSetChanged();
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener mListener) {
        viewClickListener = mListener;

    }

}
