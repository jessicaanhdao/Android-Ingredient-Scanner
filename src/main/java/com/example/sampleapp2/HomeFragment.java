package com.example.sampleapp2;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private ImageView iv;
    Button analyzeButton;
    Button retakeButton;
    Bitmap imageBitmap;
    Bitmap rotatedBitmap;
    Uri imageUri;
    static private final int REQUEST_IMAGE_CAPTURE = 101;
    final int REQUEST_CAMERA_PERMISSION_ID = 1001;


    private HomeFragmentListener listener ;
    public interface HomeFragmentListener {
        void onWordsSent(ArrayList inputArray);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        } else {
            imageUri = getActivity().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (imageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                //try {
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_ID);
                    }
                    startActivityForResult(imageIntent, REQUEST_IMAGE_CAPTURE);

                /*} catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        iv = getView().findViewById(R.id.imageView);
        analyzeButton = getView().findViewById(R.id.analyzeButton);
        retakeButton = getView().findViewById(R.id.retakeButton);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();
                if (!textRecognizer.isOperational()) {
                    Log.w("ScanActivity", "Detector Dependencies are not available");
                } else {
                    ArrayList<String> arrayList = new ArrayList<>();
                    Frame frame = new Frame.Builder().setBitmap(rotatedBitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    //StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = items.valueAt(i);
                        String[] splits = item.getValue().split("[,@&.?$+-]+");
                        for (int j = 0 ; j < splits.length; j++) {
                            if (!splits[j].trim().equals("")) {

                            }
                            arrayList.add(splits[j].trim());

                        }
                    }
                    listener.onWordsSent(arrayList);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(), imageUri);
                ExifInterface ei = new ExifInterface(   getContext().getContentResolver().openInputStream(imageUri));
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                rotatedBitmap = imageBitmap ;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(imageBitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(imageBitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(imageBitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = imageBitmap;
                }
                iv.setImageBitmap(rotatedBitmap);
                //String imageurl = getRealPathFromURI(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    //called when fragment is attached to activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //check if our activity implement this interface - HomeFragmentListenr
        if (context instanceof HomeFragmentListener) {
            listener = (HomeFragmentListener) context; //our activity
        } else {
            throw new RuntimeException(context.toString() + " must implement HomeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
