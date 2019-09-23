package com.ritara.svustudent.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.ritara.svustudent.Login;
import com.ritara.svustudent.MainActivity;
import com.ritara.svustudent.R;
import com.ritara.svustudent.Register;
import com.ritara.svustudent.utils.ListManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ListManager.ListManagerInterface {

    private ListManager grid_adapter;
    private TextView title;
    private ImageView image;
    private Context context;
    private GridLayoutManager manager;
    private Button Sign_in, Sign_up;
    private RecyclerView grid_view;
    private LinearLayout about, download;
    public static ArrayList<HomeItemModel> item_list;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        grid_view = (RecyclerView) view.findViewById(R.id.grid);
        Sign_in = (Button) view.findViewById(R.id.signin);
        Sign_up = (Button) view.findViewById(R.id.signup);
        about = (LinearLayout) view.findViewById(R.id.about);
        download = (LinearLayout) view.findViewById(R.id.download);
        title = (TextView) view.findViewById(R.id.title);
        image = (ImageView) view.findViewById(R.id.image);
        manager = new GridLayoutManager(context, 3);
        grid_view.setLayoutManager(manager);
        item_list = new ArrayList<>();
        grid_adapter = new ListManager(HomeFragment.this, getActivity(), item_list, R.layout.item_home, grid_view, "home item");

        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Login.class));
            }
        });

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Register.class));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Login.class));
            }
        });
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });

        onResponse();
        return view;
    }

    public void onResponse(){

        AndroidNetworking.get("https://shuddhairpurifier.com/SVU/svu_api.php?rule=SVU_get_home")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
//                                ((MainActivity)getActivity()).showLoader();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
//                                dismissLoader();
                        Log.e("Resp :", response.toString());

                        try {
                            if (response.getString("status").equals("1")) {
                                item_list.clear();
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HomeItemModel homeViewModel = new HomeItemModel();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    homeViewModel.setId(jsonObject.getString("id"));
                                    homeViewModel.setImage(jsonObject.getString("image"));
                                    homeViewModel.setName(jsonObject.getString("name"));
                                    item_list.add(homeViewModel);
                                }
                                grid_adapter.getBaseAdapterClass().notifyDataSetChanged();

                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
//                                dismissLoader();
                        error.getResponse();
                    }
                });
    }

    @Override
    public void onBindView(ListManager.BaseAdapterViewHolder holder, int position, String for_what) {
        ((TextView)holder.itemView.findViewById(R.id.title)).setText(item_list.get(position).getName());
        try {
            Picasso.get()
                    .load(item_list.get(position).getImage())
                    .into((ImageView)holder.itemView.findViewById(R.id.image));
        }catch (Exception e){

        }

    }

    @Override
    public void holderClass(View v, String for_what) {

    }
}