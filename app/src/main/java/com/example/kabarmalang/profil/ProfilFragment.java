package com.example.kabarmalang.profil;

import static com.example.kabarmalang.database.DBHelper.TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.kabarmalang.R;
import com.example.kabarmalang.database.DBHelper;
import com.example.kabarmalang.login.LoginActivity;
import com.example.kabarmalang.model.userModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {

    View view;
    TextView tv_nama, tv_jml_berita, tv_email;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    LinearLayout btnLogout;
    FirebaseUser user;
    SQLiteDatabase sqLiteDatabase;
    DBHelper db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profil, container, false);

        tv_nama = view.findViewById(R.id.profil_name);
        tv_jml_berita = view.findViewById(R.id.profil_jml_berita);
        tv_email = view.findViewById(R.id.profil_email);
        btnLogout = view.findViewById(R.id.btn_logout);
        user = mAuth.getCurrentUser();

        db = new DBHelper(getContext());

        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null );
        if (cursor != null) {
            cursor.moveToFirst();
            int beritaCount = cursor.getInt(0);
            tv_jml_berita.setText(String.valueOf(beritaCount));
            cursor.close();
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logout = new Intent(getContext(), LoginActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
            }
        });

        if (user != null) {
            String userId = user.getUid();
            String email = user.getEmail().toString();

            database.child("Users").child(userId).child("UserData").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userModel user = snapshot.getValue(userModel.class);
                        user.setKey(snapshot.getKey());
                        tv_nama.setText("Halo, " + user.getNama() + "!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            tv_email.setText(email);
        }

        return view;
    }
}