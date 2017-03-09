package com.shemchavez.ass.Fragment;


import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kosalgeek.android.photoutil.ImageLoader;
import com.shemchavez.ass.R;
import com.shemchavez.ass.Shared.Global;

import java.io.FileNotFoundException;

public class ChooseImageDialogFragment extends android.support.v4.app.DialogFragment{

    private TextView tvChooseImageTakePhoto, tvChooseImageSelectFromGallery;
    Callback callback;

    public ChooseImageDialogFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dialog_fragment_choose_image, container, false);

        callback = (Callback) getTargetFragment();

        tvChooseImageTakePhoto = (TextView) rootView.findViewById(R.id.tvChooseImageTakePhoto);
        tvChooseImageSelectFromGallery = (TextView) rootView.findViewById(R.id.tvChooseImageSelectFromGallery);

        tvChooseImageTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        tvChooseImageSelectFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFromGallery();
            }
        });

        return rootView;
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    private void selectFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap galleryBitmap = BitmapFactory.decodeFile(picturePath);
                if (galleryBitmap != null) {
                    callback.setBitmap(galleryBitmap);
                }
            } else {
                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    callback.setBitmap(Global.rotateBitmap(cameraBitmap, 90));
                }
            }
        }
        dismiss();
    }

    public interface Callback{
        void setBitmap(Bitmap bitmap);
    }
}
