package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CopyBookAdapter extends RecyclerView.Adapter<CopyBookAdapter.MyViewHolder> {
    private List<CopyBookOne> docList;
    private OnItemClickListener listener;
    private Context context;

    public CopyBookAdapter(Context context, List<CopyBookOne> data) {
        this.docList = data;
        this.context = context;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CopyBookAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_copy_book, parent, false);
        return new CopyBookAdapter.MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CopyBookAdapter.MyViewHolder holder, int position) {
        CopyBookOne item = docList.get(position);
        holder.txtCode.setText(String.valueOf(item.maBS));
        holder.txtstatus.setText(item.trangThai);
        holder.txtFloor.setText("Tầng " + String.valueOf(item.maTang));
        holder.txtShelves.setText("Kệ " + String.valueOf(item.maKe));
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCode, txtstatus, txtFloor, txtShelves;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            txtCode = itemView.findViewById(R.id.CopyMa);
            txtstatus = itemView.findViewById(R.id.CopyStatus);
            txtFloor = itemView.findViewById(R.id.CopyFloor);
            txtShelves = itemView.findViewById(R.id.CopyShelves);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v, position);
                        }
                    }
                }
            });
        }
    }
}
