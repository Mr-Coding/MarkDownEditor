package com.frank.markdowneditor.Activitys;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;

import com.frank.markdowneditor.Adapters.MyRecyclerView.MyDiffUtil;
import com.frank.markdowneditor.Annotation.FindView;
import com.frank.markdowneditor.Annotation.ViewId;
import com.frank.markdowneditor.Article;
import com.frank.markdowneditor.Adapters.MyRecyclerView.MyRVAdapter;
import com.frank.markdowneditor.Constants;
import com.frank.markdowneditor.Data.IO.IOTools;
import com.frank.markdowneditor.Data.IO.MD;
import com.frank.markdowneditor.Data.SettingsData;
import com.frank.markdowneditor.R;
import com.frank.markdowneditor.Tools;
import com.frank.markdowneditor.Views.ControlPanel;
import com.frank.markdowneditor.Views.MySearchView;
import com.frank.markdowneditor.Views.Winds;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyRVAdapter    adapter;
    @ViewId(id = R.id.main_recyclerview)
    private RecyclerView   recyclerView;
    @ViewId(id = R.id.main_toolbar)
    private Toolbar        toolBar;
    @ViewId(id = R.id.control_panel)
    private ControlPanel   controlPanel;
    @ViewId(id = R.id.floating_action_btn)
    private FloatingActionButton FABtn;
    @ViewId(id = R.id.my_search_view)
    private MySearchView   searchView;
    private List<Article>  articles;
    private SettingsData   settingsData;
    private IOTools        ioTools;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FindView.from(this);
        setSupportActionBar(toolBar);
        initData();
        initView();
        searchView.setVisibility(View.GONE);

        FABtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,WritingActivity.class);
                startActivity(i);
            }
        });

        controlPanel.setOnMyClickListener(new ControlPanel.OnMyClickListener() {
            @Override public void saveHtml() {
                Log.i("M","saveHtml");
                if (ioTools.savaAsHtml(adapter.getSelectArticle())){
                    Snackbar.make(getWindow().getDecorView(),"已保存到"+ ioTools.getProjectPath()+"下！",Snackbar.LENGTH_LONG).show();
                }else {
                    Snackbar.make(getWindow().getDecorView(),"保存失败！",Snackbar.LENGTH_LONG).show();
                }
            }
            @Override public void saveMd() {
                if (ioTools.saveAsMD(adapter.getSelectArticle())){
                    Snackbar.make(getWindow().getDecorView(),"已保存到"+ ioTools.getProjectPath()+"下！",Snackbar.LENGTH_LONG).show();
                }else {
                    Snackbar.make(getWindow().getDecorView(),"保存失败！",Snackbar.LENGTH_LONG).show();
                }
            }
            @Override public void cancel() {
                adapter.setOpenSelect(false);
                controlPanel.setStatus(ControlPanel.STATUS.HIDE);
            }
            @Override public void delete() {
                adapter.delItem();
            }
            @Override public void selectAll() {
                adapter.isSelectAll();
            }
        });

         //每次点击checkbox后会调用，能获取已选的Item数量
        adapter.setOnItemActionListener(new MyRVAdapter.OnItemActionListener() {
            @Override public void OnItemSelected(int count) {
                controlPanel.setItemCount(count);
                if (count <= 0) {
                    controlPanel.setBtnsEnabled(false);
                } else {
                    controlPanel.setBtnsEnabled(true);
                }
            }

            public void onItemDelete(final Article article, final int position) {
                Snackbar.make(FABtn,"已删除！",Snackbar.LENGTH_LONG)
                        .setAction("撤销删除！", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.addItem(article,position);
                    }
                }).show();
            }
        });

        searchView.setOnMyQueryTextListener(new MySearchView.OnMyQueryTextListener() {
            @Override public void onQueryTextChange(String newText) {
                adapter.filter(newText);
            }
            @Override public void onClose() {
                hideSearchPanel();
            }
        });

        //滑动动画
