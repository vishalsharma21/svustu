package com.ritara.svustudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;

public class CircularView extends Fragment {

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_circular, container, false);

    CircleMenu circleMenu = (CircleMenu) view.findViewById(R.id.circleMenu);
    circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {
      @Override
      public void onItemClick(CircleMenuButton menuButton) {

        switch (menuButton.getId()){
          case R.id.favorite:
            Toast.makeText(getActivity(), "Fav", Toast.LENGTH_SHORT).show();
            break;
          case R.id.search:
            Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
            break;
          case R.id.alert:
            Toast.makeText(getActivity(), "alert", Toast.LENGTH_SHORT).show();
            break;
          case R.id.jobs:
            Toast.makeText(getActivity(), "jobs", Toast.LENGTH_SHORT).show();
            break;
          case R.id.admission:
            Toast.makeText(getActivity(), "admission", Toast.LENGTH_SHORT).show();
            break;
          case R.id.placement:
            Toast.makeText(getActivity(), "placement", Toast.LENGTH_SHORT).show();
            break;
          case R.id.help:
            Toast.makeText(getActivity(), "help", Toast.LENGTH_SHORT).show();
            break;
          case R.id.results:
            Toast.makeText(getActivity(), "results", Toast.LENGTH_SHORT).show();
            break;
          case R.id.courses:
            Toast.makeText(getActivity(), "courses", Toast.LENGTH_SHORT).show();
            break;
          default:
            break;

        }
      }
    });

    return view;
  }
}
