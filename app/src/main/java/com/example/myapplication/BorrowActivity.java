package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BorrowActivity extends AppCompatActivity {
    TextView txtNoBook;
    DangMuonAdapter mAdapter;
    ImageView imgNoBook;
    RecyclerView rclView;
    ArrayList<ItemBook> lstDocument;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        addControl();
        callData();
    }
    private void addControl(){
        txtNoBook = findViewById(R.id.BrrNoBook);
        imgNoBook = findViewById(R.id.Brrimg);
        rclView = findViewById(R.id.BrrRecyclerView);
        rclView.setLayoutManager(new LinearLayoutManager(this));
        lstDocument = new ArrayList<>();
        mAdapter = new DangMuonAdapter(this, lstDocument);
        rclView.setAdapter(mAdapter);
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
    private void callData(){
        String url = Constant.serverLink + "/TaiLieu/GetBookBorrowing";
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
                SharedPreferences sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);
                params.put("maDocGia", username);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void  parseJsonData(String response) throws JSONException {
        JSONArray documentArr  = new JSONArray(response);
        if(documentArr.length() < 1)
        {
            imgNoBook.setVisibility(View.VISIBLE);
            txtNoBook.setVisibility(View.VISIBLE);
            rclView.setVisibility(View.GONE);
        }
        else{
            imgNoBook.setVisibility(View.GONE);
            txtNoBook.setVisibility(View.GONE);
            rclView.setVisibility(View.VISIBLE);
            lstDocument.clear();
            for(int i =  0;i < documentArr.length();i++)
            {
                JSONObject docJson = documentArr.getJSONObject(i);
                ItemBook a = new ItemBook();
                a.maDKCB = docJson.getString("maDKCB");
                a.tenTL = docJson.getString("tenTL");
                a.trangThai = docJson.getString("trangThai");
                a.ngayMuon = docJson.getString("ngayMuon");
                a.hanTra = docJson.getString("hanTra");
                lstDocument.add(a);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}