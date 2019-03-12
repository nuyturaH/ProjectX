package com.har8yun.homeworks.projectx.fragment;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.model.TravelMode;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.PlaceAutocompleteAdapter;
import com.har8yun.homeworks.projectx.mapAnim.MapAnimator;
import com.har8yun.homeworks.projectx.mapHelper.TaskLoadedCallback;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.EventViewModel;
import com.har8yun.homeworks.projectx.model.TaskViewModel;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.util.EventInformationDialog;
import com.har8yun.homeworks.projectx.util.PermissionChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import androidx.navigation.fragment.NavHostFragment;

import static com.har8yun.homeworks.projectx.fragment.TasksFragment.BUILD_MUSCLES;
import static com.har8yun.homeworks.projectx.fragment.TasksFragment.DEVELOP_STAMINA;
import static com.har8yun.homeworks.projectx.fragment.TasksFragment.LOOSE_WEIGHT;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, TaskLoadedCallback {


//    public static final String DATABASE_PATH_EVENTS = "Events";

    private static final String TAG = "MapFragment";
    public static final int PLACE_PICKER_REQUEST = 2;
    public static final int REQUEST_LOCATION_PERMISSION_CODE = 1;
    public static final String MY_LOCATION = "My Location";
    public static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(

            new LatLng(-40, -168), new LatLng(71, 136));
    private EventInformationDialog mEventInformationDialog;

    String[] mPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    //views
    private BottomNavigationView mBottomNavigationView;
    private FloatingActionButton mAddEventButton;
    private AutoCompleteTextView mSearchView;
    private FloatingActionButton mLocationButton;
    private FloatingActionButton mTasksButton;

    private MapView mapView;
    private ImageView mMagnifyView;
    private ImageView mCurrentPlace;

    //tasks
    private TaskViewModel mTaskViewModel;

    //Map, Location
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mDeviceLocation;
    private Polyline currentPolyline;
    private Place mPlace;
    private Marker mEventMarker;
    private List<Marker> mMarkerList = new ArrayList<>();
    LatLng destinationPosition;
    List<LatLng> path;


    //navigation
    private Fragment mNavHostFragment;

    //preferences
    SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //user
    private User mCurrentUser;
    private UserViewModel mUserViewModel;

    //event
    private List<Event> mEventList = new ArrayList<>();
    private Event mCurrentEvent;
    private EventViewModel mEventViewModel;

    //Firebase
    DatabaseReference mFirebaseDatabse;


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
        //getDeviceLocation();


        Toast.makeText(getContext(), "Choose Location for your Event", Toast.LENGTH_LONG).show();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mCurrentUser = user;
                Log.e("hhhh", "ViewModel " + user.toString());
            }
        });
        mCurrentUser = mUserViewModel.getUser().getValue();

        mEventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        mEventViewModel.getEvent().observe(getViewLifecycleOwner(), new Observer<Event>() {
            @Override
            public void onChanged(@Nullable final Event event) {
                mCurrentEvent = event;
            }
        });
        mCurrentEvent = mEventViewModel.getEvent().getValue();

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mTaskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
//        mTaskViewModel.getTask().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                if (s != null) {
//                    //TODO call task methods
//                    switch (s) {
//                        case LOOSE_WEIGHT:
//                            looseWeight();
//                            break;
//                        case DEVELOP_STAMINA:
//                            //developStamina();
//                            break;
//                        case BUILD_MUSCLES:
//                            //buildMuscles();
//                            break;
//                    }
//                }
//            }
//        });
//
//    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }


    //************************************** METHODS ********************************************

    private void showBotNavBar() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }


    private void initViews(View v) {
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);
        mAddEventButton = v.findViewById(R.id.fab_add_event_map);
        mLocationButton = v.findViewById(R.id.fab_my_location_map);
        mSearchView = v.findViewById(R.id.et_search_map_fragment);
        mMagnifyView = v.findViewById(R.id.iv_magnify_map_fragment);
        mTasksButton = v.findViewById(R.id.fab_tasks_map);

        mCurrentPlace = v.findViewById(R.id.iv_current_location_map);
        mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        mapView = v.findViewById(R.id.mv_map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }


        if (mCurrentEvent != null) {
            Log.d(TAG, "initViews:" + mCurrentEvent.toString());
        }

        onClickNavigate(mTasksButton, R.id.action_map_fragment_to_tasks_fragment);
        mTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                TasksFragment tasksFragment = new TasksFragment();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.add(R.id.nav_host_fragment, tasksFragment);
//                ft.commit();
//                tasksFragment.show(fm,null);
//                getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
                if (mDeviceLocation != null) {
                    mGoogleMap.clear();
                    looseWeight();
                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_map_marker_check);
//                    Bitmap anImage      = ((BitmapDrawable) myDrawable).getBitmap();
                    Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                            R.drawable.ic_add_location_red);

