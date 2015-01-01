/*
 * Copyright (c) 2015 SUN XIMENG (Nathaniel). All rights reserved.
 */

package app;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import com.google.common.hash.HashFunction;
import com.google.common.io.Files;

public class Util {

	public static List<File> allFiles(File root) {
		List<File> all = new Vector<File>();
		allFiles(root, all);
		return all;
	}

	private static void allFiles(File root, List<File> files) {
		if (root.isDirectory()) {
			for (File f : root.listFiles()) {
				allFiles(f, files);
			}
		} else {
			files.add(root);
		}
	}

	public static String getHashString(File f, HashFunction hf)
			throws IOException {
		return Files.hash(f, hf).toString();
	}

	public static String listToStringBlock(List<String> l) {
		if (l.size() == 0) {
			return "";
		}
		int size = 0;
		for (String s : l) {
			size += s.length() + 1;
		}
		StringBuilder sb = new StringBuilder(size);
		for (String s : l) {
			sb.append(s);
			sb.append('\n');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private Util() {

	}

}
