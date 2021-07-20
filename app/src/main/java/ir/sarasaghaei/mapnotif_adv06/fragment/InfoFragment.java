package ir.sarasaghaei.mapnotif_adv06.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ir.sarasaghaei.mapnotif_adv06.databinding.FragmentinfoBinding;
import ir.sarasaghaei.mapnotif_adv06.databinding.FragmentmapsBinding;
import ir.sarasaghaei.mapnotif_adv06.databinding.FragmentnotifBinding;

public class InfoFragment extends Fragment {

    FragmentinfoBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       binding =FragmentinfoBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}
