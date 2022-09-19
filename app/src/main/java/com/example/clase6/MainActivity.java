package com.example.clase6;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    ListaEmpleadosAdapter adapter;
    SensorManager sensorManager;

    @Override
    protected void onStart() {
        super.onStart();
        if (sensorManager != null) {
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (sensor != null) {
                sensorManager.registerListener(this, sensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    public void obtenerUbicacion(View view) {

        int permissionCoarse = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        int permissionFine = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCoarse == PackageManager.PERMISSION_GRANTED &&
                permissionFine == PackageManager.PERMISSION_GRANTED) {
            //tengo permisos
            FusedLocationProviderClient fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(
                    this,
                    location -> {
                        if (location != null) {
                            TextView t1 = findViewById(R.id.textViewLat);
                            TextView t2 = findViewById(R.id.textViewLong);
                            t1.setText(String.valueOf(location.getLatitude()));
                            t2.setText(String.valueOf(location.getLongitude()));
                        }
                    });
        } else {
            //no tengo permisos
            requestPermissionLocation.launch(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
        }


    }

    ActivityResultLauncher<String[]> requestPermissionLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissionLocation = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean clGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                    Boolean flGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);

                    if (flGranted != null && flGranted) {
                        Log.d("msg", "me dio permisos Fine!");
                        obtenerUbicacion(null);
                    } else if (clGranted != null && clGranted) {
                        Log.d("msg", "me dio permisos Coarsed!");
                    } else {
                        Log.d("msg", "no me dio permisos");
                    }
                }
        );


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            List<Sensor> listaSensores = sensorManager.getSensorList(Sensor.TYPE_ALL);
            for (Sensor s : listaSensores) {
                Log.d("sensor", s.getName());
            }

            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (sensor != null) { //tengo acelerómetro
                Toast.makeText(this, "Si tienes el sensor :D", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No tienes acelerómetro :(", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Su celular es muy antiguo", Toast.LENGTH_SHORT).show();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://10.101.48.212:8080";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                response -> {
                    Gson gson = new Gson();
                    EmpleadoDto empleadoDto = gson.fromJson(response, EmpleadoDto.class);

                    adapter = new ListaEmpleadosAdapter();
                    adapter.setListaEmpleados(empleadoDto.getLista());
                    adapter.setContext(MainActivity.this);

                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                },
                error -> Log.e("error volley", error.getMessage()));

        requestQueue.add(stringRequest);
    }

    private float xAnt = 0f;
    private float yAnt = 0f;
    private float zAnt = 0f;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        int sensorType = sensorEvent.sensor.getType();

        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            float xAcce = sensorEvent.values[0];
            float yAcce = sensorEvent.values[1];
            float zAcce = sensorEvent.values[2];
            if (xAcce != xAnt && yAcce != yAnt && zAcce != zAnt) {
                String msg = "x: " + xAcce + " | y: " + yAcce + " | z: " + zAcce;
                Log.d("msgSensor", msg);
            }
            xAnt = xAcce;
            yAnt = yAcce;
            zAnt = zAcce;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}