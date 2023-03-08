package com.liftPlzz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.liftPlzz.R;
import com.liftPlzz.adapter.ChatSuggestionAdapter;
import com.liftPlzz.adapter.MessageAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.Messages;
import com.liftPlzz.model.ResponseChatSuggestion;
import com.liftPlzz.model.chatuser.ChatUser;
import com.liftPlzz.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements ChatSuggestionAdapter.ItemListener {

    private String mChatUser;
    TextView mUserName, tvTitle, tvText;
    private RecyclerView recyclerView;
    TextView mUserLastSeen;
    CircleImageView mUserImage;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    String mCurrentUserId;
    DatabaseReference mDatabaseReference;
    private DatabaseReference mRootReference;
    private ImageButton mChatSendButton, mChatAddButton;
    private EditText mMessageView;
    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private MessageAdapter mMessageAdapter;
    public static final int TOTAL_ITEM_TO_LOAD = 10;
    private int mCurrentPage = 1;
    //Solution for descending list on refresh
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";
    private static final int GALLERY_PICK = 1;
    StorageReference mImageStorage;
    ChatSuggestionAdapter chatSuggestionAdapter;
    private ArrayList arrayListSuggestion = new ArrayList();

    //tv_mob,tv_name,img_driver,img_back
    TextView tv_name;
    ImageView img_back, img_call;
    CircleImageView img_driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mChatSendButton = (ImageButton) findViewById(R.id.chatSendButton);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvText = (TextView) findViewById(R.id.tv_text);

        tv_name = (TextView) findViewById(R.id.tv_name);
        img_driver = (CircleImageView) findViewById(R.id.img_driver);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_call = (ImageView) findViewById(R.id.img_call);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMessageView = (EditText) findViewById(R.id.chatMessageView);
        sharedPreferences = this.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //-----GETING FROM INTENT----
        mChatUser = getIntent().getStringExtra(Constants.USER_ID);
        String userName = getIntent().getStringExtra("user_name");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        chatSuggestionAdapter = new ChatSuggestionAdapter(arrayListSuggestion, this, this);

        try {
            // intent.putExtra("charuser",new Gson().toJson(chatUser));
            String chatuserstring = getIntent().getStringExtra("charuser");
            final ChatUser chatUser = new Gson().fromJson(chatuserstring, ChatUser.class);

            tv_name.setText(chatUser.getName());
            if (chatUser.getIs_contact_public() == 0) {
                img_call.setVisibility(View.GONE);
            } else {
                img_call.setVisibility(View.VISIBLE);
            }
            img_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    sendDummyMsgBeforeCall(chatUser.getMobile());
//                    } else {
//                        requestPermissions(new String[]{CALL_PHONE}, 1);
//                    }
                }
            });

            try {
                Glide.with(this).load(chatUser.getImage())
                        .error(R.drawable.logo_icon)
                        .placeholder(R.drawable.logo_icon).into(img_driver);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception E) {

        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatSuggestionAdapter);

        //---SETTING ONLINE------
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        //----ADDING ACTION BAR-----
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        //---INFLATING APP BAR LAYOUT INTO ACTION BAR----
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.app_bar_layout, null);
//        actionBar.setCustomView(actionBarView);

        //---ADDING DATA ON ACTION BAR----
//        mUserName=(TextView) actionBarView.findViewById(R.id.textView3);
//        mUserLastSeen = (TextView) actionBarView.findViewById(R.id.textView5);
//        mUserImage = (CircleImageView) actionBarView.findViewById(R.id.circleImageView);
//        mUserName.setText(userName);

        mRootReference = FirebaseDatabase.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
//        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mCurrentUserId = sharedPreferences.getString(Constants.USER_ID, "");
        mMessageAdapter = new MessageAdapter(messagesList, this);

        mMessagesList = (RecyclerView) findViewById(R.id.recycleViewMessageList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);
        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);

        // mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayoutManager);
        mMessagesList.setAdapter(mMessageAdapter);

        loadMessages();
        getSuggestionChatText();
        //----ADDING LAST SEEN-----
