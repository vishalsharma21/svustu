package com.ritara.svustudent.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotifyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotifyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Notification Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
