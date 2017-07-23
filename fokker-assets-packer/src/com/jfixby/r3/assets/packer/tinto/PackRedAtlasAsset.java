
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.FileSystem;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.util.path.AbsolutePath;

public class PackRedAtlasAsset {

	public static void main (String[] args) throws IOException {

		ScarabeiDesktop.deploy();

		String java_folder = "D:\\[DATA]\\[TAMER]\\red-scenes";
		AbsolutePath<FileSystem> psd_home = LocalFileSystem.newFile(java_folder).getAbsoluteFilePath();

		AbsolutePath<FileSystem> input_folder_path = psd_home.child("loader").child("atlas");

		AbsolutePath<FileSystem> output_folder_path = psd_home.child("loader").child("red-atlas-asset");

		AbsolutePath<FileSystem> input_gdx_atlas_path = input_folder_path.child("test.gdx-atlas");

	}
}
