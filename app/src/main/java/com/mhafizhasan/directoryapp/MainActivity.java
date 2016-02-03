package com.mhafizhasan.directoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    EmployeeDatabase db;
    boolean isSelectMode;

    static EmployeeDatabase.Employee selectedEmployee = null;

    /////////////////////////////////////////////////////////////////////

    Girl[] girls;
    Boy[] boys;

    class Boy {

        final String name;
        final int index;

        double[] rating =  new double[girls.length];
        double currentRating = 100;

        Girl girlfriend = null;

        Boy(String name, int index) {
            this.name = name;
            this.index = index;
        }

        boolean propose() {
            if(girlfriend != null)
                return false;
            // Choose the best girl to propose
            double bestRating = -1;
            int index = -1;
            for(int c = 0; c < girls.length; c++) {
                if(rating[c] > bestRating && rating[c] < currentRating) {
                    bestRating = rating[c];
                    index = c;
                }
            }
            // Found next best girl, propose to her
            currentRating = bestRating;
            girls[index].receiveProposal(this);

            return true;
        }

        void breakup() {
            girlfriend = null;
        }

        void couple(Girl girl) {
            girlfriend = girl;
        }

    }



    class Girl {

        final String name;
        final int index;

        double[] rating = new double[boys.length];

        Boy[] proposals = new Boy[boys.length];
        int numProposals = 0;

        Boy boyfriend = null;
        double currentRating = -1;

        Girl(String name, int index) {
            this.name = name;
            this.index = index;
        }

        void receiveProposal(Boy boy) {
            // Remember this proposal for later consideration
            proposals[numProposals] = boy;
            numProposals++;

        }

        void selectBestBoyfriend() {
            Boy betterBoy = null;
            double betterRating = 0;
            for(int c = 0; c < numProposals; c++) {
                int boyIndex = proposals[c].index;
                if(rating[boyIndex] > betterRating) {
                    betterBoy = proposals[c];
                    betterRating = rating[boyIndex];
                }
            }
            // Clear proposals
            numProposals = 0;
            if(betterBoy == null)
                return;
            // Couple if better
            if(boyfriend != null) {
                boyfriend.breakup();
            }
            boyfriend = betterBoy;
            currentRating = betterRating;
            boyfriend.couple(this);
        }
    }

    void runPerfectMarriageSimulation() {
        // First prepare a list of boys & girls
        boys = new Boy[3];
        girls = new Girl[3];

        for(int c = 0; c < boys.length; c++) {
            boys[c] = new Boy("Boy-" + c, c);
            girls[c] = new Girl("Girl-" + c, c);

            for(int i = 0; i < boys.length; i++) {
                boys[c].rating[i] = Math.random();
                girls[c].rating[i] = Math.random();
            }
        }

        /*
        // Prepare population
        boys[0] = new Boy("John", 0);
        boys[1] = new Boy("James", 1);
        boys[2] = new Boy("Ali", 2);

        girls[0] = new Girl("Chanel", 0);
        girls[1] = new Girl("Nadia", 1);
        girls[2] = new Girl("Nurul", 2);

        // Prepare rating
        boys[0].rating[0] = 1.0;
        boys[0].rating[1] = 0.66;
        boys[0].rating[2] = 0.33;

        boys[1].rating[0] = 1.0;
        boys[1].rating[1] = 0.33;
        boys[1].rating[2] = 0.66;

        boys[0].rating[0] = 0.66;
        boys[0].rating[1] = 1.0;
        boys[0].rating[2] = 0.33;

        girls[0].rating[0] = 0.33;
        girls[0].rating[1] = 0.66;
        girls[0].rating[2] = 1.0;

        girls[1].rating[0] = 0.66;
        girls[1].rating[1] = 1.0;
        girls[1].rating[2] = 0.33;

        girls[2].rating[0] = 0.66;
        girls[2].rating[1] = 1.0;
        girls[2].rating[2] = 0.33;
        */

        // Run the simulation
        int numProposals = 0;
        int numberOfDays = 1;
        do {
            // Simulate this day
            Log.e("StableMarriageProblem", "Running a simulation for day " + numberOfDays++);
            // simulate this day
            numProposals = 0;

            // For each boy, propose
            for(int c = 0; c < boys.length; c++) {
                if(boys[c].propose()) {
                    numProposals++;
                }
            }

            // Next, girls choose the best proposal
            for(int c = 0; c < girls.length; c++) {
                girls[c].selectBestBoyfriend();
            }

        } while (numProposals > 0);

        // Display optimal solution
        for(int c = 0; c < boys.length; c++) {
            Log.e("StableMarriageProblem", boys[c].name + " <3 " + boys[c].girlfriend.name +
                " with " + boys[c].currentRating + " " + boys[c].girlfriend.currentRating);
        }

    }

    /////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //runPerfectMarriageSimulation();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_directory);

        // check if select mode
        isSelectMode = getIntent().getBooleanExtra("isSelectMode", false);

        // Load database
        db = new EmployeeDatabase(this);

        // Respond to search feature
        Button searchButton = (Button) findViewById(R.id.am_ei_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO respond to button click
                // refresh();
                reset();
            }
        });

        // Respond to search input
        EditText searchInputView = (EditText) findViewById(R.id.am_ei_search_input);
        searchInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // dont care
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // dont care
            }

            @Override
            public void afterTextChanged(Editable s) {
                refresh();
            }
        });

        // Show all first
        refresh();

        // Respond to Floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, CreateEmployeeActivity.class);
                startActivity(intent);
            }
        });
        if(isSelectMode) {
            fab.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Select Supervisor");
        }

    }

    @Override
    protected  void onResume() {
        super.onResume();

        refresh();
    }

    public void reset() {
        // Get input
        EditText searchInputView = (EditText) findViewById(R.id.am_ei_search_input);
        searchInputView.getEditableText().clear();
        // Refresh
        refresh();
    }

    public void refresh() {
        // Get input
        EditText searchInputView = (EditText) findViewById(R.id.am_ei_search_input);
        String name = searchInputView.getEditableText().toString();
        // Search for it
        search(name);
    }

    public void search(String name) {
        // TODO Fill up ListView
        ListView listView = (ListView) findViewById(R.id.am_ei_listview);
        // Get all employees
        EmployeeDatabase.Employee employees[] = db.searchByName(name);
        EmployeeListAdapter adapter = new EmployeeListAdapter(this, employees);
        // Show list
        listView.setAdapter(adapter);

    }

}