//                   setAnimation(mGoogleMap,path,convertToBitmap(myDrawable,130,130));

                    MapAnimator.getInstance().setPrimaryLineColor(getResources().getColor(R.color.colorPrimary));
                    MapAnimator.getInstance().setSecondaryLineColor(getResources().getColor(R.color.colorPrimaryLight));
                    MapAnimator.getInstance().animateRoute(mGoogleMap, path);
                    mGoogleMap.addMarker(new MarkerOptions().position(destinationPosition).title("Destination"));
                    //.icon(convertToBitmap(getResources().getDrawable(R.drawable.ic_map_marker_check),130,130)));

                }


            }
        });


        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this.getActivity(), this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(), mGoogleApiClient, LAT_LNG_BOUNDS, null);
        mSearchView.setAdapter(mPlaceAutocompleteAdapter);

//        mMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mv_map);
//        mMapFragment.getMapAsync(this);

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionChecker.hasLocationPermission(getContext())) {
                    getDeviceLocation();
                } else {
                    requestLocationPermissions();
                    PermissionChecker.createLocationRequest(); //TODO
                }
            }
        });

//        mPlacePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                try {
//                    startPicking(builder.build(getActivity()));
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


//        onClickNavigate(mAddEventButton, R.id.action_map_fragment_to_create_event_fragment);


        mAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MarkerOptions markerOptions = new MarkerOptions().position(mGoogleMap.getCameraPosition().target);
                mEventMarker = mGoogleMap.addMarker(markerOptions);
                mMarkerList.add(mEventMarker);
                Event event = new Event();
                event.setPosition(mEventMarker.getPosition());
                event.setPlace(mEventMarker.getTitle());
                mEventViewModel.setEvent(event);

                NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.action_map_fragment_to_create_event_fragment);
            }
        });
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

    private void setEventsOnMap() {

        mFirebaseDatabse = FirebaseDatabase.getInstance().getReference("events");
        mFirebaseDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mEventList.clear();
//                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
//                    Event mEvent = eventSnapshot.getValue(Event.class);
//                    Log.d(TAG, "onDataChange: " + mEvent.getPosition());
//                    mEventList.add(mEvent);
////                    MarkerOptions markerOptions = new MarkerOptions().position(mEvent.getPosition());
////                    mGoogleMap.addMarker(markerOptions);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Toast.makeText(getContext(), String.valueOf(mEventList.size()), Toast.LENGTH_LONG).show();

//        for(Event event : mEventList)
//        {
//            MarkerOptions markerOptions = new MarkerOptions().position(event.getPosition());
//            mGoogleMap.addMarker(markerOptions);
//            Log.d(TAG, "setEventsOnMap: " + event.getTitle());
//        }
    }

//    private void startPicking(Intent data) {
//        mPlace = PlacePicker.getPosition(getContext(), data);
//
//        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                .getPlaceById(mGoogleApiClient, mPlace.getId());
//        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//
//    }
//
//    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
//
//        @Override
//        public void onResult(@NonNull PlaceBuffer places) {
//            if (!places.getStatus().isSuccess()) {
//                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
//                places.release();
//                return;
//            }
//
//            final Place place = places.get(0);
//
//            try {
//                mPlaceInfo = new PlaceInfo();
//                mPlaceInfo.setName(place.getName().toString());
//                Log.d(TAG, "onResult: name: " + place.getName());
//                mPlaceInfo.setAddress(place.getAddress().toString());
//                Log.d(TAG, "onResult: address: " + place.getAddress());
//                mPlaceInfo.setId(place.getId());
//                Log.d(TAG, "onResult: id:" + place.getId());
//                mPlaceInfo.setLatlng(place.getLatLng());
//                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
//                mPlaceInfo.setPhoneNumber(place.getPhoneNumber().toString());
//                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
//                Log.d(TAG, "onResult: place: " + mPlaceInfo.toString());
//            } catch (NullPointerException e) {
//                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage());
//            }
//            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
//
//                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlaceInfo);
//            places.release();
//        }
//    };

    protected void requestLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mPermissions, REQUEST_LOCATION_PERMISSION_CODE);
        }
    }


    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            Task mTaskLocation = mFusedLocationProviderClient.getLastLocation();
            mTaskLocation.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Toast.makeText(getContext(), "Location Found", Toast.LENGTH_SHORT).show();
                        mDeviceLocation = (Location) task.getResult();
                        moveCamera(new LatLng(mDeviceLocation.getLatitude(), mDeviceLocation.getLongitude()), DEFAULT_ZOOM, MY_LOCATION);

                    } else {
                        Log.d("Map", task.getException().getMessage());
                        Toast.makeText(getContext(), "Location Not Found", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } catch (SecurityException e) {
            Log.d("Map", e.getMessage());
        }

    }

