package com.frank.markdowneditor.Data.IO;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import com.frank.markdowneditor.AnalyzeString;
import com.frank.markdowneditor.Article;
import com.frank.markdowneditor.Tools;
import com.frank.markdowneditor.template.HTMLTemp;
import com.frank.markdowneditor.template.HTML_TXT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class IOTools {
    private   static String ROOT         = Environment.getExternalStorageDirectory().getAbsolutePath();
    private   static String PROJECT_NAME = "/MarkDown编辑器/";
    protected static String PROJECT_PATH = ROOT+PROJECT_NAME;

    /**
     * 获取项目绝对路径
     * @return
     */
    public static String getProjectPath() { return PROJECT_PATH; }

    /**
     * 检测项目目录是否存在，不存在则创建
     * @return 如创建成功或已存在就返回true，如不存在也没创建成功就返回false
     */
    protected static boolean checkDir(){
        File fullPath = new File(PROJECT_PATH);
        return fullPath.exists() || fullPath.mkdirs();
    }

    /**
     * 检测项目目录下的某一子目录是否存在，不存在则创建
     * @param itemName 要检测的子目录
     * @return 如创建成功或已存在就返回true，如不存在也没创建成功就返回false
     */
    private static boolean checkDir(String itemName){
        File fullPath = new File(PROJECT_PATH +"/"+itemName+"/");
        return fullPath.exists() || fullPath.mkdirs();
    }

    /**
     * 保存为图片
     * @param bmp Bitmap
     * @param article 实体类
     * @return 保存成功返回true，反之false
     */
    public static boolean saveAsImg(Bitmap bmp,Article article) {
        checkDir();
        boolean isSuccess = false;
        String title = article.getTitle();
        isSuccess = checkDir(title);
        if(isSuccess) {
            File file = new File(PROJECT_PATH+"/"+title, title+".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    /**
     * 保存多个md文件
     * @param articles 实体类集合
     * @return 保存成功返回true，反之false
     */
    public static boolean saveAsMD(List<Article> articles) {
        if (articles == null || articles.size() == 0){ return false; }
        checkDir();
        boolean isSuccess = false;
        File file = null;
        for (int i = 0;i < articles.size();i ++){
            String title = articles.get(i).getTitle();
            isSuccess = checkDir(title);
            if(isSuccess) {
                file = new File(PROJECT_PATH, title + "/");
                try {
                    file.createNewFile();
                    WriterContent(title, articles.get(i).getContent(),".md");
                } catch (IOException e) {
                    isSuccess = false;
                    e.printStackTrace();
                    Log.i("IOTools","异常："+e.toString());
                }
            }
        }
        Log.i("IOTools","isSuccess："+isSuccess);
        return isSuccess;
    }
    /**
     * 保存单个md文件
     * @param article 实体类
     * @return 保存成功返回true，反之false
     */
    public static boolean saveAsMD(Article article) {
        if (article == null){ return false; }
        checkDir();
        boolean isSuccess = false;
        String title = article.getTitle();
        isSuccess = checkDir(title);
        if(isSuccess) {
            File file = new File(PROJECT_PATH, title + "/");
            try {
                file.createNewFile();
                WriterContent(title, article.getContent(),".md");
            } catch (IOException e) {
                isSuccess = false;
                e.printStackTrace();
                Log.i("IOTools","异常："+e.toString());
            }
        }
        Log.i("IOTools","isSuccess："+isSuccess);
        return isSuccess;
    }


    /**
     * 保存多个HTML文件
     * @param articles 实体类集合
     * @return 保存成功返回true，反之false
     */
    public static boolean savaAsHtml(List<Article> articles){
        if (articles == null || articles.size() == 0){ return false; }
        checkDir();
        boolean isSuccess = false;
        File file = null;
        for (int i = 0;i < articles.size();i ++){
            String title = articles.get(i).getTitle();
            isSuccess = checkDir(title);
            if(isSuccess) {
                file = new File(PROJECT_PATH, title + "/");
                try {
                    file.createNewFile();
                    AnalyzeString analyzeString = new AnalyzeString(articles.get(i).getContent());
                    StringBuilder content =  analyzeString.getBuilder();
                    String htmlContent = HTMLTemp.START + HTMLTemp.setTitle(title) + content+HTMLTemp.END;
                    WriterContent(title,htmlContent,".html");
                } catch (IOException e) {
                    isSuccess = false;
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }
    /**
     * 保存单个HTML文件
     * @param article 实体类
     * @return 保存成功返回true，反之false
     */
    public static boolean savaAsHtml(Article article,Type type){
        if (article == null){ return false; }
        checkDir();
        boolean isSuccess = false;
        File file = null;
        String title = article.getTitle();
        isSuccess = checkDir(title);
        if(isSuccess) {
            file = new File(PROJECT_PATH, title + "/");
            try {
                file.createNewFile();
                AnalyzeString analyzeString = new AnalyzeString(article.getContent());
                StringBuilder content = null;
                String        htmlContent = "";
                if (type == Type.MARKDOWN) {
                    content = analyzeString.getBuilder();
                    htmlContent = HTMLTemp.START + HTMLTemp.setTitle(title) + content+HTMLTemp.END;
                }else if (type == Type.TXT){
                    content = new StringBuilder(article.getContent());
                    htmlContent = HTML_TXT.START + HTML_TXT.setTitle(title) + content+HTML_TXT.END;
                }
                WriterContent(title,htmlContent,".html");
            } catch (IOException e) {
                isSuccess = false;
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    public enum Type{
        TXT,
        MARKDOWN
    }

    /**
     * 为指定的文件写入内容
     * @param title 指定的文件名
     * @param fileContent 内容
     * @param fileType 后缀名
     */
    private static void WriterContent(String title, String fileContent,String fileType){
        File fullPath = new File(PROJECT_PATH +"/");
        BufferedWriter BWriter = null;
        try {
            BWriter = new BufferedWriter(new FileWriter(fullPath+"/"+title+"/"+title+fileType));
            BWriter.write(fileContent);
            BWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (BWriter != null){
                try {
                    BWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected static String ReadContent(String title, String fileType) {
        File fullPath = new File(PROJECT_PATH +"/");
        InputStreamReader isr  = null;
        BufferedReader BReader = null;
        String temp = null;
        String content = "";
        try {
            isr     = new InputStreamReader(new FileInputStream(new File(fullPath+"/"+title+fileType)), "UTF-8");
            BReader = new BufferedReader(isr);
//            BReader = new BufferedReader(new FileReader(fullPath+"/"+title+fileType));
            while ((temp = BReader.readLine()) != null){
                content = content + temp;
//                Log.i("IOTools -> ReadContent","内容："+temp);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (isr != null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (BReader != null){
                try {
                    BReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }


}
