package com.example.myapplication;

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

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {
    private List<Document> docList;
    private Context context;
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public DetailAdapter(Context context, List<Document> data) {
        this.docList = data;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_book, parent, false);
        return new DetailAdapter.MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAdapter.MyViewHolder holder, int position) {
        Document item = docList.get(position);
        holder.txtName.setText(item.tenTaiLieu);
        holder.txtAuthor.setText(item.tacGia);
        holder.txtYear.setText(String.valueOf(item.namXuatBan));
        Picasso.with(context).load(item.anhBia).
                resize(150,170).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtAuthor, txtYear;
        ImageView img;

        public MyViewHolder(View itemView,  final OnItemClickListener listener) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtNameBook);
            txtAuthor = itemView.findViewById(R.id.mainAuthor);
            img = itemView.findViewById(R.id.imgDetail);
            txtYear = itemView.findViewById(R.id.txtYear);
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
