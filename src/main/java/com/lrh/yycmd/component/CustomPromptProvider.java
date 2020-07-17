package com.lrh.yycmd.component;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

/**
 * 自定义命令提示符
 *
 * @Author lrh 2020/7/17 16:57
 */
@Component
public class CustomPromptProvider implements PromptProvider {
    public static String promot = "Shell";
    @Override
    public AttributedString getPrompt() {
        // 设置命令提示符文字
        String promot_temp = promot + "> ";
        // 设置命令提示符字体样式
        AttributedStyle promotStyle = AttributedStyle.BOLD.foreground(AttributedStyle.GREEN);
        // 返回命令提示符
        return new AttributedString(promot_temp, promotStyle);
    }
}
