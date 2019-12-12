package serviscepde.com.tr.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import serviscepde.com.tr.R;

public class DialogOrder extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public TextView txtIptal;
    public ViewGroup viewGroup;


    public DialogOrder(@NonNull Activity a) {
        super(a);

        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_sirala);
        txtIptal = findViewById(R.id.txtIptal);
        viewGroup = c.findViewById(R.id.content);
        txtIptal.setOnClickListener(this);

        Window window = this.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.txtIptal:
                dismiss();
                break;
            default:
                break;

        }
        dismiss();
    }
}
