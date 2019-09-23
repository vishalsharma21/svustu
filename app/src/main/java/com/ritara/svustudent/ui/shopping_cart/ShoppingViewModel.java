package com.ritara.svustudent.ui.shopping_cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShoppingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShoppingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Shopping Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}