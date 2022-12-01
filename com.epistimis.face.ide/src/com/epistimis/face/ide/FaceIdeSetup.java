/*
 * generated by Xtext 2.28.0
 */
package com.epistimis.face.ide;

import com.epistimis.face.FaceRuntimeModule;
import com.epistimis.face.FaceStandaloneSetup;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class FaceIdeSetup extends FaceStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new FaceRuntimeModule(), new FaceIdeModule()));
	}
	
}
