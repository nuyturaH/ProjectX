package com.har8yun.homeworks.projectx.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.EventViewModel;
import com.har8yun.homeworks.projectx.model.Skill;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class MapFragment extends Fragment implements OnMapReadyCallback {


//    public static final String DATABASE_PATH_EVENTS = "Events";

    //views
    private BottomNavigationView mBottomNavigationView;
    private FloatingActionButton mAddEventButton;
//    private FloatingActionButton mTaskButton;
    private Spinner mTaskSpinner;
    private EditText mSearchView;
    private MapView mapView;

    private GoogleMap mGoogleMap;

    //navigation
    private NavController mNavController;

    //preferences
    SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //user
    private User mCurrentUser;
    private UserViewModel mUserViewModel;

    //event
    private List<Event> mEventList = new ArrayList<>();
    private EventViewModel mEventViewModel;

    //Firebase
    DatabaseReference mDatabase;


    //constructor
    public MapFragment() {
    }


    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // mUser = sharedPreferences.getCurrentUser(getContext());
        initViews(view);
        initSearch();
        showBotNavBar();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                Log.e("hhhh", "ViewModel " + user.toString());
            }
        });

        mEventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
    }



    //************************************** METHODS ********************************************

    private void showBotNavBar() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void initViews(View v) {
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);
        mAddEventButton = v.findViewById(R.id.fab_add_event_map);
        mTaskSpinner = v.findViewById(R.id.spinner_task_map);
        mSearchView = v.findViewById(R.id.et_search_map_fragment);
        mapView = v.findViewById(R.id.mv_map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        mTaskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currentItemName = parent.getItemAtPosition(position).toString();

                if (position == 0) {
                    view.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        onClickNavigate(mAddEventButton, R.id.action_map_fragment_to_create_event_fragment);
//        mTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(getContext())
//                        .setTitle("Title")
//                        .setMessage("Message")
//                        .setPositiveButton("Ok", null)
//                        .show();
//            }
//        });


    }

    private void initSearch()
    {
        Log.d("Map","initSearch");
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event.getAction() == KeyEvent.ACTION_DOWN
                || event.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    Log.d("Map","geoLocate MAP");
                    geoLocate();
                }
                return false;
            }
        });
    }

    private void geoLocate()
    {
        Log.d("Map","geoLocate");
        String searchString  = mSearchView.getText().toString();
        Geocoder mGeocoder = new Geocoder(getContext());
        List<Address> mAddressList = new ArrayList<>();
        try {
            mAddressList = mGeocoder.getFromLocationName(searchString,1);

        }catch (IOException e)
        {
            Log.d("MAP","IOException " + e.getMessage());
        }
        if(mAddressList.size() > 0)
        {
            Address address = mAddressList.get(0);
            Log.d("Map","address " + address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),address.getAddressLine(0));
        }

    }

    private void moveCamera(LatLng latLng,String title)
    {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        MarkerOptions mMarkerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        mGoogleMap.addMarker(mMarkerOptions);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.3092293, -122.1136845))
                .title("Captain America"));

        //googleMap.addMarker()

    }

    private void addEvent()   //this method sets event  TODO: call this method when event is created
    {
        mEventList.add(mEventViewModel.getEvent().getValue());
    }

    @Override
    public void onDestroy() {
        mDatabase = FirebaseDatabase.getInstance().getReference("events");
        super.onDestroy();
        for (Event event : mEventList) {
            event.setUid(mDatabase.push().getKey());
            mDatabase.child(event.getUid()).setValue(event);

        }
    }
}
