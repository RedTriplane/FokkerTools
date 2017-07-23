
package com.jfixby.r3.assets.packer.tinto;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.jfixby.psd.unpacker.api.PSDFileContent;
import com.jfixby.psd.unpacker.api.PSDLayer;
import com.jfixby.psd.unpacker.api.PSDRaster;
import com.jfixby.psd.unpacker.api.PSDRootLayer;
import com.jfixby.psd.unpacker.api.PSDUnpacker;
import com.jfixby.psd.unpacker.api.PSDUnpackingParameters;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileOutputStream;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.log.L;

public class ReadPSD {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		final String java_folder = "D:\\[DATA]\\[TAMER]\\red-scenes";
		final File psd_home = LocalFileSystem.newFile(java_folder);

		final File scene_001_path = psd_home.child("psd-2048").child("scene-001.psd");

		// AbsolutePath<FileSystem> scene_001_path = psd_home.child("psd-512")
		// .child("scene-001.psd");

		final PSDUnpackingParameters specs = PSDUnpacker.newUnpackingSpecs();
		specs.setPSDFile(scene_001_path);

		final File output_folder = psd_home.child("output");
		// specs.setOutputFolderPath(output_path);

		output_folder.makeFolder();
		output_folder.clearFolder();

		final PSDFileContent result = PSDUnpacker.unpack(specs);

		result.print();

		final PSDRootLayer root = result.getRootlayer();

		for (int i = 0; i < root.numberOfChildren(); i++) {
			final PSDLayer child = root.getChild(i);
			process_child(child, output_folder);
		}

	}

	static int k = 0;

	private static void process_folder (final PSDLayer root, final File output_path) throws IOException {
		for (int i = 0; i < root.numberOfChildren(); i++) {
			final PSDLayer child = root.getChild(i);
			process_child(child, output_path);
		}
	}

	private static void process_child (final PSDLayer child, final File output_path) throws IOException {
		if (child.isVisible()) {
			if (child.isFolder()) {
				process_folder(child, output_path);
			} else {
				final PSDRaster raster = child.getRaster();
				final BufferedImage java_image = raster.getBufferedImage();
				final File file_path = output_path.child("img-" + k + ".png");
				final File output_file = file_path;
				k++;
				L.d("writing", file_path);
				final FileOutputStream os = output_file.newOutputStream();
				final OutputStream java_stream = os.toJavaOutputStream();
				ImageIO.write(java_image, "png", java_stream);
				java_stream.close();
			}
		}
	}
}
