package serviscepde.com.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.facebook.login.LoginManager;

public class FacebookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        LoginManager.getInstance().getDefaultAudience();
    }
}
