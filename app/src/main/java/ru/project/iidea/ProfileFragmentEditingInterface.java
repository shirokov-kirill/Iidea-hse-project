package ru.project.iidea;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public interface ProfileFragmentEditingInterface {
    public void onAddDescriptionButtonClicked();
    public void showToast(String message);
    public void unsubscribeOnTag(String tag, LinearLayout linearLayout);
    public void onBackPressed();
}
