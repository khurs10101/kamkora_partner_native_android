package com.khurshid.kamkorapartner.view.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.model.Partner;
import com.khurshid.kamkorapartner.utils.SessionManager;
import com.khurshid.kamkorapartner.view.activities.DocumentUploadActivity;
import com.khurshid.kamkorapartner.view.activities.DocumentUploadAddressActivity;
import com.khurshid.kamkorapartner.view.activities.LoginActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String MYTAG = ProfileFragment.class.getSimpleName();
    private TextView tvName, tvEmail, tvPhone,
            tvGender, tvEditProfile, tvUploadDocuments, tvUploadDocumentsAddress, tvLogout, tvLogin;
    private LinearLayout lvNotVerified, lvVerified, lvLoggedIn, lvLoggedOut;
    private CircleImageView circleImageView;
    private String stName, stEmail, stPhone, stGender;
    private Boolean isAccountVerifed = false;
    private Uri imageUri;
    private ImageView ivEditName, ivEditEmail, ivEditPhone, ivEditGender;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ivEditName = view.findViewById(R.id.iv_profile_name_edit);
        ivEditEmail = view.findViewById(R.id.iv_profile_email_edit);
        ivEditPhone = view.findViewById(R.id.iv_profile_phone_edit);
        ivEditGender = view.findViewById(R.id.iv_profile_gender_edit);
        tvName = view.findViewById(R.id.tv_profile_username);
        tvEmail = view.findViewById(R.id.tv_profile_email);
        tvPhone = view.findViewById(R.id.tv_profile_phone);
        tvGender = view.findViewById(R.id.tv_profile_gender);
        lvNotVerified = view.findViewById(R.id.lv_profile_not_verified);
        lvVerified = view.findViewById(R.id.lv_profile_verified);
        tvEditProfile = view.findViewById(R.id.tv_profile_edit);
        tvUploadDocuments = view.findViewById(R.id.tv_profile_upload_photo_id);
        tvUploadDocumentsAddress = view.findViewById(R.id.tv_profile_upload_address);
        tvLogout = view.findViewById(R.id.tv_profile_logout);
        circleImageView = view.findViewById(R.id.iv_profile_avatar);
        lvLoggedIn = view.findViewById(R.id.lv_fragment_profile_logged_in);
        lvLoggedOut = view.findViewById(R.id.lv_fragment_profile_not_logged_in);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (SessionManager.isLoggedIn(getActivity())) {
            Gson gson = new Gson();
            String userJson = SessionManager.getLoggedInUserObject(getActivity());
            if (userJson != null) {
                Partner partner = gson.fromJson(userJson, Partner.class);
                if (partner != null) {
                    if (partner.getName() != null)
                        stName = partner.getName();

                    if (partner.getEmail() != null)
                        stEmail = partner.getEmail();

                    if (partner.getPhone() != null)
                        stPhone = partner.getPhone();

                    if (partner.getAccountVerified() != null)
                        isAccountVerifed = partner.getAccountVerified();

                } else {
                    Log.d(MYTAG, "Partner object is null: " + partner);
                }
            } else {
                Log.d(MYTAG, "UserJson is null: " + userJson);
            }
            showLv(lvLoggedIn);
            hideLv(lvLoggedOut);
            initView();
        } else {
            showLv(lvLoggedOut);
            hideLv(lvLoggedIn);
            tvLogin = view.findViewById(R.id.tv_fragment_profile_login);
            tvLogin.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
        }

    }

    private void showLv(LinearLayout linearLayout) {
        linearLayout.setVisibility(View.VISIBLE);
    }

    private void hideLv(LinearLayout linearLayout) {
        linearLayout.setVisibility(View.GONE);
    }

    private void initView() {

        tvName.setText(stName);
        tvEmail.setText(stEmail);
        tvPhone.setText(stPhone);
        tvGender.setText(stGender);
        ivEditName.setOnClickListener(this);
        ivEditEmail.setOnClickListener(this);
        ivEditPhone.setOnClickListener(this);
        ivEditGender.setOnClickListener(this);

//        tvUploadDocuments.setOnClickListener(this);
        tvUploadDocuments.setOnClickListener(this);
        tvUploadDocumentsAddress.setOnClickListener(this);
        tvEditProfile.setOnClickListener(this);
        if (isAccountVerifed) {
            hideLv(lvNotVerified);
            showLv(lvVerified);
        } else {
            hideLv(lvVerified);
            showLv(lvNotVerified);
            lvNotVerified.setOnClickListener(this);
        }

        circleImageView.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_profile_name_edit) {

        }

        if (v.getId() == R.id.iv_profile_email_edit) {

        }

        if (v.getId() == R.id.iv_profile_phone_edit) {

        }

        if (v.getId() == R.id.iv_profile_gender_edit) {

        }

        if (v.getId() == R.id.lv_profile_not_verified) {

        }

        if (v.getId() == R.id.tv_profile_edit) {

        }

        if (v.getId() == R.id.tv_profile_upload_photo_id) {
            Intent intent = new Intent(getActivity(), DocumentUploadActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.tv_profile_upload_address) {
            Intent intent = new Intent(getActivity(), DocumentUploadAddressActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.iv_profile_avatar) {
            pickImageFromGallary();
        }

        if (v.getId() == R.id.tv_profile_logout) {
            SessionManager.endSession();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void pickImageFromGallary() {
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAspectRatio(1, 1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                imageUri = result.getUri();
//                previewImage();
                updateImageToServer();
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity()
                    .getContentResolver()
                    .query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor
                            .getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void updateImageToServer() {

        File file = null;
        file = new File(imageUri.getPath());
//        try {
//            ParcelFileDescriptor parcelFileDescriptor =
//                    getActivity().getContentResolver()
//                            .openFileDescriptor(imageUri, "r", null);
//            InputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
//            file = new File(getActivity().getCacheDir(), getFileName(imageUri));
//            OutputStream outputStream = new FileOutputStream(file);
//            byte[] buffer = new byte[8192];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        Log.d(MYTAG, "imageUri updateImageToServer: " + imageUri);
        Log.d(MYTAG, "mimetype via getContext: " + getMimeType(imageUri));
        Log.d(MYTAG, "mimetype via getActivity: " + getActivity().getContentResolver().getType(imageUri));
        Log.d(MYTAG, "Scheme of file: " + imageUri.getScheme());

        RequestBody requestFile =
                RequestBody.create(file,
                        MediaType.parse(getMimeType(imageUri))
                );
        MultipartBody.Part part = MultipartBody.Part.createFormData("partnerAvatar", file.getName(), requestFile);

    }

    private void previewImage() {
        Glide.with(getActivity())
                .load(imageUri)
                .into(circleImageView);
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
}
