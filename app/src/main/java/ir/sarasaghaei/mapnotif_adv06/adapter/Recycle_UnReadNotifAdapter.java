package ir.sarasaghaei.mapnotif_adv06.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.sarasaghaei.mapnotif_adv06.R;
import ir.sarasaghaei.mapnotif_adv06.databinding.BodyelayoutBinding;
import ir.sarasaghaei.mapnotif_adv06.databinding.FragmentinfoBinding;
import ir.sarasaghaei.mapnotif_adv06.databinding.FragmentnotifBinding;
import ir.sarasaghaei.mapnotif_adv06.databinding.RecyclelayoutBinding;
import ir.sarasaghaei.mapnotif_adv06.entity.Notif;
import ir.sarasaghaei.mapnotif_adv06.room.AppDatabase;

public class Recycle_UnReadNotifAdapter extends RecyclerView.Adapter<Recycle_UnReadNotifAdapter.UnreadNotifViewHolder> {


    Context context;
    onItemClickListner onItemClickListner;
    List<Notif> list;

    public Recycle_UnReadNotifAdapter(Context context, List<Notif> list) {
        this.context = context;
        this.list = list;
    }

    public class UnreadNotifViewHolder extends RecyclerView.ViewHolder{
        RecyclelayoutBinding binding;

        public UnreadNotifViewHolder(@NonNull RecyclelayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
    public void setOnItemClickListner(Recycle_UnReadNotifAdapter.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }
    public interface onItemClickListner {
        void onClick(int id);
    }
    @NonNull
    @Override
    public UnreadNotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclelayoutBinding binding = RecyclelayoutBinding
                .inflate(LayoutInflater.from(parent.getContext()));


        return new UnreadNotifViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final UnreadNotifViewHolder holder, int position) {
        final Notif item_notif = list.get(position);
        if (item_notif.getFlag_notif() == 0){
            holder.binding.cardlayout.setCardBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorUnreaditem,null));
            holder.binding.imgNotif.setImageResource(R.drawable.ic_baseline_notifications_24);
        }
        holder.binding.tvIdnotif.setText(String.valueOf(item_notif.getId_notif()));
        holder.binding.tvTitlenotif.setText(item_notif.getTitle_notif());
        holder.binding.cardlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int update = AppDatabase.getInstance(context).getNotifDAO()
                        .update_flagNotif(item_notif.getId_notif(),1);
                onItemClickListner.onClick(item_notif.getId_notif());
                holder.binding.cardlayout.setCardBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorreaditem, null));
                holder.binding.imgNotif.setImageResource(R.drawable.read_notifications_24);

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
