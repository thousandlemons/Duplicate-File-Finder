/*
 * Copyright (c) 2015 SUN XIMENG (Nathaniel). All rights reserved.
 */

package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Analyzer {
	public static Analyzer getMD5Analyzer(File root) {
		return new Analyzer(Hashing.md5(), root);
	}

	private File root;

	private HashFunction hashFunc;

	private HashMap<String, String> map = new HashMap<String, String>();

	private Map<String, Set<String>> duplicates = new HashMap<String, Set<String>>();

	public Analyzer(HashFunction hf, File f) {
		this.hashFunc = hf;
		this.root = f;
	}

	public String findDuplicates(Main.Updater updater) throws IOException,
			FileNotFoundException, NullPointerException {

		updater.setPreparing();
		List<File> all = Util.allFiles(this.root);
		Util.getHashString(all.get(0), this.hashFunc);

		updater.setTotal(all.size());
		updater.setScanning();
		for (File f : all) {

			updater.setCurrent(f.getAbsolutePath());
			try {
				String hash = Util.getHashString(f, this.hashFunc);
				String path = f.getAbsolutePath();
				if (!map.containsKey(hash)) {
					map.put(hash, path);
				} else if (!this.duplicates.containsKey(hash)) {
					HashSet<String> set = new HashSet<String>();
					set.add(path);
					this.duplicates.put(hash, set);
				} else {
					this.duplicates.get(hash).add(path);
				}
			} catch (FileNotFoundException e) {

			}

		}

		for (String h : this.duplicates.keySet()) {
			this.duplicates.get(h).add(this.map.get(h));
		}

		updater.setFinished();
		return Util.listToStringBlock(this.toStringList());
	}

	private List<String> toStringList() {
		List<String> ls = new Vector<String>();
		for (String h : this.duplicates.keySet()) {
			ls.add(h);
			ls.addAll(this.duplicates.get(h));
			ls.add("\n");
		}
		return ls;
	}

}
