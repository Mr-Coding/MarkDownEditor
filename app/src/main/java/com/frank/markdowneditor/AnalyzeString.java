package com.frank.markdowneditor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class AnalyzeString{
    private Matcher m;
    private Pattern p;
    private StringBuilder builder;

    public AnalyzeString(String content){
        builder = Analyze(content,true);
    }

    public StringBuilder getBuilder(){
        return builder;
    }

    public StringBuilder Analyze(String content,boolean isFirstAnalyze){
        String[] contents = content.split("\n");
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i < contents.length;i ++) {
//            System.out.println(contents[i]);

            if (isFirstAnalyze == true) { //是不是第一次解析
//                System.out.println("--- Is First Analyze ---");
                if (contents[i].matches("^#{1,6}\\s[\\w|\\W]*")) { //必须在最前面的，标题语法
                    System.out.println("-- 标题 --"+i);
                    String c = Title(contents[i]);
                    builder.append(c);
                    continue;
                } else if (contents[i].matches("^[-]{3,}$")) {
                    System.out.println("-- 分割线 --"+i);
                    String c = Line(contents[i]);
                    builder.append(c);
                    continue;
                }else if (contents[i].matches("^[\\s ]*[\\r]*[\\n]*")) {
                    System.out.println("-- 换行 --"+i);
                    builder.append("<br/>");
                    continue;
                }else if(contents[i].matches("^(```).*")){
                    System.out.println("-- 多行代码框 --"+i);
                    String[] results = CodeBox(contents,i);
                    i = Integer.parseInt(results[1]);
                    builder.append(results[0]);
                    continue;
                }else if (contents[i].matches(".*[ |\\s]{2}$")) { //必须在一行的最后面！
                    System.out.println("-- 回车 --"+i);
                    String c = NewLine(contents[i]);
                    builder.append(c);
                    continue;
                }
            } else {
//                System.out.println("--- Not First Analyze ---");
            }

            if (contents[i].matches(".*!\\[.*?\\]\\(.*?\\).*")) {
                System.out.println("-- 图片 --"+i);
                String c = Image(contents[i]);
                builder.append(c);
            } else if (contents[i].matches("(.*)(\\*\\*)(.*)(\\*\\*)(.*)")) {
                System.out.println("-- 粗体 --"+i);
                String c = Bold(contents[i]);
                builder.append(c);
            } else if (contents[i].matches("(.*)(_)(.*)(_)(.*)")) {
                System.out.println("-- 斜体 --"+i);
                String c = Italic(contents[i]);
                builder.append(c);
            } else if (contents[i].matches(".*\\[.*\\]\\(.*\\).*")) {
                System.out.println("-- 链接 --"+i);
                String c = Link(contents[i]);
                builder.append(c);
            }else if(contents[i].matches("(.*?)(```)(.*?)(```)(.*)")){
                System.out.println("-- 单行代码框 --"+i);
                String c = SLCodeBox(contents[i]);
                builder.append(c);
            }else {
                builder.append(contents[i]);
            }
        }
        System.out.println(builder);
        return builder;
    }

    private String[] CodeBox(String[]contents,int index){
        if (contents[index].matches("(.*?)(```)(.*?)(```)(.*)")){
            return new String[]{SLCodeBox(contents[index])+"</br>",index+""};
        }
        StringBuilder builder = new StringBuilder();
        String flag = "";
        builder.append("<pre class=\"code-box\">");
        while(!flag.matches("^(```).*")){
            index++;
            if (index >= contents.length){
                break;
            }
            flag = contents[index];
            if (flag.matches("^(```).*")) {
                break;
            }
            builder.append(flag + "\n");
        }
        builder.append("</pre>");
        return new String[]{builder.toString(),index+""};
    }

    private String SLCodeBox(String content){
        String[] contents = new String[3];
        p = Pattern.compile("(.*?)(```)(.*?)(```)(.*)");
        m = p.matcher(content);
        while(m.find()){
            contents[0] = m.group(1);
            contents[1] = m.group(3);
            contents[2] = m.group(5);
        }
        return Analyze(contents[0],false)+
                "<code class=\"singleLine-code-box\">"+
                contents[1]+
                "</code>"+
                Analyze(contents[2],false);
    }

