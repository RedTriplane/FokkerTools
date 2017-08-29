
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileFilter;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.names.Names;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.tool.box2d.packer.Box2DShapesPacker;
import com.jfixby.tool.box2d.packer.Box2DShapesPackerSettings;
import com.jfixby.tool.box2d.packer.Box2DShapesPackingStatus;

public class Box2DPacker {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();

		final String java_folder = "D:\\[DEV]\\[TOOLS]\\physics-body-editor-2.9.2";
		final File input_folder = LocalFileSystem.newFile(java_folder);
		final FileFilter filter = new FileFilter() {
			@Override
			public boolean fits (final File child) {
				final String name = child.getName().toLowerCase();
				return name.endsWith("com.jfixby.red.test.physics.box2d");
			}
		};
		final FilesList input_files = input_folder.listDirectChildren().filter(filter);

		final File output_folder = LocalFileSystem.newFile(TintoAssetsConfig.TINTO_REMOTE_ASSETS_HOME).child("bank-florida");
		output_folder.makeFolder();

		for (final File i_file : input_files) {
			L.d("------------------------------------------------------------------------------------------");

			final String package_name_string = i_file.getName();
			ID package_name = Names.newID(package_name_string);
			package_name = package_name.parent();
			final Box2DShapesPackingStatus status = new Box2DShapesPackingStatus();
			final Box2DShapesPackerSettings settings = Box2DShapesPacker.newSettings();

			settings.setInputFile(i_file);
			settings.setOutputFolder(output_folder);
			settings.setPackageName(package_name);
			try {
				Box2DShapesPacker.pack(settings);
			} catch (final Throwable e) {
				e.printStackTrace();
				final Collection<File> related_folders = status.getRelatedFolders();
				for (final File file : related_folders) {
					file.delete();
					L.d("DELETE", file);
				}
			}
			L.d(" done", package_name_string);
		}
	}

}
