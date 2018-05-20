package com.frank.markdowneditor.Activitys;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.frank.markdowneditor.Adapters.MyPagerAdapter;
import com.frank.markdowneditor.AnalyzeString;
import com.frank.markdowneditor.Article;
import com.frank.markdowneditor.Constants;
import com.frank.markdowneditor.Data.IO.IOTools;
import com.frank.markdowneditor.R;
import com.frank.markdowneditor.Views.StyleInputPanel;
import com.frank.markdowneditor.template.HTMLTemp;
import com.frank.markdowneditor.template.HTML_TXT;

import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

import static android.webkit.WebView.enableSlowWholeDocumentDraw;

public class WritingActivity extends AppCompatActivity{
    private Toolbar         toolbar;
    private ViewPager       viewPager;
    private WebView         webView;
    private EditText        titleEd,contentEd;
    private TabLayout       tabLayout;
    private RelativeLayout  page1,page2;
    private StyleInputPanel styleInputPanel;
    private List<View>      views  = new ArrayList<>();
    private String[]        titles = new String[2];
    private Article         article;
    private int id          = -1;
    private boolean         changeType = false;
    private IOTools         ioTools;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            enableSlowWholeDocumentDraw();
        }
//        if (!isFullScreen){//设置为非全屏
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }else{//设置为全屏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
        setContentView(R.layout.activity_writing);
        iniView();
        initData();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                WritingActivity.this.finish();
            }
        });

        String title   = article.getTitle();
        String content = article.getContent();
        titleEd.setText(title);
        titleEd.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                saveTitle(s.toString());
            }
        });
        contentEd.setText(content);
        contentEd.setSelection((article.getContent() == null ? 0:article.getContent().length()));
        //下面4句是设置获取焦点，必须这么写...
        contentEd.setFocusable(true);
        contentEd.setFocusableInTouchMode(true);
        contentEd.requestFocus();
        contentEd.findFocus();
        contentEd.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                saveContent(s.toString());
            }
        });

        viewPager.setAdapter(new MyPagerAdapter(views,titles));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override public void onPageSelected(int position) {
                if (position == 1) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    loadWebViewData();
                }else {
                    contentEd.setFocusable(true); //设置内容输入框的焦点
                    contentEd.setFocusableInTouchMode(true);
                    contentEd.requestFocus();
                    contentEd.findFocus();
                }
            }
            @Override public void onPageScrollStateChanged(int state) { }
        });

    }

    /**
     * 向WebView加载数据
     */
    public void loadWebViewData(){
        String title   = titleEd.getText().toString();
        String content = contentEd.getText().toString();
        AnalyzeString analyze = new AnalyzeString(content);
        StringBuilder stringBuilder = analyze.getBuilder();
        String contentWithType = "";
        if (!changeType){
            contentWithType = HTMLTemp.START+HTMLTemp.setTitle(title)+stringBuilder+HTMLTemp.END;
        }else {
            contentWithType = HTML_TXT.START+HTML_TXT.setTitle(title)+content+HTML_TXT.END;
        }
        webView.loadDataWithBaseURL(
                "about:blank",
                contentWithType,
                "text/html",
                "utf-8",
                null);
    }

    private void iniView(){
        toolbar   = findViewById(R.id.writing_toolbar);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.writing_tablayout);
        page1     = (RelativeLayout) getLayoutInflater().inflate(R.layout.page_1,null,false);
        page2     = (RelativeLayout) getLayoutInflater().inflate(R.layout.page_2,null,false);
        views.add(page1);
        views.add(page2);
        titleEd   = page1.findViewById(R.id.ed_title);
        contentEd = page1.findViewById(R.id.ed_content);
        webView   = page2.findViewById(R.id.writing_webview);
        styleInputPanel = page1.findViewById(R.id.style_input_panel);
        styleInputPanel.registerEditText(contentEd);
        titles[0] = "编辑";
        titles[1] = "预览";
    }

    public void initData(){
        ioTools = new IOTools(this);
        id = getIntent().getIntExtra("id",-1);
        if (id != -1) {
            article = DataSupport.find(Article.class, id);
        }else {
            article = new Article("无标题","", System.currentTimeMillis());
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.writing_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_meun_more:
                if (titleEd.getText().toString().equals("")){ Toast.makeText(this,"请填写标题！",Toast.LENGTH_SHORT).show();return true; }
                if (viewPager.getCurrentItem() == 0){
                    viewPager.setCurrentItem(1);
                }
                showMoreMenu(findViewById(R.id.toolbar_meun_more),this);
                break;
            case R.id.toolbar_meun_type:
                changeType = !changeType;
                if (viewPager.getCurrentItem() == 0){
                    viewPager.setCurrentItem(1);
                }
                loadWebViewData();
                invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (changeType) {
            menu.findItem(R.id.toolbar_meun_type).setIcon(R.drawable.icon_txt);
        }else {
            menu.findItem(R.id.toolbar_meun_type).setIcon(R.drawable.icon_markdown);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 保存菜单
     * @param v anchor
     * @param context 上下文
     */
    private void showMoreMenu(View v,Context context){
        PopupMenu menu = new PopupMenu(context,v);
        getMenuInflater().inflate(R.menu.writing_more_menu,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Article article = new Article();
                article.setTitle(titleEd.getText().toString());
                article.setContent(contentEd.getText().toString());
                switch(item.getItemId()){
                    case R.id.saveAsHTML:
                        if (!changeType) {
                            if (ioTools.savaAsHtml(article, IOTools.Type.MARKDOWN)) {
                                Snackbar.make(page2, "保存到" + ioTools.getProjectPath() + "下！", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(page2, "保存失败！", Snackbar.LENGTH_LONG).show();
                            }
                        }else {
                            if (ioTools.savaAsHtml(article, IOTools.Type.TXT)) {
                                Snackbar.make(page2, "保存到" + ioTools.getProjectPath() + "下！", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(page2, "保存失败！", Snackbar.LENGTH_LONG).show();
                            }
                        }
                        break;
                    case R.id.saveAsImg:
                        //获取webview缩放率
                        float scale = webView.getScale();
                        //得到缩放后webview内容的高度
                        int webViewHeight = (int) (webView.getContentHeight()*scale);
                        Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(),webViewHeight, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        //绘制
                        webView.draw(canvas);
                        if (ioTools.saveAsImg(bitmap,article)){
                            Snackbar.make(page2,"已保存到"+ ioTools.getProjectPath()+"下！",Snackbar.LENGTH_LONG).show();
                        }else {
                            Snackbar.make(page2,"保存失败！",Snackbar.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.saveAsMd:
                        if (ioTools.saveAsMD(article)){
                            Snackbar.make(page2,"已保存到"+ ioTools.getProjectPath()+"下！",Snackbar.LENGTH_LONG).show();
                        }else {
                            Snackbar.make(page2,"保存失败！",Snackbar.LENGTH_LONG).show();
                        }
                        break;
                }
                return false;
            }
        });
        menu.show();
    }

    /**
     * 保存 Content
     * @param content
     */
    public void saveContent(String content){
        if (article != null){
            article.setContent(content.toString());
            article.setTime(System.currentTimeMillis());
            if (id != -1) {
                article.update(id);
            }else {
                article.save();
            }
            Constants.refresh = true;
        }
    }
    /**
     * 保存 Title
     * @param title
     */
    public void saveTitle(String title){
        if (article != null){
            article.setTitle(title.toString());
            article.setTime(System.currentTimeMillis());
            if (id != -1) {
                article.update(id);
            }else {
                article.save();
            }
            Constants.refresh = true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (viewPager.getCurrentItem() == 1) {
                viewPager.setCurrentItem(0);
            } else {
                this.finish();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);

        }
    }

}