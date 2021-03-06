package ru.project.iidea;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.project.iidea.network.IideaBackend;
import ru.project.iidea.network.IideaBackendService;
import ru.project.iidea.network.NetworkConnectionChecker;

public class ProfileFragmentEditing extends Fragment {

    ProfileFragmentEditingInterface activity;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_profile_editing, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof  ProfileFragmentEditingInterface){
            activity = (ProfileFragmentEditingInterface) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        long myUserID = 0;
        if (this.getArguments() != null) {
            myUserID = this.getArguments().getLong("userId");
        }
        IideaBackendService server = IideaBackend.getInstance().getService();
        TextView headLineName = view.findViewById(R.id.profileHeadLineName);
        if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
            server.user(myUserID).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if(response.isSuccessful() && response.body() != null){
                        User myUser = response.body();
                        String fullName = myUser.getSurname() + ' ' + myUser.getName();
                        headLineName.setText(fullName);//????????????
                        final TextView state = view.findViewById(R.id.profileUserStatus);
                        state.setText(getString(R.string.Status, myUser.getState().toString()));
                        final Button statusButton = view.findViewById(R.id.profileUserStatusChangeButton);
                        statusButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopupMenu popupMenu = new PopupMenu(getActivity(), statusButton);
                                popupMenu.getMenuInflater().inflate(R.menu.menu_user_status, popupMenu.getMenu());
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        String newState = "";
                                        switch (item.getItemId()) {
                                            case R.id.userStatus_seeking:
                                                newState = "SEEKING";
                                                state.setText(getString(R.string.Status, getString(R.string.userStatus_seeking)));
                                                break;
                                            case R.id.userStatus_notSeeking:
                                                newState = "NOT_SEEKING";
                                                state.setText(getString(R.string.Status, getString(R.string.userStatus_notSeeking)));
                                                break;
                                            case R.id.userStatus_working:
                                                newState = "WORKING";
                                                state.setText(getString(R.string.Status, getString(R.string.userStatus_working)));
                                                break;
                                            default:
                                                return false;
                                        }
                                        final String s = newState;
                                        if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
                                            server.updateUser(null, newState, null, null, null).enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    if(response.isSuccessful()){
                                                        myUser.setState(UserState.fromString(s));
                                                        activity.showToast("?????????????? ????????????????.");
                                                    } else {
                                                        onFailure(call, new IOException());
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {
                                                    state.setText(getString(R.string.Status, myUser.getState().toString()));
                                                    activity.showToast("?????????????????? ????????????. ???????????????? ???????????? ????????????.");
                                                }
                                            });
                                        } else {
                                            activity.showToast("No Internet connection.");
                                        }
                                        return true;
                                    }
                                });
                                popupMenu.show();
                            }
                        });
                        TextView email = view.findViewById(R.id.profileEmail);
                        email.setText(myUser.getEmail());
                        email.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        TextView phoneNumber = view.findViewById(R.id.profilePhoneNumber);
                        phoneNumber.setText(myUser.getPhoneNumber());
                        phoneNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        Button button = view.findViewById(R.id.savePhoneNumberButton);
                        EditText editText = view.findViewById(R.id.profilePhoneNumber);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
                                    server.updateUser(null, null, editText.getText().toString(), null, null).enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if(response.isSuccessful()){
                                                myUser.setPhoneNumber(editText.getText().toString());
                                                activity.showToast("?????????????? ????????????????.");
                                            } else {
                                                onFailure(call, new IOException());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            activity.showToast("?????????????????? ????????????. ???????????????????? ?????????????????? ?????????? ????????????.");
                                        }
                                    });
                                } else {
                                    activity.showToast("No Internet connection.");
                                }
                            }
                        });
                        TextView vk = view.findViewById(R.id.profileVk);
                        vk.setText(myUser.getVkName());
                        vk.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        EditText editText1 = view.findViewById(R.id.profileVk);
                        TextView inst = view.findViewById(R.id.profileInst);
                        inst.setText(myUser.getInstName());
                        inst.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        EditText editText2 = view.findViewById(R.id.profileInst);
                        Button downloadCharactButton = view.findViewById(R.id.profileUploadResumeButton);
                        downloadCharactButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.onAddDescriptionButtonClicked();
                            }
                        });
                        Button saveVk = view.findViewById(R.id.saveProfileVk);
                        saveVk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
                                    server.updateUser(null, null, null, editText1.getText().toString(), null).enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if(response.isSuccessful()){
                                                myUser.setVkName(editText1.getText().toString());
                                                activity.showToast("?????????????? ????????????????.");
                                            } else {
                                                onFailure(call, new IOException());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            activity.showToast("?????????????????? ????????????. ???????????????????? ?????????????????? ?????????????? Vk ????????????.");
                                        }
                                    });
                                } else {
                                    activity.showToast("No Internet connection.");
                                }
                            }
                        });
                        Button saveInst = view.findViewById(R.id.saveProfileInst);
                        saveInst.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
                                    server.updateUser(null, null, null, null, editText2.getText().toString()).enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if(response.isSuccessful()){
                                                myUser.setInstName(editText2.getText().toString());
                                                activity.showToast("?????????????? ????????????????.");
                                            } else {
                                                onFailure(call, new IOException());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            activity.showToast("?????????????????? ????????????. ???????????????????? ?????????????????? ?????????????? Instagram ????????????.");
                                        }
                                    });
                                } else {
                                    activity.showToast("No Internet connection.");
                                }
                            }
                        });
                        LinearLayout profileListOfSubs = view.findViewById(R.id.profileListOfSubs);
                        List<ProjectType> subscribes = myUser.getSubscriptions();
                        if(!subscribes.isEmpty()){
                            for (ProjectType projectType : subscribes) {
                                LinearLayout linearLayout = new LinearLayout(getContext());
                                TextView textView = new TextView(getContext());
                                textView.setText(projectType.toString());
                                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                textView.setTextSize(21);
                                linearLayout.addView(textView);
                                Button button1 = new Button(getContext());
                                button1.setText(R.string.Unsubscribe);
                                button1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        activity.unsubscribeOnTag(textView.getText().toString(), linearLayout);
                                    }
                                });
                                linearLayout.addView(button1);
                                profileListOfSubs.addView(linearLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            }
                        }
                        LinearLayout profileProjects = view.findViewById(R.id.profileMyProjects);
                        List<Long> projectIDs = myUser.getProjects();
                        for (long projectID : projectIDs){
                            if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
                                server.project(projectID).enqueue(new Callback<Project>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Project> call, @NonNull Response<Project> response) {
                                        if(response.isSuccessful() && response.body() != null){
                                            TextView textView = new TextView(getContext());
                                            textView.setText(response.body().getName());
                                            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                            textView.setTextSize(21);
                                            profileProjects.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Project> call, @NonNull Throwable t) {

                                    }
                                });
                            } else {
                                activity.showToast("No Internet Connection.");
                            }
                        }
                    } else {
                        onFailure(call, new IOException("Error reading from server."));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    activity.showToast("Something went wrong.");
                    activity.onBackPressed();
                }
            });
            super.onViewCreated(view, savedInstanceState);
        } else {
            activity.onBackPressed();
        }
    }
}
