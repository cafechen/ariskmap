package com.hwhl.rm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.util.Log;

/**
 * Android Zip压缩解压缩
 * 
 * @author Ren.xia
 * @version 1.0
 * @updated 26-七月-2010 13:04:27
 */
public class ZipUtil {
	private static final int BUFFER_LENGTH = 4096;

	/**
	 * 数据压缩
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void compress(InputStream is, OutputStream os,
			String entryName) throws Exception {
		// 压缩输入
		ZipOutputStream zos = new ZipOutputStream(os);
		zos.setLevel(8);

		zos.putNextEntry(new ZipEntry(entryName));

		int len = 0;
		byte data[] = new byte[BUFFER_LENGTH];
		while ((len = is.read(data, 0, BUFFER_LENGTH)) != -1) {
			zos.write(data, 0, len);
		}

		// zos.flush();
		zos.closeEntry();
		zos.finish();
		zos.close();
	}

	/**
	 * 数据解压缩
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void decompress(InputStream is, OutputStream os,
			String entryName) throws Exception {
		ZipInputStream zis = new ZipInputStream(is);

		// 找到压缩点
		ZipEntry zipEntry = null;
		while ((zipEntry = zis.getNextEntry()) != null) {
			if (entryName.equalsIgnoreCase(zipEntry.getName()))
				break;
		}

		if (zipEntry != null) {
			int len = 0;
			byte data[] = new byte[BUFFER_LENGTH];
			while ((len = zis.read(data, 0, BUFFER_LENGTH)) != -1) {
				os.write(data, 0, len);
			}
		} else {
			Log.v("ZipUtil", "not found the zip entry in the zip file");
		}
		zis.close();
	}

	/**
	 * 数据压缩
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] byteCompress(byte[] data, String entryName) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// 压缩
		try {
			compress(bais, baos, entryName);
			byte[] output = baos.toByteArray();

			baos.flush();
			baos.close();

			bais.close();
			return output;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 数据解压缩
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] byteDecompress(byte[] data, String entryName) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// 解压缩

		try {
			decompress(bais, baos, entryName);
			byte[] output = baos.toByteArray();

			baos.flush();
			baos.close();

			bais.close();

			return output;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 取得压缩包中的 文件列表(文件夹,文件自选)
	 * 
	 * @param zipFileString
	 *            压缩包名字
	 * @param bContainFolder
	 *            是否包括 文件夹
	 * @param bContainFile
	 *            是否包括 文件
	 * @return
	 * @throws Exception
	 */
	public static java.util.List<File> GetFileList(String zipFileString,
			boolean bContainFolder, boolean bContainFile) throws Exception {

		android.util.Log.v("XZip", "GetFileList(String)");

		java.util.List<File> fileList = new java.util.ArrayList<File>();
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(
				zipFileString));
		ZipEntry zipEntry;
		String szName = "";

		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();

