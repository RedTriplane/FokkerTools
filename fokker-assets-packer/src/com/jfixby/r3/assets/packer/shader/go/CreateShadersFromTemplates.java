
package com.jfixby.r3.assets.packer.shader.go;

import java.io.IOException;

import com.jfixby.r3.fokker.api.FOKKER_SYSTEM_ASSETS;
import com.jfixby.r3.fokker.assets.api.shader.io.FOKKER_SHADER_PARAMS;
import com.jfixby.r3.fokker.assets.api.shader.io.R3_SHADER_SETTINGS;
import com.jfixby.r3.fokker.assets.api.shader.io.ShaderInfo;
import com.jfixby.r3.fokker.assets.api.shader.io.ShadersContainer;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.assets.Names;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.io.IO;
import com.jfixby.scarabei.api.java.ByteArray;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.json.GoogleJson;

public class CreateShadersFromTemplates {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		Json.installComponent(new GoogleJson());

		final File templates = LocalFileSystem.ApplicationHome().child("shaders").child("templates");

		final File shaders = LocalFileSystem.ApplicationHome().child("shaders");
		final File output = shaders.child("prepared");

		final List<ID> input = Collections.newList();
		input.add(Names.newID(FOKKER_SYSTEM_ASSETS.SHADERS));
		input.add(Names.newID(FOKKER_SYSTEM_ASSETS.SHADER_GDX_DEFAULT).parent());

// output.clearFolder();
		for (final ID i : input) {
			processTemplate(i, templates, output);
		}

// L.d("", root_file);

	}

	private static void processTemplate (final ID i, final File templates, final File output) throws IOException {
		final File inputTemplate = templates.child(i.toString());
		final File pkg = output.child(i.toString());
		pkg.clearFolder();
		final File root_file = pkg.child(R3_SHADER_SETTINGS.ROOT_FILE_NAME);

		final ShadersContainer container = new ShadersContainer();

		final FilesList children = inputTemplate.listDirectChildren();

		for (final File shader_folder : children) {
			addShader(container, shader_folder, pkg);
		}

		final ByteArray data = IO.serialize(container);
		root_file.writeBytes(data);
		L.d("shader writing done", root_file);
	}

	private static void addShader (final ShadersContainer container, final File template_folder, final File output)
		throws IOException {
		final String short_name = template_folder.getName();

		final File input_frag_file = template_folder.child(R3_SHADER_SETTINGS.FRAG_FILE_NAME);
		final File input_vert_file = template_folder.child(R3_SHADER_SETTINGS.VERT_FILE_NAME);

		final String frag_template_string = input_frag_file.readToString();
		final String vert_template_string = input_vert_file.readToString();

		final ShaderInfo info = new ShaderInfo();
		final ID shader_id = Names.newID(FOKKER_SYSTEM_ASSETS.SHADERS).child(short_name);
		final File content = output.child(short_name);
		content.makeFolder();
		content.clearFolder();

		final File frag_file = content.child(R3_SHADER_SETTINGS.FRAG_FILE_NAME);
		final File vert_file = content.child(R3_SHADER_SETTINGS.VERT_FILE_NAME);

		vert_file.writeString(vert_template_string);
		frag_file.writeString(frag_template_string);

		{
			info.shader_id = shader_id.toString();
			info.shader_folder_name = content.getName();
			{
				info.parameters_list.add(FOKKER_SHADER_PARAMS.SCREEN_WIDTH);
			}
			{
				info.parameters_list.add(FOKKER_SHADER_PARAMS.SCREEN_HEIGHT);
			}
			{
				info.parameters_list.add(FOKKER_SHADER_PARAMS.ALPHA_BLEND);
			}
			{
				info.parameters_list.add(FOKKER_SHADER_PARAMS.U_TEXTURE_0_CURRENT);
			}
			{
				info.parameters_list.add(FOKKER_SHADER_PARAMS.U_TEXTURE_1_ORIGINAL);
			}
			{
				info.parameters_list.add(FOKKER_SHADER_PARAMS.U_TEXTURE_2_ALPHA);
			}

		}
		container.shaders.add(info);
	}

}
