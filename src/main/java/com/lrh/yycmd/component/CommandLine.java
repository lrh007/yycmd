package com.lrh.yycmd.component;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Random;

/**
 * 自定义命令
 * @Author lrh 2020/7/17 16:57
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
            //修改提示符
            changePromot(CONTEXT_FILE.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改提示符
     * @Author lrh 2020/7/17 17:12
     */
    public static void changePromot(String promot){
        CustomPromptProvider.promot = promot;
    }

    /**
     * 设置全局变量并且改变命令提示符
     * @param promot
     */
    public static void setContextFileAndChangePromot(String promot){
        CustomPromptProvider.promot = promot;
        CONTEXT_FILE = new File(promot);
    }

    /**
     * 设置全局变量并且改变命令提示符
     * @param file
     */
    public static void setContextFileAndChangePromot(File file){
        CustomPromptProvider.promot = file.getAbsolutePath();
        CONTEXT_FILE = file;
    }
    /**
     * 查看当前目录下所有文件
     * @Author lrh 2020/7/17 15:25
     */
    @ShellMethod(value = "查看当前文件夹下所有文件.",key = {"ls","dir"})
    public String ls(@ShellOption(defaultValue = "") String path) throws IOException {
        File[] files = CONTEXT_FILE.listFiles();
        //如果传递了目录就使用传递的目录，否则就使用当前目录
        if(!"".equals(path)){
            File f = new File(path.trim());
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
            builder.append(files[i].getName());
            if(i < files.length-1){
                builder.append("\r\n");
            }
        }
        return builder.toString();
    }
    
    /**   
     * 进入指定文件夹
     * @Author lrh 2020/7/17 17:20
     */
    @ShellMethod("进入指定文件夹")
    public void cd(@ShellOption(defaultValue = "") String path){
        path = path.trim();
        if("".equals(path)){
            System.out.println("请输入文件夹！！！");
        }else{
            //返回上一级目录
            if ("..".equals(path) || "../".equals(path) || "..\\".equals(path)) {
                String parentPath = CONTEXT_FILE.getParent();
                if(parentPath != null){
                    //设置全局变量并且改变提示符
                    setContextFileAndChangePromot(parentPath);
                }
            }else{
                //进入下一级目录
                String nextPath = CONTEXT_FILE.getAbsolutePath()+"/"+path;
                File file = new File(nextPath);
                if(!file.isDirectory()){
                    System.out.println(path+" 不是一个文件夹，请重新输入！！！");
                    return;
                }
                //设置全局变量并且改变提示符
                setContextFileAndChangePromot(file);
            }
        }
    }

    /**
     * 文件复制
     * @param sourcePath
     * @param destPath
     */
    @ShellMethod(value = "复制文件.",key = {"cp","copy"})
    public void copy(@ShellOption(defaultValue = "") String sourcePath,@ShellOption(defaultValue = "") String destPath){
        sourcePath = sourcePath.trim();
        destPath = destPath.trim();
        if("".equals(sourcePath) && "".equals(destPath)){
            System.out.println("请输入源文件和目标文件！！！");
            return;
        }
        if("".equals(sourcePath)){
            System.out.println("请输入源文件！！！");
            return;
        }
        if("".equals(destPath)){
            System.out.println("请输入目标文件！！！");
            return;
        }
        File sourceFile = new File(sourcePath); //源文件
        File destFile = new File(destPath); //目标文件
        if(!sourceFile.exists()){
            System.out.println("源文件不存在，请重新输入！！！");
            return;
        }
        if(destFile.exists()){
            if(!destFile.isDirectory()){
                System.out.println("目标文件不是文件夹，请重新输入！！！");
                return;
            }
            //是文件夹,开始复制
            copyFile(sourceFile,destFile);

        }


    }

    /**
     * 复制文件
     * @param sourceFile
     * @param destFile
     */
    public void copyFile(File sourceFile,File destFile){

    }
}
