package com.main.app;

import ILil.IL1Iii;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class JmpEmptyActivity extends AppCompatActivity {
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    ComponentName componentName = new ComponentName(getIntent().getStringExtra(IL1Iii.IL1Iii(new byte[] { 19, 9, 85 }, "cb2000")), getIntent().getStringExtra(IL1Iii.IL1Iii(new byte[] { 1, 90, 31 }, "b6ec3c")));
    Intent intent = new Intent();
    intent.setComponent(componentName);
    startActivity(intent);
    finish();
  }
}