//    private void Ignore(String content){ }

    private String NewLine(String content){
        String[] contents = new String[2];
        p = Pattern.compile("(.*)([ |\\s]{2})");
        m = p.matcher(content);
        while (m.find()){
            contents[0] = m.group(1);
            contents[1] = m.group(2);
        }
        return Analyze(contents[0],false)+"<br/>";
    }

    private String Link(String content){
        String[] contents = new String[4];
        p = Pattern.compile("(.*)(\\[)(.*)(\\]\\()(.*)\\)(.*)");
        m = p.matcher(content);
        while(m.find()){
            contents[0] = m.group(1);//最左
            contents[1] = m.group(5);//链接
            contents[2] = m.group(3);//内容
            contents[3] = m.group(6);//最右
        }
        return Analyze(contents[0],false)+
                "<a class=\"Link\" href=\""+contents[1]+"\">"+contents[2]+"</a>"+
                Analyze(contents[3],false);
    }

    private String Image(String content){
        String[] contents = new String[4];
        p = Pattern.compile("(.*)!\\[(.*?)\\]\\((.*?)\\)(.*)");
        m = p.matcher(content);
        while(m.find()){
            contents[0] = m.group(1);
            contents[1] = m.group(2);
            contents[2] = m.group(3);
            contents[3] = m.group(4);
        }
        if (contents[0].equals("") && contents[3].equals("")){
            return Analyze(contents[0],false)+
                    "<img style=\"display:block;margin:0 auto\" class=\"Image\" src=\""+
                    contents[2]+
                    "\" alt=\""+
                    contents[1]+"\">"+
                    Analyze(contents[3],false);
        }else {
            return Analyze(contents[0], false) +
                    "<img align=\"bottom\" class=\"Image\" src=\"" +
                    contents[2] +
                    "\" alt=\"" +
                    contents[1] + "\">" +
                    Analyze(contents[3], false);
        }
    }

    private String Italic(String content){
        String[] contents = new String[3];
        p = Pattern.compile("(.*)(_)(.*)(_)(.*)");
        m = p.matcher(content);
        while(m.find()) {
            contents[0] = m.group(1);
            contents[1] = m.group(3);
            contents[2] = m.group(5);
        }
        return Analyze(contents[0],false)+
                "<i>"+Analyze(contents[1],false)+"</i>"+
                Analyze(contents[2],false);
    }

    private String Line(String content){
        return "<div class=\"line-style\"></div>";
    }

    private String Bold(String content){
        String[] contents = new String[3];
        p = Pattern.compile("(.*)\\*\\*(.*)\\*\\*(.*)");
        m = p.matcher(content);
        while(m.find()){
            contents[0] = m.group(1);
            contents[1] = m.group(2);
            contents[2] = m.group(3);

        }

        if (contents[0].matches(".*_.*") && contents[2].matches(".*_.*")){
            String s = Italic(content);
            m = p.matcher(s);
            while(m.find()){
                contents[0] = m.group(1);
                contents[1] = m.group(2);
                contents[2] = m.group(3);
            }
            return s;
        }
        return Analyze(contents[0],false)+
//                "<span class=\"Bold\">"+
                "<b>"+
                Analyze(contents[1],false)+
//                "</span>"+
                "</b>"+
                Analyze(contents[2],false);
    }

    private String Title(String content){
        String[] contents = new String[3];
        p = Pattern.compile("(^[#]{1,6}\\s)(.*)");
        m = p.matcher(content);
        while(m.find()){
            contents[0] = m.group(1);   contents[1] = m.group(2);
        }
        System.out.println("<><><><><><><>0:"+contents[0]+" 1:"+contents[1]);
        String c = Analyze(contents[1],false).toString();

        if (content.matches("[#]{1}\\s[\\w|\\W]*")) {
            contents[2] = "<h1>"+c+"</h1>";
        }else if (content.matches("#{2}\\s[\\w|\\W]*")){
            contents[2] = "<h2>"+c+"</h2>";
        }else if (content.matches("#{3}\\s[\\w|\\W]*")){
            contents[2] = "<h3>"+c+"</h3>";
        }else if (content.matches("#{4}\\s[\\w|\\W]*")){
            contents[2] = "<h4>"+c+"</h4>";
        }else if (content.matches("#{5}\\s[\\w|\\W]*")){
            contents[2] = "<h5>"+c+"</h5>";
        }else if (content.matches("#{6}\\s[\\w|\\W]*")){
            contents[2] = "<h6>"+c+"</h6>";
        }
        return contents[2];
    }

    public static String analyzeStr(String str){
        if (!str.matches(".*(\\.md$)")){
            System.out.println("不匹配...");
            return null;
        }
        Pattern pattern = Pattern.compile("(^.*)(.md$)");
        Matcher matcher = pattern.matcher(str);
        String s = "";
        while(matcher.find()) {
            s = matcher.group(1);
        }
        return s;
    }

}