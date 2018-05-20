package com.frank.markdowneditor.Adapters.MyRecyclerView2;

import android.support.v7.util.DiffUtil;

import com.frank.markdowneditor.Article;

import java.io.File;
import java.util.List;

public class MyDiffUtil extends DiffUtil.Callback {
    private List<File> newFiles;
    private List<File> oldFiles;

    public MyDiffUtil(List<File> newFiles, List<File> oldFiles) {
        this.newFiles = newFiles;
        this.oldFiles = oldFiles;
    }

    @Override
    public int getOldListSize() {
        return oldFiles != null ? oldFiles.size():0;
    }

    @Override
    public int getNewListSize() {
        return newFiles != null ? newFiles.size():0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFiles.get(oldItemPosition).getName().equals(newFiles.get(newItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFiles.get(oldItemPosition).getName().equals(newFiles.get(newItemPosition).getName());
    }


}
