package zvhir.dev.tutormy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Splashscreen extends AppCompatActivity {


    TextView mRoleNumberWelcome;
    FirebaseFirestore fStore;
    String userID;
    FirebaseAuth fAuth;

    Button mwelcomeLoginButton;
    Button mwelcomeRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        mRoleNumberWelcome = findViewById(R.id.welcomeroleNumber);

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){

            userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("Pengguna aplikasi").document(userID);
            documentReference.addSnapshotListener(zvhir.dev.tutormy.Splashscreen.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    mRoleNumberWelcome.setText(documentSnapshot.getString("Role"));

                    String temporaryRoleHolder = mRoleNumberWelcome.getText().toString();

                    if(temporaryRoleHolder.equals("Register as tutor")){
                        Intent pemilik = new Intent(zvhir.dev.tutormy.Splashscreen.this, zvhir.dev.tutormy.TutorDashboard.class);
                        startActivity(pemilik);
                    }
                    if(temporaryRoleHolder.equals("Register as learner")){
                        Intent pelanggan = new Intent(zvhir.dev.tutormy.Splashscreen.this, zvhir.dev.tutormy.LearnerDashboard.class);
                        startActivity(pelanggan);
                    }


                }
            });

        }



        mwelcomeRegisterButton = findViewById(R.id.welcomeRegister);
        mwelcomeRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(zvhir.dev.tutormy.Splashscreen.this, zvhir.dev.tutormy.Register.class);
                startActivity(reg);
            }
        });

        mwelcomeLoginButton = findViewById(R.id.welcomeLogin);
        mwelcomeLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log = new Intent(zvhir.dev.tutormy.Splashscreen.this, Login.class);
                startActivity(log);
            }
        });






    }
}
