
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.github.wrebecca.bleed.RebeccaTextureBleeder;
import com.jfixby.psd.unpacker.api.PSDUnpacker;
import com.jfixby.psd.unpacker.core.RedPSDUnpacker;
import com.jfixby.scarabei.adopted.gdx.json.GdxJson;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.assets.Names;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.debug.DebugTimer;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileFilter;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.java.gc.GCFisher;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.desktop.image.RedImageAWT;
import com.jfixby.texture.slicer.api.TextureSlicer;
import com.jfixby.texture.slicer.red.RedTextureSlicer;
import com.jfixby.tool.psd2scene2d.CompressionInfo;
import com.jfixby.tool.psd2scene2d.PSDRepackSettings;
import com.jfixby.tool.psd2scene2d.PSDRepacker;
import com.jfixby.tool.psd2scene2d.PSDRepackerResult;
import com.jfixby.tool.psd2scene2d.PSDRepackingStatus;
import com.jfixby.tools.bleed.api.TextureBleed;
import com.jfixby.tools.gdx.texturepacker.GdxTexturePacker;
import com.jfixby.tools.gdx.texturepacker.api.TexturePacker;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class RepackPreloader {

	private static boolean deleteGarbage = false;

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();

// PNGQuant.installComponent(new PNGQuantJavaWrapper("D:\\[DEV]\\[GIT]\\pngquant\\PNGQuant\\pngquant.exe"));

		PSDUnpacker.installComponent(new RedPSDUnpacker());
		Json.installComponent(new GdxJson());
		TexturePacker.installComponent(new GdxTexturePacker());
		TextureSlicer.installComponent(new RedTextureSlicer());
// TextureBleed.installComponent(new MaskTextureBleeder());
		ImageAWT.installComponent(new RedImageAWT());
		TextureBleed.installComponent(new RebeccaTextureBleeder());
		IndexedCompressor.installComponent(new RedIndexedCompressor());
// TexturePacker.installComponent(new RebeccaTexturePacker());

		final DebugTimer packageTimer = Debug.newTimer();
		final DebugTimer totalTimer = Debug.newTimer();

		final String java_folder = TintoAssetsConfig.RAW_ASSETS_HOME + "\\RED-INPUT";
		final File input_folder = LocalFileSystem.newFile(java_folder).child("@");

		final File logfile = LocalFileSystem.ApplicationHome().child("RepackPSDScene.log");
		logfile.delete();
		final FileFilter filter = new FileFilter() {
			@Override
			public boolean fits (final File child) {
				final String name = child.getName().toLowerCase();
				// return name.contains("GameMainUI".toLowerCase())
				// && name.endsWith(".psd");
				return name.contains("preloader".toLowerCase()) && name.endsWith(".psd");
			}
		};
		final FilesList psd_files = input_folder.listDirectChildren().filter(filter);
		if (psd_files.size() == 0) {
			L.d("No files found.");
			input_folder.listDirectChildren().print("content");
			Sys.exit();
		}
		psd_files.print("processing");
// Sys.exit();
		final File output_folder = LocalFileSystem.newFile(TintoAssetsConfig.TINTO_LOCAL_ASSETS_HOME).child("tank-0");
		output_folder.makeFolder();
		final List<CompressionInfo> compressedPNG = Collections.newList();
		;
		// output_folder.clearFolder();
		final String prefix = "com.jfixby.tinto.";
		totalTimer.reset();
		for (final File psd_file : psd_files) {
			packageTimer.reset();
			L.d("------------------------------------------------------------------------------------------");
			String package_name_string = prefix + psd_file.getName().replaceAll(" animated", "").replaceAll("border ", "scene-");
			package_name_string = package_name_string.substring(0, package_name_string.length() - ".psd".length());

			final ID package_name = Names.newID(package_name_string);

			final int max_texture_size = (256);
			final int margin = 0;
			final int texturePadding = 0;
			final int atlasPageSize = 1024;
			final float imageQuality = 1f;

			final boolean forceRasterDecomposition = !true;
			final int gemserkPadding = 0;
			L.d("     psd_file", psd_file);
			L.d("output_folder", output_folder);
			L.d(" package_name", package_name_string);
			L.d("max_texture_size", max_texture_size);

			final PSDRepackingStatus status = new PSDRepackingStatus();
			try {

				GCFisher.getMemoryStatistics().print("memory stats");

				final boolean ignore_atlas = !true;

				final PSDRepackSettings settings = PSDRepacker.newSettings();

				settings.setPSDFile(psd_file);
				settings.setPackageName(package_name);
				settings.setOutputFolder(output_folder);
				settings.setMaxTextureSize(max_texture_size);
				settings.setMargin(margin);
				settings.setIgonreAtlasFlag(ignore_atlas);
				settings.setGemserkPadding(gemserkPadding);
				settings.setAtlasMaxPageSize(atlasPageSize);
				settings.setPadding(texturePadding);
				settings.setForceRasterDecomposition(forceRasterDecomposition);
// settings.setImageQuality(imageQuality);
				settings.setUseIndexCompression(!true);
				settings.setUseInMemoryFileSystem(true);
// settings.usePNGQuant(!true);

				final PSDRepackerResult repackingResult = PSDRepacker.repackPSD(settings, status);

				compressedPNG.addAll(repackingResult.listCompressions());

			} catch (final Throwable e) {
				e.printStackTrace();
				if (deleteGarbage) {
					final Collection<File> related_folders = status.getRelatedFolders();
					for (final File file : related_folders) {
						file.delete();
						L.d("DELETE", file);
					}
				}
				Sys.exit();

			}

			compressedPNG.print("compressed files");

			L.d(" done", package_name_string);
			packageTimer.printTime("PERFORMANCE-TEST: " + package_name_string);
			totalTimer.printTime("PERFORMANCE-TEST: PROGRESS");
			logfile.writeString("package, " + package_name_string + ", " + packageTimer.getTime() + "\n", true);
			logfile.writeString("total, , " + totalTimer.getTime() + "\n", true);

		}
		totalTimer.printTime("PERFORMANCE-TEST: TOTAL");
		// PackGdxFileSystem.main(null);

	}

}
