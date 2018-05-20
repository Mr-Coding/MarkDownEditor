package com.frank.markdowneditor.Adapters.MyRecyclerView2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frank.markdowneditor.Data.IO.IOTools;
import com.frank.markdowneditor.R;

import java.io.File;
import java.util.List;

public class MyRVAdapter extends RecyclerView.Adapter {
    private final String      TAG       = "MyRVAdapter";
    private String            directory = "";
    private List<File>        files;
    private OnFolderSelectedListener listener;
    private IOTools           ioTools;

    public MyRVAdapter(List<File> files, Context context) {
        this.files = files;
        ioTools = new IOTools(context);
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_folder,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder holder1 = (MyViewHolder) holder;
        holder1.dirTV.setText(files.get(holder1.getAdapterPosition()).getName());
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String              filename  = files.get(holder1.getAdapterPosition()).getAbsolutePath();
                List<File>          newFiles  = ioTools.getDirs(filename);
                DiffUtil.DiffResult result    = DiffUtil.calculateDiff(new MyDiffUtil(newFiles,files));
                files                         = newFiles;
                result.dispatchUpdatesTo(MyRVAdapter.this);
                directory = (newFiles.size() > 0 ? newFiles.get(0).getParent() : filename);
                listener.onFolderSelected(directory);
            }
        });
    }

    /**
     * 检测是否还有父文件夹
     * @return 所在目录
     */
    public String back(){
        if (directory.equals(IOTools.getROOT())){
            Log.i(TAG,"没了。");
        }else{
            List<File> newFiles        = ioTools.getDirs(new File(directory).getParent());
            directory                  = newFiles.get(0).getParent();
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffUtil(newFiles, files));
            files                      = newFiles;
            result.dispatchUpdatesTo(this);
            listener.onFolderSelected(directory);
        }
        return directory;
    }

    public void setOnFolderSelectedListener(OnFolderSelectedListener listener){
        this.listener = listener;
    }
    public interface OnFolderSelectedListener {
        void onFolderSelected(String dir);
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dirTV;
        public MyViewHolder(View itemView) {
            super(itemView);
            dirTV = itemView.findViewById(R.id.dirTV);
        }
    }
}
