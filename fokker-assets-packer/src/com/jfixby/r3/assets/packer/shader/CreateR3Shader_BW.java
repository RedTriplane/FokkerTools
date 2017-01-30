
package com.jfixby.r3.assets.packer.shader;

import java.io.IOException;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.jfixby.r3.fokker.assets.api.shader.io.R3_SHADER_SETTINGS;
import com.jfixby.r3.fokker.assets.api.shader.io.SHADER_PARAMETER;
import com.jfixby.r3.fokker.assets.api.shader.io.ShaderInfo;
import com.jfixby.r3.fokker.assets.api.shader.io.ShaderParameterInfo;
import com.jfixby.r3.fokker.assets.api.shader.io.ShadersContainer;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.assets.Names;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.json.GoogleJson;

public class CreateR3Shader_BW {

	static final String VERT = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "attribute vec4 "
		+ ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +

		"uniform mat4 u_projTrans;\n" + " \n" + "varying vec4 vColor;\n" + "varying vec2 vTexCoord;\n" +

		"void main() {\n" + "	vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "	vTexCoord = "
		+ ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" + "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
		+ "}";

	public static final String FRAG =
		// GL ES specific stuff
		"#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" + //
			"varying LOWP vec4 vColor;\n" + "varying vec2 vTexCoord;\n" + "uniform sampler2D u_texture;\n"//
			+ "uniform float " + SHADER_PARAMETER.GRAYSCALE + ";\n"//
			+ "uniform float " + SHADER_PARAMETER.POSITION_X + ";\n"//
			+ "uniform float " + SHADER_PARAMETER.POSITION_Y + ";\n"//
			+ "uniform float " + SHADER_PARAMETER.RADIUS + ";\n"
			// uniform float POSITION_X;
			// uniform float POSITION_Y;
			// uniform float RADIUS;

			+ "void main() {\n" + "	vec4 texColor = texture2D(u_texture, vTexCoord);\n" + "	\n"
			+ "	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));\n"//
			+ "	float x = " + SHADER_PARAMETER.POSITION_X + ";\n"//
			+ "	float y = " + SHADER_PARAMETER.POSITION_Y + ";\n"//
			+ "	float r = " + SHADER_PARAMETER.RADIUS + ";\n"//
			+ "	texColor.rgb = mix(vec3(gray), texColor.rgb, " + SHADER_PARAMETER.GRAYSCALE + ");\n" + "	\n"
			+ "	gl_FragColor = texColor * vColor;\n" + "}";

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		Json.installComponent(new GoogleJson());

		// TexturePacker.installComponent(new RedTexturePacker());

		final File shaders = LocalFileSystem.ApplicationHome().child("shaders");

		final File input = LocalFileSystem.ApplicationHome().child("input");

