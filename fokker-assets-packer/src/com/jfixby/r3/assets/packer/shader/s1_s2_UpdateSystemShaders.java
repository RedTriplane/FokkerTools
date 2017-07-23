
package com.jfixby.r3.assets.packer.shader;

import java.io.IOException;

public class s1_s2_UpdateSystemShaders {

	public static void main (String[] args) throws IOException {
		s1_CreateSystemShaders.main(args);
		PackShaders.pack();

	}

}
