package com.ritara.svustudent;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;

public class CircularViewActivity extends Activity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_circular);

    CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circleMenu);
    circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {
      @Override
      public void onItemClick(CircleMenuButton menuButton) {

        switch (menuButton.getId()){
          case R.id.favorite:
            Toast.makeText(CircularViewActivity.this, "Fav", Toast.LENGTH_SHORT).show();
            break;
          case R.id.search:
            Toast.makeText(CircularViewActivity.this, "search", Toast.LENGTH_SHORT).show();
            break;
          case R.id.alert:
            Toast.makeText(CircularViewActivity.this, "alert", Toast.LENGTH_SHORT).show();
            break;
          case R.id.jobs:
            Toast.makeText(CircularViewActivity.this, "jobs", Toast.LENGTH_SHORT).show();
            break;
          case R.id.admission:
            Toast.makeText(CircularViewActivity.this, "admission", Toast.LENGTH_SHORT).show();
            break;
          case R.id.placement:
            Toast.makeText(CircularViewActivity.this, "placement", Toast.LENGTH_SHORT).show();
            break;
          case R.id.help:
            Toast.makeText(CircularViewActivity.this, "help", Toast.LENGTH_SHORT).show();
            break;
          case R.id.results:
            Toast.makeText(CircularViewActivity.this, "results", Toast.LENGTH_SHORT).show();
            break;
          case R.id.courses:
            Toast.makeText(CircularViewActivity.this, "courses", Toast.LENGTH_SHORT).show();
            break;
          default:
            break;

        }
      }
    });
  }
}
