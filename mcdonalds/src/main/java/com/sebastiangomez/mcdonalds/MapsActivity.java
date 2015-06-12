package com.sebastiangomez.mcdonalds;

import android.content.Context;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
{

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private CameraUpdate mCamera;
    private Cursor cursor;
    DataBaseManager Manager = Locations.getManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000) // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mCamera = CameraUpdateFactory.newLatLngZoom(new LatLng(6.235925000,-75.575136999) , 12);
        mMap.animateCamera(mCamera);
        cursor = Manager.cargarCursorContactos();
        if (cursor.moveToFirst()) {
            do {

                setMarker(new LatLng(cursor.getDouble(2), cursor.getDouble(3)), cursor.getString(1), "I´m Lovin' It", 0.9F, 0.1F, 0.1F, R.drawable.ic_mcdonalds);

            } while (cursor.moveToNext());

        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
// Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("Aqui TAAA");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void setMarker(LatLng position, String title, String info, float opacity, float dimension1, float dimension2, int icon){
        // Agregamos un marcador para indicar sitios de interes.
        mMap.addMarker(new MarkerOptions()
                .position(position)     // Posicion del marcador
                .title(title)           // Agrega titulo al marcador
                .snippet(info)          // Agrega información detalle relacionada con el marcador
                .alpha(opacity)         // Opacidad del icono
                .anchor(dimension1, dimension2)     // Tamaño del icono (alto y ancho)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
    }


    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

}