			if (zipEntry.isDirectory()) {

				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(szName);
				if (bContainFolder) {
					fileList.add(folder);
				}

			} else {
				File file = new File(szName);
				if (bContainFile) {
					fileList.add(file);
				}
			}
		}// end of while

		inZip.close();

		return fileList;
	}

	/**
	 * 返回压缩包中的文件InputStream
	 * 
	 * @param zipFileString
	 *            压缩文件的名字
	 * @param fileString
	 *            解压文件的名字
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream UpZip(String zipFileString, String fileString)
			throws Exception {
		android.util.Log.v("XZip", "UpZip(String, String)");
		ZipFile zipFile = new ZipFile(zipFileString);
		ZipEntry zipEntry = zipFile.getEntry(fileString);

		return zipFile.getInputStream(zipEntry);

	}

	/**
	 * 解压一个压缩文档 到指定位置
	 * 
	 * @param zipFileString
	 *            压缩包的名字
	 * @param outPathString
	 *            指定的路径
	 * @throws Exception
	 */
	public static void UnZipFolder(String zipFileString, String outPathString)
			throws Exception {
		android.util.Log.v("XZip", "UnZipFolder(String, String)");
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(
				zipFileString));
		ZipEntry zipEntry;
		String szName = "";

		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();

			if (zipEntry.isDirectory()) {

				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(outPathString + szName);
				folder.mkdirs();

			} else {

				File file = new File(outPathString  + szName);
				file.createNewFile();
				// get the output stream of the file
				FileOutputStream out = new FileOutputStream(file);
				int len = 0;
				byte[] buffer = new byte[1024];
				// read (len) bytes into buffer
				while ((len = inZip.read(buffer)) != -1) {
					// write (len) byte from buffer at the position 0
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}// end of while

		inZip.close();

	}// end of func

	/**
	 * 压缩文件,文件夹
	 * 
	 * @param srcFileString
	 *            要压缩的文件/文件夹名字
	 * @param zipFileString
	 *            指定压缩的目的和名字
	 * @throws Exception
	 */
	public static void ZipFolder(String srcFileString, String zipFileString)
			throws Exception {
		ZipFolder(srcFileString, zipFileString, null);
	}

	/**
	 * 压缩文件,文件夹
	 * 
	 * @param srcFileString
	 *            要压缩的文件/文件夹名字
	 * @param zipFileString
	 *            指定压缩的目的和名字
	 * @param filters
	 *            过滤掉不要压缩的文件集合 indexOf
	 * @throws Exception
	 */
	public static void ZipFolder(String srcFileString, String zipFileString,
			String[] filters) throws Exception {
		android.util.Log.v("XZip", "ZipFolder(String, String)");

		// 创建Zip包
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(
				zipFileString));

		// 打开要输出的文件
		File file = new File(srcFileString);

		// 压缩
		ZipFiles(file.getParent() + File.separator, file.getName(), outZip,
				filters);

		// 完成,关闭
		outZip.finish();
		outZip.close();

	}// end of func

	/**
	 * 压缩文件,文件夹
	 * 
	 * @param srcFileString
	 *            要压缩的文件/文件夹名字
	 * @param zipFileString
	 *            指定压缩的目的和名字
	 * @throws Exception
	 */
	public static void ZipFolder(String[] srcFileString, String zipFileString)
			throws Exception {
		ZipFolder(srcFileString, zipFileString, null);
	}

	/**
	 * 压缩文件,文件夹
	 * 
	 * @param srcFileString
	 *            要压缩的文件/文件夹名字
	 * @param zipFileString
	 *            指定压缩的目的和名字
	 * @param filters
	 *            过滤掉不要压缩的文件集合 indexOf
	 * @throws Exception
	 */
	public static void ZipFolder(String[] srcFileString, String zipFileString,
			String[] filters) throws Exception {
		android.util.Log.v("XZip", "ZipFolder(String, String)");

		// 创建Zip包
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(
				zipFileString));
		for (String srcFile : srcFileString) {
			// 打开要输出的文件
			File file = new File(srcFile);

			// 压缩
			ZipFiles(file.getParent() + File.separator, file.getName(), outZip,
					filters);
		}
		// 完成,关闭
		outZip.finish();
		outZip.close();

	}// end of func

	/**
	 * 压缩文件
	 * 
	 * @param folderString
	 * @param fileString
	 * @param zipOutputSteam
	 * @param filters
	 *            不进行压缩的文件
	 * @throws Exception
	 */
	private static void ZipFiles(String folderString, String fileString,
			ZipOutputStream zipOutputSteam, String[] filters) throws Exception {
		android.util.Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)"
				+ fileString);

		if (zipOutputSteam == null)
			return;

		File file = new File(folderString + fileString);

		// 判断是不是文件
		if (file.isFile()) {
			String zipSrcName = file.getName();
			// 过滤掉不要压缩的文件名
			if (filters != null) {
				for (String filter : filters) {
					if (zipSrcName.indexOf(filter) != -1) {
						return;
					}
				}
			}
			ZipEntry zipEntry = new ZipEntry(fileString);
			FileInputStream inputStream = new FileInputStream(file);
			zipOutputSteam.putNextEntry(zipEntry);

			int len;
			byte[] buffer = new byte[4096];

			while ((len = inputStream.read(buffer)) != -1) {
				zipOutputSteam.write(buffer, 0, len);
			}

			zipOutputSteam.closeEntry();
		} else {

			// 文件夹的方式,获取文件夹下的子文件
			String fileList[] = file.list();

			// 如果没有子文件, 则添加进去即可
			if (fileList.length <= 0) {
				ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
				zipOutputSteam.putNextEntry(zipEntry);
				zipOutputSteam.closeEntry();
			}

			// 如果有子文件, 遍历子文件
			for (int i = 0; i < fileList.length; i++) {
				ZipFiles(folderString, fileString + File.separator
						+ fileList[i], zipOutputSteam, filters);
			}// end of for

		}// end of if

	}// end of func

	public void finalize() throws Throwable {

	}

}