package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogoutFragment extends Fragment {
    MaterialButton btnLogin;
    String tendn, mk;
    EditText username, psw;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogoutFragment newInstance(String param1, String param2) {
        LogoutFragment fragment = new LogoutFragment();
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
        View v = inflater.inflate(R.layout.fragment_logout, container, false);
        username = v.findViewById(R.id.inputCodeReader);
        psw = v.findViewById(R.id.inputPassword);
        btnLogin = v.findViewById(R.id.buttonSignIn);
        addEvent();
        return v;
    }
    public void  parseJsonData(String response) throws JSONException {
        JSONObject objResponse = new JSONObject(response);
        String message = objResponse.getString("message");
        if(message.equals("Đăng nhập thành công")){
            Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
            saveInfoLogin();
            loadFragment(new AccountFragment());
        }

        else
            Toast.makeText(getContext(), "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_LONG).show();
    }
    public void saveInfoLogin(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", tendn);
        editor.putString("password", mk);
        editor.apply(); // or editor.commit();

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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tendn = username.getText().toString().trim();
                mk = psw.getText().toString().trim();
                if(tendn.equals("") || mk.equals("")){
                    Toast.makeText(getContext(), "Chưa nhập tên đăng nhập hoặc mật khẩu", Toast.LENGTH_LONG).show();
                    return;
                }
                username.setText(tendn);
                String url = Constant.serverLink + "/Home/LoginReader";
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
                        params.put("uname",tendn );
                        params.put("psw", mk);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }
}