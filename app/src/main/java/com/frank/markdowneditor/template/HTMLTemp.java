package com.frank.markdowneditor.template;

/**
 * Created by Frank on 2017/4/25.
 */

public class HTMLTemp {
    public static final String START = "<html>\n" +
            "    <head>\n" +
            "        <title>Test</title>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "        <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
            "        <style>\n" +
            "*{ padding: 0px; margin:  0px; font-family: \"微软雅黑\"; word-break:break-all; }\n" +
            "html,body{ width: 100%; height: 100%;}\n" +
            "*{box-sizing: border-box;}\n" +
            "pre{\n" +
            "                    white-space: pre-wrap;       /* css-3 */ \n" +
            "                    white-space: -moz-pre-wrap;  /* Mozilla, since 1999 */ \n" +
            "                    white-space: -pre-wrap;      /* Opera 4-6 */ \n" +
            "                    white-space: -o-pre-wrap;    /* Opera 7 */ \n" +
            "                    word-wrap: break-word; \n" +
            "}"+
            "#MD-Container div{ margin: 12px 0px; }\n" +
            "#MD-Container{  \n" +
            "      width: 99%;\n" +
            "      height: 100%;\n" +
            "      margin: 0 auto;\n" +
            "      padding: 15px 12px 10px 12px;\n" +
//            "      border: 1px solid rgb(221,221,221);\n" +
            "}\n" +
            ".Code-Container{\n" +
            "      margin: 12px 0px;\n" +
            "      padding: 10px 20px 10px 20px;\n" +
            "      font-size: 0.8em;\n" +
            "      font-family: \"微软雅黑\";\n" +
            "      color: rgb(147,147,147);\n" +
            "      background-color: rgb(248,248,248);\n" +
            "      border: 1px solid rgb(221,221,221);\n" +
            "      border-radius: 5px;\n" +
            "}\n" +
            "/*块级代码框*/\n" +
            ".code-box{\n" +
            "      margin: 12px 5px;\n" +
            "      display: block;\n" +
            "      padding: 10px 20px 10px 20px;\n" +
            "      font-size: 1em;\n" +
            "      font-family: Consolas;\n" +
            "      color: #717173;\n" +
            "      /*color: rgb(147,147,147);*/\n" +
            "      background-color: rgb(248,248,248);\n" +
            "      border: 1px solid rgb(221,221,221);\n" +
            "      border-radius: 5px;\n" +
            "                    white-space: pre-wrap;       /* css-3 */ \n" +
            "                    white-space: -moz-pre-wrap;  /* Mozilla, since 1999 */ \n" +
            "                    white-space: -pre-wrap;      /* Opera 4-6 */ \n" +
            "                    white-space: -o-pre-wrap;    /* Opera 7 */ \n" +
            "                    word-wrap: break-word; \n"+
            "}\n" +
            "/*单行代码框*/\n" +
            ".singleLine-code-box{\n" +
            "      display: inline-block;\n" +
            "      margin: 0px 5px;\n" +
            "      /*padding: 10px 20px 10px 20px;*/\n" +
            "      padding: 0px 3px 0px 3px;\n" +
            "      font-size: 0.8em;\n" +
            "      font-family: \"微软雅黑\";\n" +
            "      color: #808080;\n" +
            "      background-color: rgb(248,248,248);\n" +
            "      border: 1px solid rgb(221,221,221);\n" +
            "      border-radius: 3px;\n" +
            "}\n" +
            "            /*分割线*/\n" +
            ".line-style{\n" +
            "      width: 100%;\n" +
            "      height: 5px;\n" +
            "      background-color: #ccc;\n" +
            "      border-radius: 50px;\n" +
            "}\n" +
            "            /*链接*/\n" +
            ".Link{\n" +
            "      text-decoration: none;\n" +
            "}\n" +
            "            /*图片*/\n" +
            ".Image{\n" +
            "      max-width: 100%;\n" +
            "      box-sizing: content-box;\n" +
            "}\n" +
            "            /*引用*/\n" +
            ".Cite{\n" +
            "      border-left: #ccc 5px solid;\n" +
            "      padding: 5px 10px 5px 10px;\n" +
            "      margin-left: 5px;\n" +
            "}\n" +
            "            /*粗体*/\n" +
            ".Bold{      font-weight:bold;       }\n" +
            "\n" +
            "h1,h2,h3,h4,h5,h6{\n" +
            "      margin: 5px 0px;\n" +
            "}\n" +
            "\n" +
            "span{\n" +
            "      display: inline-block;\n" +
            "      margin: 2px 0px;   \n" +
            "      color: #808080;         \n" +
            "}\n" +
            "        </style>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <div id=\"MD-Container\">";

    public static String setTitle(String title){
        if (title.equals("")){
            return "";
        }
        return "<h2 style=\"width:100%;text-align:center\">"+(title.equals("")?"无标题":title)+"</h2><br/>";
    }

    public static final String END =
            "        </div>\n" +
            "    </body>\n" +
            "</html>";
}
