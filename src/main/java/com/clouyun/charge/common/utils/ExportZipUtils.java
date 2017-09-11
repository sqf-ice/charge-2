package com.clouyun.charge.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 描述: 压缩文件导出工具类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年8月4日 上午9:19:27
 */
public class ExportZipUtils {

    /**
     *
     * @param request
     * @param response
     * @param wb        excel文件
     * @throws IOException
     */
    public static void export(HttpServletRequest request, HttpServletResponse response, HSSFWorkbook wb) throws IOException {
        String path = "E:/excel/";
        String header = "二维码档案";
        //创建文件夹;
        createFile(path);
        //创建Excel文件;
        createExcelFile(path,header,wb);
        //生成.zip文件;
        craeteZipPath(path,header);
        //写出文件
        outPutZip(response, path, header);

        //删除目录下所有的文件;
        File file = new File(path);
        //删除文件;
        deleteExcelPath(file);
        //重新创建文件;
        file.mkdirs();

    }

    /**
     * 测试压缩速度用
     * @throws IOException
     */
    public static void export() throws IOException {
//        long begin = Calendar.getInstance().getTimeInMillis();
//        System.out.println("---------------------------");
//        System.out.println("-----------"+ begin +"--------------");
//        System.out.println("---------------------------");
        String path = "E:/excel/";
        String header = "二维码档案";
        //创建文件夹;
        createFile(path);
        //创建Excel文件;
//        createExcelFile(path,header,wb);
        //生成.zip文件;
        craeteZipPath(path,header);

//        long end = Calendar.getInstance().getTimeInMillis();
//        System.out.println("---------------------------");
//        System.out.println("-----------"+ end +"--------------");
//        System.out.println("---------------------------");
//        System.out.println("-----------"+ (end - begin) + "---------------");
        //写出文件
//        outPutZip(response, path, header);
//
//        //删除目录下所有的文件;
//        File file = new File(path);
//        //删除文件;
//        deleteExcelPath(file);
//        //重新创建文件;
//        file.mkdirs();

    }

    public static void main(String[] args) {
        try {
            export();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void outPutZip(HttpServletResponse response, String path, String header) throws IOException {
        OutputStream output = null;
        FileInputStream inStream = null;
        try {
            String title=header;
            response.reset();// 清空输出流
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    +new String(title.getBytes("GB2312"), "ISO_8859_1")
                    + ".zip");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            output = response.getOutputStream();
            File file = new File(path+header+".zip");
            inStream =  new FileInputStream(file);
            byte[] buf = new byte[4096];
            int readLength;
            while (((readLength = inStream.read(buf)) != -1)) {
                output.write(buf, 0, readLength);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(output != null){
                output.close();
            }
            if(inStream != null){
                inStream.close();
            }
        }
    }

    private static String createFile(String path){
        File file = new File(path);
        //判断文件是否存在;
        if(!file.exists()){
            //创建文件;
            boolean bol = file.mkdirs();
            if(bol){
                System.out.println(path+" 路径创建成功!");
            }else{
                System.out.println(path+" 路径创建失败!");
            }
        }else{
            System.out.println(path+" 文件已经存在!");
        }
        return path;
    }

    private static void createExcelFile(String path,String header,HSSFWorkbook wb) throws IOException {
        File file = new File(path+header+".xlsx");
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (out != null){
                out.close();
            }
        }
    }

    /**
     * 生成.zip文件;
     * @param path
     * @throws IOException
     */
    private static void craeteZipPath(String path,String header) throws IOException{
        File file = new File(path+header+".zip");
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        zipOutputStream.setLevel(6);
        File[] files = new File(path).listFiles();
        FileInputStream fileInputStream = null;
        byte[] buf = new byte[1024];
        int len = 0;
        try {
            if(files!=null && files.length > 0){
                for(File excelFile:files){
                    //反复写zip文件进压缩包中,内存要炸
                    if(excelFile.getName().contains("zip")){
                        continue;
                    }

                    String fileName = excelFile.getName();
                    fileInputStream = new FileInputStream(excelFile);
                    //放入压缩zip包中;
                    zipOutputStream.putNextEntry(new ZipEntry(path + "/"+fileName));

                    //读取文件;
                    while((len=fileInputStream.read(buf)) >0){
                        zipOutputStream.write(buf, 0, len);
                    }
                    //关闭;
                    zipOutputStream.closeEntry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fileInputStream != null){
                fileInputStream.close();
            }

            zipOutputStream.close();
        }
    }

    /**
     * 删除目录下所有的文件;
     */
    private static boolean deleteExcelPath(File file){
        String[] files = null;
        if(file != null){
            files = file.list();
            if(file.isDirectory()){
                for(int i =0;i<files.length;i++){
                    boolean bol = deleteExcelPath(new File(file,files[i]));
                    if(bol){
                        System.out.println("删除成功!");
                    }else{
                        System.out.println("删除失败!");
                    }
                }
            }
        }

        if(file != null){
            return file.delete();
        }else{
            return true;
        }
    }
}
