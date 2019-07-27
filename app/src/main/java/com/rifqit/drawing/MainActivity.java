package com.rifqit.drawing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.OutputStream;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    ImageButton addPhoto,done,undo,redo;
    Button save;
    TouchImageView image_preview;
    private int GALLERY = 1;
    Bitmap bmp;
    Bitmap alteredBitmap;
    FloatingActionMenu menu;
    FloatingActionButton sub1,sub2,sub3,sub4;
    boolean showingFirst;
    private static final String IMAGE_DIRECTORY = "/drawingku";


    Canvas canvas;
    Paint paint;
    Matrix matrix;
    float downx;
    float downy;
    float upx;
    float upy;
    int DefaultColor ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPhoto = findViewById(R.id.addPhoto);
        done = findViewById(R.id.done);
        undo = findViewById(R.id.undo);
        redo = findViewById(R.id.redo);
        menu = findViewById(R.id.menu);
        save = findViewById(R.id.save);
        image_preview = findViewById(R.id.preview);
        sub1 = findViewById(R.id.sub1);
        sub2 = findViewById(R.id.sub2);
        sub3 = findViewById(R.id.sub3);
        sub4 = findViewById(R.id.sub4);

        sub1.setOnClickListener(this);
        sub2.setOnClickListener(this);
        sub3.setOnClickListener(this);
        sub4.setOnClickListener(this);

        sub1.setOnLongClickListener(this);
        sub2.setOnLongClickListener(this);
        sub3.setOnLongClickListener(this);
        sub4.setOnLongClickListener(this);

        showingFirst = true;
//        menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(showingFirst == true){
//                    save.setVisibility(View.VISIBLE);
//                    done.setVisibility(View.GONE);
//                }else{
//                    save.setVisibility(View.GONE);
//                    done.setVisibility(View.VISIBLE);
//                    showingFirst = true;
//                }
//            }
//        });

        menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    done.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);
                } else {
                    done.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                }
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });

//        if (menu.isMenuHidden()){
//
//        }else {
//            done.setVisibility(View.VISIBLE);
//            save.setVisibility(View.GONE);
//        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                save.setVisibility(View.VISIBLE);
                done.setVisibility(View.GONE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_preview.getDrawable() == null){
                    Toast.makeText(MainActivity.this, "Image is empty", Toast.LENGTH_SHORT).show();
                }else {
                    requestMultiplePermissions();
                }
            }
        });

//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bitmap emptyBitmap = Bitmap.createBitmap(alteredBitmap.getWidth(), alteredBitmap.getHeight(), alteredBitmap.getConfig());
//                if (alteredBitmap.sameAs(emptyBitmap)){
//                    Toast.makeText(MainActivity.this, "empty", Toast.LENGTH_SHORT).show();
//                }else {
//                    saveBitmap(alteredBitmap);
//                }
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub1:
                if (image_preview.getDrawable() == null){
                    Toast.makeText(MainActivity.this , "Image is empty",Toast.LENGTH_SHORT).show();
                }else {
                    drawCircle();
                }
                break;
            case R.id.sub2:
                if (image_preview.getDrawable() == null){
                    Toast.makeText(MainActivity.this , "Image is empty",Toast.LENGTH_SHORT).show();
                }else {
                    brushOn();
//                    Log.e("width", alteredBitmap.getWidth()+" "+"&"+" "+canvas.getWidth()+" ");
//                    Log.e("height", alteredBitmap.getHeight()+" "+"&"+" "+canvas.getHeight()+" ");
                }
                break;
            case R.id.sub3:
                if (image_preview.getDrawable() == null){
                    Toast.makeText(MainActivity.this , "Image is empty",Toast.LENGTH_SHORT).show();
                }else {
                    DrawLine();
                }
                break;
            case R.id.sub4:
                Toast.makeText(this, "Button 4 clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.sub1:
                piccolor();
                break;
            case R.id.sub3:
                piccolor();
                break;
            case R.id.sub2:
                piccolor();
                break;
            case R.id.sub4:
                Toast.makeText(this, "Button 4 long clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {

                Uri imageFileUri = data.getData();
                try {
                    BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                    bmpFactoryOptions.inJustDecodeBounds = true;
                    bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(
                            imageFileUri), null, bmpFactoryOptions);

                    bmpFactoryOptions.inJustDecodeBounds = false;
                    bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(
                            imageFileUri), null, bmpFactoryOptions);
//
//                    Integer wdth = bmp.getWidth();
//                    Integer hght = bmp.getHeight();
////                    if (wdth > 2560 || hght > 1600) {
////                        getResizedBitmap(bmp,2560, 1600 );
////                        alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
////                    }else {
////                        alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
////                    }
//
//                    alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
//                    Log.e("widthXheight", bmp.getWidth() + " " + "&" + " " + bmp.getHeight());
//
//                    image_preview.setImageBitmap(bmp);
//                    image_preview.setImageBitmap(alteredBitmap);
//                    canvas = new Canvas(alteredBitmap);
                    image_preview.setImageURI(imageFileUri);
                    Bitmap.Config config;
                    if(bmp.getConfig() != null){
                        config = bmp.getConfig();
                    }else{
                        config = Bitmap.Config.ARGB_8888;
                    }

//                    bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri)).copy(config,true);
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageFileUri);

