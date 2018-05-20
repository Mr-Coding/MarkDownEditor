package com.frank.markdowneditor.Data.IO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.frank.markdowneditor.Article;
import com.frank.markdowneditor.Data.SettingsData;
import com.frank.markdowneditor.Tools;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MD extends IOTools {
    private static FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.toString().endsWith(".md");
        }
    };

    public void refreshPROJECT_PATH(Context context){
        PROJECT_PATH = new SettingsData(context).getDirectory(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MarkDown编辑器/");
    }

    /**
     * 在MarkDown编辑器目录下获取没有导入的md文件
     * @return files 获取到的md文件数组
     */
    private File[] findMD(){
        checkDir();
        File projPath = new File(PROJECT_PATH);
        File[] files = projPath.listFiles(fileFilter);
        return files;
    }

    /**
     * 把获取到的md文件数组保存到数据库里
     */
    public int saveAsSqlite(){
        File[] mdfiles = findMD(); //找到的.md文件
        if (mdfiles.length == 0){ Log.i("MD","没有.md文件！");return -1; }
        int length = 0;
        Article article = null;
        for (File filename : mdfiles){
            length ++;
            int index      = filename.getName().indexOf(".");
            String name    = filename.getName().substring(0,index);
            String content = ReadContent(name,".md");
            long time      = System.currentTimeMillis();
            article = new Article(name,content,time);
            article.save();
            mdMoveTo(filename,new File(PROJECT_PATH+"/"+name+"_"+Tools.stampToDate(time)));
        }
        return length;
    }

    /**
     * 把md文件移动到指定的目录下
     * @param mdFile 目标md文件
     * @param path   目标路径加原文件名
     * @return b     是否成功
     */
    public static boolean mdMoveTo(File mdFile,File path){
        boolean b = false;
        path.mkdirs();
        File newPath = new File(path+"",mdFile.getName());
        b = mdFile.renameTo(newPath);
        return b;
    }


}
