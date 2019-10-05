package com.ritara.svustudent.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.Login;
import com.ritara.svustudent.MainActivity;
import com.ritara.svustudent.MyMarksAdapter;
import com.ritara.svustudent.Payments;
import com.ritara.svustudent.R;
import com.ritara.svustudent.Register;
import com.ritara.svustudent.fragments.AdmissionFragment;
import com.ritara.svustudent.fragments.BooksFragment;
import com.ritara.svustudent.fragments.BroadcastFragment;
import com.ritara.svustudent.fragments.CalendarFragment;
import com.ritara.svustudent.fragments.ClassFragment;
import com.ritara.svustudent.fragments.Faculties;
import com.ritara.svustudent.fragments.MarksFragment;
import com.ritara.svustudent.fragments.NewsFragment;
import com.ritara.svustudent.fragments.PaidFeeFragment;
import com.ritara.svustudent.fragments.ScholarshipFragment;
import com.ritara.svustudent.fragments.ViewAttendance;
import com.ritara.svustudent.ui.vid.VideosFragment;
import com.ritara.svustudent.utils.FeeModel;
import com.ritara.svustudent.utils.ListManager;
import com.ritara.svustudent.utils.SharedPreferences_SVU;
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
    private SharedPreferences_SVU sharedPreferences_svu;
    private Dialog uplaod_complaint_dialog, emergency_dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());

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

        if(!sharedPreferences_svu.getUserId().isEmpty()){
            LinearLayout llBanner = (LinearLayout) view.findViewById(R.id.llBanner);
            llBanner.setVisibility(View.GONE);
        }

        GetStuLoginAccess();

//        if(sharedPreferences_svu.getFrom().equalsIgnoreCase("admission")){
//            ((Dashboard)getActivity()).changeFragment(new AdmissionFragment(), "Admission");
//        }

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
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.getResponse();
                    }
                });
    }

    @Override
    public void onBindView(final ListManager.BaseAdapterViewHolder holder, final int position, String for_what) {
        ((TextView)holder.itemView.findViewById(R.id.title)).setText(item_list.get(position).getName());
        holder.itemView.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String test = "";
                switch (item_list.get(position).getName()){
                    case "Gallery" :
                        test = "Gallery";
                        ((Dashboard)getActivity()).changeFragment(new ClassFragment(), "Gallery");
                        break;
                    case "Scholarship" :
                        test = "Scholarship";
                        ((Dashboard)getActivity()).changeFragment(new ScholarshipFragment(), "Scholarship");
                        break;
                    case "Pay Fee" :
                        test = "Pay Fee";
                        startActivity(new Intent(getActivity(), Payments.class));
                        break;
                    case "My Faculties" :
                        test = "Faculties";
                        ((Dashboard)getActivity()).changeFragment(new Faculties(), "My Faculties");
                        break;
                    case "My Attendance" :
                        test = "Attendance";
                        ((Dashboard)getActivity()).changeFragment(new ViewAttendance(), "My Attendance");
                        break;
                    case "My Fees" :
                        test = "My Fees";
                        ((Dashboard)getActivity()).changeFragment(new PaidFeeFragment(), "My Fees");
                        break;
                    case "Calendar" :
                        test = "Calendar";
                        ((Dashboard)getActivity()).changeFragment(new CalendarFragment(), "My Calendar");
                        break;
                    case "Message" :
                        test = "Message";
                        ((Dashboard)getActivity()).changeFragment(new BroadcastFragment(), "My Messages");
                        break;
                    case "Notice Board" :
                        test = "Notice";
                        ((Dashboard)getActivity()).changeFragment(new NewsFragment(), "Notice Board");
                        break;
                    case "Admission" :
                        test = "Admission";
                        ((Dashboard)getActivity()).changeFragment(new AdmissionFragment(), "Admission");
                        break;
                    case "Emergency" :
                        test = "Emergency";
                        openEmergencyDialog();
                        break;
                    case "Results" :
                        test = "Results";
                        ((Dashboard)getActivity()).changeFragment(new MarksFragment(), "Marks");
                        break;

                    default:
                        break;
                }
//                Toast.makeText(getActivity(), test, Toast.LENGTH_SHORT).show();
            }
        });

        try {
            Picasso.get()
                    .load(item_list.get(position).getImage())
                    .into((ImageView)holder.itemView.findViewById(R.id.image));
        }catch (Exception e){

        }

    }

    private void openEmergencyDialog() {

        emergency_dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        emergency_dialog.setContentView(R.layout.emergency_dialog);
        emergency_dialog.setCancelable(true);

        emergency_dialog.findViewById(R.id.police).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emergency_dialog.dismiss();
                try {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:100")));
                } catch (Exception e) {
                    e.getMessage();
                    e.printStackTrace();
                }
            }
        });

        emergency_dialog.findViewById(R.id.fire).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emergency_dialog.dismiss();
                try {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:101")));
                } catch (Exception e) {
                    e.getMessage();
                    e.printStackTrace();
                }
            }
        });

        emergency_dialog.findViewById(R.id.ambulance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emergency_dialog.dismiss();
                try {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:102")));
                } catch (Exception e) {
                    e.getMessage();
                    e.printStackTrace();
                }
            }
        });

        emergency_dialog.findViewById(R.id.call_president).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emergency_dialog.dismiss();
                try {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:8766318889")));
                } catch (Exception e) {
                    e.getMessage();
                    e.printStackTrace();
                }
            }
        });

        emergency_dialog.findViewById(R.id.call_general_secretary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emergency_dialog.dismiss();
                try {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:8766318889")));
                } catch (Exception e) {
                    e.getMessage();
                    e.printStackTrace();
                }
            }
        });

        emergency_dialog.findViewById(R.id.call_treasure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emergency_dialog.dismiss();
                try {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:8766318889")));
                } catch (Exception e) {
                    e.getMessage();
                    e.printStackTrace();
                }
            }
        });

        emergency_dialog.show();
        Window window = emergency_dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void holderClass(View v, String for_what) {

    }

    private void GetStuLoginAccess() {
        if (!((Dashboard)getActivity()).isloadershowing())
            ((Dashboard)getActivity()).showLoader();
        AndroidNetworking.post("http://svu.svu.edu.in/svustuservice.asmx/GetStuLoginAccess?EnrollNo=SET14A00030058&key=rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
                .addBodyParameter("EnrollNo", "SET14A00030058")
                .addBodyParameter("key", "rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
                .setTag("login")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                object.getString("AdmDate");
                                object.getString("AdmMode");
                                object.getString("BrLoction");
                                object.getString("Course");
                            }
                        }
                        catch (Exception e) {
                            e.getMessage();
                        } finally {
                            ((Dashboard)getActivity()).dismissLoader();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        anError.getResponse();
                        ((Dashboard)getActivity()).dismissLoader();
                    }
                });
    }



}