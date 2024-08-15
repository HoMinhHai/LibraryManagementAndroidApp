package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    Button btnInfo;
    Reader DocGia;
    ImageView anhDocGia;
    TextView tenDocGia;
    Button btnLogout;
    LinearLayout dangmuon, quahan, thongtin;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        thongtin = v.findViewById(R.id.infoReader);
        dangmuon = v.findViewById(R.id.BorrowBook);
        quahan = v.findViewById(R.id.QuaHan);
        anhDocGia = v.findViewById(R.id.anhDocGia);
        tenDocGia = v.findViewById(R.id.tenDocGia);
        btnLogout = v.findViewById(R.id.LogoutBtn);
        addEvent();
        callAPI();
        return v;
    }
    private void callAPI(){
        String url = Constant.serverLink + "/ChucNangCon/GetInfoReaderByCode";
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        parseJsonData(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> Toast.makeText(getContext(),"Lỗi truy vấn",Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userLogin", getActivity().MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);
                params.put("code",username);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void loadFragment(androidx.fragment.app.Fragment
                                     fragment)
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.frameFragment,fragment);
        ft.commit();
    }
    private void addEvent(){
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();
                loadFragment(new LogoutFragment());
            }
        });
        thongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
        dangmuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BorrowActivity.class);
                startActivity(intent);
            }
        });
        quahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuaHanActivity.class);
                startActivity(intent);
            }
        });
    }
    public void  parseJsonData(String response) throws JSONException {
        JSONObject objResponse = new JSONObject(response);

        String anh = objResponse.getString("anh");
        String hoTen = objResponse.getString("hoTen");
        DocGia = new Reader();
        DocGia.anh = anh;
        DocGia.hoTen = hoTen;
        DocGia.maDocGia = objResponse.getString("maDocGia");
        DocGia.donVi = objResponse.getString("donVi");
        DocGia.email = objResponse.getString("email");
        DocGia.gioiTinh = objResponse.getString("gioiTinh");
        DocGia.tinhTrangThe = objResponse.getString("tinhTrangThe");
        tenDocGia.setText(hoTen);
        String url = Constant.serverLink + Constant.linkImgRd;
        Picasso.with(getContext()).load(url + anh).
                resize(150,170).into(anhDocGia);
    }
    public void showCustomDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_information_main);


        TextView tvHoVaTen = dialog.findViewById(R.id.txtHoVaTen);
        TextView tvMaDocGia = dialog.findViewById(R.id.txtMaDocGia);
        TextView tvdv = dialog.findViewById(R.id.txtDonVi);
        TextView email = dialog.findViewById(R.id.txtDiaChiEmail);
        TextView gioitinh = dialog.findViewById(R.id.txtGioiTinh);
        TextView tinhtrangthe = dialog.findViewById(R.id.txtTinhTrangThe);

        tvHoVaTen.setText(DocGia.hoTen);
        tvMaDocGia.setText(DocGia.maDocGia);
        tvdv.setText(DocGia.donVi);
        email.setText(DocGia.email);
        gioitinh.setText(DocGia.gioiTinh);
        tinhtrangthe.setText(DocGia.tinhTrangThe);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

}