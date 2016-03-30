package com.foxconnbc.pushnotification.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.foxconnbc.pushnotification.R;
import com.foxconnbc.pushnotification.controller.CoachesAdapter;
import com.foxconnbc.pushnotification.model.Coach;

import java.util.ArrayList;

public class DataListingActivity extends AppCompatActivity {
    ListView lstView = null;
    CoachesAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_listing);

        lstView = (ListView) findViewById(R.id.lstList);
        adapter = new CoachesAdapter(this);

        lstView.setAdapter(adapter);

        ArrayList<Coach> miLista = new ArrayList<Coach>();
        miLista.add(new Coach(1, "nombre", "equipo"));
        miLista.add(new Coach(2, "nombre1", "equipo"));
        miLista.add(new Coach(3, "nombre2", "equipo"));
        miLista.add(new Coach(4, "nombre3", "equipo"));
        miLista.add(new Coach(5, "nombre4", "equipo"));

        llenarDatos(miLista);
    }

    public void llenarDatos(ArrayList<Coach> coachesList) {
        for (Coach coach : coachesList) {
            adapter.add(coach);
        }
        adapter.notifyDataSetChanged();
    }

}
