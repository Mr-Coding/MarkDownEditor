package com.frank.markdowneditor.Adapters.MyRecyclerView;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frank.markdowneditor.Activitys.WritingActivity;
import com.frank.markdowneditor.Article;
import com.frank.markdowneditor.R;
import com.frank.markdowneditor.Tools;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MyRVAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter,View.OnCreateContextMenuListener{
    private List<Article> articles;
    private List<Article> selectArticle = new ArrayList<>();
    private boolean       isOpenSelect  = false;
    private boolean       isSelectAll   = false;
    private Article       emptyArticle  = new Article(-1,"null","null", (long) 0);
    private OnItemActionListener listener;
    private int position;

    public MyRVAdapter(List<Article> articles) {
        this.articles = articles;
        articles.add(articles.size(),emptyArticle);// 填充空白Item
    }

    /***********************************************************************************************
     * 删除已选择的Item集合
     */
    public void delItem(){
        List<Article> newData = new ArrayList<>(articles);
        newData.removeAll(selectArticle);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffUtil(newData,articles));
        articles.clear();
        articles.addAll(newData);
        listener.OnItemSelected(0);
        for (Article s :selectArticle){
            Log.i("M",s.getId()+"");
            s.delete();
        }
        selectArticle.clear();
        isSelectAll = false;
//        isOpenSelect = false;
        result.dispatchUpdatesTo(this);
    }

    /**
     * 单个Item删除的操作
     * @param position
     */
    public void delItem(int position){
        List<Article> newData = new ArrayList<>(articles);
        Article article = articles.get(position);
        newData.remove(position);
        article.delete();
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffUtil(newData,articles));
        articles.clear();
        articles.addAll(newData);
        listener.OnItemSelected(0);
