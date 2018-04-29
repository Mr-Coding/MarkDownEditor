package com.frank.markdowneditor.Views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.frank.markdowneditor.R;

public class ControlPanel extends RelativeLayout implements View.OnClickListener {
    private TextView item_count;
    private Button   saveAsHtmlBtn,saveAsMdBtn,cancelBtn,selectAllBtn,deleteBtn;
    private OnMyClickListener listener;

    public ControlPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.my_control_panel,this,true);
        item_count    = findViewById(R.id.item_count);
        saveAsHtmlBtn = findViewById(R.id.saveAsHtmlBtn);
        saveAsHtmlBtn.setOnClickListener(this);
        saveAsHtmlBtn.setEnabled(false);
        saveAsMdBtn   = findViewById(R.id.saveAsMdBtn);
        saveAsMdBtn.setOnClickListener(this);
        saveAsMdBtn.setEnabled(false);
        cancelBtn     = findViewById(R.id.closeBtn);
        cancelBtn.setOnClickListener(this);
        selectAllBtn  = findViewById(R.id.selectAllBtn);
        selectAllBtn.setOnClickListener(this);
        deleteBtn     = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(this);
        deleteBtn.setEnabled(false);
    }

    public void setBtnsEnabled(boolean isEnabled){
        deleteBtn.setEnabled(isEnabled);
        saveAsHtmlBtn.setEnabled(isEnabled);
        saveAsMdBtn.setEnabled(isEnabled);
    }

    /**
     * 回掉监听器
     */
    public interface OnMyClickListener {
        void saveHtml();
        void saveMd();
        void cancel();
        void delete();
        void selectAll();
    }

    public void setOnMyClickListener(OnMyClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveAsHtmlBtn:
                listener.saveHtml();
                break;
            case R.id.saveAsMdBtn:
                listener.saveMd();
                break;
            case R.id.closeBtn:
                listener.cancel();
                break;
            case R.id.selectAllBtn:
                listener.selectAll();
                break;
            case R.id.deleteBtn:
                listener.delete();
                break;
        }
    }


    /**
     * 设置已选的Item数
     * @param count 已选的Item数
     */
    public void setItemCount(int count){
        if (item_count != null) {
            item_count.setText(count + "");
        }else {
            try {
                throw new Exception("试图组件 ControlPanel 为空！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setStatus(STATUS status){
        checkboxControlPanelAnimator(status);
    }

    /**
     * 设置显示或消失本视图，带动画效果
     * @param status "show"显示Panel，"hide"隐藏Panel
     */
    private void checkboxControlPanelAnimator(STATUS status){
        float height = this.getHeight();
        ObjectAnimator animator = new ObjectAnimator();
        if (status == STATUS.SHOW) {
            animator = ObjectAnimator.ofFloat(this, "translationY", height, 0f);
        }else if (status == STATUS.HIDE){
            animator = ObjectAnimator.ofFloat(this, "translationY", 0f,height);
        }
        animator.setDuration(200);
        animator.start();
    }

    /**
     * 本视图组件是否隐藏的枚举值
     */
    public enum STATUS{
        SHOW,
        HIDE
    }
}
