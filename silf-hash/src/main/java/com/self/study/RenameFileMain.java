package com.self.study;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author qinhaohao
 * @Date 2020/7/7 4:30 下午
 **/
public class RenameFileMain {

    public static String[] disArray=new String[]{"/Users/qinhaohao/Documents/模块二/任务三"};
    public static int startIndex=368;
    public static void main(String[] args) {
        for (String dis : disArray) {
            renameDir(dis);
        }
    }

    public static void renameDir(String dir) {
        System.out.println("重命名文件夹：" + dir);
        File file = new File(dir);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            List<File> list = Arrays.asList(files);
            list.sort(Comparator.comparing(File::getName));
            for (File f : list) {
                String name = f.getName();
                System.out.println(name);
                String newName = new StringBuffer(name).replace(0, 5, startIndex + ".").toString();
                startIndex++;
                System.out.println(newName);
                String newFileName=dir+"/"+newName;
                File newFile = new File(newFileName);
                if (f.exists()&&f.isFile()){
                    f.renameTo(newFile);
                }
            }
        }
    }
}
