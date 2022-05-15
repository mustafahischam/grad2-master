
package com.sasa.project.grad2;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasa.project.grad2.Firebase_DataObjects.Driver_posts_DataObjects;
import com.sasa.project.grad2.Firebase_DataObjects.Passenger_posts_DataObjects;
import com.sasa.project.grad2.Firebase_DataObjects.User_info_DataObjects;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Create_post_Activity extends BaseActivity implements OnMapReadyCallback, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "Create_post_Activity";


    private DatabaseReference databaseReference;

    private AutocompleteSupportFragment autocompleteSupportFragment;
    CarouselView formCarousel;

    private AutocompleteSupportFragment Destination_fragment;
    private EditText passengers_N;

    private static final String REQUIRED = "Required";

    private String Org_ID;
    private GoogleMap Map;
    private GMapV2Direction md;
    private Time_Fragments arrive_time = new Time_Fragments();
    private Time_Fragments leave_time = new Time_Fragments();
    private String source, destination;
    private Button Arrive_time_button,Leave_time_button,Post_Button,pick_day;
    private LatLng destination_Latlng, pickup_Latlng,source_Latlng;
    private Date_Fragment date = new Date_Fragment();
    private int tripDate;

    private MarkerOptions set_marker;
    private MarkerOptions [] markerOptions = new MarkerOptions[2];
    private Boolean condition = false;

    @Override/// github https://stackoverflow.com/questions/40932723/cameraposition-zoom-and-target-doesnt-work-in-android-6
    public void onMapReady(GoogleMap map){
        Map = map;
        Map.clear();
        set_marker = new MarkerOptions()
                .position(new LatLng(this.source_Latlng.latitude, this.source_Latlng.longitude))
                .title("title").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp));
        Map.addMarker(set_marker);
        CameraPosition target = CameraPosition.builder().
                target(source_Latlng).
                zoom(15).
                build();
        Map.moveCamera(CameraUpdateFactory.newCameraPosition(target));

        if(markerOptions[0] == null && source != null){ markerOptions[0] = new MarkerOptions(); }
        if(markerOptions[1] == null && destination != null){ markerOptions[1] = new MarkerOptions();}
        if(source != null) {/// latitude and lang locatiob
            markerOptions[0].position(new LatLng(this.source_Latlng.latitude, this.source_Latlng.longitude))
                    .title("title").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp));
            Map.addMarker(markerOptions[0]);
        }
        if(destination != null) {
            markerOptions[1].position(new LatLng(this.destination_Latlng.latitude, this.destination_Latlng.longitude))
                    .title("title").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp));
            Map.addMarker(markerOptions[1]);
        }

        if (condition) {
            pickup_Latlng = source_Latlng;
            Map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener(){
                @Override
                public void onCameraMove(){
                    Log.d("Camera Moving" + "", Map.getCameraPosition().target + "");
                    pickup_Latlng = Map.getCameraPosition().target;
                }
            });
        }

//
        pickup_move_view();
    }


    private void pickup_move_view(){
        if(!condition) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            if (markerOptions[0] != null) {
                builder.include(markerOptions[0].getPosition());
            }
            if (markerOptions[1] != null) {
                builder.include(markerOptions[1].getPosition());
            }
            LatLngBounds bounds = builder.build();
            //3shan mit7rksh bara el zone bta3 el shasha
            int padding = 100;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            Map.moveCamera(cu);
            if (destination == null) {
                Map.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

        }
    }
////   mn https://stackoverflow.com/questions/29042459/trying-to-get-latlng-from-an-address-or-location-in-android
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {

            address = coder.getFromLocationName(strAddress, 5);
            if (address.isEmpty()) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    int N = 5;
    private Boolean source_p = false;
    private Boolean destination_p = false;
    private Boolean Type = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Type = getIntent().getBooleanExtra("Driver_offers", true);

        if(Type){
            setContentView(R.layout.driver_post_button);
        }
        else{
            setContentView(R.layout.passenger_post_button);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Post_Button = findViewById(R.id.addposts);
        Post_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post_button_database();
            }
        });
