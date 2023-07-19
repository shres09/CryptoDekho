package com.cryptodekho.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cryptodekho.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private View fragmentView;
    private TextView labelname, labelname2;
    private FirebaseFirestore store;
    private FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        store = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        labelname = (TextView) fragmentView.findViewById(R.id.labelName);
        labelname2 = (TextView) fragmentView.findViewById(R.id.labelName1);

        store.collection("Users").document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> list = (ArrayList<String>) task.getResult().get("Coins");
                labelname.setText(list.get(0));
                labelname2.setText(list.get(1));
            }
        });
        return fragmentView;
    }

}
