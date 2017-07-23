
package com.jfixby.r3.assets.packer.raster.go;

import com.jfixby.r3.assets.packer.raster.SystemRasterPacker;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.gson.GoogleGson;

public class PackSystemTextures {

	public static void main (final String[] args) {
		ScarabeiDesktop.deploy();
		Json.installComponent(new GoogleGson());

		SystemRasterPacker.pack(tank);
	}

}
