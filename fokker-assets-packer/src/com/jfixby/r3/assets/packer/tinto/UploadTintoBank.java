
package com.jfixby.r3.assets.packer.tinto;

import java.io.IOException;

import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileConflistResolver;
import com.jfixby.scarabei.api.file.FileSystem;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.api.s3.S3Component;
import com.jfixby.scarabei.aws.api.s3.S3FileSystem;
import com.jfixby.scarabei.aws.api.s3.S3FileSystemConfig;
import com.jfixby.scarabei.aws.desktop.s3.DesktopS3;

public class UploadTintoBank {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		Json.installComponent("com.jfixby.cmns.adopted.gdx.json.RedJson");
		S3.installComponent(new DesktopS3());
		final S3Component s3 = S3.invoke();
		final S3FileSystemConfig aws_specs = s3.newFileSystemConfig();
		aws_specs.setBucketName("com.red-triplane.assets");//
		final S3FileSystem S3 = s3.newFileSystem(aws_specs);
		final File remote = S3.ROOT().child("bank-tinto");

		final File local = LocalFileSystem.newFile(TintoAssetsConfig.TINTO_REMOTE_ASSETS_HOME).child("bank-tinto");

// remote.listAllChildren().print("remote");

// local.listAllChildren().print("local");

		final FileSystem FS = remote.getFileSystem();

		FS.copyFolderContentsToFolder(local, remote, FileConflistResolver.OVERWRITE_IF_NEW);

	}

}