//        //先实例化Callback
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
//        //用Callback构造ItemtouchHelper
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
//        touchHelper.attachToRecyclerView(recyclerView);

    }

    private void initView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter((adapter = new MyRVAdapter(articles)));
    }

    private void initData(){
        ioTools = new IOTools(this);
        articles = DataSupport.findAll(Article.class);
        Tools.sortByTime(articles);
        settingsData = new SettingsData(this);
    }

    @Override public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Intent i = new Intent(MainActivity.this,WritingActivity.class);
                i.putExtra("id",articles.get(adapter.getPosition()).getId());
                startActivity(i);
                break;
            case 1:
                adapter.delItem(adapter.getPosition());
                break;
            case 2:
                Winds.rename(MainActivity.this, articles.get(adapter.getPosition()).getTitle(), adapter,recyclerView);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_search_menu_btn:
                if (adapter.getOpenSelect()){
                    adapter.setOpenSelect(false);
                    controlPanel.setStatus(ControlPanel.STATUS.HIDE);
                }
                showSearchPanel();
                break;
            case R.id.main_opchk_menu_btn:
                if (adapter.getOpenSelect()){
                    adapter.setOpenSelect(false);
                    controlPanel.setStatus(ControlPanel.STATUS.HIDE);
                }else {
                    adapter.setOpenSelect(true);
                    controlPanel.setStatus(ControlPanel.STATUS.SHOW);
                }
                break;
            case R.id.main_setting_menu_btn:
                Intent intent1 = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent1);
                break;
            case R.id.main_help_menu_btn:
                Intent intent2 = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(intent2);
                break;
            case R.id.main_about_menu_btn:
                Intent intent3 = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent3);
                break;
//            case R.id.main_more_menu_btn:
//                showMoreMenu(findViewById(R.id.main_more_menu_btn));
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * “设置”，“帮助” 和 “关于” 按钮
     * @param view
     */
    private void showMoreMenu(View view){
        PopupMenu menu = new PopupMenu(getApplicationContext(),view);
        getMenuInflater().inflate(R.menu.main_more_menu,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()){
                    case R.id.settings_but:
                        intent = new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.help_but:
                        intent = new Intent(MainActivity.this,HelpActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.about_but:
                        intent = new Intent(MainActivity.this,AboutActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        menu.show();
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (adapter.getOpenSelect()) {
                adapter.setOpenSelect(false);
                controlPanel.setStatus(ControlPanel.STATUS.HIDE);
            }else if (toolBar.getVisibility() == View.GONE){
                hideSearchPanel();
            }else {
                this.finish();
            }
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //隐藏搜索界面
    private void hideSearchPanel() {
        //自动弹出键盘
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //强制隐藏Android输入法窗口
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        toolBar.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
        searchView.setInput("");
        adapter.refreshData();
    }
    //显示搜索界面
    private void showSearchPanel() {
        searchView.setVisibility(View.VISIBLE);
        toolBar.setVisibility(View.GONE);
        searchView.setInput("");
//        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override protected void onStart() {
        super.onStart();
        if (Constants.refresh){ //如果是从编辑页面返回到主页面，则不刷新，否则会出错！
            return;
        }
        boolean isOpenFindMd = settingsData.isOpenFindMd(false);
        if (isOpenFindMd){
            MD md = new MD();
            md.refreshPROJECT_PATH(this);
            int length = md.saveAsSqlite();
            if (length != -1){
                List<Article> newArticles = DataSupport.findAll(Article.class);
                Tools.sortByTime(newArticles);
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffUtil(newArticles,articles));
                result.dispatchUpdatesTo(adapter);
                adapter.setData(newArticles);
                recyclerView.scrollToPosition(0);
                Snackbar.make(getWindow().getDecorView()," 已导入 "+length+" 个md文件！(ง •_•)ง",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override protected void onRestart() {
        super.onRestart();
        if (Constants.refresh){
            adapter.refreshData();
            recyclerView.scrollToPosition(0);
            Constants.refresh = false;
        }
    }
}
