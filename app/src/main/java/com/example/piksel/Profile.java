package com.example.piksel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Profile extends AppCompatActivity {


    private ImageView BackArrow;
    private ImageView PhotoProfile;
    private Button EditProfile;
    private TextView NameProfile;
    private TextView EmailProfile;
    private Uri ImageUri;
    private String userID, userName, userEmail, ProfilePhoto;
    private DocumentReference documentReference;
    private EditText pricePhoto;
    private ProgressBar progressBarVideo;
    private StorageReference storageReference;
    private ProgressBar progressBarImage;
    private ImageView imageView;
    private FirebaseFirestore FStore = FirebaseFirestore.getInstance();
    private LinearLayout DatePickerLinear;
    private TextView DayText, MonthText, YearText;
    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        BackArrow = findViewById(R.id.BackArrow);
        PhotoProfile = findViewById(R.id.PhotoProfile);
        EditProfile = findViewById(R.id.EditProfile);
        NameProfile = findViewById(R.id.NameProfile);
        EmailProfile = findViewById(R.id.EmailProfile);
        progressBarVideo = findViewById(R.id.Progressbar_BucketVideoProfile);
        progressBarVideo.setVisibility(View.INVISIBLE);
        pricePhoto = findViewById(R.id.PricePhotoProfile);
        storageReference = FirebaseStorage.getInstance().getReference("Photos");
        progressBarImage = findViewById(R.id.Progressbar_BucketImageProfile);
        imageView = findViewById(R.id.ImageviewUploadProfile);
        Button buttonUpload = findViewById(R.id.UploadButtonProfile);
        ImageView logoutImage = findViewById(R.id.LogoutToolbar1);
        DatePickerLinear = findViewById(R.id.DatePicker);
        DayText = findViewById(R.id.DatePickerDay);
        MonthText = findViewById(R.id.DatePickerMonth);
        YearText = findViewById(R.id.DatePickerYear);


        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        documentReference = FirebaseFirestore.getInstance().collection("Users").document(userID);


        setValue();
        initDate();


        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Start_activity.class));
                finish();
            }
        });


        EditProfile.setOnClickListener(new View.OnClickListener() {
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


        DatePickerLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

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
            }
        };


        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        //datePickerDialog.show();
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
        if (ImageUri != null && !TextUtils.isEmpty(pricePhoto.getText().toString())) {
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
                                    int price = Integer.parseInt(pricePhoto.getText().toString());
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("imageUrl", url);
                                    map.put("userID", userID);
                                    map.put("askedPrice", price);
                                    map.put("like", 0);
                                    map.put("highestBid", 0);
                                    map.put("HighestID", "-1");
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