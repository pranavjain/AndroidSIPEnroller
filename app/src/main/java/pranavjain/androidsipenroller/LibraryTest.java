package pranavjain.androidsipenroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;

import java.io.IOException;

import pranavjain.androidsipenrollerlib.SIPEnroller;

public class LibraryTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SIPEnroller user1 = new SIPEnroller();
        user1.provider = "SIP2SIP";
        user1.email = "myemail@domain.com";
        user1.pass = "K33pC@lm";
        user1.display = "SampleDisplay_1";
        user1.username = "UserName.09.sample";
        String output;
        try {
            output =user1.EnrolUser(getApplicationContext());
            System.out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
