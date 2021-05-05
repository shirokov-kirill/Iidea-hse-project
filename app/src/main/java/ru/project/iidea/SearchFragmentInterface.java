package ru.project.iidea;

import android.view.View;
import java.util.List;

public interface SearchFragmentInterface {

    public void onSearchButtonClicked(List<ProjectType> types);
    public void onAddSubscriptionClicked(View view, ProjectType type);
    public void onBackButtonPressed();
}
