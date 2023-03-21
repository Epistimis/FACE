/**
 * 
 */
package com.epistimis.face.generator;

import java.util.List;

import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;

import com.epistimis.face.face.IntegrationUoPInstance;
import com.epistimis.face.face.UopUnitOfPortability;
import com.epistimis.uddl.generator.IUddlLangGenerator;
import com.epistimis.uddl.uddl.PlatformEntity;

/**
 * @author stevehickman
 *
 */
public interface IFaceLangGenerator extends IUddlLangGenerator {

	void processAnInstance(IntegrationUoPInstance inst,  IFileSystemAccess2 fsa, IGeneratorContext context);
	void processAComponent(UopUnitOfPortability comp, List<PlatformEntity> entities, IFileSystemAccess2 fsa, IGeneratorContext context);


}
