package ir.sarasaghaei.mapnotif_adv06.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import ir.sarasaghaei.mapnotif_adv06.Const;
import ir.sarasaghaei.mapnotif_adv06.MainActivity;
import ir.sarasaghaei.mapnotif_adv06.R;
import ir.sarasaghaei.mapnotif_adv06.adapter.Recycle_UnReadNotifAdapter;
import ir.sarasaghaei.mapnotif_adv06.databinding.FragmentnotifBinding;
import ir.sarasaghaei.mapnotif_adv06.entity.Notif;
import ir.sarasaghaei.mapnotif_adv06.room.AppDatabase;

public class NotifFragment extends Fragment {

    FragmentnotifBinding binding;
    Recycle_UnReadNotifAdapter adapter;

    public interface CallbackNotifFragment{
        void onclicklocation(String latitude, String longitude);
    }
    private CallbackNotifFragment listenerNotif;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       binding = FragmentnotifBinding.inflate(getLayoutInflater());

        if(getArguments() != null &&
                getArguments().containsKey(Const.TITLE) &&
                getArguments().containsKey(Const.BODY)) {
            binding.includeBody.tvTitlenotif.setText(getArguments().getString(Const.TITLE));
            binding.includeBody.tvBodynotif.setText(getArguments().getString(Const.BODY));
            if (getArguments().getString(Const.LATITUDE) != null &&
                    getArguments().getString(Const.LONGITUDE) != null) {
                int update = AppDatabase.getInstance(getContext()).getNotifDAO()
                        .update_flagNotif(Integer.parseInt(getArguments().getString(Const.MAX_ID)),1);
                binding.includeBody.tvDatanotif.setText(R.string.view_in_map);
                binding.includeBody.tvDatanotif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listenerNotif != null) {
                            listenerNotif.onclicklocation(getArguments().getString(Const.LATITUDE),
                                    getArguments().getString(Const.LONGITUDE));
                        }
                    }
                });

            }
        }

        // check activity is null
        if(getActivity() != null) {

            //set Recycleview data
            binding.recyclUnreadNotif.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclUnreadNotif.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
            List<Notif> list = AppDatabase.getInstance(getContext()).getNotifDAO().getNotif_orderby();
            adapter = new Recycle_UnReadNotifAdapter(getContext(), list);
            adapter.setOnItemClickListner(new Recycle_UnReadNotifAdapter.onItemClickListner() {
                @Override
                public void onClick(int id) {
                    ((MainActivity) getActivity()).addBadgeUnreadNotif();
                    final Notif viewNotif = AppDatabase.getInstance(getContext())
                            .getNotifDAO().getbyid(id);
                    if (viewNotif != null) {
                        binding.includeBody.tvTitlenotif.setText(viewNotif.getTitle_notif());
                        binding.includeBody.tvBodynotif.setText(viewNotif.getDetail_notif());
                        if (viewNotif.getLatitude() != null && viewNotif.getLongitude() != null) {
                            binding.includeBody.tvDatanotif.setText("View in map");
                            binding.includeBody.tvDatanotif.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (listenerNotif != null) {
                                        listenerNotif.onclicklocation(viewNotif.getLatitude(), viewNotif.getLongitude());
                                    }
                                }
                            });
                        }
                    }
                }
            });
            binding.recyclUnreadNotif.setAdapter(adapter);
        }
        return binding.getRoot();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CallbackNotifFragment){
            listenerNotif = (CallbackNotifFragment) context;
        }
    }
}
