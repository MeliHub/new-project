package com.reidius.lawrenceafriyie.overwatchmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.reidius.lawrenceafriyie.overwatchmap.adapter.ViewIncidentAdapter;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiClient;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiInterface;
import com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewIncidents extends AppCompatActivity {

    final static String TAG = "ViewIncidents";


    List<ReportIncident> incidentList;
    RecyclerView recyclerView;
    ViewIncidentAdapter viewIncidentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_incidents);

        try {
            incidentList = new ArrayList<>();

            recyclerView = (RecyclerView)findViewById(R.id.incident_recycler);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<List<ReportIncident>>  call = apiInterface.getIncidents("Open");
            call.enqueue(new Callback<List<ReportIncident>>() {
                @Override
                public void onResponse(Call<List<ReportIncident>> call, Response<List<ReportIncident>> response) {
                    incidentList = response.body();
                    Log.d(TAG, "onResponse: " + incidentList.toString());

                    viewIncidentAdapter = new ViewIncidentAdapter(getApplicationContext(), incidentList);
                    recyclerView.setAdapter(viewIncidentAdapter);
                }

                @Override
                public void onFailure(Call<List<ReportIncident>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                }
            });
        }
        catch (Exception ex)
        {
            FirebaseCrash.report(new Exception("App Name : My first Android non-fatal error"));
            finish();
            startActivity(getIntent());
        }

    }
}
