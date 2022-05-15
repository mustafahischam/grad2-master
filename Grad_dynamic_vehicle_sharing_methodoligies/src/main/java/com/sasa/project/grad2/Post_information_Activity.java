package com.sasa.project.grad2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasa.project.grad2.Firebase_DataObjects.Comments_DataObjects;
import com.sasa.project.grad2.Firebase_DataObjects.Driver_posts_DataObjects;
import com.sasa.project.grad2.Firebase_DataObjects.Passenger_posts_DataObjects;
import com.sasa.project.grad2.Firebase_DataObjects.Post_info_DataObjects;
import com.sasa.project.grad2.Firebase_DataObjects.SDL_DataObjects;
import com.sasa.project.grad2.Firebase_DataObjects.User_info_DataObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;






public class Post_information_Activity extends MainActivity
        implements View.OnClickListener, OnMapReadyCallback {

    private static final String TAG = "Post_information_Activity";
    private String Post_id;
    private Comments_adapter commentsadapter;
    public static final String Post_Id = "post_key";
    public static final String type = "Post-type";
    public static final String EXTRA_POST_OBJECT = "post";
    private Button Comment_button,Map_button,Join_button;

    private ValueEventListener onClick_post;

    private Post_info_DataObjects post_infoDataObjects;
    private Post_info_DataObjects.Post_type Post_type ;

    private TextView user_name, source_text, destination_text;
    private EditText textBox_comment;
    private ImageButton delete_button;

    private RecyclerView Comments_Scroll;
    View my_view;
    private DatabaseReference Post_Reference, databaseReference,Comment_Reference;
    private GoogleMap mymap;
    private boolean mapReady;
    private LatLng source_latlng,destination_latlng;
    private MarkerOptions source_marker;
    private MarkerOptions destination_marker;


    private GMapV2Direction gMapV2Direction = new GMapV2Direction();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.post_information_activity);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        try {
            Post_type = Post_info_DataObjects.Post_type.valueOf(getIntent().getStringExtra(Post_information_Activity.type));
        }
        catch (NullPointerException ex) { // if there is no type error // no organization
        }
        Post_id = getIntent().getStringExtra(Post_Id);// pass data from post information " get intent "
        if (Post_id == null || Post_id.equals("")) {
            post_infoDataObjects = getIntent().getParcelableExtra(Post_information_Activity.EXTRA_POST_OBJECT);
            if (post_infoDataObjects == null) {
                throw new IllegalArgumentException("null");
            }
        }
        source_text = findViewById(R.id.source_postlayout);
        user_name = findViewById(R.id.postlayout_username);
        destination_text = findViewById(R.id.destination_postlayout);
        Comments();
        Join_button_type();

        // setupFindMatchingPostsButton();

        Map_Button();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (post_infoDataObjects == null) {
            Load_posts_firebase();// lw post_infoDataObjects maknsh feh data
        }
        else {
            post_layout1(post_infoDataObjects);
            Post_layout();// intent
        }

    }

    private void Source_Destination_location() {// load in list
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Source_Destination_location_Fragment.Sdl,
                SDL_load_data());
        Fragment source_destination_location_fragment = new Source_Destination_location_Fragment();
        source_destination_location_fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.soure_location_view, source_destination_location_fragment, "SDV").commit();
    }
    private ArrayList<SDL_DataObjects>
    SDL_load_data() {/////////// Destination
        ArrayList<SDL_DataObjects> SDL = new ArrayList<>();
        SDL_DataObjects sdll = new SDL_DataObjects();
        sdll.type = "destination";
        sdll.text = "Going To..";
        sdll.address = post_infoDataObjects.destination;
        sdll.time = Post_date_time_information.Arrive_before_time(post_infoDataObjects); // "9:00 AM";
        SDL.add(sdll);
        //// location for driver and passenger
        //driver
        if ( Post_type == Post_info_DataObjects.Post_type.Driver) {
            SDL_DataObjects SDL_D = new SDL_DataObjects();
            SDL_D.type = "Driver";
            SDL_D.text = "Driver Leave from ";
            SDL_D.User_name = post_infoDataObjects.User_name;
            SDL_D.address = post_infoDataObjects.source;
            SDL_D.time = Post_date_time_information.Leave_after_time(post_infoDataObjects);
            SDL.add(SDL_D);
        }
        // passenger
        else if (Post_type == Post_info_DataObjects.Post_type.Passenger) {
            SDL_DataObjects SDL_P = new SDL_DataObjects();
            SDL_P.type = "passenger";
            SDL_P.text = "Take Passenger From...";
            SDL_P.User_name = post_infoDataObjects.User_name;
            SDL_P.address = post_infoDataObjects.source;
            SDL_P.time = Post_date_time_information.Leave_after_time(post_infoDataObjects);
            SDL.add(SDL_P);
        }
        return SDL;
    }

    private void Comments() {
        textBox_comment = findViewById(R.id.field_comment_text);
        Comment_button = findViewById(R.id.button_post_comment);
        Comments_Scroll = findViewById(R.id.Comments_scroll);
        Comment_button.setOnClickListener(this);
        Comments_Scroll.setLayoutManager(new LinearLayoutManager(this));
    }

    private void Join_button_type() {
        Join_button = findViewById(R.id.button_create_carpool);

        if(Post_type == Post_info_DataObjects.Post_type.Passenger) {
            Join_button.setText("Offer a Ride");
        }
        else if(Post_type == Post_info_DataObjects.Post_type.Driver) {
            Join_button.setText("Request a Ride");
        }
        Join_button.setEnabled(true);
        Join_button.setVisibility(View.INVISIBLE);
        Join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Request sent ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void Post_layout() {
        Source_Destination_location();
        LinearLayout topLayout = findViewById(R.id.layout_top);
        if (Post_type == Post_info_DataObjects.Post_type.Driver) {
            topLayout.setBackgroundColor(getResources().getColor(R.color.blue1));
        }
        else if (Post_type == Post_info_DataObjects.Post_type.Passenger) {
            topLayout.setBackgroundColor(getResources().getColor(R.color.blue1));
        }

    }

    @SuppressLint("ResourceType")
    private void Map_Button() {
        final View mMapOverlay = findViewById(R.id.Map_View);
        my_view = findViewById(R.id.Map_layout);
        Map_button = findViewById(R.id.Open_Map);
        Map_button.setOnClickListener(new View.OnClickListener() {
           // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
               int x = Math.round(Map_button.getX() + (Map_button.getWidth()));
                int y = Math.round(Map_button.getY() + (Map_button.getHeight()));
                float Map_circle = (float) Math.hypot(x, y);
                if(my_view.getVisibility() == View.INVISIBLE){
                    Animator alpha_animation = ViewAnimationUtils.createCircularReveal(my_view, x, y, 0, Map_circle);
                    alpha_animation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mMapOverlay.setVisibility(View.GONE);
                            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.source_map);
                            mapFragment.getMapAsync(Post_information_Activity.this);
                        }
                    });
                    my_view.setVisibility(View.VISIBLE);
                    alpha_animation.start();
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void Load_posts_firebase() {

        if(Post_type == Post_info_DataObjects.Post_type.Passenger) {
            Post_Reference = FirebaseDatabase.getInstance().getReference()
                    .child("posts").child("Passenger_Requests").child(Post_id);
            Comment_Reference = FirebaseDatabase.getInstance().getReference()
                    .child("post-comments").child(Post_id);
        }
        else if(Post_type == Post_info_DataObjects.Post_type.Driver) {
            Post_Reference = FirebaseDatabase.getInstance().getReference()
                    .child("posts").child("Driver_offers").child(Post_id);
            Comment_Reference = FirebaseDatabase.getInstance().getReference()
                    .child("post-comments").child(Post_id);
        }

        ValueEventListener listener = new ValueEventListener() {/// location data change
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(Post_type == Post_info_DataObjects.Post_type.Passenger) {
                    final Passenger_posts_DataObjects post = dataSnapshot.getValue(Passenger_posts_DataObjects.class);
                    if (post != null && (post.Type == null )) {
                        post.Type = Post_info_DataObjects.Post_type.Passenger; //update fire base
                    }

                    if(post != null) {
                        post_infoDataObjects = post;
                        post_layout1(post);
                        Post_layout();
                    }
                    if(post != null && post.User_id.equals(getUid())){
             ////// dlete button visible if user's post is viewd ---fat7 el post bta3o     Passenger
                        delete_button = findViewById(R.id.delete_post);
                        delete_button.setVisibility(View.VISIBLE);
                        delete_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete_image(post);
                            }
                        });
                    }
                }
                ////// dlete button visible if user's post is viewd ---fat7 el post bta3o     Driver
                else if(Post_type == Post_info_DataObjects.Post_type.Driver) {
                    final Driver_posts_DataObjects post = dataSnapshot.getValue(Driver_posts_DataObjects.class);
                    if (post != null && (post.Type == null )) {
                        post.Type = Post_info_DataObjects.Post_type.Driver;
                    }
                    if(post != null) {
                        post_infoDataObjects = post;
                        post_layout1(post);
                        Post_layout();
                    }
                    if(post != null && post.User_id.equals(getUid())){
                        delete_button = findViewById(R.id.delete_post);
                        delete_button.setVisibility(View.VISIBLE);
                        delete_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete_image(post);
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        Post_Reference.addValueEventListener(listener);
        onClick_post = listener;

        commentsadapter = new Comments_adapter(this, Comment_Reference);
        Comments_Scroll.setAdapter(commentsadapter);
    }


    private void post_layout1(Post_info_DataObjects postInfo) {

        source_text.setText(postInfo.source);
        destination_text.setText(postInfo.destination);
        TextView textView = findViewById(R.id.date_postlayout);
        textView.setText(Post_date_time_information.post_date(postInfo));
        TextView postDateTimeText = findViewById(R.id.time_postlayout);
        postDateTimeText.setText("Arrival time " + Post_date_time_information.Arrive_before_time(postInfo));

        TextView toText = findViewById(R.id.to_postlayout);
        toText.setText("to");
        Join_Button(getUid(), postInfo);
        user_post_info();
        /// 3nwan
        source_latlng = gMapV2Direction.getLocationFromAddress(Post_information_Activity.this, postInfo.source);
        destination_latlng = gMapV2Direction.getLocationFromAddress(Post_information_Activity.this, postInfo.destination);
        source_marker = new MarkerOptions()
                .position(new LatLng(source_latlng.latitude, source_latlng.longitude))
                .title("Source")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp));
        destination_marker = new MarkerOptions()
                .position(new LatLng(destination_latlng.latitude, destination_latlng.longitude))
                .title("Destination")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp));
    }


    private void user_post_info() {

        DatabaseReference users =FirebaseDatabase.getInstance().getReference().child("users").child(post_infoDataObjects.User_id);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User_info_DataObjects postUserInfo = dataSnapshot.getValue(User_info_DataObjects.class);
                user_name.setText(User_post_information.User_post_name(postUserInfo));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    private void Join_Button(String userId, Post_info_DataObjects postInfo) {
        if (postInfo.User_id != null) {
            if (!postInfo.User_id.equals(userId)) {
                Join_button.setEnabled(true);
                Join_button.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_post_comment) {
            Comment_button();
        }
    }
    private void Comment_button() {
        final String User_id = getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(User_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User_info_DataObjects userInfo = dataSnapshot.getValue(User_info_DataObjects.class);
                        String User_Name = userInfo.First_name +" "+userInfo.Last_name;
                        /// create new text box
                        String commentText = textBox_comment.getText().toString();
                        Comments_DataObjects commentsDataObjects = new Comments_DataObjects(User_id, User_Name, commentText);
                        Comment_Reference.push().setValue(commentsDataObjects);
                        textBox_comment.setText(null);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void delete_image(Post_info_DataObjects postInfo){
        if(postInfo.Type == Post_info_DataObjects.Post_type.Passenger){
            delete_data(postInfo, "Passenger_Requests");
        }
        else if(postInfo.Type == Post_info_DataObjects.Post_type.Driver){
            delete_data(postInfo, "Driver_offers");

        }
    }

    @SuppressLint("LongLogTag")
    private void delete_data(Post_info_DataObjects postInfo, String drive_or_ride){
        String post_org = postInfo.organizationId;
        databaseReference.child("organization_posts").child(post_org).child(drive_or_ride).child(postInfo.postId).removeValue();
        databaseReference.child("posts").child(drive_or_ride).child(postInfo.postId).removeValue();
        databaseReference.child("postInfo-comments").child(postInfo.postId).removeValue();
        databaseReference.child("user-posts").child(getUid()).child(drive_or_ride).child(postInfo.postId).removeValue();
//        databaseReference.child("facility_organization_posts").child(post_org).child(drive_or_ride).child(postInfo.postId).removeValue();
//        databaseReference.child("posts").child(drive_or_ride).child(postInfo.postId).removeValue();
//        databaseReference.child("postInfo-comments").child(postInfo.postId).removeValue();
//        databaseReference.child("user-posts").child(getUid()).child(drive_or_ride).child(postInfo.postId).removeValue();
        finish();
    }

    private static class comment_layout extends RecyclerView.ViewHolder {
        public TextView userName, text;
        public comment_layout(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.comment_author);
            text = itemView.findViewById(R.id.comment_body);
        }
    }

    private static class Comments_adapter extends RecyclerView.Adapter<comment_layout> {

        private Context context;
        private DatabaseReference reference;
        private ChildEventListener childEventListener;
        private List<String> mCommentIds = new ArrayList<>();
        private List<Comments_DataObjects> commentsDataObjects = new ArrayList<>();

        public Comments_adapter(final Context context, DatabaseReference ref) {
            this.context = context; // current state
            reference = ref;
            ChildEventListener childEventListener = new ChildEventListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    // add from the class
                    Comments_DataObjects commentsDataObjects = dataSnapshot.getValue(Comments_DataObjects.class);
                    mCommentIds.add(dataSnapshot.getKey());
                    Comments_adapter.this.commentsDataObjects.add(commentsDataObjects);
                    notifyItemInserted(Comments_adapter.this.commentsDataObjects.size() - 1);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            };
            ref.addChildEventListener(childEventListener);
            this.childEventListener = childEventListener;
        }

        @Override
        public comment_layout onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.post_comment, parent, false);
            return new comment_layout(view);
        }
        @Override
        public void onBindViewHolder(comment_layout holder, int position) {
            Comments_DataObjects commentsDataObjects = this.commentsDataObjects.get(position);
            holder.userName.setText(commentsDataObjects.User_name);
            holder.text.setText(commentsDataObjects.Comment);
        }

        @Override
        public int getItemCount() {
            return commentsDataObjects.size();
        }

    }

////https://stackoverflow.com/questions/31328143/android-google-maps-onmapready-store-googlemap
    public void onMapReady(GoogleMap map){
        mymap = map;
        if(source_marker != null)
            mymap.addMarker(source_marker);
        if(destination_marker != null)
            mymap.addMarker(destination_marker);
        try {
            if(source_latlng != null && destination_latlng != null)
                gMapV2Direction.drawDirections(source_latlng, destination_latlng, mymap);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(source_marker != null && destination_marker != null)
            Map_position();
    }

    private void Map_position(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(source_marker.getPosition());
        builder.include(destination_marker.getPosition());
        System.out.println(source_marker.getPosition());
        System.out.println(destination_marker.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 100;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mymap.moveCamera(cameraUpdate);
    }

}
