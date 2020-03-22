package serviscepde.com.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NotificationDetailActivity extends AppCompatActivity {

    String title;
    String content;

    TextView title_tv;
    TextView content_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        title_tv = findViewById(R.id.title);
        content_tv = findViewById(R.id.content);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            title = bundle.getString("title");
            content = bundle.getString("message");
        }

        title_tv.setText(title);
        content_tv.setText(content);
    }
}
