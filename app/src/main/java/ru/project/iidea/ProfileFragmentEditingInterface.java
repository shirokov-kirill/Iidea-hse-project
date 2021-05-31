package ru.project.iidea;

import android.widget.LinearLayout;

public interface ProfileFragmentEditingInterface {
    void onAddDescriptionButtonClicked();
    void showToast(String message);
    void unsubscribeOnTag(String tag, LinearLayout linearLayout);
    void onBackPressed();
}
