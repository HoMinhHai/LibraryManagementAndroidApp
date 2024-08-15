package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThesisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThesisFragment extends Fragment {
    ArrayList<Document> lstDocument = new ArrayList<>();
    RecyclerView recyclerView;
    private DocumentAdapter mAdapter;
    public String linkImg = "/Content/images/books/";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ThesisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThesisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThesisFragment newInstance(String param1, String param2) {
        ThesisFragment fragment = new ThesisFragment();
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
        View view = inflater.inflate(R.layout.fragment_thesis, container, false);
        recyclerView = view.findViewById(R.id.recyclerThesis);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        lstDocument.clear();
        mAdapter = new DocumentAdapter(getContext(), lstDocument);
        recyclerView.setAdapter(mAdapter);
        GetNewThesis();
        addControl();
        return view;
    }
    private void addControl(){
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = lstDocument.get(position).id;
                Intent intent = new Intent(getContext(), LocationActivity.class);
                intent.putExtra("ID_TL", id);
                startActivity(intent);
            }
        });
    }
    public void GetNewThesis(){
        String url = Constant.serverLink + "/TaiLieu/GetDocumentByCategory?type=thesis";
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                response -> {
                    try {
                        parseJsonData(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show());
        requestQueue.add(stringRequest);
    }
    public void  parseJsonData(String response) throws JSONException {
        JSONArray documentArr  = new JSONArray(response);
        for(int i =  0;i < documentArr.length();i++)
        {
            JSONObject docJson = documentArr.getJSONObject(i);
            Document a  = new Document();
            a.id = docJson.getInt("ID_TL");
            a.tenTaiLieu = docJson.getString("TenTaiLieu");
            a.tacGia = docJson.getString("tg");
            a.anhBia = Constant.serverLink + linkImg + docJson.getString("AnhBia");
            lstDocument.add(a);
        }
        mAdapter.notifyDataSetChanged();

    }
}