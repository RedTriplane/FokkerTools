
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileConflistResolver;
import com.jfixby.scarabei.api.file.FileSystem;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.net.http.Http;
import com.jfixby.scarabei.api.net.http.HttpFileSystem;
import com.jfixby.scarabei.api.net.http.HttpFileSystemSpecs;
import com.jfixby.scarabei.api.net.http.HttpURL;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.api.s3.S3Component;
import com.jfixby.scarabei.aws.api.s3.S3FileSystem;
import com.jfixby.scarabei.aws.api.s3.S3FileSystemConfig;
import com.jfixby.scarabei.aws.desktop.s3.DesktopS3;

public class UploadR3Bank {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		Json.installComponent("com.jfixby.cmns.adopted.gdx.json.RedJson");
		S3.installComponent(new DesktopS3());
		final S3Component s3 = S3.invoke();

		final String bankName = "bank-r3";

		{
			final S3FileSystemConfig aws_specs = s3.newFileSystemConfig();
			aws_specs.setBucketName("com.red-triplane.assets");//
			final S3FileSystem S3 = s3.newFileSystem(aws_specs);
			final File remote = S3.ROOT().child(bankName).child("tank-0");
			final File local = LocalFileSystem.newFile(TintoAssetsConfig.R3_LOCAL_ASSETS_HOME).child(bankName).child("tank-0");

			final FileSystem FS = remote.getFileSystem();

			FS.copyFolderContentsToFolder(local, remote, FileConflistResolver.OVERWRITE_ON_HASH_MISMATCH);
		}
		{

			final File assets_cache_folder = LocalFileSystem.ApplicationHome().child("assets-cache");
			assets_cache_folder.makeFolder();

			final HttpFileSystemSpecs http_specs = Http.newHttpFileSystemSpecs();
			final String urlString = "https://s3.eu-central-1.amazonaws.com/com.red-triplane.assets/" + bankName;
			final HttpURL url = Http.newURL(urlString);
			http_specs.setRootUrl(url);
			final HttpFileSystem fs = Http.newHttpFileSystem(http_specs);
			final File httpRemote = fs.ROOT();

			httpRemote.listAllChildren().print("all-children");

		}

	}

}
