package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    String data, category;
    ArrayList<String> dataSpin = new ArrayList<>();
    FloatingSearchView searchView;
    ImageView imgNoBook;
    TextView txtNoBook;
    Spinner spFilter;
    ArrayList<Document> lstDocument;
    DetailAdapter mAdapter;
    RecyclerView recyclerViewBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        addControl();
        addEvent();
        Intent intent = getIntent();
        data = intent.getStringExtra("inputString");
        category = intent.getStringExtra("Category");
        callData();
    }
    private void addEvent(){
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                data = currentQuery;
                category = spFilter.getSelectedItem().toString();
                callData();
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = lstDocument.get(position).id;
                Intent intent = new Intent(SearchActivity.this, LocationActivity.class);
                intent.putExtra("ID_TL", id);
                startActivity(intent);
            }
        });
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
        imgNoBook = findViewById(R.id.imgNoBook);
        txtNoBook = findViewById(R.id.txtNoBook);
        recyclerViewBook = findViewById(R.id.recyclerManyBooks);
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(this));
        lstDocument = new ArrayList<>();
        mAdapter = new DetailAdapter(this, lstDocument);
        recyclerViewBook.setAdapter(mAdapter);
        searchView = findViewById(R.id.searchingBookBar);
        spFilter = findViewById(R.id.sp_filter_Book);
        this.initData();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,dataSpin);
        spFilter.setAdapter(adapter);


    }
    private void initData()
    {
        dataSpin.add("Tìm theo tiêu đề");
        dataSpin.add("Chủ đề");
        dataSpin.add("Tác giả");
        dataSpin.add("Năm xuất bản");
        dataSpin.add("Nhà xuất bản");
    }
    private void callData(){
            String url = Constant.serverLink + "/TaiLieu/GetInfoDocumentSearching";
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
                    params.put("SearchString", data);
                    params.put("category", category);
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
                recyclerViewBook.setVisibility(View.GONE);
            }
            else{
                imgNoBook.setVisibility(View.GONE);
                txtNoBook.setVisibility(View.GONE);
                recyclerViewBook.setVisibility(View.VISIBLE);
                lstDocument.clear();
                for(int i =  0;i < documentArr.length();i++)
                {
                    JSONObject docJson = documentArr.getJSONObject(i);
                    Document a  = new Document();
                    a.id = docJson.getInt("id");
                    a.tenTaiLieu = docJson.getString("tenTL");
                    a.tacGia = docJson.getString("tacGia");
                    a.namXuatBan = docJson.getInt("namXuatBan");
                    a.anhBia = Constant.serverLink + Constant.linkImg + docJson.getString("anhBia");
                    lstDocument.add(a);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
}