//        mRootReference.child("users").child(mChatUser).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String onlineValue=dataSnapshot.child("online").getValue().toString();
//                String imageValue = dataSnapshot.child("thumb_image").getValue().toString();
//
//                Picasso.with(com.example.singhkshitiz.letschat.ChatActivity.this).load(imageValue).placeholder(R.drawable.user_img).into(mUserImage);
//                if(onlineValue.equals("true")){
//                    mUserLastSeen.setText("online");
//                }
//                else{
//                    GetTimeAgo getTimeAgo = new GetTimeAgo();
//                    long lastTime = Long.parseLong(onlineValue);
//                    String lastSeen = getTimeAgo.getTimeAgo(lastTime,getApplicationContext());
//                    mUserLastSeen.setText(lastSeen);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        //----ADDING SEEN OF MESSAGES----
        mRootReference.child("chats").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(mChatUser)) {

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("time_stamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chats/" + mChatUser + "/" + mCurrentUserId, chatAddMap);
                    chatUserMap.put("chats/" + mCurrentUserId + "/" + mChatUser, chatAddMap);

                    mRootReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(getApplicationContext(), "Successfully Added chats feature", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), "Cannot Add chats feature", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("onCancelled", "" + databaseError.getMessage());
//                Toast.makeText(getApplicationContext(), "Something went wrong.. Please go back..", Toast.LENGTH_SHORT).show();
            }
        });

        //----SEND MESSAGE--BUTTON----

        mChatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageView.getText().toString();

                sendMessage(message);

            }
        });
       /*
        //----THE WRAP CONTENT OF IMAGE VIEW IS GIVING ERROR--- SO REMOVING THIS FUNCTIONALITY-------


       mChatAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent=new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);
            }
        });
        */
        //----LOADING 10 MESSAGES ON SWIPE REFRESH----
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemPos = 0;
                mCurrentPage++;
                loadMoreMessages();

            }
        });

    }

    private void sendMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;
            DatabaseReference user_message_push = mRootReference.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();
            String push_id = user_message_push.getKey();
            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                    } else {
                        Log.e("CHAT_ACTIVITY_sent", "Message sent");
                        mMessageView.setText("");
                    }

                }
            });


        }
    }

    private void sendDummyMsgBeforeCall(String mobile) {
        String number = sharedPreferences.getString(Constants.MOBILE, "");
        String name = sharedPreferences.getString(Constants.NAME, "");

        String message = "Hello. I am interested to join you as travel partner,\n" +
                "Lift Please find my details below:\n" +
                "Name: " + name + "\n" +
                "Mobile No.: " + number;
        sendMessage(message);

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobile));
        startActivity(intent);
    }

    //---FIRST 10 MESSAGES WILL LOAD ON START----
    private void loadMessages() {

        DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages messages = (Messages) dataSnapshot.getValue(Messages.class);

                itemPos++;

                if (itemPos == 1) {
                    String mMessageKey = dataSnapshot.getKey();

                    mLastKey = mMessageKey;
                    mPrevKey = mMessageKey;
                }

                messagesList.add(messages);
                mMessageAdapter.notifyDataSetChanged();

                mMessagesList.scrollToPosition(messagesList.size() - 1);

                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //---ON REFRESHING 10 MORE MESSAGES WILL LOAD----
    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = (Messages) dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();


                if (!mPrevKey.equals(messageKey)) {
                    messagesList.add(itemPos++, message);

                } else {
                    mPrevKey = mLastKey;
                }

                if (itemPos == 1) {
                    String mMessageKey = dataSnapshot.getKey();
                    mLastKey = mMessageKey;
                }


                mMessageAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);

                mLinearLayoutManager.scrollToPositionWithOffset(10, 0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //---THIS FUNCTION IS CALLED WHEN SYSTEM ACTIVITY IS CALLED---
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //---FOR PICKING IMAGE FROM GALLERY ACTIVITY AND SENDING---
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            //---GETTING IMAGE DATA IN FORM OF URI--
            Uri imageUri = data.getData();
            final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootReference.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            final String push_id = user_message_push.getKey();

            //---PUSHING IMAGE INTO STORAGE---
//            StorageReference filepath = mImageStorage.child("message_images").child(push_id+".jpg");
//            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                    if(task.isSuccessful()){
//
//                        @SuppressWarnings("VisibleForTests")
//                        String download_url = task.getResult().getDownloadUrl().toString();
//
//                        Map messageMap = new HashMap();
//                        messageMap.put("message",download_url);
//                        messageMap.put("seen",false);
//                        messageMap.put("type","image");
//                        messageMap.put("time",ServerValue.TIMESTAMP);
//                        messageMap.put("from",mCurrentUserId);
//
//                        Map messageUserMap = new HashMap();
//                        messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
//                        messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);
//
//                        mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener(){
//
//                            @Override
//                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                if(databaseError != null){
//                                    Log.e("CHAT_ACTIVITY","Cannot add message to database");
//                                }
//                                else{
//                                    Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
//                                    mMessageView.setText("");
//                                }
//
//                            }
//                        });
//                    }
//
//                }
//            });


        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //mDatabaseReference.child(mCurrentUserId).child("online").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // mDatabaseReference.child(mCurrentUserId).child("online").setValue(ServerValue.TIMESTAMP);

    }

    public void getSuggestionChatText() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseChatSuggestion> call = api.getChatSuggestions();
        call.enqueue(new Callback<ResponseChatSuggestion>() {
            @Override
            public void onResponse(Call<ResponseChatSuggestion> call, Response<ResponseChatSuggestion> response) {
                Constants.hideLoader();
                if (response.body() != null && response.code() == 200) {
                    arrayListSuggestion.clear();
                    tvTitle.setText(response.body().getText());
                    tvText.setText(response.body().getTitle());
                    arrayListSuggestion.addAll(response.body().getSuggestions());
                    chatSuggestionAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ChatActivity", "Not getting response");
                }
            }


            @Override
            public void onFailure(Call<ResponseChatSuggestion> call, Throwable throwable) {
                Constants.hideLoader();
//                view.showMessage(throwable.getMessage());
            }
        });
    }

    @Override
    public void onItemClickSuggestion(String copyItem) {
        mMessageView.setText(copyItem);
    }
}

