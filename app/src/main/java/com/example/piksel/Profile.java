package com.example.piksel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Profile extends AppCompatActivity {


    private ImageView PhotoProfile;
    private TextView NameProfile;
    private TextView EmailProfile;
    private Uri ImageUri;
    private String userID, userName, userEmail, ProfilePhoto;
    private DocumentReference documentReference;
    private EditText pricePhoto;
    private StorageReference storageReference;
    private ProgressBar progressBarImage;
    private ImageView imageView;
    private FirebaseFirestore FStore = FirebaseFirestore.getInstance();
    private TextView DayText, MonthText, YearText;
    private DatePickerDialog datePickerDialog;
    private TextView HourText,MinuteText;
    private TimePickerDialog timePickerDialog;
    private int Hour=-1,Minute=-1,Day=-1,Month=-1,Year=-1;
    private String MonthName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ImageView backArrow = findViewById(R.id.BackArrow);
        PhotoProfile = findViewById(R.id.PhotoProfile);
        Button editProfile = findViewById(R.id.EditProfile);
        NameProfile = findViewById(R.id.NameProfile);
        EmailProfile = findViewById(R.id.EmailProfile);
        ProgressBar progressBarVideo = findViewById(R.id.Progressbar_BucketVideoProfile);
        progressBarVideo.setVisibility(View.INVISIBLE);
        pricePhoto = findViewById(R.id.PricePhotoProfile);
        storageReference = FirebaseStorage.getInstance().getReference("Photos");
        progressBarImage = findViewById(R.id.Progressbar_BucketImageProfile);
        imageView = findViewById(R.id.ImageviewUploadProfile);
        Button buttonUpload = findViewById(R.id.UploadButtonProfile);
        ImageView logoutImage = findViewById(R.id.LogoutToolbar1);
        LinearLayout datePickerLinear = findViewById(R.id.DatePicker);
        DayText = findViewById(R.id.DatePickerDay);
        MonthText = findViewById(R.id.DatePickerMonth);
        YearText = findViewById(R.id.DatePickerYear);
        HourText=findViewById(R.id.TimePickerHour);
        MinuteText=findViewById(R.id.TimePickerMinute);
        LinearLayout timePickerLinear = findViewById(R.id.TimerPicker);






        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        documentReference = FirebaseFirestore.getInstance().collection("Users").document(userID);


        setValue();
        initDate();
        initTime();


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Start_activity.class));
                finish();
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, EditProfile.class));
                finish();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                //startActivity(new Intent(Profile.this, Start_activity.class));
                //finish();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenFileChooser();

            }
        });


        logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
            }
        });


        datePickerLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
                datePickerDialog.show();
            }
        });



        timePickerLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
                datePickerDialog.show();
            }
        });

    }














    private void initTime() {

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                HourText.setText("Hour: "+String.valueOf(hourOfDay));
                MinuteText.setText("Minute: "+String.valueOf(minute));
                Hour=hourOfDay;
                Minute=minute;
            }

        };

        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);


        timePickerDialog=new TimePickerDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK,timeSetListener,hour,minute,true);
    }


    private String getMonth(int month)
    {
        switch (month)
        {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";

            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return null;
    }


    private void initDate() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;

                DayText.setText("Day: " + String.valueOf(dayOfMonth));
                MonthText.setText("Month: " + String.valueOf(month));
                YearText.setText("Year: " + String.valueOf(year));
                Year=year;
                Month=month;
                Day=dayOfMonth;

            }
        };


        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

    }


    private void setValue() {

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    userName = Objects.requireNonNull(documentSnapshot.get("Name")).toString();
                    userEmail = Objects.requireNonNull(documentSnapshot.get("Email")).toString();
                    ProfilePhoto = Objects.requireNonNull(documentSnapshot.get("Profile_photo")).toString();
                    NameProfile.setText(userName);
                    EmailProfile.setText(userEmail);
                    if (!TextUtils.equals(ProfilePhoto, "-1")) {
                        Glide.with(getCurrentFocus()).load(ProfilePhoto).into(PhotoProfile);
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Snackbar.make(getCurrentFocus(), "Error while Loading", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    private void uploadFile() {
        Timestamp timestamp= Timestamp.now();
        Boolean flagTime=(Hour!=-1 && Minute!=-1 && Day!=-1 && Month!=-1 && Year!=1);
        if (ImageUri != null && !TextUtils.isEmpty(pricePhoto.getText().toString()) && flagTime) {
            StorageReference FileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(ImageUri));
            progressBarImage.setVisibility(View.VISIBLE);


            FileReference.putFile(ImageUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBarImage.setProgress(0);
                                    progressBarImage.setVisibility(View.INVISIBLE);
                                }
                            }, 500);


                            FileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    String deadline=String.valueOf(Day)+" "+getMonth(Month)+" "+String.valueOf(Year)+" at "+
                                            String.valueOf(Hour)+
                                            ":"+String.valueOf(Minute);

                                    int price = Integer.parseInt(pricePhoto.getText().toString());
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("imageUrl", url);
                                    map.put("userID", userID);
                                    map.put("askedPrice", price);
                                    map.put("like", 0);
                                    map.put("highestBid", 0);
                                    map.put("HighestID", "-1");
                                    map.put("DeadlineMonth",Month);
                                    map.put("DeadlineDay",Day);
                                    map.put("DeadlineYear",Year);
                                    map.put("DeadlineHour",Hour);
                                    map.put("DeadlineMinute",Minute);
                                    map.put("deadline",deadline);
                                    map.put("DeleteFlag",true);



                                    FStore.collection("Photos").document().set(map);
                                    Toast.makeText(getApplicationContext(), "uploaded successfully", Toast.LENGTH_SHORT).show();
                                    imageView.setImageResource(R.drawable.add_photo);
                                    pricePhoto.getText().clear();
                                }
                            });

                        }
                    }).
                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Snackbar.make(getCurrentFocus(), e.toString(), Snackbar.LENGTH_SHORT).show();
                        }
                    }).
                    addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                            double progress = 100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                            progressBarImage.setProgress((int) progress);
                        }
                    });
        } else {
            Snackbar.make(getCurrentFocus(), "No file found", Snackbar.LENGTH_SHORT).show();
        }
    }


    private void OpenFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            ImageUri = data.getData();
            Picasso.get().load(ImageUri).into(imageView);
        }
    }

}