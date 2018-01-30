
package com.jfixby.r3.assets.packer.go;

import java.io.IOException;

import com.jfixby.r3.assets.packer.font.SystemFontPacker;
import com.jfixby.r3.assets.packer.raster.SystemRasterPacker;
import com.jfixby.r3.fokker.adaptor.cfg.FokkerStarterConfig;
import com.jfixby.r3.fokker.api.FOKKER_SYSTEM_ASSETS;
import com.jfixby.r3.fokker.assets.api.shader.io.R3_SHADER_SETTINGS;
import com.jfixby.r3.fokker.assets.api.shader.io.ShaderInfo;
import com.jfixby.r3.fokker.assets.api.shader.io.ShadersContainer;
import com.jfixby.r3.fokker.shader.api.FokkerShaderPackageReader;
import com.jfixby.r3.rana.api.pkg.PackerSpecs;
import com.jfixby.r3.rana.api.pkg.io.BankHeaderInfo;
import com.jfixby.r3.rana.red.pkg.bank.PackageUtils;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFile;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.io.IO;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.names.Names;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.filesystem.virtual.InMemoryFileSystem;

public class SystemAssetsBankBuilder {
	public static final void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();

		final LocalFile outputFolder = LocalFileSystem.newFile("D:\\[DEV]\\[GIT-3]\\Jet\\jet-desktop\\assets");
		deployR3Bank(outputFolder);
	}

	public static void deployR3Bank (final File outputFolder) throws IOException {
		final String bankname = FOKKER_SYSTEM_ASSETS.LOCAL_BANK_NAME.toString();
		final File localBankFolder = outputFolder.child(bankname);
		localBankFolder.makeFolder();
		localBankFolder.clearFolder();

		writeBankHeader(outputFolder, bankname);

		final File tankFolder = localBankFolder.child("tank-0");

		final File shadersFolder = LocalFileSystem.ApplicationHome().child("shaders").child("prepared");
		final File rasterFolder = LocalFileSystem.ApplicationHome().child("raster");
		final String fontName = FOKKER_SYSTEM_ASSETS.GENERIC_FONT.toString();
		final File fontsFolder = LocalFileSystem.ApplicationHome().child("fonts").child(fontName);

		packShaders(shadersFolder, tankFolder);
		packRaster(rasterFolder, tankFolder);
		packDefaultFont(fontsFolder, tankFolder);

	}

	public static File writeBankHeader (final File localAssets, final String bankname) throws IOException {

		final File localBankFolder = localAssets.child(bankname);
		localBankFolder.makeFolder();

		final File bankHeaderFile = localBankFolder.child(BankHeaderInfo.FILE_NAME);
		L.d("writing", bankHeaderFile);

		final BankHeaderInfo bankHeader = new BankHeaderInfo();
		bankHeader.bank_name = bankname;

		bankHeaderFile.writeString(Json.serializeToString(bankHeader).toString());

		return localBankFolder;
	}

	private static void packDefaultFont (final File fontFolder, final File tank) throws IOException {

		final String fontName = FOKKER_SYSTEM_ASSETS.GENERIC_FONT.toString();
// final File fontFolder = LocalFileSystem.ApplicationHome().child("fonts").child(fontName);
		final List<String> steps = (FOKKER_SYSTEM_ASSETS.GENERIC_FONT).steps();
		steps.reverse();
		final String fontFileName = Names.newID(steps).toString();
		SystemFontPacker.packSystemFont(tank, fontFolder, fontFileName);

	}

	private static void packRaster (final File systemRaster, final File tank) throws IOException {
		SystemRasterPacker.pack(systemRaster, tank);
	}

	private static void packShaders (final File shaders, final File tank) throws IOException {
// final File shaders = LocalFileSystem.ApplicationHome().child("shaders").child("prepared");
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

	public static void createStarterConfig (final File tankFolder, final String fokkerStarterAssetId,
		final int fokkerStarterConfigWidth, final int fokkerStarterConfigHeight, final String title) throws IOException {

		final File pkg = tankFolder.child(fokkerStarterAssetId);
		pkg.makeFolder();
		final String id_string = pkg.getName();

		final FokkerStarterConfig fokkerConfig = new FokkerStarterConfig();

		fokkerConfig.params.put(FokkerStarterConfig.useGL30, true + "");
		fokkerConfig.params.put(FokkerStarterConfig.width, fokkerStarterConfigWidth + "");
		fokkerConfig.params.put(FokkerStarterConfig.height, fokkerStarterConfigHeight + "");
		fokkerConfig.params.put(FokkerStarterConfig.TITLE, title);

		final PackerSpecs specs = new PackerSpecs();

		final InMemoryFileSystem tmp = new InMemoryFileSystem();

		specs.packageFolder = (pkg);
		specs.rootFileName = FokkerStarterConfig.FILE_NAME;

		final File tmpFolder = tmp.ROOT();
		final File tmpFile = tmpFolder.child(specs.rootFileName);
		tmpFile.writeString(Json.serializeToString(fokkerConfig).toString());

		final FilesList files = tmpFolder.listDirectChildren();
		specs.packedFiles.addAll(files);

		final List<ID> packed = Collections.newList();
		final ID id_i = Names.newID(id_string);
		packed.add(id_i);
		specs.packedAssets.addAll(packed);

		specs.packageFormat = (FokkerStarterConfig.PACKAGE_FORMAT);
		specs.version = ("1.0");
		L.d("packing", pkg);
		PackageUtils.pack(specs);
	}

}
