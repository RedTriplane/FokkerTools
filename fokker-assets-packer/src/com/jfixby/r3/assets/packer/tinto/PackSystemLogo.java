
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.jfixby.r3.fokker.api.FOKKER_SYSTEM_ASSETS;
import com.jfixby.r3.rana.api.pkg.io.PackageDescriptor;
import com.jfixby.rana.api.pkg.StandardPackageFormats;
import com.jfixby.scarabei.adopted.gdx.json.GdxJson;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.io.IO;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class PackSystemLogo {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		Json.installComponent(new GdxJson());

		packSystemAsset(FOKKER_SYSTEM_ASSETS.RASTER_IS_MISING);
		packSystemAsset(FOKKER_SYSTEM_ASSETS.BLACK);
		packSystemAsset(FOKKER_SYSTEM_ASSETS.DEBUG_BLACK);
		packSystemAsset(FOKKER_SYSTEM_ASSETS.LOGO);

	}

	private static void packSystemAsset (final ID id) throws IOException {
		final File home = LocalFileSystem.newFile(TintoAssetsConfig.TINTO_REMOTE_ASSETS_HOME);
		final File bank = home.child("bank-r3");

		final PackageDescriptor descriptor = new PackageDescriptor();
		descriptor.format = StandardPackageFormats.libGDX.Texture;
		descriptor.timestamp = "" + Sys.SystemTime().currentTimeMillis();
		descriptor.version = "1.0";
		descriptor.packed_assets.add(id.toString());
		descriptor.root_file_name = id + ".png";

		final String debug_info = Json.serializeToString(descriptor).toString();
		L.d(debug_info);

		final File package_folder = bank.child(id.toString());

		final File debug = package_folder.child("debug.txt");
		debug.writeString(debug_info);
		final File file_pit = package_folder.child(PackageDescriptor.PACKAGE_DESCRIPTOR_FILE_NAME);
		L.d("writing", file_pit);
		file_pit.writeBytes(IO.serialize(descriptor));
	}
}
