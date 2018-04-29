package com.frank.markdowneditor.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.frank.markdowneditor.R;

public class StyleInputPanel extends LinearLayout implements View.OnClickListener {
    private EditText editText;

    public StyleInputPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.panel_style_btns,this,true);
        findViewById(R.id.title1_but) .setOnClickListener(this);
        findViewById(R.id.title3_but) .setOnClickListener(this);
        findViewById(R.id.title5_but) .setOnClickListener(this);
        findViewById(R.id.bold_but)   .setOnClickListener(this);
        findViewById(R.id.italic_but) .setOnClickListener(this);
        findViewById(R.id.img_but)    .setOnClickListener(this);
        findViewById(R.id.link_but)   .setOnClickListener(this);
        findViewById(R.id.codeBox_but).setOnClickListener(this);
        findViewById(R.id.line_but)   .setOnClickListener(this);
    }

    public void registerEditText(EditText editText){
        this.editText = editText;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title1_but:
                inputType(editText,"# ",null,2);
                break;
            case R.id.title3_but:
                inputType(editText,"### ",null,4);
                break;
            case R.id.title5_but:
                inputType(editText,"##### ",null,6);
                break;
            case R.id.bold_but:
                inputType(editText,"**","**",2);
                break;
            case R.id.italic_but:
                inputType(editText,"_","_",1);
                break;
            case R.id.img_but:
                inputType(editText,"![]()",null,2);
                break;
            case R.id.link_but:
                inputType(editText,"[]()",null,1);
                break;
            case R.id.codeBox_but:
                inputType(editText,"```","```",3);
                break;
            case R.id.line_but:
                inputType(editText,"---\n",null,4);
                break;
        }
    }

    private void inputType(EditText editText,String typeStart,String typeEnd,int selection){
        if (editText == null){
            try { throw new Exception("EditText NullPointerException,请通过registerEditText来注册一个EditText！");
            } catch (Exception e) { e.printStackTrace(); }
            return;
        }
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        Editable editable = editText.getText();
        if (start == end) {
            if (typeEnd == null){
                editable.insert(start,typeStart);
            }else {
                editable.insert(start, typeStart + typeEnd);
            }
            editText.setSelection(start + selection);
        }else {
            if (typeEnd == null){
                String str = editText.getText().toString();
                String str2 = str.substring(start,end);
                str = str.replace(str2,typeStart);
                editText.setText(str);
                editText.setSelection(start + selection);
                return;
            }
            editable.insert(start,typeStart);
            editable.insert(end+typeStart.length(),typeEnd);
        }
    }
}
