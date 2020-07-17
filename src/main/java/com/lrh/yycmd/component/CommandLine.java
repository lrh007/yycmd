package com.lrh.yycmd.component;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @Author lrh 2020/7/17 11:01
 */
@ShellComponent
public class CommandLine {
    /**   
     * 全局文件
     * @Author lrh 2020/7/17 15:24
     */
    private static File CONTEXT_FILE = null;

    /**
     * 初始化确定当前class文件的所在目录
     * @Author lrh 2020/7/17 15:21
     */
    static {
        Enumeration<URL> resources = null;
        try {
            resources = Thread.currentThread().getContextClassLoader().getResources("");
            while(resources.hasMoreElements()){
                String file1 = resources.nextElement().getFile();
                if(file1!=null && !"".equals(file1)){
                    CONTEXT_FILE = new File(file1);
                    break;
                }
            }
            assert CONTEXT_FILE != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ShellMethod(value = "Add Tow Integer numbers.", key = "sum")
    public int add(int a, int b) {
        return a + b;
    }

    @ShellMethod(value = "Display stuff.",prefix = "-")
    public String echo(int a,int b,@ShellOption("--third") int c){
        return String.format("You said a=%d, b=%d, c=%d", a, b, c);
    }

    @ShellMethod("Say hello.")
    public String greet(@ShellOption(defaultValue = "world") String who){
        return "hello "+who;
    }
    /**   
     * 查看当前目录下所有文件
     * @Author lrh 2020/7/17 15:25
     */
    @ShellMethod(value = "Output File path.",key = {"ls","dir"})
    public String ls(@ShellOption(defaultValue = "") String path) throws IOException {
        File[] files = CONTEXT_FILE.listFiles();
        //如果传递了目录就使用传递的目录，否则就使用当前目录
        if(!"".equals(path)){
            File f = new File(path);
            if(f.exists()){
                if(!f.isDirectory()){
                    System.out.println("路径不是文件夹，请重新输入！！！");
                    return null;
                }
                files = f.listFiles();
            }else{
                System.out.println("文件目录不存在！！！");
                return null;
            }
        }

        StringBuilder builder = new StringBuilder();
        assert files != null;
        for (int i = 0; i < files.length; i++) {
            builder.append(files[i].getAbsolutePath());
            if(i < files.length-1){
                builder.append("\r\n");
            }
        }
        return builder.toString();
    }

}
