
package com.jfixby.r3.assets.packer.raster;

import java.io.IOException;

import com.jfixby.r3.fokker.texture.api.FokkerTexturePackageReader;
import com.jfixby.r3.rana.api.pkg.PackerSpecs;
import com.jfixby.r3.rana.red.pkg.bank.PackageUtils;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.names.Names;

public class SystemRasterPacker {
	public static void pack (final File rasterFolder, final File tank) throws IOException {

		final FilesList files_list = rasterFolder.listDirectChildren();
		for (int i = 0; i < files_list.size(); i++) {
			final File file = files_list.getElementAt(i);
			try {
				packSystemRaster(tank, file);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void packSystemRaster (final File tank, final File file) throws IOException {

		final String packagename = file.nameWithoutExtension();
		final File pkg = tank.child(packagename);
		L.d("paking", pkg);
		pkg.makeFolder();

		final String id_string = packagename;
		final ID asset = Names.newID(id_string);
		final File asset_folder = pkg;

		final PackerSpecs specs = new PackerSpecs();
		specs.packageFolder = (asset_folder);

		specs.packedFiles.add(file);

		specs.rootFileName = (file.getName());

		final File root_file = file;
		final List<ID> packed = Collections.newList();
		final ID id_i = Names.newID(packagename);
		packed.add(id_i);

		specs.packedAssets.addAll(packed);

		final List<ID> required = Collections.newList();
		specs.requiredAssets.addAll(required);

		specs.packageFormat = (FokkerTexturePackageReader.PACKAGE_FORMAT_TEXTURE);
		specs.version = ("1.0");
		L.d("packing", root_file);
		PackageUtils.pack(specs);

	}
}
