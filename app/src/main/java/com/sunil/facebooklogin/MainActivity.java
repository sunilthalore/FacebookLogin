package com.sunil.facebooklogin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private TextView info;
    private CallbackManager callbackManager;

    public boolean isFacebookAvailable()
    {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Test; please ignore");
        intent.setType("text/plain");

        final PackageManager pm = this.getApplicationContext().getPackageManager();
        for(ResolveInfo resolveInfo: pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)){
            ActivityInfo activity = resolveInfo.activityInfo;
            Log.i("actividad ->", activity.name);
            if (activity.name.contains("com.facebook.katana")) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        isFacebookAvailable();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.info);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID:"
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token:"
                                + loginResult.getAccessToken().getToken()
                );


            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled");

            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed");

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
