
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileFilter;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.tools.bleed.api.TextureBleed;
import com.jfixby.tools.bleed.api.TextureBleedSpecs;

public class GemserkFolder {

	public static void main (String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		String java_folder = TintoAssetsConfig.TINTO_REMOTE_ASSETS_HOME
			+ "\\bank-florida\\com.jfixby.tinto.scene-001.psd.children.psd.raster.gdx-atlas\\content";
		File input_folder = LocalFileSystem.newFile(java_folder);
		FileFilter filter = new FileFilter() {
			@Override
			public boolean fits (File child) {
				String name = child.getName().toLowerCase();
				return name.endsWith(".png");
			}
		};
		FilesList psd_files = input_folder.listDirectChildren().filter(filter);
		psd_files.print("processing");

		TextureBleedSpecs specs = TextureBleed.newSpecs();

		specs.setInputFolder(input_folder);

		TextureBleed.process(specs);

	}
}