//        isOpenSelect = false;
        result.dispatchUpdatesTo(this);
        listener.onItemDelete(article,position);
    }

    /**
     * 添加Item的操作
     * @param article
     * @param position
     */
    public void addItem(Article article,int position){
        List<Article> newData = new ArrayList<>(articles);
        newData.add(position,article);
        article.save();
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffUtil(newData,articles));
        articles.clear();
        articles.addAll(newData);
        result.dispatchUpdatesTo(this);
    }

    /**
     * 刷新数据
     */
    public void refreshData(){
        List<Article> newData = new ArrayList<>(DataSupport.findAll(Article.class));
        Tools.sortByTime(newData);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffUtil(newData,articles));
        articles.clear();
        articles.addAll(newData);
        result.dispatchUpdatesTo(this);
    }

    /**
     * 重命名
     * @param title 标题
     */
    public void rename(String title) {
        Long time = System.currentTimeMillis();
        Article articles = this.articles.get(getPosition());
        articles.setTitle((title==""||title.equals("") ? "无标题":title));
        articles.setTime(time);
        this.articles.remove(getPosition());
        this.articles.add(0,articles);
        int id = articles.getId();
        articles.update(id);

        notifyItemChanged(getPosition());
        notifyItemRangeChanged(getPosition(), this.articles.size());
        notifyDataSetChanged();
    }
        /**********************************************************************************************/

    /**
     * 设置新数据
     * @param articles 新数据集合
     */
    public void setData(List<Article> articles){
        this.articles = new ArrayList<>(articles);
    }

    /**
     * 设置是否开启多选模式
     * @param isOpenSelect
     */
    public void setOpenSelect(boolean isOpenSelect){
        this.isOpenSelect = isOpenSelect;
        selectArticle.clear();
        isSelectAll = false;
        notifyDataSetChanged();
    }

    /**
     * 全选开关
     */
    public void isSelectAll(){
        if (isSelectAll){
            isSelectAll = false;
            selectArticle.clear();
        }else {
            isSelectAll = true;
            selectArticle = new ArrayList<>(articles);
            selectArticle.remove(emptyArticle);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取多选模式的状态
     * @return
     */
    public boolean getOpenSelect(){ return isOpenSelect; }

    public List<Article> getSelectArticle(){
        return selectArticle;
    }


    public int getPosition() {return position; }
    public void setPosition(int position) { this.position = position; }


    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == R.layout.item_1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_1, parent, false);
            return new MyViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_2, null, false);
            return new MyViewHolder2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder){
            final MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.tvTitle.setText(articles.get(holder1.getAdapterPosition()).getTitle());
            holder1.tvContent.setText(articles.get(holder1.getAdapterPosition()).getContent());
            holder1.tvTime.setText(Tools.stampToDate(articles.get(holder1.getAdapterPosition()).getTime()));
            holder1.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                    if (isChecked){
                        if(!selectArticle.contains(articles.get(holder1.getAdapterPosition()))) {
                            selectArticle.add(articles.get(holder1.getAdapterPosition()));
                        }
                    }else {
                        if(selectArticle.contains(articles.get(holder1.getAdapterPosition()))) {
                            selectArticle.remove(articles.get(holder1.getAdapterPosition()));
                        }
                    }
                    listener.OnItemSelected(selectArticle.size());
                }
            });
            settingSelect(holder1,holder1.getAdapterPosition());
            holder1.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    setPosition(holder1.getAdapterPosition());
                    return false;
                }
            });
            if (!isOpenSelect) {
                holder1.itemView.setOnCreateContextMenuListener(this);
            }else {
                holder1.itemView.setOnCreateContextMenuListener(null);
            }
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (getOpenSelect()){
                        if (!holder1.chk.isChecked()) {
                            holder1.chk.setChecked(true);
                        }else {
                            holder1.chk.setChecked(false);
                        }
                    }else {
                        Intent i = new Intent(v.getContext(),WritingActivity.class);
                        i.putExtra("id",articles.get(holder1.getAdapterPosition()).getId());
                        v.getContext().startActivity(i);
                    }
                }
            });
        }
    }

    /**
     * 多选相关设置
     * @param holder ViewHolder
     * @param position Item索引
     */
    private void settingSelect(MyViewHolder holder,int position){
        //判断多选是否开启，如开启就显示CheckBox，反之隐藏
        holder.chk.setVisibility((isOpenSelect ? View.VISIBLE : View.GONE));
        if (selectArticle != null){
            //判断已选集合里是否包含该项Item，包含就勾选CheckBox，反之反选
            if (selectArticle.contains(articles.get(position))){
                holder.chk.setChecked(true);
            }else {
                holder.chk.setChecked(false);
            }
        }
    }

    /**
     * 上下的拖动时的相关操作
     * @param fromPosition
     * @param toPosition
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置,（暂时不考虑）
    }

    /**
     * 从右向左侧滑时的相关操作
     * @param position
     */
    @Override
    public void onItemDissmiss(int position) {
        //移除数据
        delItem(position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE,0,Menu.NONE,R.string.open);
        menu.add(Menu.NONE,1,Menu.NONE,R.string.delete);
        menu.add(Menu.NONE,2,Menu.NONE,R.string.rename);
    }


    /**
     * 获取已选Item数量的回掉监听接口
     */
    public interface OnItemActionListener {
        void OnItemSelected(int count);
        void onItemDelete(Article article,int position);
    }
    /**
     * 设置回掉监听接口的方法
     * @param listener
     */
    public void setOnItemActionListener(OnItemActionListener listener){
        this.listener = listener;
    }

    /**
     * 通过给定的字符串从集合里选出匹配的实体类
     * @param query 查询的字符串
     */
    public void filter(String query){
        List<Article> filterResults = new ArrayList<>();
        if (!query.equals("")){
            List<Article> articlesOrigin = DataSupport.findAll(Article.class);
            for (Article article : articlesOrigin) {
                String title = article.getTitle();
                if (title.contains(query)) {
                    Log.i("MyRVAdapter", query);
                    filterResults.add(article);
                }
            }
            Tools.sortByTime(filterResults);
        }
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffUtil(filterResults,articles));
        this.articles.clear();
        this.articles.addAll(filterResults);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        if (articles.get(position) == emptyArticle){
            return R.layout.item_2;
        }else {
            return R.layout.item_1;
        }
    }

    @Override
    public int getItemCount() { return articles.size(); }

    /***********************************************************************************************
     * ViewHolders
     */
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvContent,tvTime;
        CheckBox chk;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle   = itemView.findViewById(R.id.item_tv_title);
            tvContent = itemView.findViewById(R.id.item_tv_content);
            tvTime    = itemView.findViewById(R.id.item_tv_time);
            chk       = itemView.findViewById(R.id.item_chk);
        }
    }
    class MyViewHolder2 extends RecyclerView.ViewHolder{
        public MyViewHolder2(View itemView) {
            super(itemView);
        }
    }
    /**********************************************************************************************/

}
