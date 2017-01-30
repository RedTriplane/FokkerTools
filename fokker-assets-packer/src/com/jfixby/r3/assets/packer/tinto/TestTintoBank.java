
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.net.http.Http;
import com.jfixby.scarabei.api.net.http.HttpFileSystem;
import com.jfixby.scarabei.api.net.http.HttpFileSystemSpecs;
import com.jfixby.scarabei.api.net.http.HttpURL;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class TestTintoBank {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();

		final String bankName = "bank-tinto";
		{

			final File assets_cache_folder = LocalFileSystem.ApplicationHome().child("assets-cache");
			assets_cache_folder.makeFolder();

			final HttpFileSystemSpecs http_specs = Http.newHttpFileSystemSpecs();
			final String urlString = "https://s3.eu-central-1.amazonaws.com/com.red-triplane.assets/" + bankName;
			final HttpURL url = Http.newURL(urlString);
			http_specs.setRootUrl(url);
			http_specs.setCacheSize(200);
			final HttpFileSystem fs = Http.newHttpFileSystem(http_specs);
			final File httpRemote = fs.ROOT();

			httpRemote.listAllChildren().print("all-children");

		}
	}

}
