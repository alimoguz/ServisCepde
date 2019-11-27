package serviscepde.com.tr.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import serviscepde.com.tr.R;

public class DialogChooseCategory extends Dialog implements View.OnClickListener {


    public Activity c;
    public Dialog d;

    public static ViewGroup viewGroup;



    public DialogChooseCategory(Activity a) {
        super(a);

        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose_category);

        viewGroup = c.findViewById(R.id.content);

        Window window = this.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


    }

    @Override
    public void onClick(View v) {

    }
}
