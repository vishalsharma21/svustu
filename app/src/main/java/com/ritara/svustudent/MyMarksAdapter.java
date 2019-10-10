package com.ritara.svustudent;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ritara.svustudent.fragments.MarksFragment;
import com.ritara.svustudent.fragments.PaidFeeFragment;
import com.ritara.svustudent.utils.FeeModel;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class MyMarksAdapter extends RecyclerView.Adapter<MyMarksAdapter.ViewHolder> implements View.OnTouchListener, Handler.Callback{

    private final ArrayList<FeeModel> mValues;
    private Context context;
    private SharedPreferences_SVU sharedPreferences_svu;
    WebView wb;
    private WebViewClient client;
    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;
    private final Handler handler;// = new Handler(this);
    private Dialog dialog;

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == CLICK_ON_URL){
            handler.removeMessages(CLICK_ON_WEBVIEW);
            dialog.dismiss();
            return true;
        }
        if (msg.what == CLICK_ON_WEBVIEW){
            Toast.makeText(context, "WebView clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.wvResult && event.getAction() == MotionEvent.ACTION_DOWN){
            handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500);
        }
        return false;
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            handler.sendEmptyMessage(CLICK_ON_URL);
            return false;
        }
    }

    public MyMarksAdapter(ArrayList<FeeModel> items, Context context) {
        mValues = items;
        this.context = context;
        handler = new Handler(this);
        sharedPreferences_svu = SharedPreferences_SVU.getInstance(context);
    }

    @Override
    public MyMarksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_marks, parent, false);

        return new MyMarksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyMarksAdapter.ViewHolder holder, final int position) {
        holder.txtMarks.setText(mValues.get(position).getName());

        holder.txtMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultDialog(mValues.get(position).getName());
            }
        });

    }

    private void showResultDialog(String name) {
        String[] marks = name.split(":");
        String mrks = marks[1].trim();
        dialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_result_view);
        String url = "http://45.115.168.40/result/View.aspx?r="+sharedPreferences_svu.getUserId()+"&c="+mrks;

        wb=(WebView)dialog.findViewById(R.id.wvResult);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setPluginState(WebSettings.PluginState.ON);
//        wb.getSettings().setPluginsEnabled(true);
//        wb.setWebViewClient(new HelloWebViewClient());

        client = new WebViewClient(){
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                handler.sendEmptyMessage(CLICK_ON_URL);
                return false;
            }
        };

        wb.setWebViewClient(client);
        wb.setVerticalScrollBarEnabled(false);


        wb.loadUrl(url);

        wb.evaluateJavascript(
                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        Log.e("HTML", html);
                        // code here
                    }
                });
//        WebView webView = dialog.findViewById(R.id.wvResult);
        //http://192.168.2.100:84/PrintStatus.aspx?EnrollNo=SOL17A05010014&Sem=Ist%20%20Sem

//        ​​​​​​​​WebSettings webSettings = webView.getSettings(); ​​
//        ​​​​​​webSettings.setBuiltInZoomControls(true);
//        ​​​​​​​​webView.loadUrl("https://www.ritaraapps.com");

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView txtMarks;
        public ArrayList<FeeModel> mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtMarks = (TextView) view.findViewById(R.id.txtMarks);

        }

        @Override
        public String toString() {

            return "";
        }
    }
}
