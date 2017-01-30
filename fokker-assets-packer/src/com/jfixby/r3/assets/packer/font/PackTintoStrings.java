
package com.jfixby.r3.assets.packer.font;

import java.io.IOException;

import com.jfixby.r3.assets.packer.tinto.TintoAssetsConfig;
import com.jfixby.r3.ext.api.string.StringPackageEntry;
import com.jfixby.r3.ext.api.string.StringsPackage;
import com.jfixby.r3.rana.api.pkg.io.PackageDescriptor;
import com.jfixby.r3.rana.red.pkg.bank.PackageUtils;
import com.jfixby.rana.api.pkg.StandardPackageFormats;
import com.jfixby.scarabei.adopted.gdx.json.GdxJson;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.assets.Names;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.localize.StringValueID;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.localization.SimpleLocale;
import com.jfixby.scarabei.red.localization.SimpleLocalization;
import com.jfixby.scarabei.red.localization.SimpleLocalizationSpecs;

public class PackTintoStrings {

	static String RU = "русский";
	static String EN = "english";
	static String IT = "italiano";

	public static final void main (final String[] arg) throws IOException {
		ScarabeiDesktop.deploy();
		Json.installComponent(new GdxJson());

		final FilesList langFiles = LocalFileSystem.ApplicationHome().child("input").child("ru").listDirectChildren();

		final File bank = LocalFileSystem.newFile(TintoAssetsConfig.TINTO_REMOTE_ASSETS_HOME).child("tank-0");

		final ID package_id = Names.newID("com.jfixby.tinto.strings");
		final File package_folder = bank.child(package_id.toString());
		package_folder.makeFolder();
		package_folder.clearFolder();

		final File package_content_folder = package_folder.child(PackageDescriptor.PACKAGE_CONTENT_FOLDER);
		package_content_folder.makeFolder();
		final StringsPackage root = new StringsPackage();
		final List<ID> packed_texts = Collections.newList();

		for (int i = 2; i <= 20; i++) {
			final String index = ("" + (1000 + i)).substring(1, 4);
			final String scene_id = ".scene-" + index;
			final ID text_id = Names.newID(package_id.toString() + scene_id + ".txt");
			final ID string_id_prefix = Names.newID(package_id.parent().child("strings").toString() + scene_id + ".txt");
			add(text_id, string_id_prefix, root, i, packed_texts, langFiles, scene_id);
// L.d("----");
		} // com.jfixby.tinto.text.scene-002.txt

		final String localizations_list_file_name = package_id.child(StringsPackage.PACKAGE_FILE_EXTENSION).toString();
		final File root_file = package_content_folder.child(localizations_list_file_name);
		final String root_data = Json.serializeToString(root).toString();
		root_file.writeString(root_data);
		final Collection<ID> required = Collections.newList();
		PackageUtils.producePackageDescriptor(package_folder, StandardPackageFormats.RedTriplane.String, "1.0", packed_texts,
			required, localizations_list_file_name);
	}

	private static void add (final ID text_id, final ID string_id_prefix, final StringsPackage root, final int locale_index,
		final List<ID> packed_texts, final FilesList langFiles, final String scene_id) throws IOException {

// bank.listAllChildren().print("bank");
// Sys.exit();

// L.d("text_id ", text_id);
// L.d("string_id_prefix", string_id_prefix);

// langFiles.print("langFiles");
// Sys.exit();

		{
			final ID id = string_id_prefix.child(RU);
			packed_texts.add(id);

			final FilesList list = langFiles.filter(file -> file.getName().contains(scene_id));
// langFiles.print("langFiles");
			Debug.checkTrue("langFile" + " <<< " + text_id, list.size() > 0);
			final File langFile = list.getElementAt(0);
			Debug.checkNull("langFile" + " <<< " + text_id, langFile);

			final StringPackageEntry entry = new StringPackageEntry();
			entry.id = id.toString();
			root.entries.add(entry);
			entry.string_data = langFile.readToString();
		}
		{
			final ID id = string_id_prefix.child(EN);
			packed_texts.add(id);
			final StringPackageEntry entry = new StringPackageEntry();
			entry.id = id.toString();
			entry.string_data = Pangram_EN;
			root.entries.add(entry);
		}
		{
			final ID id = string_id_prefix.child(IT);
			packed_texts.add(id);
			final StringPackageEntry entry = new StringPackageEntry();
			entry.id = id.toString();
			entry.string_data = Pangram_IT;
			root.entries.add(entry);
		}

// com.jfixby.tinto.strings.scene-014.txt.italiano
// L.d();

	}

	public static final String Pangram_EN = "The quick brown fox jumps over the lazy dog.";
	public static final String Pangram_RU = "Съешь же ещё этих мягких французских булок, да выпей чаю.";
	public static final String Pangram_IT = "Quel vituperabile xenofobo zelante assaggia il whisky ed esclama: alleluja!";

	private static File packLanguage (final String key, final String value, final File package_content_folder,
		final String string_value_id_string, final ID locale_id) throws IOException {
		final SimpleLocalization localization = new SimpleLocalization();

		final StringValueID parameter_name = localization.getStringValuesContainer().spawnNewStringValueID(string_value_id_string);

		final SimpleLocalizationSpecs loc_specs = localization.newSimpleLocalizationSpecs();
		loc_specs.setLanguageName(key);
		final SimpleLocale localization_ru = localization.newLocalization(loc_specs);
		localization_ru.set(parameter_name, value);
		final File file = package_content_folder.child(locale_id.toString() + ".json");
		localization.writeToFile(file, localization_ru);

		return file;
	}
}
