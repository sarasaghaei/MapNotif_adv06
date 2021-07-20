package ir.sarasaghaei.mapnotif_adv06.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.util.List;
import ir.sarasaghaei.mapnotif_adv06.Const;
import ir.sarasaghaei.mapnotif_adv06.R;
import ir.sarasaghaei.mapnotif_adv06.databinding.FragmentmapsBinding;
import static ir.sarasaghaei.mapnotif_adv06.Const.TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    FragmentmapsBinding binding;
    private static final float ZOOM = 15f;
    private GoogleMap mMap;
    private Marker marker;
    LatLng gps_latLng;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentmapsBinding.inflate(getLayoutInflater());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        init();
        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(getContext() != null) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }
        //Check Trun on or off location for lastlocation in map
        CheckLocation();

        // get your location and add marker and move the camera
        //GetDeviceLocation();

        if (getArguments() != null &&
                getArguments().containsKey(Const.LATITUDE) &&
                getArguments().containsKey(Const.LONGITUDE)) {

                LatLng latLng = new LatLng(Double.parseDouble(getArguments().getString(Const.LATITUDE))
                        , Double.parseDouble(getArguments().getString(Const.LONGITUDE)));
                String namelocation = GetPhysicalNameLocation(latLng);
                addmarker(latLng, namelocation);

        } else if (getArguments() != null &&
                getArguments().containsKey(Const.GPS_LATITUDE) &&
                getArguments().containsKey(Const.GPS_LONGITUDE)) {
            LatLng latLng = new LatLng(Double.parseDouble(getArguments().getString(Const.GPS_LATITUDE))
                    , Double.parseDouble(getArguments().getString(Const.GPS_LONGITUDE)));
            String namelocation = GetPhysicalNameLocation(latLng);
            addmarker(latLng, namelocation);
        } else {
            // get your location and add marker and move the camera
            GetDeviceLocationFuse();
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                String nameLocation = GetPhysicalNameLocation(latLng);
                addmarker(latLng, nameLocation);
            }
        });
    }

    private void init() {

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mMap.setMyLocationEnabled(true);
                LatLng latLng = SetPhysicalNAmeLocation(binding.etSearch.getText().toString());
                if (latLng != null) {
                    addmarker(latLng, binding.etSearch.getText().toString());
                } else {
                    Snackbar snackbar = Snackbar.make(v, "Sorry. Location service is disabled... Try again later", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
        binding.tvMylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLocation();
                GetDeviceLocationFuse();

            }
        });

        binding.etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    LatLng latLng = SetPhysicalNAmeLocation(binding.etSearch.getText().toString());
                    if (latLng != null) {
                        addmarker(latLng, binding.etSearch.getText().toString());
                    } else {
                        Snackbar snackbar = Snackbar.make(v, "Sorry. Location service is disabled... Try again later", BaseTransientBottomBar.LENGTH_SHORT);
                        snackbar.show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void addmarker(LatLng lng, String title) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(lng);
        markerOptions.title(title);

        //for remove ago marker
        if (marker != null) {
            marker.remove();
        }

        marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, ZOOM));

    }

    private void GetDeviceLocation() {

        // get your location and add marker and move the camera
        //With LocationChangeListener
        if (mMap != null) {
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    String nameLocation = GetPhysicalNameLocation(latLng);
                    addmarker(latLng, nameLocation);
                }
            });
        }

    }

    private void GetDeviceLocationFuse() {

        // get your location and add marker and move the camera
        //With FuseLocationProviderClient
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this.getActivity());
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Toast.makeText(this.getActivity(), "Location is off", Toast.LENGTH_SHORT).show();


            //
            //
            //
            //send request on location
            //
            //
            //


            return;
        } else {
            Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
            if (lastLocation != null) {
                lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            String nameLocation = GetPhysicalNameLocation(latLng);
                            addmarker(latLng, nameLocation);
                        } else {
                            GetDeviceLocation();
                        }

                    }
                });
            }
        }

    }

    private String GetPhysicalNameLocation(LatLng lactation) {
        Geocoder geocoder = new Geocoder(this.getActivity());
        try {
            List<Address> fromLocation = geocoder.getFromLocation(lactation.latitude, lactation.longitude, 1);

            return fromLocation.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Your Location";
    }

    private LatLng SetPhysicalNAmeLocation(String address) {
        Geocoder geocoder = new Geocoder(this.getActivity());
        try {
            List<Address> fromLocationName = geocoder.getFromLocationName(address, 1);
            return new LatLng(fromLocationName.get(0).getLatitude(), fromLocationName.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private boolean CheckLocation() {
        boolean gps_enabled = false, network_enabled = false;
        LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e(TAG, "CheckLocation: "+ex.getMessage() );
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.e(TAG, "CheckLocation: "+ex.getMessage() );
        }
        if (!gps_enabled && !network_enabled) {
            new MaterialAlertDialogBuilder(this.getActivity())
                    .setTitle(R.string.errorlocation_off)
                    .setMessage(R.string.message_errorlocation)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();

            Log.e(TAG, "CheckLocation :         Location is off...");
            return false;
        }
        return true;
    }

}