//    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
//
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
//
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//
//
//        if (placeInfo != null) {
//
//            try {
//                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
//                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n";
//
//                MarkerOptions options = new MarkerOptions()
//                        .position(latLng)
//                        .title(placeInfo.getName())
//                        .snippet(snippet);
//                mMarker = mGoogleMap.addMarker(options);
//            } catch (NullPointerException e) {
//                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage());
//            }
//
//        } else {
//            mGoogleMap.addMarker(new MarkerOptions().position(latLng));
//        }
//    }


    private void moveCamera(LatLng latLng, float zoom, String title) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions mMarkerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        mGoogleMap.addMarker(mMarkerOptions);
    }

    private void initSearch() {
        Log.d("Map", "initSearch");
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    Log.d("Map", "geoLocate MAP");
                    geoLocate();
                }
                return false;
            }
        });
        mMagnifyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });
    }

    private void geoLocate() {
        Log.d("Map", "geoLocate");
        String searchString = mSearchView.getText().toString();
        Geocoder mGeocoder = new Geocoder(getContext());
        List<Address> mAddressList = new ArrayList<>();
        try {
            mAddressList = mGeocoder.getFromLocationName(searchString, 1);
//            mGeocoder.get
            mAddressList = mGeocoder.getFromLocationName(searchString, 1);

        } catch (IOException e) {
            Log.d("MAP", "IOException " + e.getMessage());
        }
        if (mAddressList.size() > 0) {
            Address address = mAddressList.get(0);
            Log.d("Map", "address " + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }

    }


    private boolean goingToEvent;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        setEventsOnMap();

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (final Event event : mEventList) {
                    if (marker.getPosition().equals(event.getPosition())) {
                        mEventInformationDialog = new EventInformationDialog(getContext());
                        mEventInformationDialog.mTitleView.setText(event.getTitle());
                        mEventInformationDialog.mDescriptionView.setText(event.getDescription());
                        mEventInformationDialog.mDateLocationView.setText("Date " + event.getDate().toString() + "Location " + event.getPlace());
                        //checking if current user is the creator of event
                        if (event.getCreator().equals(mCurrentUser)) {
                            mEventInformationDialog.mEditEventView.setVisibility(View.VISIBLE);
                            mEventInformationDialog.mEditEventView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //TODO navigate to EditEventFragment
                                }
                            });
                        }
                        mEventInformationDialog.show();
                        mEventInformationDialog.mGoingButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goingToEvent = !goingToEvent;
                                if (goingToEvent) {
                                    mEventInformationDialog.mGoingButton.setText("Not Going");
                                    event.getParticipants().add(mCurrentUser);
                                } else {
                                    mEventInformationDialog.mGoingButton.setText("Going");
                                    event.getParticipants().remove(mCurrentUser);
                                }
                            }
                        });
                        mEventInformationDialog.mCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goingToEvent = false;
                                mEventInformationDialog.dismiss();
                            }
                        });
                    }
                }
                return true;
            }
        });
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    private void addEvent()   //this method sets event  TODO: call this method when event is created
    {
        mEventList.add(mEventViewModel.getEvent().getValue());
    }


    private LatLng getDestinationPoint(LatLng source, double brng, double dist) {
        dist = dist / 6371;
        brng = Math.toRadians(brng);

        double lat1 = Math.toRadians(source.latitude), lon1 = Math.toRadians(source.longitude);
        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist) +
                Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(dist) *
                        Math.cos(lat1),
                Math.cos(dist) - Math.sin(lat1) *
                        Math.sin(lat2));
        if (Double.isNaN(lat2) || Double.isNaN(lon2)) {
            return null;
        }
        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }

    private void looseWeight() {
        double angle = ThreadLocalRandom.current().nextDouble(0, 360);
        LatLng sourcePosition = new LatLng(mDeviceLocation.getLatitude(), mDeviceLocation.getLongitude());
        String origin = mDeviceLocation.getLatitude() + "," + mDeviceLocation.getLongitude();

        destinationPosition = getDestinationPoint(sourcePosition, angle, 1);
        String destination = destinationPosition.latitude + "," + destinationPosition.longitude;

        mGoogleMap.addMarker(new MarkerOptions().position(sourcePosition).title("Origin")
                .icon(convertToBitmap(getResources().getDrawable(R.drawable.map_marker_radius), 130, 130)));

//        mGoogleMap.addMarker(new MarkerOptions().position(destinationPosition).title("Destination"));

        //Define list to get all latlng for the route
        path = new ArrayList();

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(getResources().getString(R.string.google_maps_key))
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, origin, destination);
        try {
            DirectionsResult res = req.mode(TravelMode.WALKING).await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }
//        Draw the polyline
//        if (path.size() > 0) {
//            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.RED).width(6);
//            mGoogleMap.addPolyline(opts);
//        }

    }

    public static void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final Bitmap bitmap) {
        Marker marker = myMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .position(directionPoint.get(0))
                .flat(true));

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), DEFAULT_ZOOM));

        animateMarker(myMap, marker, directionPoint, false);
    }

    private static void animateMarker(GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
                                      final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final long duration = 30000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                if (i < directionPoint.size())
                    marker.setPosition(directionPoint.get(i));
                i++;

                if (t < 1.0) {
                    handler.postDelayed(this, 15);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public BitmapDescriptor convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(mutableBitmap);
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mGoogleMap.addPolyline((PolylineOptions) values[0]);
    }

}
