package fun.ductri.myapplication;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ductr on 07/08/2016.
 */
public class myservice2  extends IntentService{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public myservice2(String name) {
        super(name);
    }

    public myservice2() {
        super("DS");
    }

    //7055
    @Override
    protected void onHandleIntent(Intent intent) {

        String phoneNumber = intent.getExtras().get("phone_number").toString();
        String times = intent.getExtras().get("times").toString();
        int timeNo = Integer.parseInt(times);
        for (int i=0; i<timeNo; i++) {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            //Make a call
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:"+phoneNumber));
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

            //Check whether out-coming call is answered
            String outputFile = Environment.getExternalStorageDirectory().
                    getAbsolutePath() + "/callrecordings/";
            outputFile = "/Removable/MicroSD/callrecordings/";
            File file = new File(outputFile);

            boolean existed = false;
            Calendar lastModified = getLast(file.listFiles());
            Calendar current = Calendar.getInstance();
            current.setTime(new Date());
            current.add(Calendar.SECOND, -30);
            while (!existed && current.before(now)) {
                current = Calendar.getInstance();
                current.setTime(new Date());
                current.add(Calendar.MINUTE, -1);
                lastModified = getLast(file.listFiles());
                existed = lastModified.after(now);
                //Log.i("ductri", "now"+now.getTime().toString());
                //Log.i("ductri", "current"+current.getTime().toString());
                Log.i("ductri", Boolean.toString(current.before(now)));
            }
            Log.i("ductri", lastModified.getTime().toString());

            //Hang out
            if (existed) {
                killCall(getApplicationContext());
                SystemClock.sleep(2000);
                Log.i("ductri", i+"kill");
            }
            now = lastModified;
        }
        killCall(getApplicationContext());
        Log.i("ductri", "kill");
        killCall(getApplicationContext());
        Log.i("ductri", "kill");
        killCall(getApplicationContext());
        Log.i("ductri", "kill");
    }

    private Calendar getLast(File[] files) {
        Date t = new Date(files[0].lastModified());
        for (int i=1;i<files.length; i++) {
            Date temp = new Date(files[i].lastModified());

            if (temp.after(t)) {
                t = temp;
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(t);
        return cal;
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
