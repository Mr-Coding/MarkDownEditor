package com.frank.markdowneditor.Activitys;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.frank.markdowneditor.Adapters.MyRecyclerView2.MyRVAdapter;
import com.frank.markdowneditor.Data.IO.IOTools;
import com.frank.markdowneditor.Data.SettingsData;
import com.frank.markdowneditor.R;

import java.io.File;
import java.util.List;

public class SettingSaveDirActivity extends AppCompatActivity {
    private final String         TAG = "SettingSaveDirActivity";
    private RecyclerView         save_dir_rv;
    private TextView             dirfornowTV, backTV;
    private List<File>           files;
    private MyRVAdapter          adapter;
    private String               directory;
    private SettingsData         data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_save_dir);
        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });

        data      = new SettingsData(this);
        directory = data.getDirectory(IOTools.getROOT()+"/MarkDown编辑器/");

        files     = new IOTools().getDirs(directory);

        findViewById(R.id.floating_action_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                data.setDirectory(directory);
                finish();
            }
        });

        dirfornowTV = findViewById(R.id.dirfornowTV);
        dirfornowTV.setSingleLine(true);
        dirfornowTV.setSelected(true);
        dirfornowTV.setText(directory);
        Log.i(TAG,directory);

        save_dir_rv = findViewById(R.id.save_dir_rv);
        save_dir_rv.setLayoutManager(new LinearLayoutManager(this));
        save_dir_rv.addItemDecoration(new MyItemDecoration());
        save_dir_rv.setAdapter((adapter = new MyRVAdapter(files,this)));

        adapter.setDirectory(directory);
        adapter.setOnFolderSelectedListener(new MyRVAdapter.OnFolderSelectedListener() {
            @Override public void onFolderSelected(String directory) {
                dirfornowTV.setText(directory);
                SettingSaveDirActivity.this.directory = directory;
                if (directory.equals(IOTools.getROOT())){
                    backTV.setTextColor(Color.GRAY);
                }else {
                    backTV.setTextColor(0xff3b82d9);
                }
            }
        });

        backTV = findViewById(R.id.backTV);
        backTV.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (adapter.back().equals(IOTools.getROOT())){
                    backTV.setTextColor(Color.GRAY);
                }else {
                    backTV.setTextColor(0xff3b82d9);
                }
            }
        });

    }

    class MyItemDecoration extends RecyclerView.ItemDecoration{
        @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,1);
        }
    }
}
