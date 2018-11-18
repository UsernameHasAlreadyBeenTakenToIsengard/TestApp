package davos.VoiceRecorderApp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private Button buttonPlay, buttonRecord, buttonStop;
    private ImageView imageMic;
    private static MediaRecorder mediaRecorder = null;
    private static MediaPlayer mediaPlayer = null;
    private String mFileName;


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int PERMISSION_ALL = 1;
        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL);
        }
        setContentView(R.layout.activity_main);
        buttonPlay = (Button) findViewById(R.id.btnPlay);
        buttonRecord = (Button) findViewById(R.id.btnRecord);
        buttonStop = (Button) findViewById(R.id.btnStop);
        imageMic = (ImageView) findViewById(R.id.micImage);


        buttonStop.setEnabled(false);
        buttonPlay.setEnabled(false);


        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(mFileName);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                    buttonStop.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFileName = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/MyRecordings/recording" + System.currentTimeMillis() + ".3gp";
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile(mFileName);
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException ise) {

                } catch (IOException ioe) {

                }

                buttonRecord.setEnabled(false);
                buttonStop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                imageMic.setColorFilter(0xffff0000);


            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer = null;
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    Toast.makeText(getApplicationContext(), "Playback Stopped", Toast.LENGTH_LONG).show();
                    imageMic.setColorFilter(0x000000);
                }
                if (mediaRecorder != null) {
                    mediaRecorder.reset();
                //    mediaRecorder.stop();
                  //  mediaRecorder.release();
                    mediaRecorder = null;
                    Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
                    imageMic.setColorFilter(0x000000);
                }
                buttonRecord.setEnabled(true);
                buttonStop.setEnabled(false);
                buttonPlay.setEnabled(true);
            }
        });


    }
}




