package ru.project.iidea;

import android.view.View;
import java.util.List;

public interface SearchFragmentInterface {

    void onSearchButtonClicked(List<ProjectType> types);
    void onAddSubscriptionClicked(View view, ProjectType type);
    void onBackButtonPressed();
}
