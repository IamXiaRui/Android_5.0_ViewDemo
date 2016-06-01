package com.libcore.io;

import java.io.File;
import java.io.IOException;

public class IoUtils {

	/**
	 * Closes 'closeable', ignoring any checked exceptions. Does nothing if
	 * 'closeable' is null.
	 */
	public static void closeQuietly(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (RuntimeException rethrown) {
				throw rethrown;
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * Recursively delete everything in {@code dir}.
	 */
	// TODO: this should specify paths as Strings rather than as Files
	public static void deleteContents(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			throw new IOException("listFiles returned null: " + dir);
		}
		for (File file : files) {
			if (file.isDirectory()) {
				deleteContents(file);
			}
			if (!file.delete()) {
				throw new IOException("failed to delete file: " + file);
			}
		}
	}
}
