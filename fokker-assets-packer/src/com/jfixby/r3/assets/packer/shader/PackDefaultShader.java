
package com.jfixby.r3.assets.packer.shader;

import java.io.IOException;

import com.jfixby.r3.assets.packer.cfg.ConfigLoader;
import com.jfixby.r3.assets.packer.cfg.R3AssetsPackerConfig;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.json.GoogleJson;

public class PackDefaultShader {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		Json.installComponent(new GoogleJson());

		final R3AssetsPackerConfig cfg = ConfigLoader.load("r3-assets-packer-config.json");

		final File outputBankFolder = LocalFileSystem.newFile(cfg.outputBankFolderPath);
		final File tank = outputBankFolder.child(cfg.targetTank);
// tank.listDirectChildren().print("tank");

		final String shaderPackageName = "com.badlogic.gdx.graphics.g2d.SpriteBatch";
		final File packageFolder = tank.child(shaderPackageName);
		packageFolder.makeFolder();

		final File inputFiles = LocalFileSystem.ApplicationHome().child("shaderPackageName");

		ShadersPacker.pack(tank);

	}

}
