package com.jec.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZipUtils {

	static final int BUFFER = 2048;
	private String tempDir;
	private String storePath;
	public static String BASE = "tmp";
	public static String DATA="DATA";
	static final Logger LOG = LoggerFactory.getLogger(ZipUtils.class);

	public ZipUtils(String tempDir,String storePath) {
		super();
		this.tempDir = tempDir;
		this.storePath=storePath;
	}

	public static ZipUtils builder(String zipFileName) {
		String tmpPath = ZipUtils.BASE + "/" + zipFileName
				+ UUID.randomUUID().toString();
		String storePath=ZipUtils.DATA + "/" + zipFileName
				+ UUID.randomUUID().toString();
		File file = new File(tmpPath);
		file.mkdirs();
		File store=new File(storePath);
		store.mkdirs();
		LOG.info("Temp directory:" + tmpPath);
		return new ZipUtils(tmpPath,storePath);
	}
	
	
	public List<String> unzip(String source,boolean check) throws Exception {
		List<String> entries = new ArrayList<String>();
		String parentPath= check ? this.tempDir:this.storePath;
		try {
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			@SuppressWarnings("resource")
			ZipFile zipfile = new ZipFile(source);
			@SuppressWarnings("rawtypes")
			Enumeration e = zipfile.entries();
			while (e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				LOG.info("Extracting: " + entry);
				entries.add(entry.getName());
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];
				
				FileOutputStream fos = new FileOutputStream(parentPath+ "/"
						+ entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
				is.close();
			}

		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw e;
		}
		return entries;
	}

	public void dispose() {
		try {
			File file = new File(this.tempDir);
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {
			LOG.info(e.getMessage());
		}
	}
}
