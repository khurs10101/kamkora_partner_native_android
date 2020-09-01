package com.khurshid.kamkorapartner.view.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.api.ApiClient;
import com.khurshid.kamkorapartner.utils.SessionManager;
import com.khurshid.kamkorapartner.view.dialogs.DialogError;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentUploadAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MYTAG = DocumentUploadActivity.class.getSimpleName();
    private static final int GALLERY_REQUEST_CODE_ID = 1;
    @BindView(R.id.iv_upload_address)
    ImageView ivId;
    //    @BindView(R.id.iv_upload_address_id)
//    ImageView ivAddressId;
    @BindView(R.id.tv_document_address_upload)
    TextView tvIdUpload;
    //    @BindView(R.id.tv_document_address_upload)
//    TextView tvAddressIdUpload;
    @BindView(R.id.lv_document_address_upload_success)
    LinearLayout lvIdSuccess;
    @BindView(R.id.lv_document_address_upload_failed)
    LinearLayout lvIdFailed;
    private Uri imageUriId;
//    private Uri imageUriAddressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload_address);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ivId.setOnClickListener(this);
//        ivAddressId.setOnClickListener(this);
        tvIdUpload.setOnClickListener(this);
//        tvAddressIdUpload.setOnClickListener(this);
    }

    public void showLv(LinearLayout linearLayout) {
        if (linearLayout.getVisibility() == View.GONE) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    public void hideLv(LinearLayout linearLayout) {
        if (linearLayout.getVisibility() == View.VISIBLE) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void pickImageFromGalleryForID() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        String[] mimeTypes = {"image/jpeg", "image/jpg", "image/png"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivityForResult(intent, GALLERY_REQUEST_CODE_ID);
        startCropActivityAddressId();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE_ID) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getData() != null) {
                    imageUriId = data.getData();
//                    startCropActivityId();
                }
            }
        }

//        if (requestCode == GALLERY_REQUEST_CODE_ADDRESS_ID) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (data.getData() != null) {
//                    imageUriAddressId = data.getData();
//                    startCropActivityAddressId();
//                }
//            }
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Log.d(MYTAG, "Uri of cropped Image: " + result.getUri());
                Log.d(MYTAG, "Uri Information: " + result.describeContents());
                imageUriId = result.getUri();
                previewImageId();
            }
        }
    }

    private void previewImageId() {
        Glide.with(this)
                .load(imageUriId)
                .into(ivId);
    }

//    private void startCropActivityId() {
//        CropImage.activity(imageUriId).start(this);
//    }

    private void startCropActivityAddressId() {
//        CropImage.activity(imageUriAddressId).start(this);
        CropImage
                .activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_upload_address) {
            pickImageFromGalleryForID();
        }

        if (v.getId() == R.id.tv_document_address_upload) {
            if (imageUriId != null)
                prepareParams();
            else
                DialogError
                        .newInstance("Please select address id from gallary")
                        .show(getSupportFragmentManager(), "");
        }
    }

    private void prepareParams() {

        File file = new File(imageUriId.getPath());
        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse(getMimeType(imageUriId)));
        MultipartBody.Part addressIdImage =
                MultipartBody.Part.createFormData("addressId", file.getName(), requestFile);

        RequestBody docType =
                RequestBody.create("addressId", MediaType.parse("multipart/form-data"));

        Call<JsonObject> call = ApiClient
                .getInterface()
                .uploadPartnerAddressProof(SessionManager.getLoggedInUserId(this),
                        docType,
                        addressIdImage);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
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