package cn.edu.ustc.igank.ui.about;


import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.ustc.igank.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutDialogFragment extends DialogFragment {


    public AboutDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_dialog, container, false);
        getDialog().setTitle(R.string.about_dialog_title);
        return rootView;
    }

}
