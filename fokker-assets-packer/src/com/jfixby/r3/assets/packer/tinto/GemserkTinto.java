
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.github.wrebecca.bleed.RebeccaTextureBleeder;
import com.jfixby.r3.rana.api.pkg.io.PackageDescriptor;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.tools.bleed.api.TextureBleed;
import com.jfixby.tools.bleed.api.TextureBleedResult;
import com.jfixby.tools.bleed.api.TextureBleedSpecs;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class GemserkTinto {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		TextureBleed.installComponent(new RebeccaTextureBleeder());

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
		Collections.scanCollection(packages, (e, i) -> process(e));
		L.d("EXIT");
	}

	private static void process (final File e) {
		final File content = e.child(PackageDescriptor.PACKAGE_CONTENT_FOLDER);

		final TextureBleedSpecs bleedSpecs = TextureBleed.newSpecs();
		bleedSpecs.setDebugMode(!true);
		bleedSpecs.setPaddingSize(16);
		final File pagesFolder = content;
		L.d("Gemserking", pagesFolder);
		bleedSpecs.setInputFolder(pagesFolder);
		TextureBleedResult gemserk_result;
		try {
			gemserk_result = TextureBleed.process(bleedSpecs);
			gemserk_result.print();
		} catch (final IOException e1) {
			e1.printStackTrace();
		}

	}

}
