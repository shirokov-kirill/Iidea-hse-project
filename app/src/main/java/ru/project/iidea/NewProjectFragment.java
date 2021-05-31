package ru.project.iidea;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

public class NewProjectFragment extends Fragment {

    NewProjectFragmentInterface activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_project, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof NewProjectFragmentInterface){
            activity = (NewProjectFragmentInterface) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button categoryButton = view.findViewById(R.id.category_button);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), categoryButton);
                popupMenu.getMenuInflater().inflate(R.menu.menu_category, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.category_it:
                                categoryButton.setText(R.string.category_it);
                                return true;
                            case R.id.category_culture:
                                categoryButton.setText(R.string.category_culture);
                                return true;
                            case R.id.category_business:
                                categoryButton.setText(R.string.category_business);
                                return true;
                            case R.id.category_history:
                                categoryButton.setText(R.string.category_history);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        final EditText editText = view.findViewById(R.id.edit_new_project_description);
        final EditText editText1 = view.findViewById(R.id.edit_new_project_name);
        Button button = view.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onCreateNewProjectClicked(categoryButton.getText().toString(), editText1.getText().toString(), editText.getText().toString(),ProjectState.New.toString());
            }
        });
    }

    private int generateRandomID(){
        return 0;//TODO
    }
}
