package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.reidius.lawrenceafriyie.overwatchmap.ClosedIncidents;
import com.reidius.lawrenceafriyie.overwatchmap.InProgress;
import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.adapter.IncidentViewAdapter;
import com.reidius.lawrenceafriyie.overwatchmap.adapter.SectionsPagerAdapter;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiClient;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiInterface;
import com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncidentView extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private IncidentViewAdapter incidentViewAdapter;
    private List<ReportIncident> incidentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_view);

        recyclerView = (RecyclerView)findViewById(R.id.incident_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        incidentViewAdapter = new IncidentViewAdapter();
        recyclerView.setAdapter(incidentViewAdapter);


        incidentList = new ArrayList<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<ReportIncident>> call = apiInterface.getIncidents("Open");

        call.enqueue(new Callback<List<ReportIncident>>() {
            @Override
            public void onResponse(Call<List<ReportIncident>> call, Response<List<ReportIncident>> response) {
                incidentList = response.body();
                incidentViewAdapter.IncidentViewAdapter(getApplicationContext(), incidentList);
                Log.d("IncidentView", incidentList.toString());
            }

            @Override
            public void onFailure(Call<List<ReportIncident>> call, Throwable t) {
                Log.d("IncidentView", t.toString());
            }
        });

        // Tab Setup
        setupTabLayout();
    }
    // Search for incidents
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.incident_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.incident_serach).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                incidentViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                incidentViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.incident_serach){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    // Tab
    private void setupTabLayout(){
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabTapped(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabTapped(tab.getPosition());
            }
        });
    }
    private void onTabTapped(int position){
        switch (position){
            case 0:
                Intent intent = new Intent(this, IncidentView.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, InProgress.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(this, ClosedIncidents.class);
                startActivity(intent2);
                break;
        }
    }
}
