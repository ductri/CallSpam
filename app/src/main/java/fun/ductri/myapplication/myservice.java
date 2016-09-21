package fun.ductri.myapplication;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by ductr on 06/08/2016.
 */
//19907
public class myservice extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public myservice(String name) {
        super(name);
    }

    public myservice() {
        super("RSSPullService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:900"));
        phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(phoneIntent);
//
//        for (int i=0;i<10;i++) {
//            //Make call
//            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
//            phoneIntent.setData(Uri.parse("tel:0982246560"));
//            phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            startActivity(phoneIntent);
//
//
//            boolean stop = false;
//
//            String outputFile = Environment.getExternalStorageDirectory().
//                    getAbsolutePath() + "/javacodegeeksRecording.3gpp";
//            MediaRecorder myRecorder = new MediaRecorder();
//
//            SystemClock.sleep(1000);
//            while (!stop) {
//                SystemClock.sleep(500);
//                myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//                myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//                myRecorder.setOutputFile(outputFile);
//                try {
//                    myRecorder.prepare();
//                    myRecorder.start();
//
//                } catch (Exception e) {
//                    Log.i("ductri", "start recording fail");
//                    stop = true;
//                    killCall(getApplicationContext());
//                }
//                if (!stop) {
//                    Log.i("ductri", "start recording successfully");
//                    myRecorder.reset();
//                }
//            }
//            boolean killed = killCall(getApplicationContext());
//            while (!killed) {
//                killed = killCall(getApplicationContext());
//            }
        //}
    }

    private boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.d("ductri","PhoneStateReceiver **" + ex.toString());
            return false;
        }
        return true;
    }

}