		writeShadersPack(shaders, Names.newID("com.jfixby.tinto.scene-002.psd.shader"));
		writeShadersPack(shaders, Names.newID("com.jfixby.tinto.scene-010.psd.shader"));

	}

	private static void writeShadersPack (final File shaders, final ID package_id) throws IOException {

		final File output = shaders.child(package_id.toString());
		output.makeFolder();
		output.clearFolder();

		final File root_file = output.child(R3_SHADER_SETTINGS.ROOT_FILE_NAME);
		final ShadersContainer container = new ShadersContainer();
		createBW(container, "bw.circle", output, package_id);
		createSpine(container, "spine.light", output, package_id);

		final String params_data = getData(container);
		root_file.writeString(params_data);
		L.d("shader writing done", params_data);
	}

	private static void createBW (final ShadersContainer container, final String shader_id_postfix_string, final File output,
		final ID package_id) throws IOException {
		{

			final File content = output.child(shader_id_postfix_string);

			ID shader_id = Names.newID(shader_id_postfix_string);
			shader_id = package_id.child(shader_id);

			final File frag_file = content.child(R3_SHADER_SETTINGS.FRAG_FILE_NAME);
			final File vert_file = content.child(R3_SHADER_SETTINGS.VERT_FILE_NAME);

			frag_file.writeString(FRAG);
			vert_file.writeString(VERT);

			final ShaderInfo info = new ShaderInfo();
			{
				info.shader_id = shader_id.toString();
				info.shader_folder_name = content.getName();
				{
					final ShaderParameterInfo parameter = new ShaderParameterInfo(SHADER_PARAMETER.GRAYSCALE, "float", 1);
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
				{
					final ShaderParameterInfo parameter = new ShaderParameterInfo(SHADER_PARAMETER.RADIUS, "float", 0);
					info.parameters_list.add(parameter);
				}
			}
			container.shaders.add(info);

		}
	}

	private static void createSpine (final ShadersContainer container, final String shader_id_postfix_string, final File output,
		final ID package_id) throws IOException {
		{

			final File content = output.child(shader_id_postfix_string);

			ID shader_id = Names.newID(shader_id_postfix_string);
			shader_id = package_id.child(shader_id);

			final File frag_file = content.child(R3_SHADER_SETTINGS.FRAG_FILE_NAME);
			final File vert_file = content.child(R3_SHADER_SETTINGS.VERT_FILE_NAME);

			frag_file.writeString("#ifdef GL_ES\n" //
				+ "precision mediump float;\n" //
				+ "#endif\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;\n" //
				+ "uniform sampler2D u_normals;\n" //
				+ "uniform vec3 light;\n" //
				+ "uniform vec3 ambientColor;\n" //
				+ "uniform float ambientIntensity; \n" //
				+ "uniform vec2 resolution;\n" //
				+ "uniform vec3 lightColor;\n" //
				+ "uniform bool useNormals;\n" //
				+ "uniform bool useShadow;\n" //
				+ "uniform vec3 attenuation;\n" //
				+ "uniform float strength;\n" //
				+ "uniform bool yInvert;\n" //
				+ "\n" //
				+ "void main() {\n" //
				+ "  // sample color & normals from our textures\n" //
				+ "  vec4 color = texture2D(u_texture, v_texCoords.st);\n" //
				+ "  vec3 nColor = texture2D(u_normals, v_texCoords.st).rgb;\n" //
				+ "\n" //
				+ "  // some bump map programs will need the Y value flipped..\n" //
				+ "  nColor.g = yInvert ? 1.0 - nColor.g : nColor.g;\n" //
				+ "\n" //
				+ "  // this is for debugging purposes, allowing us to lower the intensity of our bump map\n" //
				+ "  vec3 nBase = vec3(0.5, 0.5, 1.0);\n" //
				+ "  nColor = mix(nBase, nColor, strength);\n" //
				+ "\n" //
				+ "  // normals need to be converted to [-1.0, 1.0] range and normalized\n" //
				+ "  vec3 normal = normalize(nColor * 2.0 - 1.0);\n" //
				+ "\n" //
				+ "  // here we do a simple distance calculation\n" //
				+ "  vec3 deltaPos = vec3( (light.xy - gl_FragCoord.xy) / resolution.xy, light.z );\n" //
				+ "\n" //
				+ "  vec3 lightDir = normalize(deltaPos);\n" //
				+ "  float lambert = useNormals ? clamp(dot(normal, lightDir), 0.0, 1.0) : 1.0;\n" //
				+ "  \n" //
				+ "  // now let's get a nice little falloff\n" //
				+ "  float d = sqrt(dot(deltaPos, deltaPos));  \n" //
				+ "  float att = useShadow ? 1.0 / ( attenuation.x + (attenuation.y*d) + (attenuation.z*d*d) ) : 1.0;\n" //
				+ "  \n" //
				+ "  vec3 result = (ambientColor * ambientIntensity) + (lightColor.rgb * lambert) * att;\n" //
				+ "  result *= color.rgb;\n" //
				+ "  \n" //
				+ "  gl_FragColor = v_color * vec4(result, color.a);\n" //
				+ "}");
			vert_file.writeString("attribute vec4 a_position;\n" //
				+ "attribute vec4 a_color;\n" //
				+ "attribute vec2 a_texCoord0;\n" //
				+ "uniform mat4 u_proj;\n" //
				+ "uniform mat4 u_trans;\n" //
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = a_color;\n" //
				+ "   v_texCoords = a_texCoord0;\n" //
				+ "   gl_Position =  u_projTrans * a_position;\n" //
				+ "}\n" //
				+ "");

			final ShaderInfo info = new ShaderInfo();
			{
				info.shader_id = shader_id.toString();
				info.shader_folder_name = content.getName();

			}
			container.shaders.add(info);

		}
	}

	private static String getData (final ShadersContainer container) {
		return Json.serializeToString(container).toString();
	}
}
