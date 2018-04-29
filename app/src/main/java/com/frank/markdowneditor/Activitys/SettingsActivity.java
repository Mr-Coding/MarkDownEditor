package com.frank.markdowneditor.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.frank.markdowneditor.Data.SettingsData;
import com.frank.markdowneditor.R;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar      toolbar;
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

        data = new SettingsData(this,"settings");
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
//                if (isChecked){
//                    MD.saveAsSqlite();
//                }
            }
        });


    }

    public void initViews(){
        toolbar = findViewById(R.id.toolbar);
        settingsSwitch = findViewById(R.id.settings_switch);
    }
}
