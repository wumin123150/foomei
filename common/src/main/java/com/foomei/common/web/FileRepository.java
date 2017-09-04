package com.foomei.common.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foomei.common.io.FileTreeWalker;
import com.foomei.common.io.FileUtil;
import com.foomei.common.io.WebFileUtil;


/**
 * 本地文件存储
 */
public class FileRepository {
	private Logger log = LoggerFactory.getLogger(FileRepository.class);

	public String storeByExt(byte[] data, String path, String ext) throws IOException {
		String filename = WebFileUtil.generateFilename(path, ext);
		File dest = retrieve(filename);
		dest = WebFileUtil.getUniqueFile(dest);
		store(data, dest);
		return filename;
	}

	public String storeByExt(BufferedImage image, String path, String ext) throws IOException {
		String filename = WebFileUtil.generateFilename(path, ext);
		File dest = retrieve(filename);
		dest = WebFileUtil.getUniqueFile(dest);
		store(image, dest, ext);
		return filename;
	}
	
	public String storeByPath(String file, String path) throws IOException {
		return storeByPath(retrieve(file), path);
	}

	public String storeByPath(File file, String path) throws IOException {
		String ext = FileUtil.getFileExtension(file.getName()).toLowerCase(Locale.ENGLISH);
		String filename = WebFileUtil.generateFilename(path, ext);
		File dest = retrieve(filename);
		dest = WebFileUtil.getUniqueFile(dest);
		store(file, dest);
		return filename;
	}

	public String storeByFilename(File file, String filename) throws IOException {
		File dest = retrieve(filename);
		store(file, dest);
		return filename;
	}
	
	public String storeByFilename(byte[] data, String filename) throws IOException {
		File dest = retrieve(filename);
		store(data, dest);
		return filename;
	}

	private void store(byte[] data, File dest) throws IOException {
		try {
		    FileUtil.makesureDirExists(dest.getParentFile());
			FileUtil.write(data, dest);
		} catch (IOException e) {
			log.error("Transfer file error when upload file", e);
			throw e;
		}
	}

	private void store(BufferedImage image, File dest, String format) throws IOException {
		try {
		    FileUtil.makesureDirExists(dest.getParentFile());
			ImageIO.write(image, format, dest);
		} catch (IOException e) {
			log.error("Save image error", e);
			throw e;
		}
	}

	private void store(File file, File dest) throws IOException {
		try {
		    FileUtil.makesureDirExists(dest.getParentFile());
		    FileUtil.copyFile(file, dest);
		} catch (IOException e) {
			log.error("Transfer file error when upload file", e);
			throw e;
		}
	}

	public void deleteByPath(String path) {
		File file = retrieve(path);
		FileUtil.deleteFile(file);
	}
	
	public void deleteRelevantByPath(String path) {
	    File file = retrieve(path);
	    Collection<File> files = FileTreeWalker.listFileWithWildcardFileName(file.getParentFile(), file.getName() + "*");
        for (File relevant : files) {
            FileUtil.deleteFile(relevant);
        }
    }

	public File retrieve(String name) {
		return new File(ThreadContext.getUploadPath() + name);
	}
}
