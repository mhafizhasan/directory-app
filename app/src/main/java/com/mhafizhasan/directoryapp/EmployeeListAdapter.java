package com.mhafizhasan.directoryapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by dermis on 1/9/16.
 */
public class EmployeeListAdapter extends BaseAdapter {

    final MainActivity activity;
    final EmployeeDatabase.Employee[] employees;

    public EmployeeListAdapter(MainActivity activity, EmployeeDatabase.Employee[] employees) {
        this.activity = activity;
        this.employees = employees;
    }

    @Override
    public int getCount() {
        return employees.length;
    }

    @Override
    public Object getItem(int position) {
        return employees[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate row layout
        View layout = activity.getLayoutInflater().inflate(R.layout.am_employee_item, parent, false);

        // Get employee at position
        final EmployeeDatabase.Employee employee = employees[position];

        // Respond to clicks
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(activity.isSelectMode) {
                    MainActivity.selectedEmployee = employee;
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                    return;
                }
                // Show employee detail activity
                Log.d("EmployeeListAdapter", "Showing details for employee " + employee.ID);

                // Start EmployeeDetailsActivity
                Intent intent = new Intent(activity, EmployeeDetailsActivity.class);
                intent.putExtra("EMPLOYEE_ID", employee.ID);                // tell which employee
                activity.startActivity(intent);
            }
        });

        // TODO Fill up layout with employee details
        TextView nameView = (TextView) layout.findViewById(R.id.am_ei_name);
        TextView positionView = (TextView) layout.findViewById(R.id.am_ei_position);

        nameView.setText(employee.name);
        positionView.setText(employee.position + ", " + employee.department);

        return layout;
    }
}
