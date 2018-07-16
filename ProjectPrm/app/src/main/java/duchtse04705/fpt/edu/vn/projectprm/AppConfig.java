package duchtse04705.fpt.edu.vn.projectprm;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

class AppConfig {
        public static final String clientAccessToken = "a1711597bde64149940c27fe9e4dfe02";
        public static final String regex = ";";
        
        public static void checkAudioRecordPermission(Activity activity) {
                if (ActivityCompat.checkSelfPermission( activity,
                        Manifest.permission.RECORD_AUDIO )
                        != PackageManager.PERMISSION_GRANTED) {
                        
                        // Should we show an explanation?
                        if (! ActivityCompat.shouldShowRequestPermissionRationale( activity,
                                Manifest.permission.RECORD_AUDIO )) {
                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions( activity,
                                        new String[]{Manifest.permission.RECORD_AUDIO},
                                        33 );
                        }
                }
        }
}
