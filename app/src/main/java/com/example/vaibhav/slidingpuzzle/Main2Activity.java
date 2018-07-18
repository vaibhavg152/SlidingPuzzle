package com.example.vaibhav.slidingpuzzle;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";
    private Button btnTryAgain,btnSeeSolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d(TAG, "onCreate: ");

        btnTryAgain = (Button) findViewById(R.id.btnTryWithNewImage);
        btnSeeSolution = (Button) findViewById(R.id.btnSeeSolution);

        btnSeeSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seeSolution();
            }
        });

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void seeSolution() {
        Log.d(TAG, "seeSolution: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setMessage("Are you sure you want to quit and see the solution? :( ;(");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loadSolution();
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadSolution() {
        Log.d(TAG, "loadSolution: ");

        Uri uri = Uri.parse("https://math.stackexchange.com/questions/2714774/rigorous-proof-to-show-that-the-15-puzzle-problem-is-unsolvable");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);

    }

}
