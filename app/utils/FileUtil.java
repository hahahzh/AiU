package utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <p>CopyFile.java</p>
 * <p>Created on Apr 17, 2009, 4:33:43 PM</p>
 * <p>Copyright (c) 2007-2009. CUCKOO Workgroup, USTC, P.R.China</p>
 * @author Ren Jian
 */
public class FileUtil {

    public static boolean copy(File fileFrom, File fileTo) {
        try {
            FileInputStream in = new java.io.FileInputStream(fileFrom);
            FileOutputStream out = new FileOutputStream(fileTo);
            byte[] bt = new byte[1024];
            int count;
            while ((count = in.read(bt)) > 0) {
                out.write(bt, 0, count);
            }
            in.close();
            out.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * 循环删除文件及下面所有的子文件
     * @param file
     */
    public static void deleteFile(File file) {
        if(file.exists()){
            if(file.isDirectory()){
                for(File f:file.listFiles()){
                    deleteFile(f);
                }
                file.delete();
            }else{
                file.delete();
            }
        }
        
    }

    public static File getSystemTempFolder() throws IOException{
        File tempFile = File.createTempFile("temp","");
        File parent = tempFile.getParentFile();
        tempFile.delete();
        return parent;
    }

    public static String getFileExt(File  file){
        int index = file.getName().indexOf(".");
        if(index>0){
            return file.getName().substring(index+1,file.getName().length());
        }else{
            return "";
        }
    }
}
