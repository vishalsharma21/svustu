package com.ritara.svustudent.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ritara.svustudent.BaseActivity;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.DropDownAnim;
import com.ritara.svustudent.Login;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.Db_Chat;
import com.ritara.svustudent.utils.Model;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

import static android.app.Activity.RESULT_OK;
import static com.ritara.svustudent.utils.Constants.BASE_URL;

public class BroadcastFragment extends Fragment implements View.OnClickListener {

    private ImageView imgBack, imgSend, imgProImg, attach, imgComment;
    private TextView textName, txtComment;
    private ListView lstChat;
    private RelativeLayout open_attachment;
    private EditText etChat;
    private Bundle bundle;
    private SharedPreferences_SVU sharedPreference_main;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ChatAdapter chatAdapter;
    private ArrayList<Model> chat_list;
    private float mDensity;
    private Uri uriFilePath;
    private String image_path = "";
    private File file;
    private String name = "", reciepient_id = "", imgUrl = "", from_where = "", jumpTo = "", delId = "";
    private String[] permissions = new String[]{android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_broadcast, container, false);
        checkPermissions();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if(SharedPreferences_SVU.getInstance(getActivity()).get_Logged()) {
            initView(root);

            sharedPreference_main.setOnChat(true);

            getChat("get_chat", getParams("get_chat"));

            try {
                chat_list = new ArrayList<>();
                chat_list.addAll(Db_Chat.getInstance(getActivity()).get_DB_Chat_Details());
                setAdapter();
            } catch (Exception e) {
                e.printStackTrace();
            }

            IntentFilter filter = new IntentFilter("com.yourcompany.testIntent");
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    getChat("get_chat_from_br", getParams("get_chat"));
                }
            };
            getActivity().registerReceiver(receiver, filter);
        }else{
            startActivity(new Intent(getActivity(), Login.class));
        }

        return root;
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    private void initView(View root) {
        sharedPreference_main = SharedPreferences_SVU.getInstance(getActivity());

        imgBack = root.findViewById(R.id.imgBack);
        imgSend = root.findViewById(R.id.imgSend);
        textName = root.findViewById(R.id.textName);
        etChat = root.findViewById(R.id.etChat);
        lstChat = root.findViewById(R.id.lstChat);
        attach = root.findViewById(R.id.attach);
        open_attachment = root.findViewById(R.id.open_attachment);
        mDensity = getResources().getDisplayMetrics().density;
        txtComment = root.findViewById(R.id.txtComment);
        imgComment = root.findViewById(R.id.imgComment);

        chat_list = new ArrayList<>();

        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            try {
                if (bundle.getString("senders_id") == null) {
                    name = bundle.getString("KEY_FULLNAME");
                    reciepient_id = bundle.getString("KEY_USER_ID");
                    from_where = "";
                } else {
                    name = bundle.getString("senders_name");
                    reciepient_id = bundle.getString("senders_id");
                    from_where = "firebase";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        textName.setText("ML Academians");
        imgComment.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgSend.setOnClickListener(this);
        attach.setOnClickListener(this);
        root.findViewById(R.id.image_layout).setOnClickListener(this);
        root.findViewById(R.id.file_layout).setOnClickListener(this);
        root.findViewById(R.id.video_layout).setOnClickListener(this);
        root.findViewById(R.id.simple_linear).setOnClickListener(this);

        etChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (open_attachment.getVisibility() == View.VISIBLE) {
                    closeDialog("1");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void uploadImage(File photo, final String from) {
        AndroidNetworking.upload(BASE_URL)
                .addMultipartFile("image",photo)
                .addMultipartParameter("rule", "upload_image")
                .setTag("upload_image")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equals("1")) {
                                SendChat(response.getString("url"), from);
//                                showToast("Uploaded_Successfully");
                            } else {
//                                showToast(response.getString("message"));
                            }
                        }catch (Exception e){
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgComment:
                txtComment.setVisibility(View.GONE);
                imgComment.setVisibility(View.GONE);
                jumpTo = "";
                break;
            case R.id.imgBack:
                getActivity().finish();
                break;
            case R.id.imgSend:
                if (!etChat.getText().toString().equals("")) {
//                    showLoader();
                    SendChat("", "");
                } else {
//                    showToast("No network found...");
                }
                break;
            case R.id.attach:
                ((Dashboard)getActivity()).hideSoftKeyBoard();
                int cx = getResources().getDisplayMetrics().widthPixels - (int) (60 * mDensity);
                int cy = (int) (10 * mDensity);

                if (open_attachment.getVisibility() == View.INVISIBLE) {

                    int finalRadius = Math.max(open_attachment.getWidth(), open_attachment.getHeight());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Animator anim = ViewAnimationUtils.createCircularReveal(open_attachment, cx, cy, 0, finalRadius);
                        anim.start();
                    } else {
                        new DropDownAnim(open_attachment, open_attachment.getHeight(), true);
                    }

                    open_attachment.setVisibility(View.VISIBLE);
                    lstChat.setEnabled(false);

                } else {
                    closeDialog("1");
                }
                break;
            case R.id.etChat:
                if (open_attachment.getVisibility() == View.VISIBLE) {
                    closeDialog("1");
                }
                break;
            case R.id.simple_linear:
                if (open_attachment.getVisibility() == View.VISIBLE) {
                    closeDialog("1");
                }
                break;
            case R.id.image_layout:
                closeDialog("2");
                break;
            case R.id.file_layout:
                closeDialog("3");
                break;
            case R.id.video_layout:
                closeDialog("4");
                break;
            default:
                break;
        }
    }

    private void closeDialog(String from) {
        int cx = getResources().getDisplayMetrics().widthPixels - (int) (60 * mDensity);
        int cy = (int) (10 * mDensity);
        int initialRadius = open_attachment.getWidth();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = ViewAnimationUtils.createCircularReveal(open_attachment, cx, cy, initialRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    open_attachment.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();

        } else {
            new DropDownAnim(open_attachment, open_attachment.getHeight(), true);
            open_attachment.setVisibility(View.INVISIBLE);
        }
        lstChat.setEnabled(true);

        if (from.equalsIgnoreCase("2")) {
            openDialog("image");
        } else if (from.equalsIgnoreCase("4")) {
            openDialog("video");
        } else if (from.equalsIgnoreCase("3")) {
            openFile();
        }
    }

    private void openDialog(final String from) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_gallery);
        dialog.setCancelable(false);
        dialog.show();

        if (from.equalsIgnoreCase("video")) {
            ((TextView) dialog.findViewById(R.id.take_photo)).setText("Take Video");
        }
        dialog.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (from.equalsIgnoreCase("video")) {
                    openCamera(4);
                } else {
                    openCamera(5);
                }
            }
        });

        dialog.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (from.equalsIgnoreCase("video")) {
                    openGallery(2);
                } else {
                    openGallery(3);
                }
            }
        });
    }

    private void openCamera(int value) {
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            File mainDirectory = new File(Environment.getExternalStorageDirectory(), "MyFolder/tmp");
            if (!mainDirectory.exists())
                mainDirectory.mkdirs();

            Calendar calendar = Calendar.getInstance();

            uriFilePath = Uri.fromFile(new File(mainDirectory, "IMG_" + calendar.getTimeInMillis() + ".png"));
        }

        if (value == 4) {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, value);
            }
        } else {
            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, uriFilePath);
            startActivityForResult(intent1, value);
        }
    }

    private void openFile() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        try {
            startActivityForResult(Intent.createChooser(i, "Choose One : "), 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openGallery(int value) {
        String fileType;
        if (value == 3) {
            fileType = "image/*";
        } else {
            fileType = "video/*";
        }
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType(fileType);
        startActivityForResult(photoPickerIntent, value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                try {
                    ExifInterface exif = new ExifInterface(uriFilePath.getPath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    Matrix matrix = new Matrix();

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }
                    image_path = uriFilePath.getPath();
                    file = new File(new URI("file://" + image_path.replace(" ", "%20")));
                    uploadImage(file, "image");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                try {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    image_path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                    file = new File(new URI("file://" + image_path.replace(" ", "%20")));
                    uploadImage(file, "image");
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                assert uri != null;
                Cursor cursor1 = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    cursor1 = getActivity().getContentResolver().query(uri, null, null, null, null, null);
                }

                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);

                    if (cursor1 != null && cursor1.moveToFirst()) {
                        String displayName = cursor1.getString(cursor1.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        File mainDir = new File(Environment.getExternalStorageDirectory(), "Ml_Academy/docs/");
                        if (!mainDir.exists())
                            mainDir.mkdirs();
                        File targetFile = new File(mainDir + displayName);
                        if (!targetFile.exists())
                            targetFile.createNewFile();

                        byte[] buffer = new byte[inputStream.available()];
                        inputStream.read(buffer);
                        OutputStream outputStream = new FileOutputStream(targetFile);
                        outputStream.write(buffer);
                        String image_path = targetFile.getPath();
                        file = new File(new URI("file://" + image_path.replace(" ", "%20")));
                        uploadImage(file, "file");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        file = new File(new URI("file://" + data.getData().getPath().replace(" ", "%20")));
                        uploadImage(file, "file");
                    } catch (URISyntaxException e1) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        cursor1.close();
                    } catch (Exception e) {
                        try {
                            file = new File(new URI("file://" + data.getData().getPath().replace(" ", "%20")));
                            uploadImage(file, "file");
                        } catch (URISyntaxException e2) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (requestCode == 2 || requestCode == 4) {
            try {
                Uri selectedVideo = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                file = new File(new URI("file://" + filePath.replace(" ", "%20")));
                uploadImage(file, "video");
                cursor.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private HashMap<String, String> getParams(String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rule", "get_chat");
        params.put("batch", sharedPreference_main.getBatch());
        return params;
    }

    /*@Override
    public void onResponse(String response, String type) {
        try {
//            if (type.equalsIgnoreCase("get_chat")) {
            dismissLoader();
//            }
            JSONObject object = new JSONObject(response);
//            if (type.equalsIgnoreCase("get_chat")) {
            if (object.getString("status").equals("1")) {
                sharedPreference_main.setChatNotiCount(0);
                if (type.equalsIgnoreCase("get_chat")) {
                    etChat.setText("");
                }
                chat_list = new ArrayList<>();
                JSONArray array = object.getJSONArray("data");
                Db_Chat db_chat = Db_Chat.getInstance(ChatActivity.this);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    Model model = new Model();
                    model.setId(jsonObject.getString("id"));
                    model.setUser_id(jsonObject.getString("user_id"));
                    model.setTimestamp(jsonObject.getString("timestamp").substring(0, jsonObject.getString("timestamp").length() - 1));
                    model.setText(Base_activity.getenDecoodedString(jsonObject.getString("text")));
                    model.setImage(jsonObject.getString("image"));
                    model.setVideo(jsonObject.getString("video"));
                    model.setFile(jsonObject.getString("file"));
                    model.setName(jsonObject.getString("name"));
                    model.setMobile_no(jsonObject.getString("mobile_number"));
                    model.setGroup_chat_id(jsonObject.getString("receiver_id"));
                    if (!db_chat.IsItemExist(jsonObject.getString("id"))) {
                        db_chat.insertDB_ChatDetails(model);
                    }
                }
                chat_list = new ArrayList<>();
                chat_list.addAll(db_chat.get_DB_Chat_Details());
                setAdapter();
            }
//            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            dismissLoader();
        }
    }*/

    private void SendChat(final String imgUrl, final String from) {

        HashMap<String, String> mBodyParameterMap = new HashMap<>();
        if (imgUrl.length() > 0) {
            if (from.equalsIgnoreCase("video")) {
                mBodyParameterMap.put("video", imgUrl);
            } else if (from.equalsIgnoreCase("file")) {
                mBodyParameterMap.put("file", imgUrl);
            } else {
                mBodyParameterMap.put("image", imgUrl);
            }
        } else {
            mBodyParameterMap.put("message", BaseActivity.getEncodedString(etChat.getText().toString()).length()>0 ? etChat.getText().toString() : "Deleted#" + delId);
        }

        AndroidNetworking.post(BASE_URL)
                .addBodyParameter("rule", "gp_chat")
                .addBodyParameter("sender_id", sharedPreference_main.getUserId())
                .addBodyParameter("batch", "2019")
                .addBodyParameter("profile_img", sharedPreference_main.getProfilepic())
                .addBodyParameter("mobile_number", sharedPreference_main.getUserId())
                .addBodyParameter("name", sharedPreference_main.get_Username())
                .addBodyParameter("receiver_id", ""+ jumpTo)
                .addBodyParameter("society_id", "SVU")
                .addBodyParameter("house_number", "C150")
                .addBodyParameter(mBodyParameterMap)
                .setTag("set_contribution")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equals("1")) {
                                lstChat.setSelection(lstChat.getAdapter().getCount() - 1);
                                etChat.setText("");
                                getChat("get_chat", getParams("get_chat"));
//                                hit_api.hitVolleyApi(ChatActivity.this, "get_chat", ChatActivity.this, getParams("get_chat"));
                                txtComment.setVisibility(View.GONE);
                                imgComment.setVisibility(View.GONE);
                                jumpTo = "";
                            } else {
                                ((Dashboard)getActivity()).showToast(response.getString("message"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.getResponse();
                    }
                });

    }

    private void setAdapter() {
        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(getActivity());
        }
        chatAdapter.setListData(chat_list);
        lstChat.setAdapter(chatAdapter);
        lstChat.setSelection(chat_list.size() - 1);
    }

    private void getChat(final String get_chat_from_br, HashMap<String, String> get_chat){
        AndroidNetworking.post(BASE_URL)
                .addBodyParameter("rule", "get_chat")
                .setTag("Get_Chat")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject object) {

                        try{
                            if (object.getString("status").equals("1")) {
                                sharedPreference_main.setChatNotiCount(0);
                                if (get_chat_from_br.equalsIgnoreCase("get_chat")) {
                                    etChat.setText("");
                                }
                                chat_list = new ArrayList<>();
                                JSONArray array = object.getJSONArray("data");
                                Db_Chat db_chat = Db_Chat.getInstance(getActivity());

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);

                                    if(jsonObject.getString("text").contains("Deleted#")){
                                        String[] txt = jsonObject.getString("text").split("#");
                                        db_chat.delete(txt[1]);

                                    }else {
                                        Model model = new Model();
                                        model.setId(jsonObject.getString("id"));
                                        model.setUser_id(jsonObject.getString("user_id"));
                                        model.setTimestamp(jsonObject.getString("timestamp").substring(0, jsonObject.getString("timestamp").length() - 1));
                                        model.setText(BaseActivity.getenDecoodedString(jsonObject.getString("text")));
                                        model.setImage(jsonObject.getString("image"));
                                        model.setVideo(jsonObject.getString("video"));
                                        model.setFile(jsonObject.getString("file"));
                                        model.setName(jsonObject.getString("name"));
                                        model.setMobile_no(jsonObject.getString("mobile_number"));
                                        model.setGroup_chat_id(jsonObject.getString("receiver_id"));

                                        if (!db_chat.IsItemExist(jsonObject.getString("id"))) {
                                            db_chat.insertDB_ChatDetails(model);
                                        }
                                    }
                                }
                                chat_list = new ArrayList<>();
                                chat_list.addAll(db_chat.get_DB_Chat_Details());
                                setAdapter();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.getResponse();
                    }
                });
    }

    private class ChatAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Model> chatData;

        public ChatAdapter(Context context) {
            this.context = context;
        }

        public void setListData(ArrayList<Model> chatData) {
            this.chatData = chatData;
        }

        @Override
        public int getCount() {
            return chatData.size();
        }

        @Override
        public Object getItem(int i) {
            return chatData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View viewForOtherMsg = LayoutInflater.from(context).inflate(R.layout.message, viewGroup, false);
            View viewForMyMsg = LayoutInflater.from(context).inflate(R.layout.message2, viewGroup, false);

            String textChat = "";

            if (chatData.get(i).getUser_id().equalsIgnoreCase(sharedPreference_main.getUserId())) {

                if(chatData.get(i).getGroup_chat_id().length()>0) {
                    String[] spText = chatData.get(i).getGroup_chat_id().split("#");
                    textChat = Html.fromHtml("<b> COMMENT@ " + spText[1] + " : <br>" + "" + chatData.get(i).getText() + "</i>").toString();
                }else{
                    textChat = chatData.get(i).getText();
                }
                ((TextView) viewForMyMsg.findViewById(R.id.message2)).setText(textChat);
                ((TextView) viewForMyMsg.findViewById(R.id.timestamp1)).setText(chatData.get(i).getTimestamp());

                viewForMyMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(chatData.get(i).getGroup_chat_id().length()>0) {
                            String[] pos = chatData.get(i).getGroup_chat_id().split("#");
                            lstChat.smoothScrollToPosition(Integer.parseInt(pos[2]));
                        }
                    }
                });

                if (!chatData.get(i).getImage().equalsIgnoreCase("")) {
                    viewForMyMsg.findViewById(R.id.image).setVisibility(View.VISIBLE);
                    showByGlide(chatData.get(i).getImage(), (ImageView) viewForMyMsg.findViewById(R.id.image));
                } else if (!chatData.get(i).getFile().equalsIgnoreCase("")) {
                    viewForMyMsg.findViewById(R.id.file_layout).setVisibility(View.VISIBLE);
                    ((TextView) viewForMyMsg.findViewById(R.id.file_text)).setText(chatData.get(i).getFile().split("/")[chatData.get(i).getFile().split("/").length - 1]);
                } else if (!chatData.get(i).getVideo().equalsIgnoreCase("")) {
                    viewForMyMsg.findViewById(R.id.video_layout).setVisibility(View.VISIBLE);
                    showByGlide(chatData.get(i).getVideo(), (ImageView) viewForMyMsg.findViewById(R.id.video));
                }

                viewForMyMsg.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showOpener(chatData.get(i).getImage());
                    }
                });

                viewForMyMsg.findViewById(R.id.video_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        ((Base_activity) context).showLoader();
                        new DownloadFile().execute("" + chatData.get(i).getVideo());
                    }
                });

                viewForMyMsg.findViewById(R.id.file_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        ((Base_activity) context).showLoader();
                        new DownloadFile().execute("" + chatData.get(i).getFile());
                    }
                });

                viewForMyMsg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("Delete message?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        delId = chatData.get(i).getId();

                                        AndroidNetworking.post(BASE_URL)
                                                .addBodyParameter("rule", "delete_chat")
                                                .addBodyParameter("id", "" + chatData.get(i).getId())
                                                .setTag("")
                                                .setPriority(Priority.MEDIUM)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject object) {
                                                        try{
                                                            Db_Chat db_chat = Db_Chat.getInstance(getActivity());
                                                            db_chat.delete(chatData.get(i).getId());
                                                            Log.e("Chat Delete", object.getString("status"));

                                                            chat_list = new ArrayList<>();
                                                            chat_list.addAll(Db_Chat.getInstance(getActivity()).get_DB_Chat_Details());
                                                            setAdapter();
                                                            etChat.setText("");
                                                            SendChat("", "");
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    @Override
                                                    public void onError(ANError error) {
                                                        // handle error
                                                    }
                                                });
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                        return true;
                    }
                });

                return viewForMyMsg;
            } else {
                try{
                    String[] spText = chatData.get(i).getGroup_chat_id().split("#");
                    textChat = Html.fromHtml("<b> COMMENT@ " + spText[1] + " : <br>" + "" + chatData.get(i).getText() + "</i>").toString();
                }catch (Exception e){
                    textChat = chatData.get(i).getText();
                }

                ((TextView) viewForOtherMsg.findViewById(R.id.message1)).setText(textChat);
                ((TextView) viewForOtherMsg.findViewById(R.id.name)).setText(chatData.get(i).getName());
                ((TextView) viewForOtherMsg.findViewById(R.id.house_no)).setText(chatData.get(i).getHouse_no());
                ((TextView) viewForOtherMsg.findViewById(R.id.timestamp)).setText(chatData.get(i).getTimestamp());

                if (!chatData.get(i).getImage().equalsIgnoreCase("")) {
                    viewForOtherMsg.findViewById(R.id.image).setVisibility(View.VISIBLE);
                    showByGlide(chatData.get(i).getImage(), (ImageView) viewForOtherMsg.findViewById(R.id.image));
                } else if (!chatData.get(i).getFile().equalsIgnoreCase("")) {
                    viewForOtherMsg.findViewById(R.id.file_layout).setVisibility(View.VISIBLE);
                    ((TextView) viewForOtherMsg.findViewById(R.id.file_text)).setText(chatData.get(i).getFile().split("/")[chatData.get(i).getFile().split("/").length - 1]);

                } else if (!chatData.get(i).getVideo().equalsIgnoreCase("")) {
                    viewForOtherMsg.findViewById(R.id.video_layout).setVisibility(View.VISIBLE);
                    showByGlide(chatData.get(i).getVideo(), (ImageView) viewForOtherMsg.findViewById(R.id.video));
                }

                viewForOtherMsg.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showOpener(chatData.get(i).getImage());
                    }
                });

                viewForOtherMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(chatData.get(i).getGroup_chat_id().length()>0) {
                            String[] pos = chatData.get(i).getGroup_chat_id().split("#");
                            lstChat.smoothScrollToPosition(Integer.parseInt(pos[2]));
                        }
                    }
                });

                viewForOtherMsg.findViewById(R.id.video_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        ((Base_activity) context).showLoader();
                        new DownloadFile().execute("" + chatData.get(i).getVideo());
                    }
                });

                viewForOtherMsg.findViewById(R.id.file_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        ((Base_activity) context).showLoader();
                        new DownloadFile().execute("" + chatData.get(i).getFile());
                    }
                });

                viewForOtherMsg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(chatData.get(i).getText().length()>0) {
                            txtComment.setText(chatData.get(i).getText());
                            txtComment.setVisibility(View.VISIBLE);
                            jumpTo = "#"+ chatData.get(i).getName()+"#"+i;
                        }else{

                            txtComment.setText(chatData.get(i).getName()+", Media File");
                            jumpTo = "#"+ chatData.get(i).getName()+", Media File"+"#"+i;
                        }

                        return false;
                    }
                });

                return viewForOtherMsg;
            }
        }

        private void showByGlide(String file, ImageView image) {
            try {
                Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.app_icon).error(R.drawable.app_icon))
                        .load(file).into((image));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void showOpener(String img) {

            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_zoommable_view);

            PhotoView image = dialog.findViewById(R.id.img_user_img);
            try {
                Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.app_icon).error(R.drawable.app_icon))
                        .load(img).into((image));

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.show();
        }

        private class DownloadFile extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... url) {
                try {
                    File path = new File(Environment.getExternalStorageDirectory(), "Ml_Academy/docs/");
                    if (!path.exists())
                        path.mkdirs();

                    String data[] = url[0].split("/");
                    String url1 = url[0].replace(" ", "%20");
                    URL u = new URL(url1);
                    InputStream dis = new BufferedInputStream(u.openStream());
                    byte[] buffer = new byte[8192];
                    int length;
                    FileOutputStream fos = new FileOutputStream(new File(path, data[data.length - 1]));
                    while ((length = dis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    fos.flush();
                    fos.close();
                    dis.close();
                    ((BaseActivity) context).dismissLoader();
                    openFile1(context, path + "/" + data[data.length - 1]);

                } catch (Exception e) {
                    ((Dashboard)getActivity()).dismissLoader();
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

        }

        public void openFile1(Context context, String url) {
            try {
                Uri uri = Uri.fromFile(new File(url));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                    intent.setDataAndType(uri, "application/msword");
                } else if (url.toString().contains(".pdf")) {
                    intent.setDataAndType(uri, "application/pdf");
                } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                    intent.setDataAndType(uri, "application/x-wav");
                } else if (url.toString().contains(".rtf")) {
                    intent.setDataAndType(uri, "application/rtf");
                } else if (url.toString().contains(".wav") || url.toString().contains(".mp3") || url.toString().contains(".AMR")) {
                    intent.setDataAndType(uri, "audio/x-wav");
                } else if (url.toString().contains(".gif")) {
                    intent.setDataAndType(uri, "image/gif");
                } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                    intent.setDataAndType(uri, "image/jpeg");
                } else if (url.toString().contains(".txt")) {
                    intent.setDataAndType(uri, "text/plain");
                } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                    intent.setDataAndType(uri, "video/*");
                } else {
                    intent.setDataAndType(uri, "*/*");
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreference_main.setOnChat(false);
    }


}