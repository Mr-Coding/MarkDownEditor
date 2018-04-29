package com.frank.markdowneditor.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frank.markdowneditor.R;

public class MySearchView extends LinearLayout implements TextWatcher, View.OnClickListener {
    private EditText  input;
    private ImageView clean;
    private TextView  close;
    private OnMyQueryTextListener listener;

    public MySearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.my_search_view, this, true);
        input = findViewById(R.id.search_input);
        input.setFocusable(true);
        input.setFocusableInTouchMode(true);
        input.requestFocus();
        input.findFocus();

        clean = findViewById(R.id.search_clean);
        input.addTextChangedListener(this);
        clean.setVisibility(GONE);
        clean.setOnClickListener(this);
        findViewById(R.id.closeBtn).setOnClickListener(this);
    }

    public void setInput(String content){
        input.setText(content);
    }

    public void setOnMyQueryTextListener(OnMyQueryTextListener listener){
        this.listener = listener;
    }

    public interface OnMyQueryTextListener{
        void onQueryTextChange(String newText);
        void onClose();
    }

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
    @Override public void afterTextChanged(Editable s) {
        String str = s.toString();
        if (str.equals("")){
            clean.setVisibility(GONE);
        }else {
            clean.setVisibility(VISIBLE);
        }
        if (listener != null) {
            listener.onQueryTextChange(s.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_clean:
                input.setText("");
                break;
            case R.id.closeBtn:
                listener.onClose();
                break;
        }
    }
}
