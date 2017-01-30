
package com.jfixby.r3.assets.packer.raster.go;

import com.jfixby.r3.assets.packer.raster.SystemRasterPacker;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.json.GoogleJson;

public class PackSystemTextures {

	public static void main (final String[] args) {
		ScarabeiDesktop.deploy();
		Json.installComponent(new GoogleJson());

		SystemRasterPacker.pack(tank);
	}

}
