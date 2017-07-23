
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;
import java.util.jar.Pack200.Packer;

import com.jfixby.r3.rana.api.pkg.io.PackageDescriptor;
import com.jfixby.r3.rana.red.pkg.bank.PackageUtils;
import com.jfixby.rana.api.pkg.StandardPackageFormats;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.tools.gdx.texturepacker.api.AtlasPackingResult;
import com.jfixby.tools.gdx.texturepacker.api.TexturePacker;
import com.jfixby.tools.gdx.texturepacker.api.TexturePackingSpecs;

public class PackAtlas {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();

		final File input_folder = LocalFileSystem.newFile("D:\\[DATA]\\[RED-ASSETS]\\packing\\input");
		final File output_folder = input_folder.parent().child("output");
		output_folder.makeFolder();
		output_folder.clearFolder();

		final String package_name = "com.jfixby.redtriplane.r3.render.internal.raster";

		final TexturePackingSpecs specs = TexturePacker.newPackingSpecs();

		specs.setOutputAtlasFileName(package_name);
		specs.setOutputAtlasFolder(output_folder);
		specs.setInputRasterFolder(input_folder);

		final Packer packer = TexturePacker.newPacker(specs);

		final AtlasPackingResult atlas_result = packer.pack();

		atlas_result.print();

		final File altas_file = atlas_result.getAtlasOutputFile();
		final String atlas_name = altas_file.getName();

		final File package_bank = LocalFileSystem.newFile(TintoAssetsConfig.TINTO_REMOTE_ASSETS_HOME + "\\bank-europa");

		final File content_folder = package_bank.child(package_name).child(PackageDescriptor.PACKAGE_CONTENT_FOLDER);

		content_folder.makeFolder();
		content_folder.clearFolder();
		content_folder.getFileSystem().copyFolderContentsToFolder(output_folder, content_folder);

		final Collection<ID> packed = atlas_result.listPackedAssets();
		packed.print("packed");

		final Collection<ID> required = Collections.newList();
		PackageUtils.producePackageDescriptor(content_folder.parent(), StandardPackageFormats.libGDX.Atlas, "1.0", packed, required,
			atlas_name);

	}

}
