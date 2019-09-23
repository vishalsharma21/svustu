<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:layout_margin="10dp"
    android:background="@drawable/card_background">
    <ImageView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:src="@drawable/new_logo"
        android:layout_gravity="center"
        android:layout_marginTop="50dp"/>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="The Operating System for Highger Education"
        android:textColor="#000"
        android:textSize="19dp"
        android:gravity="center"
        android:layout_marginTop="20dp"/>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Social Learning Platform providing one-stop solution for academics,skills and careers to students, educators and inistitutions of higher education."
        android:layout_marginLeft="10dp"
        android:textColor="#3F3D3D"
        android:textSize="18dp"
        android:gravity="center"
        android:layout_marginBottom="35dp"
        android:layout_marginTop="20dp"/>

    <View
        android:layout_width="match_parent"
        android:layout_height="0.5dp"
        android:background="@drawable/gradient"/>

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_margin="10dp"
        >

        <TextView
            android:id="@+id/version"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Version No."
            android:textSize="20dp"
            android:layout_marginTop="10dp"
            android:textColor="#000"
            android:layout_marginLeft="10dp">

        </TextView>

        <TextView
            android:id="@+id/version_no"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="2.2"
            android:layout_below="@id/version"
            android:textSize="20dp"
            android:textColor="@color/text"
            android:layout_marginLeft="10dp">

        </TextView>


        <TextView
            android:id="@+id/build"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Build Id."
            android:textSize="20dp"
            android:layout_alignParentRight="true"
            android:layout_marginTop="10dp"
            android:textColor="#000"
            >

        </TextView>

        <TextView
            android:id="@+id/build_no"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="2019208C1.3"
            android:layout_below="@id/build"
            android:layout_alignParentRight="true"
            android:textSize="20dp"
            android:textColor="@color/text"
            android:layout_marginLeft="10dp">

        </TextView>


    </RelativeLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="0.5dp"
        android:background="@drawable/gradient"/>

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_margin="10dp"
        >

        <TextView
            android:id="@+id/release"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Release Date"
            android:textSize="20dp"
            android:layout_marginTop="10dp"
            android:textColor="#000"
            android:layout_marginLeft="10dp">

        </TextView>

        <TextView
            android:id="@+id/release_date"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@id/release"
            android:layout_marginLeft="10dp"
            android:layout_marginTop="0dp"
            android:text="21-09-2019"
            android:textColor="@color/text"
            android:textSize="20dp">

        </TextView>


    </RelativeLayout>


</LinearLayout>


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:background="@drawable/card_background"
        android:layout_margin="10dp">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginLeft="10dp"
            android:text="For any help or support, please co