package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationActivity extends AppCompatActivity {
    TextView txtTen, txtTacGia, txtChuDe, txtLoai, txtNXB, txtNamXB;
    ImageView img;
    int id_TaiLieu;
    private CopyBookAdapter adapter;
    private ArrayList<CopyBookOne> itemList;
    RecyclerView rcView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        addControl();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Xử lý khi nhấn nút "Up"
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void addControl(){
        txtTen = findViewById(R.id.CopyTenTaiLieu);
        txtTacGia = findViewById(R.id.CopyTacGia);
        txtChuDe = findViewById(R.id.CopyChuDe);
        txtLoai = findViewById(R.id.CopyLoaiTaiLieu);
        img = findViewById(R.id.CopyImage);
        txtNamXB = findViewById(R.id.CopyNamXB);
        txtNXB = findViewById(R.id.CopyNXB);
        rcView = findViewById(R.id.CopyRecyclerView);


        rcView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        adapter = new CopyBookAdapter(this, itemList);
        rcView.setAdapter(adapter);

        Intent intent = getIntent();
        id_TaiLieu = intent.getIntExtra("ID_TL", -1);
        callData();
        loadListCopyBook();
    }
    public void loadListCopyBook(){
        String url = Constant.serverLink + "/TaiLieu/GetListCopyBookByAndroid";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        handleJson(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id_TaiLieu));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void handleJson(String response) throws JSONException{
        JSONArray documentArr  = new JSONArray(response);
        for(int i =  0;i < documentArr.length();i++)
        {
            JSONObject docJson = documentArr.getJSONObject(i);
            CopyBookOne a  = new CopyBookOne();
            a.maBS = docJson.getInt("maBS");
            a.maKe = docJson.getInt("maKe");
            a.maTang = docJson.getInt("maTang");
            a.trangThai = docJson.getString("trangThai");

            itemList.add(a);
        }
        adapter.notifyDataSetChanged();
    }
    private void callData(){
        String url = Constant.serverLink + "/TaiLieu/GetDocumentInfoByAndroid";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        parseJsonData(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id_TaiLieu));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void  parseJsonData(String response) throws JSONException {
        JSONObject documentArr  = new JSONObject(response);
        txtTen.setText(documentArr.getString("tenTL"));
        txtTacGia.setText(documentArr.getString("TacGia"));
        txtChuDe.setText(documentArr.getString("chuDe"));
        txtLoai.setText(documentArr.getString("loaiTL"));
        txtNamXB.setText(String.valueOf(documentArr.getString("namXuatBan")));
        txtNXB.setText(documentArr.getString("tenNXB"));
        String url = Constant.serverLink + Constant.linkImg;
        Picasso.with(this).load(url + documentArr.getString("anhBia")).
                resize(150,170).into(img);
    }
}