/// moving screen layout
        formCarousel = findViewById(R.id.carouselView);
        formCarousel.setPageCount(N);
        formCarousel.setViewListener(viewListener);
        formCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private Boolean rechoose = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override

            public void onPageSelected(int i) { // load page resources functional
                Post_Button.setVisibility(View.GONE);
                condition = false;
                if(i == 0) { /// select location
                    condition = false;
                    if (!Places.isInitialized()) {
                        Places.initialize(getApplicationContext(), "AIzaSyDbJdB5ZPbyVqbFMehjikoLLjjSwXFYhLk");
                    }
                    autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.source_fragment);
                    autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS));
                    autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

                        @Override
                        public void onPlaceSelected(Place place) {

                            Log.i(TAG, "Place: " + place.getName());
                            System.out.println(place.getAddress()+" rashad");
                            source = place.getAddress().toString();
                            Create_post_Activity.this.source_p = true;
                            rechoose = true; //Source was changed
                            Create_post_Activity.this.source_Latlng = Create_post_Activity.this.getLocationFromAddress(Create_post_Activity.this, Create_post_Activity.this.source);
                            if (Create_post_Activity.this.destination_p){
                               // Create_post_Activity.this.route_present = true;
                                Create_post_Activity.this.destination_p = false;
                                Create_post_Activity.this.destination_Latlng = Create_post_Activity.this.getLocationFromAddress(Create_post_Activity.this, Create_post_Activity.this.destination);
                            }
                            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.source_map);
                            supportMapFragment.getMapAsync(Create_post_Activity.this);
                            //}
                        }

                        @Override
                        public void onError(Status status) {

                            Log.i(TAG, "An error occured: " + status);
                        }
                    });
                    if(rechoose){
                        rechoose = false;
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.source_map);
                        mapFragment.getMapAsync(Create_post_Activity.this);
                    }
                }
                if(i == 1) { /// select destination
                    condition = false;
                    if (!Places.isInitialized()) {
                        Places.initialize(getApplicationContext(), "AIzaSyDbJdB5ZPbyVqbFMehjikoLLjjSwXFYhLk");
                    }
                    Destination_fragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.destination_fragment);
                    Destination_fragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS));
                    Destination_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                        @Override//////https://stackoverflow.com/questions/38753426/movecamera-doesnt-work-after-onplaceselected
                        public void onPlaceSelected(Place place) {
                            //TODO: Get info about the selected place
                            Log.i(TAG, "Place: " + place.getName());
                            destination = place.getAddress().toString();
                            Create_post_Activity.this.destination_p = true;
                            rechoose = true;//Destination was changed
                            Create_post_Activity.this.destination_Latlng = Create_post_Activity.this.getLocationFromAddress(Create_post_Activity.this, Create_post_Activity.this.destination);
                            if (Create_post_Activity.this.source_p){

                                Create_post_Activity.this.source_p = false;
                                Create_post_Activity.this.source_Latlng = Create_post_Activity.this.getLocationFromAddress(Create_post_Activity.this, Create_post_Activity.this.source);
                            }
                            else {
                            }
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.destination_map);
                            mapFragment.getMapAsync(Create_post_Activity.this);
                            //}
                        }

                        @Override
                        public void onError(Status status) {
                            //TODO: Handle the error
                            Log.i(TAG, "An error occured: " + status);
                        }
                    });
                    if(rechoose){
                        rechoose = false;
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.source_map);
                        mapFragment.getMapAsync(Create_post_Activity.this);
                    }
                }
                if(i==2){  // slelct pickup lel passenger
                    condition = false;
                    if(Type) passengers_N = findViewById(R.id.Number_Passenger);
                    else{
                        condition = true;

                        System.out.println(source +" msms");
                        Create_post_Activity.this.source_Latlng = Create_post_Activity.this.getLocationFromAddress(Create_post_Activity.this, Create_post_Activity.this.source);
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pickup_map);
                        supportMapFragment.getMapAsync(Create_post_Activity.this);
                    }
                }
                if(i==3){/// time page

                    Create_post_Activity.this.Arrive_time_button = findViewById(R.id.arrive_before_time);
                    Create_post_Activity.this.Arrive_time_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimeDialog(v, true);
                        }
                    });

                    Create_post_Activity.this.Leave_time_button = findViewById(R.id.leave_after_time);
                    Create_post_Activity.this.Leave_time_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimeDialog(v, false);
                        }
                    });
                }
                if (i==4){ /// date page
                    Create_post_Activity.this.pick_day = findViewById(R.id.Choose_date);
                    Create_post_Activity.this.pick_day.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            DateDialog(v);
                        }
                    });
                    Post_Button.setVisibility(View.VISIBLE);
                    Post_Button.invalidate();
                }
                else{
                    Post_Button.setVisibility(View.GONE);
                    Post_Button.invalidate();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    ViewListener viewListener = new ViewListener() { // casroul
        @Override////////////////https://stackoverflow.com/questions/42200433/carousel-in-recyclerview   create el view
        public View setViewForPosition(int j) {
            View post_from = null;
            condition = false;
            if(j == 0) {
                condition = false;
                post_from= getLayoutInflater().inflate(R.layout.create_post_activity_source, null);
                if (!Places.isInitialized()) {
                    Places.initialize(getApplicationContext(), "AIzaSyDbJdB5ZPbyVqbFMehjikoLLjjSwXFYhLk");
                }
                autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.source_fragment);
                autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS));
                autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                        System.out.println(place.getAddress()+" rashad");
                        source = place.getAddress().toString();
                        source_Latlng = getLocationFromAddress(Create_post_Activity.this, source);

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.source_map);
                        mapFragment.getMapAsync(Create_post_Activity.this);
                    }

                    @Override
                    public void onError(@NonNull Status status) {
                        
                    }


                });


            }
            if(j == 1) {
                condition = false;
                post_from= getLayoutInflater().inflate(R.layout.create_post_activity_destination, null);
                if (!Places.isInitialized()) {
                    Places.initialize(getApplicationContext(), "AIzaSyDbJdB5ZPbyVqbFMehjikoLLjjSwXFYhLk");
                }
                Destination_fragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.destination_fragment);
                Destination_fragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS));
                Destination_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        Log.i(TAG, "Place: " + place.getName());
                        destination = place.getAddress().toString();
                        if(destination != null && !destination.equals("")) {
                            Create_post_Activity.this.source_Latlng = Create_post_Activity.this.getLocationFromAddress(Create_post_Activity.this, Create_post_Activity.this.source);
                            Create_post_Activity.this.destination_Latlng = Create_post_Activity.this.getLocationFromAddress(Create_post_Activity.this, Create_post_Activity.this.destination);
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.destination_map);
                            mapFragment.getMapAsync(Create_post_Activity.this);
                        }
                    }

                    @Override
                    public void onError(Status status) {
                        Log.i(TAG, "An error occured: " + status);
                    }
                });
            }
            if(j==2){
                if(Type){
                    post_from= getLayoutInflater().inflate(R.layout.create_post_activity_passengernumber, null);
                    passengers_N = (EditText) post_from.findViewById(R.id.Number_Passenger);
                }
                else {
                    condition = false;
                    post_from= getLayoutInflater().inflate(R.layout.create_post_activity_pickup, null);
                    if(source != null){
                        Create_post_Activity.this.source_Latlng = Create_post_Activity.this.getLocationFromAddress(Create_post_Activity.this, Create_post_Activity.this.source);
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pickup_map);
                        mapFragment.getMapAsync(Create_post_Activity.this);
                    }
                }
                Post_Button.setVisibility(View.VISIBLE);
                Post_Button.invalidate();
            }
            if(j==3){
                post_from = getLayoutInflater().inflate(R.layout.create_post_activity_time, null);
            }
            if (j==4){
                post_from = getLayoutInflater().inflate(R.layout.create_post_activity_date, null);
            }
            else{
                Post_Button.setVisibility(View.GONE);
                Post_Button.invalidate();
            }
            Create_post_Activity.this.Activity_title((TextView) post_from.findViewById(R.id.Driver_offer_title));
            return post_from;
        }
    };


    private void Activity_title(TextView tv){
        if(Type)tv.setText("Driver's Offer");
        else tv.setText("Passenger's Requests");
    }
    private int setleave_time = 0;
    private int setarrive_time = 0;
    private Boolean if_clicked_time = false;
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        String time;
        if(hourOfDay>12){
            time = (hourOfDay-12) + ":" + minute  + "PM";
        }
        else{
            time = (hourOfDay) + ":" + minute  + "AM";
        }

        if(if_clicked_time) {
            this.Arrive_time_button.setText(time);
            setarrive_time = (hourOfDay*100) + (minute);
        }
        else {
            this.Leave_time_button.setText(time);
            setleave_time = (hourOfDay*100) + (minute);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){

        String picked_date = month + " " + day + ", " + year;
        this.pick_day.setText(picked_date);
        tripDate = (year * 10000) + (month * 100) + (day);
    }

    public void DateDialog(View v){

        date.show(getFragmentManager(), "datePicker");
    }

    /// like sa3a 3adya
    public void TimeDialog(View v, Boolean arrive_before_time) {

        if(arrive_before_time){
            if_clicked_time = true;
            arrive_time.show(getFragmentManager(), "timePicker");
        }
        else{
            if_clicked_time = false;
            leave_time.show(getFragmentManager(), "timePicker");
        }
    }


    private void Post_button_database() {
//        Destination_fragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.destination_fragment);
//        Destination_fragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS));
        //
        final String source = this.source;
        final String destination = this.destination;

        int passenger_num = 0;
        if(Type && !passengers_N.getText().toString().equals("")){
            passenger_num = Integer.parseInt(passengers_N.getText().toString());
        }
        else if(Type && passengers_N.getText().toString().equals("")){
            passengers_N.setError(REQUIRED);
            return;
        }
        final LatLng pickup_latlng = pickup_Latlng;
        final int passengerCount = passenger_num;


/// error if no post =0
        if(Type && passenger_num==0) {
            passengers_N.setError("there is no posts");
            return;
        }

        if (TextUtils.isEmpty(source)) {
            return;
        }

        if (TextUtils.isEmpty(destination)) {
            return;
        }

        view_post_button(false);
        Toast.makeText(this, "Posting done..", Toast.LENGTH_SHORT).show();

        final String userId = getUid();
        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) { //update firebase data
                        User_info_DataObjects userInfo = dataSnapshot.getValue(User_info_DataObjects.class);
                        if (userInfo == null) {
                            Toast.makeText(Create_post_Activity.this,
                                    "no user info null",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Org_ID = userInfo.default_organization;// save with new default


                            if(Type) {
                                add_driver_post_firebase(userId, User_post_information.User_post_name(userInfo),
                                        source, destination, passengerCount, setleave_time, setarrive_time, tripDate, Org_ID);
                            }
                            else{
                                add_passenger_post_firebase(userId, User_post_information.User_post_name(userInfo),
                                        source, destination, pickup_latlng, setleave_time, setarrive_time, tripDate, Org_ID);
                            }
                        }

                        view_post_button(true);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        view_post_button(true);
                    }
                });
    }

    private void view_post_button(boolean enabled) {
        if (enabled) {
            Post_Button.setVisibility(View.VISIBLE);
        } else {
            Post_Button.setVisibility(View.GONE);
        }
    }

    private void add_driver_post_firebase(String user_id, String user_name, String source, String destination, int count,
                                          int dep_time, int time, int date, String organizationId) {


        String key = databaseReference.child("posts").child("Driver_offers").push().getKey();
        Driver_posts_DataObjects driver_add_post = new Driver_posts_DataObjects(user_id, user_name, source, destination, count, dep_time, time, date);
        driver_add_post.organizationId = organizationId;
        driver_add_post.postId = key;

        Map<String, Object> postValues = driver_add_post.DriverObjects();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/Driver_offers/" + key, postValues);
        childUpdates.put("/user-posts/" + user_id + "/Driver_offers/" + key, postValues);
        childUpdates.put("/organization_posts/" + organizationId + "/Driver_offers/" + key, postValues);

        databaseReference.updateChildren(childUpdates);
    }

    private void add_passenger_post_firebase(String userId, String username, String source, String destination, LatLng pickupPoint,
                                             int dep_time, int arr_time, int t_day, String organizationId){

        String key = databaseReference.child("posts").child("Passenger_Requests").push().getKey();
        Passenger_posts_DataObjects rideRequest = new Passenger_posts_DataObjects(userId, username, source, destination, dep_time, arr_time, t_day);
        rideRequest.organizationId = organizationId;
        rideRequest.postId = key;

        Map<String, Object> postValues = rideRequest.PassengerObjects();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> childUpdates2 = new HashMap<>();
        childUpdates.put("/posts/Passenger_Requests/" + key, postValues);

        childUpdates.put("/user-posts/" + userId + "/Passenger_Requests/" + key, postValues);
        childUpdates.put("/organization_posts/" + organizationId + "/Passenger_Requests/" + key, postValues);
        databaseReference.updateChildren(childUpdates);
        databaseReference.updateChildren(childUpdates2);
    }
}
