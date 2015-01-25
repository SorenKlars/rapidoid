package org.rapidoid.util;

/*
 * #%L
 * rapidoid-utils
 * %%
 * Copyright (C) 2014 - 2015 Nikolche Mihajlovski
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.List;

public class IO {

	public static URL resource(String filename) {
		return classLoader().getResource(filename);
	}

	public static ClassLoader classLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static File file(String filename) {
		File file = new File(filename);

		if (!file.exists()) {
			URL res = resource(filename);
			if (res != null) {
				return new File(res.getFile());
			}
		}

		return file;
	}

	public static byte[] loadBytes(InputStream input) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		byte[] buffer = new byte[4 * 1024];

		try {
			int readN = 0;
			while ((readN = input.read(buffer)) != -1) {
				output.write(buffer, 0, readN);
			}
		} catch (IOException e) {
			throw U.rte(e);
		}

		return output.toByteArray();
	}

	public static byte[] loadBytes(String filename) {
		InputStream input = classLoader().getResourceAsStream(filename);

		if (input == null) {
			File file = new File(filename);

			if (file.exists()) {
				try {
					input = new FileInputStream(filename);
				} catch (FileNotFoundException e) {
					throw U.rte(e);
				}
			}
		}

		return input != null ? loadBytes(input) : null;
	}

	public static byte[] classBytes(String fullClassName) {
		return loadBytes(fullClassName.replace('.', '/') + ".class");
	}

	public static String load(String filename) {
		byte[] bytes = loadBytes(filename);
		return bytes != null ? new String(bytes) : null;
	}

	public static List<String> loadLines(String filename) {
		InputStream input = classLoader().getResourceAsStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		List<String> lines = U.list();

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			throw U.rte(e);
		}

		return lines;
	}

	public static List<String> loadLines(String filename, final boolean filterEmpty, final String commentPrefix) {

		List<String> lines = loadLines(filename);

		List<String> lines2 = U.list();

		for (String line : lines) {
			String s = line.trim();
			if ((!filterEmpty || !s.isEmpty()) && (commentPrefix == null || !s.startsWith(commentPrefix))) {
				lines2.add(s);
			}
		}

		return lines2;
	}

	public static void save(String filename, String content) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filename);
			out.write(content.getBytes());
			close(out, false);
		} catch (Exception e) {
			close(out, true);
			throw U.rte(e);
		}
	}

	public static void close(OutputStream out, boolean quiet) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			if (!quiet) {
				throw U.rte(e);
			}
		}
	}

	public static void close(InputStream in, boolean quiet) {
		try {
			in.close();
		} catch (IOException e) {
			if (!quiet) {
				throw U.rte(e);
			}
		}
	}

	public static void delete(String filename) {
		new File(filename).delete();
	}

	@SuppressWarnings("resource")
	public static MappedByteBuffer mmap(String filename, MapMode mode, long position, long size) {
		try {
			File file = new File(filename);
			FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
			return fc.map(mode, position, size);
		} catch (Exception e) {
			throw U.rte(e);
		}
	}

	public static MappedByteBuffer mmap(String filename, MapMode mode) {
		File file = new File(filename);
		U.must(file.exists());
		return mmap(filename, mode, 0, file.length());
	}

}