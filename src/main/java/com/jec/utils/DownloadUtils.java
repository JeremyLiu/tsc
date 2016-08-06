package com.jec.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.System.out;

/**
 * Created by jeremyliu on 6/25/16.
 */
public class DownloadUtils {

    static final Logger LOG = LoggerFactory.getLogger(DownloadUtils.class);

    public static void download(HttpServletResponse response, String filename, long length, File file) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
        // 设置输出长度
        response.setHeader("Content-Length", String.valueOf(length));
        // 获取输入流
        bis = new BufferedInputStream(new FileInputStream(file));
        // 输出流
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        // 关闭流
        bis.close();
        bos.close();
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, String filename, long length, File file) throws IOException {
        AtomicLong start = new AtomicLong(-1);
        AtomicLong end = new AtomicLong(-1);
        if (getRange(request, start, end)) {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            // 分析输出范围
            if (end.longValue() > length) {
                end.set(length);
                LOG.warn("断点续传：请求长度过长");
            }
            long fromLength = start.longValue();
            long toLength = (end.longValue() < 0) ? length : end.longValue();

            // 设置输出长度
            response.setHeader("Content-Range", "bytes " + fromLength + "-" + (toLength - 1) + "/" + length);
            response.setHeader("Content-Length", String.valueOf(toLength - fromLength));

            try {
                // 获取输入流
                bis = new BufferedInputStream(new FileInputStream(file));
                bis.skip(fromLength);
                // 输出流
                bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];
                long bytesToRead = toLength - fromLength;
                int bytesRead = 0;
                while (bytesToRead > 0 && -1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    if (bytesToRead < bytesRead)
                        bytesRead = (int) bytesToRead;
                    bos.write(buff, 0, bytesRead);
                    bytesToRead -= bytesRead;
                }

                // 关闭流
                bis.close();
                bos.close();

                if (bytesToRead > 0) {
                    throw new Exception("断点续传：未能完全读取文件");
                }

            } catch (SocketException e) {
                LOG.debug("断点续传：切断");
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        } else {
            download(response, filename, length, file);
        }
    }

    public static void download(HttpServletResponse response, String filename, long length, File file, String imei) throws IOException {
        byte[] _imei = imei.getBytes();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
        // 设置输出长度
        response.setHeader("Content-Length", String.valueOf(length + _imei.length));
        // 获取输入流
        bis = new BufferedInputStream(new FileInputStream(file));
        // 输出流
        bos = new BufferedOutputStream(response.getOutputStream());
        bos.write(_imei, 0, _imei.length);
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        // 关闭流
        bis.close();
        bos.close();
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, String filename, long length, File file, String imei) throws IOException {
        AtomicLong start = new AtomicLong(-1);
        AtomicLong end = new AtomicLong(-1);
        if (getRange(request, start, end)) {
            byte[] _imei = imei.getBytes();

            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            // 分析输出范围
            if (end.longValue() > length + _imei.length) {
                end.set(length + _imei.length);
                LOG.warn("断点续传：请求长度过长");
            }
            long fromLength = start.longValue();
            long toLength = (end.longValue() < 0) ? (length + _imei.length) : end.longValue();

            // 设置输出长度
            response.setHeader("Content-Range", "bytes " + fromLength + "-" + (toLength - 1) + "/" + (length + _imei.length));
            response.setHeader("Content-Length", String.valueOf(toLength - fromLength));

            try {
                // 获取输入流
                bis = new BufferedInputStream(new FileInputStream(file));
                bis.skip(fromLength);
                // 输出流
                bos = new BufferedOutputStream(response.getOutputStream());
                if (fromLength == 0)
                    bos.write(_imei, 0, _imei.length);

                byte[] buff = new byte[2048];
                long bytesToRead = toLength - fromLength;
                int bytesRead = 0;
                while (bytesToRead > 0 && -1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    if (bytesToRead < bytesRead)
                        bytesRead = (int) bytesToRead;
                    bos.write(buff, 0, bytesRead);
                    bytesToRead -= bytesRead;
                }

                // 关闭流
                bis.close();
                bos.close();

                if (bytesToRead > 0) {
                    throw new Exception("断点续传：未能完全读取文件");
                }

            } catch (SocketException e) {
                LOG.debug("断点续传：切断");
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        } else {
            download(response, filename, length, file, imei);
        }
    }

    private static boolean getRange(HttpServletRequest request, AtomicLong start, AtomicLong end) {
        String range = request.getHeader("Range");
        if (range == null)
            return false;
        range = range.substring(range.indexOf('=') + 1);
        String startStr = (range.indexOf('-') < 0) ? range : range.substring(0, range.indexOf('-'));
        String endStr = (range.indexOf('-') < 0 || range.indexOf('-') == range.length() - 1) ? null : range.substring(range.indexOf('-') + 1);
        try {
            if(!startStr.equals(""))
                start.set(Long.parseLong((startStr)));
            if (endStr != null)
                end.set(Long.parseLong(endStr));
            return true;
        } catch (Exception e) {
            LOG.error("断点续传：格式错误");
            return false;
        }
    }

    public static void downloadMp3(HttpServletRequest request, HttpServletResponse response, String[] files){
        if(files.length == 0)
            return;
        AtomicLong start = new AtomicLong(0);
        AtomicLong end = new AtomicLong(-1);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType("audio/mpeg");
        getRange(request,start,end);

        List<File> readFiles = new ArrayList<>();
        long count = 0;
        long firstFileStart = -1;
        long lastFileEnd = -1;
        for(String filename: files){
            File file = new File(filename);
            if(file.exists()){
                long fileLength = file.length();
                count += fileLength;
                if(count>start.longValue()){
                    if(end.longValue() > 0 && count> end.longValue()) {
                        if(lastFileEnd == -1){
                            readFiles.add(file);
                            if(end.longValue()>0)
                                lastFileEnd = end.longValue() - count + fileLength;
                        }
                    }else {
                        readFiles.add(file);
                        if (firstFileStart == -1)
                            firstFileStart = start.longValue() - count + fileLength;
                    }
                }
            }
        }
        if(end.longValue() < 0)
            end.set(count);
        response.setHeader("Content-Range", "bytes " + start.longValue() + "-" + (end.longValue() - 1) + "/" + count);
        response.setHeader("Content-Length", String.valueOf(end.longValue() - start.longValue()));
        int readFileLength = readFiles.size();
        if(readFileLength == 0)
            return;
        try{
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());

            //读取第一个文件
            readFile(bos, readFiles.get(0), firstFileStart, readFileLength == 1?lastFileEnd: -1);

            for(int i=1 ;i< readFileLength-1;i++)
                readFile(bos,readFiles.get(i),0,-1);

            //读取最后一个文件
            if(readFileLength>1)
                readFile(bos, readFiles.get(readFileLength-1),0,lastFileEnd);

            bos.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void readFile(OutputStream out, File file, long start, long end) throws IOException{
        if(start < 0)
            start = 0;
        if(end < 0)
            end = file.length();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.skip(start);

        byte[] buff = new byte[2048];
        long bytesToRead =  end- start;
        int bytesRead = 0;
        while (bytesToRead > 0 && -1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            if (bytesToRead < bytesRead)
                bytesRead = (int) bytesToRead;
            out.write(buff, 0, bytesRead);
            bytesToRead -= bytesRead;
        }
        bis.close();
    }
}

