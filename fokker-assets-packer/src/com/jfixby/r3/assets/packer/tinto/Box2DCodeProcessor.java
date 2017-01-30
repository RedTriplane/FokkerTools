
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileFilter;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class Box2DCodeProcessor {

	static final FileFilter filter = new FileFilter() {
		@Override
		public boolean fits (File child) {
			String name = child.getName().toLowerCase();
			return name.endsWith(".java");
		}
	};

	public static void main (String[] args) throws IOException {
		ScarabeiDesktop.deploy();

		String java_folder = "D:\\[DEV]\\[CODE]\\[WS-15]\\box2d-long";
		File input_folder = LocalFileSystem.newFile(java_folder);

		processFolder(input_folder);

	}

	private static void processFolder (File input_folder) throws IOException {
		L.d("", input_folder);
		FilesList java_files = input_folder.listDirectChildren();
		for (int i = 0; i < java_files.size(); i++) {
			File e = java_files.getElementAt(i);
			if (e.isFolder()) {
				processFolder(e);
			} else {
				if (filter.fits(e)) {
					processFile(e);
				}
			}
		}
	}

	private static void processFile (File e) throws IOException {
		L.d("", e);
		processFloatFile(e);

	}

	private static void processFloatFile (File e) throws IOException {
		String input = e.readToString();
		input = input.replaceAll("float", "double");
		input = input.replaceAll("Float", "Double");
		e.writeString(input);
	}

	private static void processDoubleFile (File e) throws IOException {
		String input = e.readToString();
		input = input.replaceAll("double", "long");
		input = input.replaceAll("Double", "Long");
		e.writeString(input);
	}

}
