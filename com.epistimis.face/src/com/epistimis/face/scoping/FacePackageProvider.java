/**
 * 
 */
package com.epistimis.face.scoping;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;

import com.epistimis.uddl.scoping.UddlPackageProvider;

/**
 * Provides the same EPackage access that Validators do  - except this is public instead of protected
 */
public class FacePackageProvider extends UddlPackageProvider {

	@Override
	public List<EPackage> getEPackages() {
		List<EPackage> result = new ArrayList<EPackage>(super.getEPackages());
		result.add(com.epistimis.face.face.FacePackage.eINSTANCE);
		return result;
	}

}
