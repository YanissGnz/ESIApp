package com.example.esiapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.esiapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {

   private static final int RESULT_OK = 0;
    private static final String TAG = "ProfileFragment" ;
    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    StorageReference storageRef;

    //view
    ImageView profile_picture,edit_profile_picture,edit_information,edit_account;
    TextView email,full_name;
    Spinner gender,grade,group;
    EditText password_edit;


    String email1,pass_word , full_Name,picture;
    private static final int TAKE_IMAGE_CODE=1001;

    public ProfileFragment(){ }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);



        //-----------------------------------information(gender,grade,grp)------------------------------------------------------------------------------------

        gender =view.findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        gender.setOnItemSelectedListener(this);



        grade =view.findViewById(R.id.grade_spinner);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),R.array.grade,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(adapter1);
        grade.setOnItemSelectedListener(this);


        group =view.findViewById(R.id.groupe_spinner);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.group,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group.setAdapter(adapter2);
        group.setOnItemSelectedListener(this);




        email =view.findViewById(R.id.profile_email_editor);
        full_name = view.findViewById(R.id.profile_fullname);
        password_edit = view.findViewById(R.id.profile_password);
        profile_picture = view.findViewById(R.id.post_profile_picture);
        edit_profile_picture = view.findViewById(R.id.edit_profile_picture);
        edit_information = view.findViewById(R.id.edit_information);
        edit_account = view.findViewById(R.id.edit_account);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        storageRef = FirebaseStorage.getInstance().getReference("uploads");



        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    email1 = ""+ds.child("email").getValue();
                    full_Name = ""+ds.child("full_name").getValue();
                    pass_word = ""+ds.child("password").getValue();
                    picture = ""+ds.child("profile_picture").getValue();

                    full_name.setText(full_Name);
                    email.setText(email1);
                    password_edit.setText(pass_word);
                    try {
                        Picasso.get().load(picture).into(profile_picture);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.male).into(profile_picture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        });



        edit_profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               if (intent.resolveActivity(getActivity().getPackageManager()) !=null ){
                   startActivityForResult(intent,TAKE_IMAGE_CODE);
               }
            }
        });



        return view;
    }

    public void onActivityResult(int requestCode ,int resultCode, @NonNull Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == TAKE_IMAGE_CODE ){
            switch (resultCode){
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profile_picture.setImageBitmap(bitmap);
                    handleupload(bitmap);
            }
        }
    }

    private void handleupload(Bitmap bitmap) {
        ByteArrayOutputStream baos =new ByteArrayOutputStream();
        bitmap.compress((Bitmap.CompressFormat.JPEG),100,baos);

        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference rfrnce=FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid+".jpec");
        rfrnce.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(rfrnce);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG,"onSuccess:"+uri);
                        setUserProfileUrl(uri);
                    }
                });

    }

    private void setUserProfileUrl(Uri uri){
        FirebaseUser usr=FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request =new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        usr.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Profile image failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

