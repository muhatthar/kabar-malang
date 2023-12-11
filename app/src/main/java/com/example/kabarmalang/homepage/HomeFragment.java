package com.example.kabarmalang.homepage;

import static com.example.kabarmalang.database.DBHelper.TABLE_NAME;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kabarmalang.R;
import com.example.kabarmalang.adapter.BeritaAdapter;
import com.example.kabarmalang.database.DBHelper;
import com.example.kabarmalang.model.beritaModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SQLiteDatabase sqLiteDatabase;
    DBHelper db;
    RecyclerView rvHome;
    BeritaAdapter beritaAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        rvHome = homeView.findViewById(R.id.rvBerita);
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getContext());
        rvHome.setLayoutManager(mLayout);
        rvHome.setHasFixedSize(true);
        rvHome.setItemAnimator(new DefaultItemAnimator());

        db = new DBHelper(getContext());

        displayData();
        return homeView;
    }

    private void displayData() {
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + "", null);
        ArrayList<beritaModel> beritaModels = new ArrayList<>();
        while (cursor.moveToNext()) {
            int berita_id = cursor.getInt(0);
            String title = cursor.getString(1);
            String desc = cursor.getString(2);
            String date = cursor.getString(3);
            byte[] image = cursor.getBlob(4);
            String location = cursor.getString(5);
            String latitude = cursor.getString(6);
            String longitude = cursor.getString(7);
            beritaModels.add(new beritaModel(berita_id, title, desc, date, image, location, latitude, longitude));
        }
        cursor.close();
        beritaAdapter = new BeritaAdapter(getContext(), R.layout.item_berita, beritaModels, sqLiteDatabase);
        rvHome.setAdapter(beritaAdapter);
    }
}