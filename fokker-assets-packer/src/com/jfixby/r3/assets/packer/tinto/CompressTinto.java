
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.jfixby.r3.rana.api.pkg.io.PackageDescriptor;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class CompressTinto {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		IndexedCompressor.installComponent(new RedIndexedCompressor());
// PNGQuant.installComponent(new PNGQuantJavaWrapper("D:\\[DEV]\\[GIT]\\pngquant\\PNGQuant\\pngquant.exe"));

		final File tinto_banks = LocalFileSystem.newFile(TintoAssetsConfig.TINTO_REMOTE_ASSETS_HOME);

		final File bank = tinto_banks.child("bank-tinto");
		final FilesList packages = bank.listDirectChildren(file -> {
			if (!file.getName().endsWith(".gdx-atlas")) {
				return false;
			}
			if (file.getName().contains("splash-icons")) {
				return false;
			}

			return true;
		});
		for (final File e : packages) {
			process(e);
		}
		L.d("EXIT");
	}

	private static void process (final File e) throws IOException {
		final File content = e.child(PackageDescriptor.PACKAGE_CONTENT_FOLDER);

		final FilesList pngs = content.listDirectChildren(file -> file.extensionIs("png") & file.getName().contains(""));
// pngs.print("content");
		Collections.scanCollection(pngs, (f, i) -> processPNG(f));

	}

	private static void processPNG (final File original) {
// final CompressionResult result = PNGQuant.compress(original, original);
// result.print("" + original);
// if (!result.isOK()) {
// L.d("WARNING!");
// Sys.sleep(2000);
// }
	}

}
