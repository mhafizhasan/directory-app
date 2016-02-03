package com.mhafizhasan.directoryapp;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EmployeeDetailsActivity extends AppCompatActivity {

    // Declear luar dari mathod untuk public access. @ mathod guna object ini
    EmployeeDatabase.Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the ID of the employee that needs to be shown
        String id = getIntent().getStringExtra("EMPLOYEE_ID");

        // Open database
        EmployeeDatabase db = new EmployeeDatabase(this);
        employee = db.getEmployee(id);

        // Get views to update
        TextView nameView = (TextView) findViewById(R.id.ced_name);
        TextView titleView = (TextView) findViewById(R.id.ced_title);
        TextView ageView = (TextView) findViewById(R.id.ced_age);
        TextView genderView = (TextView) findViewById(R.id.ced_gender);
        TextView officePhoneView = (TextView) findViewById(R.id.ced_officePhone);
        TextView emailView = (TextView) findViewById(R.id.ced_email);
        TextView addressView = (TextView) findViewById(R.id.ced_address);
        TextView kpiView = (TextView) findViewById(R.id.ced_kpi);
        TextView supervisorView = (TextView) findViewById(R.id.ced_supervisorName);
        TextView idView = (TextView) findViewById(R.id.ced_id);

        // Update views
        nameView.setText(employee.name);
        titleView.setText(employee.position + ", " + employee.department);
        ageView.setText("" + employee.age);
        genderView.setText(employee.gender == 1 ? "Male" : "Female");
        officePhoneView.setText(employee.officePhone);
        emailView.setText(employee.email);
        addressView.setText(employee.address);
        kpiView.setText("" + employee.KPI);
        supervisorView.setText(employee.supervisorName);
        idView.setText(employee.ID);

        // Hide supervisor if not exist
        if(employee.supervisorID == null || employee.supervisorID.isEmpty()) {
            LinearLayout supervisorLayout = (LinearLayout) findViewById(R.id.ced_supervisor_layout);
            supervisorLayout.setVisibility(View.GONE);
        }

        // Replace activity title with firstname
        String[] names = employee.name.split(" ");
        getSupportActionBar().setTitle(names[0]);
    }

    // Method untuk handel button CALL
    public void onClickCall(View view) {
        Log.d("EmployeeDetailsActivity", "Calling number now");

        // Untuk call nih add call permision dalam AndroidManifest
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + employee.officePhone));
        startActivity(intent);
    }

    // Method untuk handel button COMPOSE
    public void onClickComposeEmail(View view) {
        Log.d("EmployeeDetailsActivity", "Compose email");

        // Guna try and catch untuk elak app crash sekiranya hp x setup email.
        try {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{employee.email});
            startActivity(intent);

            // throw new RuntimeException();  // Untuk test catch

        } catch (Throwable e) {

            // Papar msg jika mobile x setup apa2 email untuk membuat penghantaran email. Salah satu sahaja Snackbar @ Tost.
            Snackbar.make(findViewById(R.id.ced_supervisor_layout), "No e-mail setup", Snackbar.LENGTH_LONG).show();
            //Toast.makeText(this, "No e-mail setup", Toast.LENGTH_LONG).show();
        }
    }

    // Method untuk handel button SHOW
    public void onClickShowMaps(View view) {
        Log.d("EmployeeDetailsActivity", "Showing map");

        // Open web search untuk cari alamat.
        // TODO Akn betulkan kemudian untuk link dengan google maps
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, employee.address);
        startActivity(intent);
    }

    // Method untuk handel button SHARE
    public void onClickShare(View view) {
        Log.d("EmployeeDetailsActivity", "Share KPI");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "KPI");  // Ini hanya belaku untuk app yang ada Subject. Contoh Google keep!!
        intent.putExtra(Intent.EXTRA_TEXT, employee.name + "'s KPI is " + employee.KPI + "!!!");
        startActivity(Intent.createChooser(intent, "Share using"));
    }


    // Method untuk handel button OPEN supervisor
    public void onClickOpenSupervisor(View view) {
        Log.d("EmployeeDetailsActivity", "Opening supervisor activity");

        // Start EmployeeDetailsActivity
        Intent intent = new Intent(this, EmployeeDetailsActivity.class);
        intent.putExtra("EMPLOYEE_ID", employee.supervisorID);  // Send employee ID to activity
        startActivity(intent);
    }

    // Method nih untuk betulkan button back. IMPLEMENT back button bila dalam page supervisor. Untuk crate method ni just taip onOpti than pilih dari senarai pilihan autocomplete
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }
}
