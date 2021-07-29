package zvhir.dev.tutormy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class LearnerProfile extends AppCompatActivity {

    TextView mprofileName, mprofileEmail, mprofilePhone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_profile);


        mprofileName = findViewById(R.id.profileName);
        mprofileEmail = findViewById(R.id.profileEmail);
        mprofilePhone = findViewById(R.id.profilePhone);

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Pengguna aplikasi").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                mprofileName.setText(documentSnapshot.getString("Fullname") + " (Learner)");
                mprofileEmail.setText("Email: " + documentSnapshot.getString("Email"));
                mprofilePhone.setText("Phone number: " + documentSnapshot.getString("Phone"));

            }
        });

    }


    //------------------------------------------------------------------ BACK PRESS -----------------------------------------
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

}
