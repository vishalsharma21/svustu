package com.ritara.svustudent.Adapters;

import android.view.View;

public interface BindAdapter {
    void onBindHolder(Common_RecyclerView_Adapter.ViewHolder viewHolder, String from, int position, int x);

    void onView_holder(View itemView, String from);
}
