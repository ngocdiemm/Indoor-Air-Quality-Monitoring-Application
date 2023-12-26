package com.uit.myairquality;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView txtContact;
    private ImageView btnContact;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_settings, container, false);
       txtContact = view.findViewById(R.id.txtContact);
       btnContact = view.findViewById(R.id.btnContact);

        Switch darkModeSwitch = view.findViewById(R.id.darkMode);

        // Set the initial state of the switch based on stored preferences
        darkModeSwitch.setChecked(SharedPreferenceManager.getInstance(requireContext()).getStateMode());

        // Set listener for switch change
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the state of dark mode in SharedPreferences
                SharedPreferenceManager.getInstance(requireContext()).saveStateMode(isChecked);

                // Apply dark mode immediately (you may need to recreate the activity for changes to take effect)
                // You can implement a more dynamic way to apply dark mode across the app
                applyDarkMode(isChecked);
            }
        });
       txtContact.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               loadFragment(false);
           }
       });
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(false);
            }
        });
       return view;
    }
    // Method to apply dark mode (you can customize this based on your app's theme handling)
    private void applyDarkMode(boolean isDarkMode) {
        // Example: You can set the theme programmatically or recreate the activity
        if (isDarkMode) {
            // Apply dark mode theme
            requireActivity().setTheme(R.style.DarkTheme);
        } else {
            // Apply light mode theme
            requireActivity().setTheme(R.style.LightTheme);
        }

        // Recreate the activity to apply the theme changes
        requireActivity().recreate();
    }

    private void loadFragment( boolean isAppInitialized){
        ContactUsFragment contactUsFragment = new ContactUsFragment();
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        if (isAppInitialized){
            fragmentTransaction.add(R.id.frame,contactUsFragment);
        } else {
            fragmentTransaction.replace(R.id.frame,contactUsFragment    );
        }

        fragmentTransaction.commit();
    }
}