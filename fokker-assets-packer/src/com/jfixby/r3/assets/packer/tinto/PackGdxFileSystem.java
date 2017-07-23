
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.jfixby.r3.fokker.io.assets.index.AssetsInfo;
import com.jfixby.r3.fokker.io.assets.index.GdxAssetsFileSystemPacker;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.gson.GoogleGson;

public class PackGdxFileSystem {

	public static void main (final String[] args) throws IOException {
		if (args != null) {
			ScarabeiDesktop.deploy();
			Json.installComponent(new GoogleGson());
		}

		final File input_folder = LocalFileSystem.newFile("D:\\[DATA]\\[RED-ASSETS]\\gdx-assets-prepared");
		final File output_folder = LocalFileSystem.newFile("D:\\[DATA]\\[RED-ASSETS]\\gdx-assets-packed");
		output_folder.makeFolder();
		output_folder.clearFolder();

		final AssetsInfo info = getCurrent();

		GdxAssetsFileSystemPacker.index(input_folder, output_folder);

		final String data = Json.serializeToString(info).toString();
		output_folder.child(AssetsInfo.FILE_NAME).writeString(data);
		// os.close();
		info.print();

	}

	private static AssetsInfo getCurrent () throws IOException {
		final File info_file = LocalFileSystem.ApplicationHome().child(AssetsInfo.FILE_NAME);
		AssetsInfo info = null;
		if (info_file.exists()) {
			try {
				final String data = info_file.readToString();
				info = Json.deserializeFromString(AssetsInfo.class, data);
			} catch (final IOException e) {
				e.printStackTrace();
				info = new AssetsInfo();
			}
		} else {
			info = new AssetsInfo();
		}

		info.next();
		final String data = Json.serializeToString(info).toString();
		info_file.writeString(data);
		return info;
	}
}
