package com.ritara.svustudent.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ritara.svustudent.Adapters.AttendanceAdapter;
import com.ritara.svustudent.Adapters.GalleryAdapter;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.Model;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.ritara.svustudent.utils.Constants.BASE_URL;
import static com.ritara.svustudent.utils.Constants.GET_GALLERY;
import static com.ritara.svustudent.utils.Constants.IMAGE;
import static com.ritara.svustudent.utils.Constants.RULE;


public class ClassFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class,container,false);

        getGal(view);

        return view;
    }

    private void getGal(final View view) {
        if (!((Dashboard)getActivity()).isloadershowing())
            ((Dashboard)getActivity()).showLoader();
        AndroidNetworking.post(BASE_URL)
                .addBodyParameter(RULE, GET_GALLERY)
                .setTag(GET_GALLERY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<Model> models = new ArrayList<>();
                            ((Dashboard)getActivity()).dismissLoader();
                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                Model model = new Model();
                                JSONObject object = response.getJSONArray("data").getJSONObject(i);
                                String[] ttl = object.getString(IMAGE).split("upload/");
                                model.setImage(object.getString(IMAGE));
                                model.setName(ttl[1]);
                                models.add(model);
                            }
                            RecyclerView rcGal = (RecyclerView) view.findViewById(R.id.rcGal);
                            rcGal.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                            rcGal.setAdapter(new GalleryAdapter(models, getActivity()));
                        }
                        catch (Exception e) {
                            e.getMessage();
                            e.printStackTrace();
                            ((Dashboard)getActivity()).dismissLoader();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        ((Dashboard)getActivity()).dismissLoader();
                    }
                });
    }


}
