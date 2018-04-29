package com.frank.markdowneditor.Views;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.frank.markdowneditor.Adapters.MyRecyclerView.MyRVAdapter;
import com.frank.markdowneditor.R;

public class Winds {
    private static AlertDialog dialog;

    /**
     * 生成 AlertDialog
     * @param context 上下文
     * @return parent 父试图
     */
    public static View dialogBuilder(Context context,int resource,boolean b){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View parent = LayoutInflater.from(context).inflate(resource,null,false);
        dialogBuilder.setView(parent);
        dialogBuilder.setCancelable(b);
        dialogBuilder.create();
        dialog = dialogBuilder.show();
        return parent;
    }

    /**
     * 重命名时弹出的窗口
     * @param context 上下文
     * @param adapter RecyclerView的适配器
     */
    public static void rename(final Context context, String oldTitle, final MyRVAdapter adapter, final RecyclerView rv){
        View parent = dialogBuilder(context, R.layout.dialog_input_layout,true);
        ((TextView)parent.findViewById(R.id.dialog_notice)).setText(R.string.reame);
        final EditText et = parent.findViewById(R.id.input_name_ed);
        et.setText(oldTitle);
        et.setSelection(oldTitle.length());
        Button OKbtn      = parent.findViewById(R.id.input_name_btn);
        OKbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et.getText().toString();
                adapter.rename(title);
                dialog.dismiss();
                rv.scrollToPosition(0);
            }
        });
    }

}
