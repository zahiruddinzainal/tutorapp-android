package zvhir.dev.tutormy;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class LearnerDashboard extends AppCompatActivity {

    ImageView mNav;
    TextView mAbout;
    TextView mTutorial;
    TextView mProfile;
    TextView mLogout;

    Button mChooseDate;
    Button mBookNow;
    TextView mDate;



    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;
    private zvhir.dev.tutormy.LearnerRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learner_dashboard);

        mNav = findViewById(R.id.navigator);
        mNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(zvhir.dev.tutormy.LearnerDashboard.this);
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
                        Intent a = new Intent(zvhir.dev.tutormy.LearnerDashboard.this, About.class);
                        startActivity(a);
                    }
                });

                mTutorial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent tut = new Intent(zvhir.dev.tutormy.LearnerDashboard.this, LearnerTutorial.class);
                        startActivity(tut);
                    }
                });

                mProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pr = new Intent(zvhir.dev.tutormy.LearnerDashboard.this, zvhir.dev.tutormy.LearnerProfile.class);
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


        mProgressCircle = findViewById(R.id.progress_circle);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        mAdapter = new zvhir.dev.tutormy.LearnerRecyclerAdapter(zvhir.dev.tutormy.LearnerDashboard.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

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

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(zvhir.dev.tutormy.LearnerDashboard.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void whatsapp(View view) {
        final int pos = (int) view.getTag(); //get tag from previous set tag


        final Dialog dialog = new Dialog(zvhir.dev.tutormy.LearnerDashboard.this);
        dialog.setContentView(R.layout.learner_date);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // set adapter background jadi transparent
        dialog.show();

        mChooseDate = (Button) dialog.findViewById(R.id.chooseDate);
        mBookNow = (Button) dialog.findViewById(R.id.book);
        mDate = (TextView) dialog.findViewById(R.id.datechoosen);
        mDate.setText("Pick a date");

        mChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sublimePicker();

            }
        });

        mBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String date = mDate.getText().toString();

                if (date.equals("Pick a date")){

                    Toast.makeText(LearnerDashboard.this, "Please pick a date", Toast.LENGTH_SHORT).show();

//                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please pick a date first", Snackbar.LENGTH_LONG);
//                    snackbar.show();

                }
                else {
                    book(pos, date);
                }
            }
        });






        /*
        Upload selectedItem = mUploads.get(pos); // initialize db class
        String nama = selectedItem.getName(); // get position key from db
        String harga = selectedItem.getHarga(); // get position key from db


        String wsURL = "https://api.whatsapp.com/send?phone=60177945248&text=Hi%20";
        String message = wsURL + "Cikgu.%20Saya%20berminat%20menyertai%20kelas%20" + nama;

        Uri uri = Uri.parse(message); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);


         */
    }

    private void book(int pos, String date) {
        Upload selectedItem = mUploads.get(pos); // initialize db class
        String nama = selectedItem.getName(); // get position key from db
        String namatutorr = selectedItem.getNamaTutor(); // get position key from db
        String phoneTutor = selectedItem.getPhoneTutor(); // get position key from db

        String wsdepan = "https://api.whatsapp.com/send?phone="; // letak url depan ws
        String wsBlkg = "&text=Hi%20"; // letak url belakang ws

        String wsURL = wsdepan + phoneTutor + wsBlkg;

        // Combine ws url
        String message = wsURL + "teacher " + namatutorr + " I would like to join your " + nama +
                " class in " + date + "\n\nAre you okay with the date? If so, may I get further information? ";

        Uri uri = Uri.parse(message); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }




    public void sublimePicker() {
        SublimePickerDialogFragment.Callback mFragmentCallback = new SublimePickerDialogFragment.Callback( ) {
            @Override
            public void onCancelled() {
            }
            @Override
            public void onDateTimeRecurrenceSet(SelectedDate mSelectedDate,
                                                int hourOfDay, int minute,
                                                SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                                String recurrenceRule) {

                if (mSelectedDate != null) {

                    //sample handling. add your own handling here.
                    int year = mSelectedDate.getEndDate().get(Calendar.YEAR);
                    int month = mSelectedDate.getEndDate().get(Calendar.MONTH);
                    int day = mSelectedDate.getEndDate().get(Calendar.DATE);
                    GregorianCalendar date = new GregorianCalendar(year, month, day, hourOfDay, minute, 0);
                    long epoch = date.getTimeInMillis();
                    Date dt = new Date(epoch);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM, yyyy h:mm a");
                    mDate.setText(sdf.format(dt));

                }
            }
        };
        SublimePickerDialogFragment pickerFrag = new SublimePickerDialogFragment();
        pickerFrag.setCallback(mFragmentCallback);


        SublimeOptions opts = new SublimeOptions().setCanPickDateRange(false).setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER | SublimeOptions.ACTIVATE_TIME_PICKER).setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", opts);
        pickerFrag.setArguments(bundle);
        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(zvhir.dev.tutormy.LearnerDashboard.this.getSupportFragmentManager(), "SUBLIME_PICKER");
    }

}
