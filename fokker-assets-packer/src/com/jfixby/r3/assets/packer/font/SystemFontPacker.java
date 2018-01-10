
package com.jfixby.r3.assets.packer.font;

import java.io.IOException;

import com.jfixby.r3.fokker.font.api.FokkerFontPackageReader;
import com.jfixby.r3.rana.api.pkg.PackerSpecs;
import com.jfixby.r3.rana.red.pkg.bank.PackageUtils;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.names.Names;

public class SystemFontPacker {
// public static void pack (final File tank) throws IOException {
//
// final File shaders = LocalFileSystem.ApplicationHome().child("fonts");
// final FilesList font_folders_list = shaders.listDirectChildren();
// for (int i = 0; i < font_folders_list.size(); i++) {
// final File fontFolder = font_folders_list.getElementAt(i);
// try {
//// packSystemFont(tank, fontFolder);
// } catch (final IOException e) {
// e.printStackTrace();
// }
// }
// }

	public static void packSystemFont (final File tank, final File fontFolder, final String fontFileName) throws IOException {

		final String packagename = fontFolder.getName();
		final File pkg = tank.child(packagename);
		L.d("paking", pkg);
		pkg.makeFolder();

		final String id_string = packagename;
		final ID asset = Names.newID(id_string);
		final File asset_folder = pkg;

		final PackerSpecs specs = new PackerSpecs();
		specs.packageFolder = (asset_folder);

		specs.packedFiles.addAll(fontFolder.listDirectChildren());

		specs.rootFileName = (fontFileName);

		final File root_file = fontFolder.child(fontFileName);
		final List<ID> packed = Collections.newList();
		final ID id_i = Names.newID(fontFolder.getName());
		packed.add(id_i);

		specs.packedAssets.addAll(packed);

		final List<ID> required = Collections.newList();
		specs.requiredAssets.addAll(required);

		specs.packageFormat = (FokkerFontPackageReader.PACKAGE_FORMAT);
		specs.version = ("1.0");
		L.d("packing", root_file);
		PackageUtils.pack(specs);

	}
}
