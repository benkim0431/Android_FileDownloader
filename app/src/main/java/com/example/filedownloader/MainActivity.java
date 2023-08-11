package com.example.filedownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    LinearLayout lnFileDownload;
    SeekBar skFileSize;
    TextView txtFileSize, txtRatingValue;
    RatingBar rtApplication;
    Button btnDownload;
    ProgressBar prgFileDownload;
    int progressVal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For use snackbar, we need layout.
        lnFileDownload = (LinearLayout)findViewById(R.id.content);
        skFileSize = (SeekBar) findViewById(R.id.skbarFileSize);
        txtFileSize = (TextView) findViewById(R.id.txtFilesize);
        txtRatingValue = (TextView) findViewById(R.id.txtRating);
        rtApplication = (RatingBar)findViewById(R.id.rtApp);
        btnDownload = (Button)findViewById(R.id.btnDownload);
        prgFileDownload = (ProgressBar) findViewById(R.id.prgDownload);

        skFileSize.setMax(99);
//        skFileSize.setMin(1);
        skFileSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int currentFileSize = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentFileSize = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                txtFileSize.setText(String.valueOf((currentFileSize)));
                txtFileSize.setText(String.valueOf((skFileSize.getProgress())));
            }
        });

        rtApplication.setRating(0);
        rtApplication.setNumStars(5);

        rtApplication.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtRatingValue.setText(Float.toString(rating));
            }
        });

        prgFileDownload.setProgress(0);

        btnDownload.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int fileSize = getFileSize();
                prgFileDownload.setMax(fileSize);
                initDownload();
            }
        });
    }
    private int getFileSize(){
        int fileBytes = skFileSize.getProgress();
        return (fileBytes);
    }

    private void initDownload(){
        String uri = "https://drive.google.com/uc?export=download&id=1F-1614HBqXaHFSTGX5aWtp9YGkeZMK1R";
        downloadFile(getApplicationContext(), "Welcome", ".pdf", uri.trim());
    }

    // Context class
    private void downloadFile(Context context, String filename, String fileExtension, String url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS.toString(), filename + fileExtension);
        //
        assert downloadManager != null;
        downloadManager.enqueue(request);
        loadProgressBar(progressVal);
//        Snackbar.make(lnFileDownload, "Downloading...", Snackbar.LENGTH_LONG).show();

    }

    private void loadProgressBar(int progressValue){
        prgFileDownload.setProgress(progressValue);
        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }
                catch (InterruptedException e){

                }
                loadProgressBar(progressValue + 10);
                Log.d("hbkim",""+ progressValue);
            }
        });
        thread1.start();
    }
}