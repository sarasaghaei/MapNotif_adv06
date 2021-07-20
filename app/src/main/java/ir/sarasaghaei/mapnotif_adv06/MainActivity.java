package ir.sarasaghaei.mapnotif_adv06;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ir.sarasaghaei.mapnotif_adv06.databinding.ActivityMainBinding;
import ir.sarasaghaei.mapnotif_adv06.fragment.InfoFragment;
import ir.sarasaghaei.mapnotif_adv06.fragment.MapFragment;
import ir.sarasaghaei.mapnotif_adv06.fragment.NotifFragment;
import ir.sarasaghaei.mapnotif_adv06.room.AppDatabase;

public class MainActivity extends FragmentActivity implements NotifFragment.CallbackNotifFragment {

    ActivityMainBinding binding;
    private static final String TAG = Const.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addBadgeUnreadNotif();
        binding.menubar.getMenu().getItem(1).setChecked(true);
        checkpermission();


        // call notif fragment when click in notification
        Intent getNotified = getIntent();
        Bundle bundleNotify = getNotified.getExtras();
        if (bundleNotify != null){
            NotifFragment fragment = new NotifFragment();
            fragment.setArguments(bundleNotify);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }


        binding.menubar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_map:
                        fragment = new MapFragment();
                        binding.menubar.getMenu().getItem(1).setChecked(true);
                        break;
                    case R.id.menu_info:
                        fragment = new InfoFragment();
                        binding.menubar.getMenu().getItem(0).setChecked(true);
                        break;
                    case R.id.menu_notif:
                        fragment = new NotifFragment();
                        binding.menubar.getMenu().getItem(2).setChecked(true);
                        break;
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                return false;
            }
        });

    }

    private boolean isServiceOK() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int result = api.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            Log.e(TAG, "isServiceOK: Success");
            return true;

        } else if (api.isUserResolvableError(result)) {
            Log.e(TAG, "isServiceOK: " + api.getErrorString(result));

        } else {
            Log.e(TAG, "isServiceOK: not Available");
        }

        return false;

    }

    private void checkpermission() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            if (isServiceOK()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new MapFragment())
                        .commit();
            } else {
                LocationManager lm = (LocationManager) getSystemService(this.LOCATION_SERVICE);
                if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Location GPS_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    MapFragment mapFragment = new MapFragment();
                    Bundle latLng = new Bundle();
                    if (GPS_location != null ) {
                        latLng.putString(Const.GPS_LATITUDE, String.valueOf(GPS_location.getLatitude()));
                        latLng.putString(Const.GPS_LONGITUDE, String.valueOf(GPS_location.getLongitude()));
                    }
                    mapFragment.setArguments(latLng);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_container, mapFragment)
                            .commit();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (isServiceOK()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new MapFragment())
                        .commit();
            }
        }
    }

    //add badge unread notification in bottom navigation menu
    public void addBadgeUnreadNotif() {

        int UnreadNotif = AppDatabase.getInstance(this).getNotifDAO().getCountunreadNotif();

        int menuItemId = binding.menubar.getMenu().getItem(2).getItemId();
        binding.menubar.removeBadge(menuItemId);
        if (UnreadNotif >0) {

            BadgeDrawable badge = binding.menubar.getOrCreateBadge(menuItemId);
            badge.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary , null));
            badge.setNumber(UnreadNotif);
        }
    }


    // when from notif fragment on_view_map cliced , and get data resived to map fragment
    @Override
    public void onclicklocation(String latitude, String longitude) {
        MapFragment mapFragment = new MapFragment();
        Bundle latLng = new Bundle();
        latLng.putString(Const.LATITUDE,latitude);
        latLng.putString(Const.LONGITUDE,longitude);
        mapFragment.setArguments(latLng);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,mapFragment)
                .commit();
    }

}