package zvhir.dev.tutormy;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class TutorDashboard extends AppCompatActivity {


    ImageView mNav;
    TextView mAbout;
    TextView mTutorial;
    TextView mProfile;
    TextView mLogout;
    Button mCreate;


    FirebaseFirestore fStore;
    String userID;
    FirebaseAuth fAuth;

    private StorageReference mMyStorageRef;
    TextView mindicatorEmpty;
    private DatabaseReference mMyDatabaseRef;

    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;
    private TutorRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_dashboard);

        mindicatorEmpty = (TextView)findViewById(R.id.indicatorEmpty);

        mNav = findViewById(R.id.navigator);
        mNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(zvhir.dev.tutormy.TutorDashboard.this);
                dialog.setContentView(R.layout.navigator_dialog);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // set adapter background jadi transparent
                dialog.show();

                mAbout = (TextView)dialog.findViewById(R.id.about);
                mTutorial = (TextView)dialog.findViewById(R.id.tutorial);
                mProfile = (TextView)dialog.findViewById(R.id.profile);
                mLogout = (TextView)dialog.findViewById(R.id.logout);



                mAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Intent a = new Intent(zvhir.dev.tutormy.TutorDashboard.this, About.class);
                        startActivity(a);
                    }
                });


                mTutorial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent tut = new Intent(zvhir.dev.tutormy.TutorDashboard.this, TutorTutorial.class);
                        startActivity(tut);
                    }
                });

                mProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pr = new Intent(zvhir.dev.tutormy.TutorDashboard.this, TutorProfile.class);
                        startActivity(pr);
                    }
                });

                mLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Splashscreen.class));
                        finish();
                    }
                });




            }
        });


        mCreate = findViewById(R.id.createClass);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create = new Intent(zvhir.dev.tutormy.TutorDashboard.this, TutorCreate.class);
                startActivity(create);
            }
        });

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        mAdapter = new TutorRecyclerAdapter(zvhir.dev.tutormy.TutorDashboard.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("myuploads/" + userID);


        DatabaseReference emptyRef = FirebaseDatabase.getInstance().getReference();
        emptyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("myuploads/" + userID)){
                    mindicatorEmpty.setVisibility(View.INVISIBLE);
                }
                else {
                    mindicatorEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(zvhir.dev.tutormy.TutorDashboard.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void adminDelete(View view){

        int positionAdmin = (int) view.getTag();
        Upload selectedItem = mUploads.get(positionAdmin);
        final String selectedKey = selectedItem.getKey();

        mMyDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                mMyDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(zvhir.dev.tutormy.TutorDashboard.this, "Class deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
