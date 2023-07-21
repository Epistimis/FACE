/*
 * generated by Xtext 2.28.0
 */
package com.epistimis.face;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
//import org.eclipse.xtext.scoping.IGlobalScopeProvider;

import com.epistimis.face.generator.QueryUtilities;
//import com.epistimis.face.scoping.FaceGlobalScopeProvider;
import com.epistimis.face.scoping.FaceResourceDescriptionsStrategy;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class FaceRuntimeModule extends AbstractFaceRuntimeModule {

	/**
	 * Concept taken from org.eclipse.ocl.examples.pivot.tests.PivotTestCase.java
	 * It appears that the idea is to uniquely identify the plugin. So the question is 
	 * "Where should this identifier be?" 
	 * 
	 * It seemed to me that the RuntimeModule is a [Schelling point](https://en.wikipedia.org/wiki/Focal_point_(game_theory))
	 * 
	 * The value should be the package name. If we can dynamically determine this, so much the better.
	 */
	public static final @NonNull String PLUGIN_ID = "com.epistimis.face";


	public Class<? extends ConnectionProcessor> bindConnectionProcessor() {
		return ConnectionProcessor.class;
	}

	public Class<? extends IntegrationContextProcessor> bindIntegrationContextProcessor() {
		return IntegrationContextProcessor.class;
	}

	@Override
	public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
		// TODO Auto-generated method stub
		return FaceQNP.class;
	}

	public Class<? extends QueryUtilities> bindQueryUtilities() {
		return QueryUtilities.class;
	}

	/** Enable this if there are performance issues with name resolution. And then look at the strategy to see what should
	 * be excluded from the index
	 * */
	public  Class<? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
		return FaceResourceDescriptionsStrategy.class;
	}


//	// Enable imports by uncommenting this. The default is to import anything visible in a project
//	// See section 3.3.1,3.3.2 of the Advanced XText Manual PDF 
//	// or https://blogs.itemis.com/en/in-five-minutes-to-transitive-imports-within-a-dsl-with-xtext
//	@Override
//	public
//	Class<? extends IGlobalScopeProvider> bindIGlobalScopeProvider() {
//		return FaceGlobalScopeProvider.class;
//	}

}
