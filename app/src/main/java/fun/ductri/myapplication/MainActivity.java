package fun.ductri.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.EditText;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.util.Calendar;

public class MainActivity extends Activity {

    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    private String outputFile = null;
    private Button startBtn;
    private EditText phoneNumber;
    private EditText times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // store it to sd card
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/javacodegeeksRecording.3gpp";

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(outputFile);


        phoneNumber = (EditText) findViewById(R.id.phone_number);
        times = (EditText) findViewById(R.id.times);
        startBtn = (Button) findViewById(R.id.start);
        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //start(v);
                start();
                //killCall(getApplicationContext());
            }
        });


        //add PhoneStateListener
//        PhoneCallListener phoneListener = new PhoneCallListener();
//        TelephonyManager telephonyManager = (TelephonyManager) this
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
    }



    public void start() {
//        Intent myIntent = new Intent(getApplicationContext(), myservice.class);
//        getApplication().startService(myIntent);
        String phoneNo = phoneNumber.getText().toString();
        String timeNo = times.getText().toString();
        if (!phoneNo.equals("") && !timeNo.equals("")) {
            Intent myIntent = new Intent(getApplicationContext(), myservice2.class);
            myIntent.putExtra("phone_number", phoneNumber.getText().toString());
            myIntent.putExtra("times", timeNo);
            getApplication().startService(myIntent);
            startBtn.setEnabled(false);
        }
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "ductri";

        private int count = 0;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
//                    Intent i = getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);

                    if (count < 4) {
                        Intent myIntent = new Intent(getApplicationContext(), myservice2.class);
                        Calendar now = Calendar.getInstance();
                        myIntent.putExtra("time", now);
                        getApplication().startService(myIntent);
                    }
                    count++;
                    isPhoneCalling = false;
                }

            }
        }
    }

}

