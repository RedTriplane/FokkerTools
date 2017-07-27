
package com.jfixby.r3.assets.packer;

import java.io.IOException;

import com.jfixby.r3.assets.packer.font.SystemFontPacker;
import com.jfixby.r3.assets.packer.raster.SystemRasterPacker;
import com.jfixby.r3.fokker.FOKKER_SYSTEM_ASSETS;
import com.jfixby.r3.fokker.assets.api.shader.io.R3_SHADER_SETTINGS;
import com.jfixby.r3.fokker.assets.api.shader.io.ShaderInfo;
import com.jfixby.r3.fokker.assets.api.shader.io.ShadersContainer;
import com.jfixby.r3.fokker.shader.api.FokkerShaderPackageReader;
import com.jfixby.r3.rana.api.pkg.PackerSpecs;
import com.jfixby.r3.rana.api.pkg.io.BankHeaderInfo;
import com.jfixby.r3.rana.red.pkg.bank.PackageUtils;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.assets.Names;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.io.IO;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.gson.GoogleGson;

public class RebuildFokkerBank {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		Json.installComponent(new GoogleGson());

		final File outputFolder = LocalFileSystem.newFile(FokkerAssetsConfig.FOKKER_LOCAL_ASSETS_HOME);

		final BankHeaderInfo bankHeader = new BankHeaderInfo();
		bankHeader.bank_name = FOKKER_SYSTEM_ASSETS.LOCAL_BANK_NAME;

		final File bankFolder = outputFolder.child(bankHeader.bank_name);
		bankFolder.clearFolder();
		final File tankFolder = bankFolder.child("tank-0");

		tankFolder.makeFolder();

		final File bankHeaderFile = bankFolder.child(BankHeaderInfo.FILE_NAME);
		L.d("writing", bankHeaderFile);
		bankHeaderFile.writeString(Json.serializeToString(bankHeader).toString());

		packShaders(tankFolder);
		packRaster(tankFolder);
		packDefaultFont(tankFolder);

	}

	private static void packDefaultFont (final File tank) throws IOException {

		final String fontName = FOKKER_SYSTEM_ASSETS.GENERIC_FONT.toString();
		final File fontFolder = LocalFileSystem.ApplicationHome().child("fonts").child(fontName);
		final List<String> steps = Names.newID(FOKKER_SYSTEM_ASSETS.GENERIC_FONT).steps();
		steps.reverse();
		final String fontFileName = Names.newID(steps).toString();
		SystemFontPacker.packSystemFont(tank, fontFolder, fontFileName);

	}

	private static void packRaster (final File tank) throws IOException {
		SystemRasterPacker.pack(tank);
	}

	private static void packShaders (final File tank) throws IOException {
		final File shaders = LocalFileSystem.ApplicationHome().child("shaders").child("prepared");
// shaders.listAllChildren().print("shaders");

		final FilesList folders_list = shaders.listDirectChildren();
		for (int i = 0; i < folders_list.size(); i++) {
			final File folder = folders_list.getElementAt(i);
			try {
				packShader(tank, folder);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void packShader (final File bank, final File folder) throws IOException {
		final String id_string = folder.getName();
		final ID asset = Names.newID(id_string);
		final File asset_folder = bank.child(id_string);

		final PackerSpecs specs = new PackerSpecs();
		specs.packageFolder = (asset_folder);

		final FilesList files = folder.listDirectChildren();
		specs.packedFiles.addAll(files);

		specs.rootFileName = (R3_SHADER_SETTINGS.ROOT_FILE_NAME);

		final File root_file = folder.child(specs.rootFileName);
		final List<ID> packed = Collections.newList();
		final ShadersContainer container = readInfo(root_file);
		for (final ShaderInfo shader : container.shaders) {
			final ID id_i = Names.newID(shader.shader_id);
			packed.add(id_i);
		}

		specs.packedAssets.addAll(packed);

		final List<ID> required = Collections.newList();
		specs.requiredAssets.addAll(required);

		specs.packageFormat = (FokkerShaderPackageReader.PACKAGE_FORMAT);
		specs.version = ("1.0");
		L.d("packing", root_file);
		PackageUtils.pack(specs);
	}

	private static ShadersContainer readInfo (final File root_file) throws IOException {
		return IO.deserialize(ShadersContainer.class, root_file.readBytes());
	}

}
