/*
 * generated by Xtext 2.33.0
 */
/*
 * Copyright (c) 2022, 2023 Epistimis LLC (http://www.epistimis.com).
 */
package com.epistimis.face.web;

import com.epistimis.face.FaceRuntimeModule;
import com.epistimis.face.FaceStandaloneSetup;
import com.epistimis.face.ide.FaceIdeModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages in web applications.
 */
public class FaceWebSetup extends FaceStandaloneSetup {
	
	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new FaceRuntimeModule(), new FaceIdeModule(), new FaceWebModule()));
	}
	
}
