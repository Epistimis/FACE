/*
 * generated by Xtext 2.31.0
 */
/*
 * Copyright (c) 2022, 2023 Epistimis LLC (http://www.epistimis.com).
 */
package com.epistimis.face;


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
public class FaceStandaloneSetup extends FaceStandaloneSetupGenerated {

	public static void doSetup() {
		new FaceStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}
