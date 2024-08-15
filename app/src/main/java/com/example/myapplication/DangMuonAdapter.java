package com.example.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DangMuonAdapter extends RecyclerView.Adapter<DangMuonAdapter.MyViewHolder> {
    private List<ItemBook> docList;
    private Context context;

    public DangMuonAdapter(Context context, List<ItemBook> data) {
        this.docList = data;
        this.context = context;
    }

    @NonNull
    @Override
    public DangMuonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dangmuon, parent, false);
        return new DangMuonAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DangMuonAdapter.MyViewHolder holder, int position) {
        ItemBook item = docList.get(position);
        holder.txtName.setText(item.tenTL);
        holder.txtma.setText(item.maDKCB);
        holder.txtngaymuon.setText(item.ngayMuon);
        holder.txthantra.setText(item.hanTra);
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtma, txtngaymuon, txthantra;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.item_Ten);
            txtma = itemView.findViewById(R.id.item_Masach);
            txtngaymuon = itemView.findViewById(R.id.item_ngaymuon);
            txthantra = itemView.findViewById(R.id.item_hantra);
        }
    }
}
