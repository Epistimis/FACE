/*
 * generated by Xtext 2.33.0
 */
/*
 * Copyright (c) 2022 - 2024 Epistimis LLC (http://www.epistimis.com).
 */
package com.epistimis.face.generator

import com.epistimis.face.face.UopProgrammingLanguage
import com.epistimis.face.face.UopUnitOfPortability
import com.epistimis.uddl.uddl.PlatformEntity
import com.google.inject.Inject
import java.text.MessageFormat
import java.util.HashMap
import java.util.Map
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import org.eclipse.xtext.naming.IQualifiedNameProvider
import com.epistimis.uddl.exceptions.QueryMatchException
import org.apache.log4j.Logger

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class FaceGenerator extends AbstractGenerator {

//	@Inject
//	IResourceServiceProvider.Registry reg;
	@Inject extension IQualifiedNameProvider qnp;
	// @Inject extension IndexUtilities ndxUtil;
	// @Inject extension QueryProcessor qp; 
	@Inject extension QueryUtilities qu;

	static Logger logger = Logger.getLogger(FaceGenerator);

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
//		/**
//		 * Generate all the data structures
//		 */
//		val uddlGen = new UddlGenerator();
//		/** Eventually, we should only call the language specific generators directly for the selected entities referenced by
//		 * a UopUnitOfPortability - since we will also know which language to use (components are language specific)
//		 */
//		uddlGen.doGenerate(resource, fsa, context);
		// Make sure all cross references are resolved
		// EcoreUtil.resolveAll(resource.resourceSet);
		/**
		 * Set up the map of programming language specific generators
		 * TODO: This should be configurable externally
		 */
		val Map<UopProgrammingLanguage, IFaceLangGenerator> languageSpecificGenerators = new HashMap<UopProgrammingLanguage, IFaceLangGenerator>();
		languageSpecificGenerators.put(UopProgrammingLanguage.CPP, new CPPFunctionGenerator(qu));
		languageSpecificGenerators.put(UopProgrammingLanguage.GO, new GoFunctionGenerator(qu));
		languageSpecificGenerators.put(UopProgrammingLanguage.PYTHON, new PythonFunctionGenerator(qu));
		languageSpecificGenerators.put(UopProgrammingLanguage.SCALA, new ScalaFunctionGenerator(qu));
		languageSpecificGenerators.put(UopProgrammingLanguage.SQL, new RDBMSFunctionGenerator(qu));
		languageSpecificGenerators.put(UopProgrammingLanguage.TS, new TypescriptFunctionGenerator(qu));

		// Use Typescript for Javascript
		languageSpecificGenerators.put(UopProgrammingLanguage.JS, new TypescriptFunctionGenerator(qu));

		/**
		 * Generate the functions
		 */
		for (comp : resource.allContents.toIterable.filter(UopUnitOfPortability)) {

			try {

				val Map<String, PlatformEntity> entities = getReferencedPlatformEntities(comp);
				if (comp.transportAPILanguage != UopProgrammingLanguage.UNSPECIFIED) {
					// Now call the relevant generator
					val generator = languageSpecificGenerators.get(comp.transportAPILanguage);
					if (generator === null) {
						val fmttedMessage = MessageFormat.format(
							"Component {0} is supposed to be generated in {1} but no generator yet available for that language",
							qnp.getFullyQualifiedName(comp).toString(), comp.transportAPILanguage.toString);

						logger.info(fmttedMessage);
					} else {
						// for (PlatformEntity entity : entities.values) {
						generator.processEntities(entities.values, fsa, context);
						// }
						generator.processAComponent(comp, entities.values, fsa, context);
					}

				}
			} catch (QueryMatchException excp) {
				var fmttedMessage = "Unable to generate code: " + excp.toString;
				for (Throwable t: excp.suppressed) {
					fmttedMessage += "\n" + t.toString;
				}
				
				logger.info(fmttedMessage);
			}
		}
	}
}
