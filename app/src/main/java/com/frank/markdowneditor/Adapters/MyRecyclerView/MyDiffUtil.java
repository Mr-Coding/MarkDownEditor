package com.frank.markdowneditor.Adapters.MyRecyclerView;

import android.support.v7.util.DiffUtil;

import com.frank.markdowneditor.Article;

import java.util.List;

public class MyDiffUtil extends DiffUtil.Callback {
    private List<Article> newArticles;
    private List<Article> oldArticles;

    public MyDiffUtil(List<Article> newArticles, List<Article> oldArticles) {
        this.newArticles = newArticles;
        this.oldArticles = oldArticles;
    }

    @Override
    public int getOldListSize() {
        return oldArticles != null ? oldArticles.size():0;
    }

    @Override
    public int getNewListSize() {
        return newArticles != null ? newArticles.size():0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldArticles.get(oldItemPosition).getId() != newArticles.get(newItemPosition).getId()){
            return false;
        }
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Article oldArticle = oldArticles.get(oldItemPosition);
        Article newArticle = newArticles.get(newItemPosition);
        return oldArticle.getTime().equals(newArticle.getTime());
    }


}
