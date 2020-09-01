package com.khurshid.kamkorapartner.test;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.api.ApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadExample extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_REQUEST_CODE = 2;
    private static final String MYTAG = FileUploadExample.class.getSimpleName();
    @BindView(R.id.iv_test_image_preview)
    ImageView ivPreview;
    @BindView(R.id.bt_test_select_file)
    Button btSelectFile;
    @BindView(R.id.bt_test_upload_file)
    Button btUploadFile;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload_example);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        btSelectFile.setOnClickListener(this);
        btUploadFile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_test_select_file) {
            pickImageFromGallery();
        }

        if (v.getId() == R.id.bt_test_upload_file) {
            prepareParams();
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
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

    private void prepareParams() {

        File file = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver()
                            .openFileDescriptor(imageUri, "r", null);
            InputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
            file = new File(getCacheDir(), getFileName(imageUri));
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(imageUri)),
                        file
                );
        MultipartBody.Part part = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

//        MediaType CONTENT_TYPE = MediaType.parse("multipart/form-data");
//        RequestBody bodyData = new MultipartBody.Builder()
//                .setType(CONTENT_TYPE)
//                .addFormDataPart("name", "heroalom")
//                .addFormDataPart("email", "hero5@alom5.com")
//                .addPart(part)
//                .build();

        Call<JsonObject> call = ApiClient.getInterface()
                .testPost(part);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(MYTAG, String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(MYTAG, "Error posting: " + t.getMessage());
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/jpg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
                previewImage();
            }
        }
    }

    private void previewImage() {
        Glide.with(this)
                .load(imageUri)
                .into(ivPreview);
    }
}