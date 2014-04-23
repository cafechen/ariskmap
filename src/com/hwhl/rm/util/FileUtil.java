package com.hwhl.rm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import android.os.Environment;

public class FileUtil {
	/**
	 * 删除文件夹
	 * 
	 * @param file
	 */
	public static void deleteFileDir(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					FileUtil.deleteFileDir(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
	}

	/**
	 * 从inStream拷贝到SD卡生成文件
	 * 
	 * @return
	 */
	public static boolean createFileByStream(InputStream inStream,
			String dstFileName) {
		try {
			createPathByFile(dstFileName);// 创建多级目录结构

			int len = 0;
			byte[] bytes = new byte[1024];
			FileOutputStream fos = new FileOutputStream(new File(dstFileName));
			while ((len = inStream.read(bytes, 0, 1024)) > 0) {
				fos.write(bytes, 0, len);
			}
			inStream.close();
			fos.close();
		} catch (FileNotFoundException e) {
			AppManager.showToastMessage("创建文件失败");
			return false;
		} catch (IOException e) {
			AppManager.showToastMessage("读写错误,请检查");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 读取文本文件
	 * 
	 * @param inputStream
	 *            　输入流
	 * @param charSet
	 *            字符级:gbk,utf-8等
	 * @return
	 */
	public static String getTxtStream(InputStream inputStream, String charSet) {
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, charSet);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				if (sb.length() > 0)
					sb.append("\n");
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String getTxtFile(String fileName, String charSet) {
		try {
			return getTxtStream(new FileInputStream(new File(fileName)),
					charSet);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 建立目录
	public static boolean createPath(String path) {
		File dstfile = new File(path);
		if (!dstfile.exists())
			dstfile.mkdirs();// 创建多级目录结构
		return true;
	}

	// 根据文件建立目录
	public static boolean createPathByFile(String fileName) {
		int start = fileName.lastIndexOf("/");
		if (start > 0) {
			String path = fileName.substring(0, start);
			createPath(path);
		}
		return true;
	}

	// 检查 sd卡文件是否存在
	public static boolean checkSDFileExits(String fileName, boolean sdName) {
		// 文件名没有带SD卡路径，加上
		if (!sdName) {
			fileName = Environment.getExternalStorageDirectory() + fileName;
		}
		File file = new File(fileName);

		return file.exists();
	}

	// 检查是否SDK准备好
	public static boolean checkSDExist() {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	// 获取SD卡的路径，如果SD卡没准备好返回null
	public static String getSDPath() {
		if (checkSDExist()) {
			return Environment.getExternalStorageDirectory().toString();// 获取跟目录
		} else {
			return null;
		}
	}

	/**
	 * 将SDK的文件写入到Stream
	 * 
	 * @return
	 */
	public static boolean readFileToStream(String srcFileName,
			OutputStream outStream) {
		try {
			int len = 0;
			byte[] bytes = new byte[1024];
			FileInputStream fis = new FileInputStream(new File(srcFileName));
			while ((len = fis.read(bytes, 0, 1024)) > 0) {
				outStream.write(bytes, 0, len);
			}
			outStream.flush();
			outStream.close();
			fis.close();
		} catch (FileNotFoundException e) {
			AppManager.showToastMessage("SD卡文件没有找到");
			return false;
		} catch (IOException e) {
			AppManager.showToastMessage("读写错误,请检查");
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	/**
     * 生产文件 如果文件所在路径不存在则生成路径
     * 
     * @param fileName
     *            文件名 带路径
     * @param isDirectory 是否为路径
     * @return
     * @author yayagepei
     * @date 2008-8-27
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    } 

}
