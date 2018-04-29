package com.frank.markdowneditor;

import org.junit.Test;

public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        String content = "## Title2\n" +
                "# Title1";
        System.out.println("文字："+content);
        System.out.println("----------------------------------------------");
        AnalyzeString analyzeString = new AnalyzeString(content);
        System.out.println("结果："+analyzeString.getBuilder());
    }

}