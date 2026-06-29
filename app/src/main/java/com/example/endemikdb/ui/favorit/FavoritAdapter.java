package com.example.endemikdb.ui.favorit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.endemikdb.R;
import com.example.endemikdb.database.entity.Favorit;

import java.util.ArrayList;
import java.util.List;

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.ViewHolder> {

    private Context context;
    private List<Favorit> list = new ArrayList<>();
    private OnItemClickListener listener;
    private OnHeartClickListener heartListener;

    public interface OnItemClickListener {
        void onItemClick(Favorit favorit);
    }

    public interface OnHeartClickListener {
        void onHeartClick(Favorit favorit);
    }

    public FavoritAdapter(Context context, OnItemClickListener listener, OnHeartClickListener heartListener) {
        this.context = context;
        this.listener = listener;
        this.heartListener = heartListener;
    }

    public void setData(List<Favorit> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_endemik, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorit item = list.get(position);

        holder.tvName.setText(item.getNama());
        holder.tvLocation.setText(item.getAsal());
        holder.btnHeart.setImageResource(R.drawable.ic_heart_filled);

        Glide.with(context)
                .load(item.getFoto())
                .centerCrop()
                .placeholder(R.drawable.bg_chip_unselected)
                .into(holder.imgEndemik);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.btnHeart.setOnClickListener(v -> heartListener.onHeartClick(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgEndemik;
        TextView tvName, tvLocation;
        ImageButton btnHeart;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEndemik = itemView.findViewById(R.id.imgEndemik);
            tvName = itemView.findViewById(R.id.tvName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            btnHeart = itemView.findViewById(R.id.btnHeart);
        }
    }
}