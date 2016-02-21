package com.estimote.examples.demos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.estimote.examples.demos.R;

public class AllDemosActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.all_demos);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(getTitle());

    findViewById(R.id.notify_demo_button).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startNotifyDemoActivity();
      }
    });

  }

  private void startNotifyDemoActivity(){
    Intent intent = new Intent(AllDemosActivity.this, NotifyDemoActivity.class);
    startActivity(intent);
  }
}
