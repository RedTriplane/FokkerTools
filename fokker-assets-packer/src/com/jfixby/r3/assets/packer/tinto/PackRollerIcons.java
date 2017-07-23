
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

public class PackRollerIcons {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		final String java_path_icons = "D:\\[DATA]\\[TAMER]\\icons";
		final String java_path_banks = "D:\\[DATA]\\[RED-ASSETS]\\gdx-assets-prepared";
		final File input_folder = LocalFileSystem.newFile(java_path_icons);

		final String package_name = "com.jfixby.tinto.GameMainUI.roller";
		final File output_package_folder = LocalFileSystem.newFile(java_path_banks).child("bank-greece").child(package_name);
		output_package_folder.makeFolder();
		output_package_folder.clearFolder();
		final File atlas_output_folder = output_package_folder.child(PackageDescriptor.PACKAGE_CONTENT_FOLDER);
		atlas_output_folder.makeFolder();

		final TexturePackingSpecs specs = TexturePacker.newPackingSpecs();
		specs.setOutputAtlasFileName(package_name);
		specs.setOutputAtlasFolder(atlas_output_folder);
		specs.setInputRasterFolder(input_folder);

		final Packer packer = TexturePacker.newPacker(specs);

		final AtlasPackingResult atlas_result = packer.pack();

		atlas_result.print();
		final Collection<ID> packed = atlas_result.listPackedAssets();
		final File altas_file = atlas_result.getAtlasOutputFile();
		final String atlas_name = altas_file.getName();
		final Collection<ID> required = Collections.newList();
		PackageUtils.producePackageDescriptor(output_package_folder, StandardPackageFormats.libGDX.Atlas, "1.0", packed, required,
			atlas_name);
	}

}
