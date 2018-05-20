package com.frank.markdowneditor.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.frank.markdowneditor.Data.IO.IOTools;
import com.frank.markdowneditor.Data.SettingsData;
import com.frank.markdowneditor.R;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar      toolbar;
    private TextView     dirTV;
    private Switch       settingsSwitch;
    private SettingsData data;
    private boolean      isOpenFindMd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });

        new IOTools(this).getDirs();

        data = new SettingsData(this);
        isOpenFindMd = data.isOpenFindMd(false);
        if (isOpenFindMd){
            settingsSwitch.setChecked(true);
        }else {
            settingsSwitch.setChecked(false);
        }

        settingsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setOpenFindMd(isChecked);
            }
        });

        findViewById(R.id.change_dir_panel).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this,SettingSaveDirActivity.class);
                startActivity(i);
            }
        });

    }

    public void initViews(){
        toolbar = findViewById(R.id.toolbar);
        dirTV   = findViewById(R.id.dirTV);
        settingsSwitch = findViewById(R.id.settings_switch);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String directory = data.getDirectory(IOTools.getROOT()+"/MarkDown编辑器/");
        dirTV.setText(directory);
    }
}
