package com.example.vaibhav.slidingpuzzle;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_IMAGE = 1,gridSize = 4;
    private static final long oneMinute = 60 * 1000;
    private int blankPos,numPieces,attemptsLeft;
    private long timeLeftInMillis;
    private Bitmap selectedBitmap;
    private Button btnSelectImage,btnViewImage;
    private TextView txtTimer;
    private ArrayList<ImageView> pieces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        btnSelectImage = (Button) findViewById(R.id.btnSelectImage);
        btnViewImage   = (Button) findViewById(R.id.btnViewImage);
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        pieces = new ArrayList<>();
        attemptsLeft = 2;
        numPieces = gridSize*gridSize;
        blankPos = numPieces -1;
        timeLeftInMillis = oneMinute;

        ImageView [] imageArray = new ImageView[numPieces];
        imageArray[0]  = (ImageView) findViewById(R.id.btn11);
        imageArray[1]  = (ImageView) findViewById(R.id.btn12);
        imageArray[2]  = (ImageView) findViewById(R.id.btn13);
        imageArray[3]  = (ImageView) findViewById(R.id.btn14);
        imageArray[4]  = (ImageView) findViewById(R.id.btn21);
        imageArray[5]  = (ImageView) findViewById(R.id.btn22);
        imageArray[6]  = (ImageView) findViewById(R.id.btn23);
        imageArray[7]  = (ImageView) findViewById(R.id.btn24);
        imageArray[8]  = (ImageView) findViewById(R.id.btn31);
        imageArray[9]  = (ImageView) findViewById(R.id.btn32);
        imageArray[10] = (ImageView) findViewById(R.id.btn33);
        imageArray[11] = (ImageView) findViewById(R.id.btn34);
        imageArray[12] = (ImageView) findViewById(R.id.btn41);
        imageArray[13] = (ImageView) findViewById(R.id.btn42);
        imageArray[14] = (ImageView) findViewById(R.id.btn43);
        imageArray[15] = (ImageView) findViewById(R.id.btn44);

        for (ImageView img:imageArray) {
            img.setClickable(false);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            pieces.add(img);
        }

        for (int i = 0; i< numPieces; i++){
            final int finalI = i;
            pieces.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveBlank(finalI);
                }
            });
        }

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewImage();
            }
        });
    }

    private void viewImage() {
        Log.d(TAG, "viewImage: ");

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.image_dialog);

        ImageView imgOriginal = (ImageView) dialog.findViewById(R.id.imgOriginal);
        imgOriginal.setImageBitmap(selectedBitmap);

        dialog.show();
    }

    private void moveBlank(int pos) {
        Log.d(TAG, "moveBlank: ");

        if (pos == blankPos)
            return;
        if (pos%gridSize != gridSize-1 && blankPos == pos+1){
            moveBlankLeft();
        }else
        if (pos%gridSize != 0 && blankPos == pos-1){
            moveBlankRight();
        }else
        if (pos/gridSize != gridSize-1 && blankPos == pos+gridSize){
            moveBlankUp();
        }else
        if (pos/gridSize != 0 && blankPos == pos-gridSize){
            moveBlankDown();
        }else{
            toastMessage("Invalid move!");
        }

    }

    private void moveBlankDown() {
        Log.d(TAG, "moveBlankDown: ");

        BitmapDrawable drawable = (BitmapDrawable) pieces.get(blankPos+gridSize).getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        pieces.get(blankPos).setImageBitmap(bitmap);

        pieces.get(blankPos+gridSize).setImageDrawable(null);
        blankPos+=gridSize;
        Log.d(TAG, "moveBlankUp: blank is at "+blankPos);
    }

    private void moveBlankUp() {
        Log.d(TAG, "moveBlankUp: ");

        BitmapDrawable drawable = (BitmapDrawable) pieces.get(blankPos-gridSize).getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        pieces.get(blankPos).setImageBitmap(bitmap);

        pieces.get(blankPos-gridSize).setImageDrawable(null);
        blankPos-=gridSize;
        Log.d(TAG, "moveBlankUp: blank is at "+blankPos);

    }

    private void moveBlankRight() {
        Log.d(TAG, "moveBlankRight: ");

        BitmapDrawable drawable = (BitmapDrawable) pieces.get(blankPos+1).getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        pieces.get(blankPos).setImageBitmap(bitmap);

        pieces.get(blankPos+1).setImageDrawable(null);
        blankPos+=1;
        Log.d(TAG, "moveBlankUp: blank is at "+blankPos);

    }

    private void moveBlankLeft() {
        Log.d(TAG, "moveBlankLeft: ");

        BitmapDrawable drawable = (BitmapDrawable) pieces.get(blankPos-1).getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        pieces.get(blankPos).setImageBitmap(bitmap);

        pieces.get(blankPos-1).setImageDrawable(null);
        blankPos-=1;
        Log.d(TAG, "moveBlankUp: blank is at "+blankPos);
    }

    private void openFileChooser() {
        Log.d(TAG, "openFileChooser: ");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");

        if (resultCode == RESULT_OK && data!=null && data.getData()!=null){
            if (requestCode == REQUEST_IMAGE){
                Uri imageUri = data.getData();
                try {
                    btnSelectImage.setEnabled(false);
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                    cropLoadImage(selectedBitmap);
                    startTimer();
                } catch (IOException e) {
                    toastMessage("Couldnt load image! :(");
                    e.printStackTrace();
                }
            }
        }

    }

    private void startTimer() {
        Log.d(TAG, "startTimer: ");

        new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                updateTimeLeft(l);
            }

            @Override
            public void onFinish() {
                timeUp();
            }

        }.start();
    }

    private void timeUp() {
        Log.d(TAG, "timeUp: ");

        if (attemptsLeft==0){
            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
            startActivity(intent);
            toastMessage("You Loser!!!!");
            finish();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Time Up!!");
        String text = "You failed. You have "+attemptsLeft+" attempts left.";

        builder.setMessage(text);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tryAgain();
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void tryAgain() {
        Log.d(TAG, "tryAgain: ");

        attemptsLeft--;
        blankPos = numPieces -1;
        timeLeftInMillis = oneMinute;
        startTimer();
        cropLoadImage(selectedBitmap);
    }

    private void updateTimeLeft(long l) {
        Log.d(TAG, "updateTimeLeft: ");

        timeLeftInMillis = l;
        int timeSeconds = ((int) timeLeftInMillis)/1000;
        int minutesLeft = timeSeconds/60;
        int secondsLeft = timeSeconds%60;
        txtTimer.setText("0"+minutesLeft+":"+(secondsLeft<10 ? "0" : "")+secondsLeft);
    }

    private void cropLoadImage(Bitmap srcBitmap) {
        Log.d(TAG, "cropLoadImage: ");

        final int totalHeight = srcBitmap.getHeight();
        final int totalWidth  = srcBitmap.getWidth();
        final int colWidth = totalWidth/gridSize;
        final int colHeight = totalHeight/gridSize;

        //srcBitmap = cropToSquare(srcBitmap);
        Bitmap[] bitmaps = new Bitmap[numPieces];

        for (int i = 0; i< numPieces -1; i++) {
            int startX = i%gridSize;
            int startY = i/gridSize;
            startX*=colWidth;
            startY*=colHeight;
            bitmaps[i] = Bitmap.createBitmap(srcBitmap, startX, startY, colWidth, colHeight, null, true);
        }

        for (ImageView img :pieces)
            img.setClickable(true);

        int l = numPieces -3;
        for (int i=0; i<l; i++)
            pieces.get(i).setImageBitmap(bitmaps[i]);

        pieces.get(l).setImageBitmap(bitmaps[l+1]);
        pieces.get(l+1).setImageBitmap(bitmaps[l]);
        pieces.get(l+2).setImageDrawable(null);

        btnViewImage.setVisibility(View.VISIBLE);

    }

    public static Bitmap cropToSquare(Bitmap bitmap){
        Log.d(TAG, "cropToSquare: ");
        
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    private void toastMessage(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

}