//                    int wdt = bmp.getWidth();
//                    int hght = bmp.getHeight();
//                    //bitmapMaster is Mutable bitmap
//                    if (wdt>2048){
//                        alteredBitmap = Bitmap.createBitmap(wdt/2, hght/2, config);
//                    }else if (hght>2048){
//                        alteredBitmap = Bitmap.createBitmap(wdt/2, hght/2, config);
//                    }else if(hght>2048 && wdt>2048){
//                        alteredBitmap = Bitmap.createBitmap(wdt/2, hght/2, config);
//                    }
//                    else {
//                        alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), config);
//                    }

                    //bitmapMaster is Mutable bitmap
                    alteredBitmap = bmp.copy(Bitmap.Config.ARGB_8888,true);

                    paint = new Paint();
                    canvas = new Canvas(alteredBitmap);
                    canvas.setBitmap(alteredBitmap);

                    image_preview.setImageBitmap(alteredBitmap);

                } catch (Exception e) {
                    Log.v("ERROR", e.toString());
                }
//                contentURI = data.getData();
//
////                image_preview.setImageURI(contentURI);
//                byte[] nknknkn = convertImageToByte(contentURI);
//                Log.e("sizeImage", contentURI.getPath());
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
////                    path = saveImage(bitmap);
////                    Toast.makeText(MainActivity.this, "foto disimpan", Toast.LENGTH_SHORT).show();
//                    mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//                    image_preview.setImageBitmap(mutableBitmap);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(MainActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
//                }
            }
        }
//        else if (requestCode == CAMERA) {
//            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(thumbnail);
//            path = saveImage(thumbnail);
//            Toast.makeText(EditProfil.this, "foto disimpan", Toast.LENGTH_SHORT).show();
//        }
    }

    private void OpenColorPickerDialog(boolean AlphaSupport) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(MainActivity.this, DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                DefaultColor = color;

                paint.setColor(color);

            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

                Toast.makeText(MainActivity.this, "Color Picker Closed", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();

    }
    @SuppressLint("ClickableViewAccessibility")
    public void brushOn(){


        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);
        matrix = new Matrix();
        canvas.drawBitmap(bmp,null, new RectF(0, 0, alteredBitmap.getWidth(), alteredBitmap.getHeight()), paint);

        image_preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        downx = event.getX();
                        downy = event.getY();
                        image_preview.invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        upx = event.getX();
                        upy = event.getY();
                        canvas.drawLine(downx, downy, upx, upy, paint);
                        image_preview.invalidate();
                        downx = upx;
                        downy = upy;
                        break;
                    case MotionEvent.ACTION_UP:
                        upx = event.getX();
                        upy = event.getY();
                        canvas.drawLine(downx, downy, upx, upy, paint);
                        image_preview.invalidate();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    default:
                        break;
                }
                return true;
            }

        });
    }

    public void drawCircle(){

        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawCircle(200,200,100,paint);

    }

    public void DrawLine() {

        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.WHITE);

        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        canvas.drawLine(70, alteredBitmap.getHeight() / 2, alteredBitmap.getWidth() - 70, alteredBitmap.getHeight() / 2, paint);
    }

    public void saveBitmap(Bitmap btmp){
        ContentValues contentValues = new ContentValues(3);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "Draw On Me");

        Uri imageFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        try {
            OutputStream imageFileOS = getContentResolver().openOutputStream(imageFileUri);
            btmp.compress(Bitmap.CompressFormat.JPEG, 90, imageFileOS);
            Toast t = Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT);
            t.show();

        } catch (Exception e) {
            Log.v("EXCEPTION", e.getMessage());
        }
    }

    public void  piccolor(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup) findViewById(R.id.your_dialog_root_element));
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setView(layout);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        SeekBar sb = (SeekBar)layout.findViewById(R.id.your_dialog_seekbar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                paint.setStrokeWidth(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//                Button finish = layout.findViewById(R.id.finish_dialog_button);
        Button color = layout.findViewById(R.id.color_dialog_button);

//                finish.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenColorPickerDialog(true);
                alertDialog.dismiss();
            }
        });
    }


    private void  requestMultiplePermissions(){
        Dexter.withActivity(MainActivity.this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Log.e("Izin ", " Diterima");
                            saveBitmap(alteredBitmap);
                        }
                        else if (report.isAnyPermissionPermanentlyDenied()) {
                            Log.e("Izin ", " DiTolak premanent");
                        }else {
                            Log.e("Izin ", " Ditolak");
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Terjadi ssesuatu!", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }



}
