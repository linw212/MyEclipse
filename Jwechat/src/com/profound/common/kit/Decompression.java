package com.profound.common.kit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/** 
 * 类说明:压缩、解压文件公用类
 *
 */
public class Decompression {
private static final int BUFFEREDSIZE = 1024;
    
	/**
	 * 解压zip格式的压缩文件到指定位置
	 * @param zipFileName 压缩文件
	 * @param extPlace 解压目录
	 * @throws Exception
	 * @return 解压的文件数量(不包含文件夹)
	 */
	@SuppressWarnings("rawtypes")
	public synchronized static int unzip(String zipFileName, String extPlace,boolean focus) throws Exception {
		int num=0;
		ZipFile zipFile=null;
		try {
	    	(new File(extPlace)).mkdirs();
		    File f = new File(zipFileName);
		    zipFile = new ZipFile(zipFileName);
		    if((!f.exists()) && (f.length() <= 0)) {
		    	throw new Exception("要解压的文件不存在!");
		    }
		    String strPath, gbkPath, strtemp;
		    File tempFile = new File(extPlace);
		    strPath = tempFile.getAbsolutePath();
		    java.util.Enumeration e = zipFile.getEntries();
		    while(e.hasMoreElements()){
			    org.apache.tools.zip.ZipEntry zipEnt = (ZipEntry) e.nextElement();
			    gbkPath=zipEnt.getName();
			    if(zipEnt.isDirectory()){
			    	if(!focus)
			    	{
					    strtemp = strPath + File.separator + gbkPath;
					    File dir = new File(strtemp);
					    dir.mkdirs();
			    	}
				    continue;
			    } else {
				    //读写文件
				    InputStream is = zipFile.getInputStream(zipEnt);
				    BufferedInputStream bis = new BufferedInputStream(is);

				    if(focus&&gbkPath.lastIndexOf("/")!=-1)
				    {
				    	gbkPath=gbkPath.substring(gbkPath.lastIndexOf("/")+1);
				    	File tf=new File(strPath + File.separator + gbkPath);
				    	if(tf.exists())
				    		tf.delete();
				    }
				    strtemp = strPath + File.separator + gbkPath;
				    num++;
				    //建目录
				    String strsubdir = gbkPath;
				    for(int i = 0; i < strsubdir.length(); i++) {
					    if(strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
						    String temp = strPath + File.separator + strsubdir.substring(0, i);
						    File subdir = new File(temp);
						    if(!subdir.exists())
						    subdir.mkdir();
					    }
				    }
				    FileOutputStream fos = new FileOutputStream(strtemp);
				    BufferedOutputStream bos = new BufferedOutputStream(fos);
				    int c;
				    while((c = bis.read()) != -1) {
				    	bos.write((byte) c);
				    }
				    bis.close();
				    is.close();
				    bos.close();
				    fos.close();
			    }
		    }
	    } catch(Exception e) {
		    e.printStackTrace();
		    throw e;
	    }finally{
	    	zipFile.close();
	    }
		return num;
	}
	
	/**
	 * 压缩zip格式的压缩文件
	 * @param inputFilename 压缩的文件或文件夹及详细路径
	 * @param zipFilename 输出文件名称及详细路径
	 * @throws IOException
	 */
	public synchronized static void zip(String inputFilename, String zipFilename) throws IOException {
		zip(new File(inputFilename), zipFilename);
	}
	
	/**
	 * 压缩zip格式的压缩文件
	 * @param inputFile 需压缩文件
	 * @param zipFilename 输出文件及详细路径
	 * @throws IOException
	 */
	public synchronized static void zip(File inputFile, String zipFilename) throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
		try {
			zip(inputFile, out, "");
		} catch (IOException e) {
			throw e;
		} finally {
			out.close();
		}
	}
	
	/**
	 * 压缩zip格式的压缩文件
	 * @param inputFile 需压缩文件
	 * @param out 输出压缩文件
	 * @param base 结束标识
	 * @throws IOException
	 */
	private synchronized static void zip(File inputFile, ZipOutputStream out, String base) throws IOException {
		if (inputFile.isDirectory()) {
			File[] inputFiles = inputFile.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < inputFiles.length; i++) {
				zip(inputFiles[i], out, base + inputFiles[i].getName());
			}
		} else {
			if (base.length() > 0) {
				out.putNextEntry(new ZipEntry(base));
			} else {
				out.putNextEntry(new ZipEntry(inputFile.getName()));
			}
			FileInputStream in = new FileInputStream(inputFile);
			try {
				int c;
				byte[] by = new byte[BUFFEREDSIZE];
				while ((c = in.read(by)) != -1) {
					out.write(by, 0, c);
				}
			} catch (IOException e) {
				throw e;
			} finally {
				in.close();
			}
		}
	}
}


