package com.example.just_hungry;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.just_hungry.models.PostModel;
import com.example.just_hungry.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

public class NewOrderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<PostModel> posts;
    UserModel resultUser = null;
    private static final int HEADER_VIEW_TYPE = 0;
    private static final int ITEM_VIEW_TYPE = 1;
    SharedPreferences preferences;
    FragmentManager fragmentManager;

    //constructor
    public NewOrderRecyclerAdapter(Context context, ArrayList<PostModel> posts, FragmentManager parentFragmentManager) {
        this.context = context;
        this.posts = posts;
        this.preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        this.fragmentManager = parentFragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // This is where you inflate the layout (giving look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == HEADER_VIEW_TYPE) {
            View headerView = inflater.inflate(R.layout.new_order_header_view, parent, false);
            return new HeaderViewHolder(headerView);
        } else {
            View itemView = inflater.inflate(R.layout.post_row, parent, false);
            return new PostViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        position = position -1 ;  // Adjust the position for the header view
        PostViewHolder postHolder = (PostViewHolder) holder;
        PostModel targetPost = posts.get(position);
        if (getItemViewType(position) == HEADER_VIEW_TYPE) {
            NewOrderRecyclerAdapter.HeaderViewHolder headerHolder = (NewOrderRecyclerAdapter.HeaderViewHolder) holder;
            Button newOrderButton = headerHolder.newOrderButton;
            newOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("HELLO");
                    // TODO UNCOMMENT THIS to redirect to josh forms!
                    // fragmentManager.beginTransaction().replace(R.id.fragment_container, new NewOrderFormFragment()).commit();
                    // Generate new order and push to firebase
                    SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
                    String userId = preferences.getString("userId", "");
                    HashMap<String, Object> newRandomOrderHM = new PostModel(userId).getHashMapForFirestore();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    System.out.println(userId);
                    db.collection("posts").add(newRandomOrderHM)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(context, "Random Post with posterId" +userId  + "added!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error adding post to Firestore", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
            return;
        }


        postHolder.itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //System.out.println("TOGGLED");
                if (postHolder.buttonContainer.getVisibility() == View.GONE) {
                    postHolder.buttonContainer.setVisibility(View.VISIBLE);
                } else {
                    postHolder.buttonContainer.setVisibility(View.GONE);
                }
            }
        });
        postHolder.joinButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postHolder.joinButton.getText().toString().equalsIgnoreCase("Join")) {
                    // Join function call
                    String userId = preferences.getString("userId", "");
                    String postId;
                    postHolder.joinButton.setText("Leave");
                } else {
                    // Leave function call
                    postHolder.joinButton.setText("Join");
                }
            }
        });
        int finalPosition = position;
        postHolder.chatButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //!TODO UNCOMMENT FOR CHAT
                Toast.makeText(context, "Chat button clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, ChatActivity.class);
//                intent.putExtra("invitationId", posts.get(finalPosition).getPosterId());
//                context.startActivity(intent);
            }
        });

        // This is where you set the data to the views, assigning values to the views we created in the onCreateViewHolder in recycler view row layout file
        // based on the position of the row
        postHolder.storeName.setText(posts.get(position).getStoreName());
        postHolder.timing.setText(posts.get(position).getTiming());
        if (posts.get(position).getLocation() != null) postHolder.location.setText(posts.get(position).getLocation().getStringLocation());
        if (posts.get(position).getDateCreated() != null) postHolder.dateCreated.setText(posts.get(position).getDateCreated());
        // holder.participantCount.setText(posts.get(position).getParticipantCount());

        // get image from firebase db
        if (Utils.isNetworkAvailable(context)) {
            Glide.with(context)
                    .load("https://loremflickr.com/320/240/tasty")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(postHolder.postImage);
        }
        try {
            if (posts.get(position).getPosterId() != null) {
                String posterId = posts.get(position).getPosterId();

                Utils.getUserById(posterId, poster -> {
                    if (poster == null) {
                        return;
                    }
                    resultUser = poster;
                    String name = resultUser.getName();
                    if (resultUser != null && !name.equalsIgnoreCase("")) {
                        postHolder.posterName.setText(name);
                    }
                });
            }
        }catch (Exception e) {
            System.out.println("ERROR: " + e);
        }

    }

    @Override
    public int getItemCount() {
        // This is where you return the number of rows
        // the recycler vie just wants to know the number of rows you want to display (add 1 for header)
        return posts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW_TYPE;
        } else {
            return ITEM_VIEW_TYPE;
        }
    }
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewHelloUser;
        public TextView textViewFancySomeFood;
        public Button newOrderButton;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textViewHelloUser = itemView.findViewById(R.id.textViewNewOrder);
            textViewFancySomeFood = itemView.findViewById(R.id.textViewNewOrderHint);
            newOrderButton = itemView.findViewById(R.id.buttonAddOrder);
        }
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        // This is where you declare the views you want to use in the recycler view row layout file
        // grabbing the views from our post row layout file kinda like onCreate method

        ImageView postImage;
        TextView storeName;
        TextView timing;
        TextView location;
        TextView posterName;
        ImageView posterImage;
        TextView dateCreated;
        TextView participantCount;
        ConstraintLayout buttonContainer;

        Button joinButton;
        Button chatButton;



        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            // This is where you initialize the views
            postImage = itemView.findViewById(R.id.postImage);
            storeName = itemView.findViewById(R.id.storeNameCardText);
            timing = itemView.findViewById(R.id.timingCardText);
            posterName = itemView.findViewById(R.id.posterNameCardText);
            location = itemView.findViewById(R.id.locationCardText);
            posterImage = itemView.findViewById(R.id.posterCardImage);
            dateCreated = itemView.findViewById(R.id.dateCreatedCardText);
            participantCount = itemView.findViewById(R.id.participantCountCardText);
            joinButton = itemView.findViewById(R.id.joinButton);
            chatButton = itemView.findViewById(R.id.chatButton);
            joinButton.setText("Leave");
//            chatButton.setVisibility(View.GONE);
            buttonContainer = itemView.findViewById(R.id.button_container);
        }
    }
}
