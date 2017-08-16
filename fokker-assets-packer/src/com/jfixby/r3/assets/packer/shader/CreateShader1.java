
package com.jfixby.r3.assets.packer.shader;

import java.io.IOException;

import com.jfixby.r3.fokker.assets.api.shader.io.R3_SHADER_SETTINGS;
import com.jfixby.r3.fokker.assets.api.shader.io.SHADER_PARAMETER;
import com.jfixby.r3.fokker.assets.api.shader.io.ShaderInfo;
import com.jfixby.r3.fokker.assets.api.shader.io.ShaderParameterInfo;
import com.jfixby.r3.fokker.assets.api.shader.io.ShadersContainer;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.io.IO;
import com.jfixby.scarabei.api.java.ByteArray;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.json.JsonString;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.names.Names;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.json.GoogleJson;

public class CreateShader1 {
	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		Json.installComponent(new GoogleJson());
		final ID shader_id1 = Names.newID("com.jfixby.r3.shader.shader1");
		final ID shaderID = shader_id1;

		final File output_folder = LocalFileSystem.ApplicationHome().child("shaders").child("" + shaderID.parent());
		output_folder.makeFolder();
		final File content = output_folder;
		content.makeFolder();

		final String name = shaderID.getLastStep();

		final File pack = content.child(name);
		pack.makeFolder();
		final File root_file = output_folder.child(R3_SHADER_SETTINGS.ROOT_FILE_NAME);
		final ShadersContainer container = new ShadersContainer();

		add(name, container, shaderID);

		final ByteArray params_data = IO.serialize(container);
		root_file.writeBytes(params_data);
// L.d("shader writing done", params_data);
		final JsonString debugString = Json.serializeToString(container);
		L.d(debugString);

	}

	private static void add (final String name, final ShadersContainer container, final ID shader_id) {
		final ShaderInfo info = new ShaderInfo();
		info.shader_id = shader_id.toString();
		info.shader_folder_name = name;
		info.isOverlay = false;
		{
			final ShaderParameterInfo parameter = new ShaderParameterInfo("grayscale", "float", 1);
			info.parameters_list.add(parameter);
		}
		{
			final ShaderParameterInfo parameter = new ShaderParameterInfo(SHADER_PARAMETER.POSITION_X, "float", 0);
			info.parameters_list.add(parameter);
		}
		{
			final ShaderParameterInfo parameter = new ShaderParameterInfo(SHADER_PARAMETER.POSITION_Y, "float", 0);
			info.parameters_list.add(parameter);
		}

		container.shaders.add(info);
	}

}
