package edu.selu.android.classygames;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class MainActivity extends Activity {

	public static final String LOG_TAG = "ClassyGames";
    Facebook facebook = new Facebook("324400870964487");
    AlertDialog.Builder alertDialogBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        alertDialogBuilder = new AlertDialog.Builder(this);
        facebook.authorize(this, new DialogListener() {
            @Override
            public void onComplete(Bundle values) {}

            @Override
            public void onFacebookError(FacebookError error) {
            	Log.d(LOG_TAG, "CHECK YOUR LOGCAT TO COPY AND PASTE THIS KEY INTO OUR FACEBOOK APP'S SETTINGS!!" + error.toString());
				alertDialogBuilder.setMessage(error.toString());
				AlertDialog alert = alertDialogBuilder.create();
				alert.show();
            }

            @Override
            public void onError(DialogError e) {}

            @Override
            public void onCancel() {}
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}