package com.example.kabarmalang.adapter;

import static com.example.kabarmalang.database.DBHelper.TABLE_NAME;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kabarmalang.R;
import com.example.kabarmalang.database.DBHelper;
import com.example.kabarmalang.detail.DetailActivity;
import com.example.kabarmalang.edit.EditActivity;
import com.example.kabarmalang.googleMaps.GoogleMapsEditActivity;
import com.example.kabarmalang.model.beritaModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.BeritaViewHolder> {
    private Context context;
    int singleData;
    private ArrayList<beritaModel> beritaModelArrayList;
    SQLiteDatabase sqLiteDatabase;

    public BeritaAdapter(Context context, int singleData, ArrayList<beritaModel> beritaModelArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.singleData = singleData;
        this.beritaModelArrayList = beritaModelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public BeritaAdapter.BeritaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View beritaView =inflater.inflate(R.layout.item_berita, parent, false);
        return new BeritaViewHolder(beritaView);
    }

    @Override
    public void onBindViewHolder(@NonNull BeritaAdapter.BeritaViewHolder holder, int position) {
        beritaModel beritaModel = beritaModelArrayList.get(position);
        byte[] image = beritaModel.getBeritaImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.beritaImage.setImageBitmap(bitmap);
        holder.title.setText(beritaModel.getTitle());
        holder.date.setText(beritaModel.getDate());

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Intent detailBerita = new Intent(context, DetailActivity.class);
            bundle.putInt("row_id", beritaModel.getBerita_id());
            bundle.putString("title", beritaModel.getTitle());
            bundle.putString("desc", beritaModel.getDesc());
            bundle.putString("date", beritaModel.getDate());
            bundle.putByteArray("img", beritaModel.getBeritaImage());
            bundle.putString("loc", beritaModel.getLocation());
            bundle.putString("lat", beritaModel.getLatitude());
            bundle.putString("lng", beritaModel.getLongitude());
            detailBerita.putExtra("beritaDetails", bundle);
            context.startActivity(detailBerita);
        });

        holder.btnEdit.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            Intent editForm = new Intent(context, EditActivity.class);
            bundle.putInt("row_id", beritaModel.getBerita_id());
            bundle.putString("title", beritaModel.getTitle());
            bundle.putString("desc", beritaModel.getDesc());
            bundle.putString("date", beritaModel.getDate());
            bundle.putByteArray("img", beritaModel.getBeritaImage());
            bundle.putString("loc", beritaModel.getLocation());
            bundle.putString("lat", beritaModel.getLatitude());
            bundle.putString("lng", beritaModel.getLongitude());
            editForm.putExtra("beritaDatas", bundle);
            context.startActivity(editForm);

        });

        holder.btnDelete.setOnClickListener(v -> {
            Dialog popUp =new Dialog(context);
            popUp.setContentView(R.layout.dialog_delete);
            Window window =popUp.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

            AppCompatButton confirm =popUp.findViewById(R.id.btnDeleteConfirm);
            AppCompatButton cancel =popUp.findViewById(R.id.btnDeleteCancel);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popUp.dismiss();
                }
            });

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper db = new DBHelper(context);
                    sqLiteDatabase = db.getReadableDatabase();
                    long edit = sqLiteDatabase.delete(TABLE_NAME, "berita_id=" + beritaModel.getBerita_id(), null);
                    if (edit != -1) {
                        Toast.makeText(context, "Berhasil Menghapus", Toast.LENGTH_SHORT).show();

                        beritaModelArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                    popUp.dismiss();
                }
            });
            popUp.show();
        });
    }


    @Override
    public int getItemCount() {
        return beritaModelArrayList.size();
    }

    public class BeritaViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView beritaImage;
        TextView title, date;
        ImageButton btnEdit, btnDelete;

        public BeritaViewHolder(@NonNull View itemView) {
            super(itemView);

            beritaImage = itemView.findViewById(R.id.thread_avatar);
            title = itemView.findViewById(R.id.thread_title);
            date = itemView.findViewById(R.id.thread_time);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public void filterList(ArrayList<beritaModel> filterBerita) {
        beritaModelArrayList = filterBerita;
        notifyDataSetChanged();
    }
}

