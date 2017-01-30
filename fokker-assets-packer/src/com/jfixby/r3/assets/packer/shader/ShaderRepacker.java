
package com.jfixby.r3.assets.packer.shader;

import java.io.IOException;

import com.jfixby.r3.fokker.assets.api.shader.io.R3_SHADER_SETTINGS;
import com.jfixby.r3.fokker.assets.api.shader.io.ShaderInfo;
import com.jfixby.r3.fokker.assets.api.shader.io.ShadersContainer;
import com.jfixby.r3.fokker.shader.api.FokkerShaderPackageReader;
import com.jfixby.r3.rana.api.pkg.PackerSpecs;
import com.jfixby.r3.rana.red.pkg.bank.PackageUtils;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.assets.Names;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.io.IO;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.json.GoogleJson;

public class ShaderRepacker {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		Json.installComponent(new GoogleJson());
		repack();
	}

	public static void repack () throws IOException {
		final File bank = LocalFileSystem.ApplicationHome().child("assets").child("com.jfixby.r3.shader.assets.local")
			.child("tank-0");
		final File shaders = LocalFileSystem.ApplicationHome().child("shaders");
		final FilesList folders_list = shaders.listDirectChildren();
		for (int i = 0; i < folders_list.size(); i++) {
			final File folder = folders_list.getElementAt(i);
			try {
				packShader(bank, folder);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static ShadersContainer readInfo (final File root_file) throws IOException {
		return IO.deserialize(ShadersContainer.class, root_file.readBytes());
	}

	private static void packShader (final File bank, final File folder) throws IOException {
		final String id_string = folder.getName();
		final ID asset = Names.newID(id_string);
		final File asset_folder = bank.child(id_string);

		final PackerSpecs specs = new PackerSpecs();
		specs.setPackageFolder(asset_folder);

		final FilesList files = folder.listDirectChildren();
		specs.addPackedFiles(files);

		specs.setRootFileName(R3_SHADER_SETTINGS.ROOT_FILE_NAME);

		final File root_file = folder.child(specs.getRootFileName());
		final List<ID> packed = Collections.newList();
		final ShadersContainer container = readInfo(root_file);
		for (final ShaderInfo shader : container.shaders) {
			final ID id_i = Names.newID(shader.shader_id);
			packed.add(id_i);
		}

		specs.setPackedAssets(packed);

		final List<ID> required = Collections.newList();
		specs.setRequiredAssets(required);

		specs.setPackageFormat(FokkerShaderPackageReader.PACKAGE_FORMAT);
		specs.setVersion("1.0");

		PackageUtils.pack(specs);
	}

}
