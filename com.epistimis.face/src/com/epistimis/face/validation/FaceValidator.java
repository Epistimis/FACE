/*
 * generated by Xtext 2.33.0
 */
/*
 * Copyright (c) 2022 - 2024 Epistimis LLC (http://www.epistimis.com).
 */
package com.epistimis.face.validation;

import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.EValidatorRegistrar;

import com.epistimis.face.TemplProcessor;
import com.epistimis.face.face.FacePackage;
import com.epistimis.face.face.UopTemplate;
import com.epistimis.face.generator.QueryUtilities;
import com.epistimis.face.templ.templ.MainTemplateMethodDecl;
import com.epistimis.face.templ.templ.TemplPackage;
import com.epistimis.face.templ.templ.TemplateSpecification;
import com.epistimis.face.templ.validation.TemplValidator;
import com.epistimis.uddl.util.IndexUtilities;
import com.google.inject.Inject;

/**
 * This class contains custom validation rules.
 *
 * See
 * https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
public class FaceValidator extends AbstractFaceValidator {

	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	static ClassLoader classLoader = FaceValidator.class.getClassLoader();

	@Inject
	IQualifiedNameProvider qnp;

//	@Inject
//	QueryProcessor qp;

	@Inject
	protected QueryUtilities qu;

	@Inject
	protected IndexUtilities ndxUtil;
	
	@Inject
	protected TemplProcessor tproc;
	
	@Inject
	protected TemplValidator templValidator;

	boolean conditionalsRegistered = false;


	protected static String ISSUE_CODE_PREFIX = "com.epistimis.face.";
	public static String CONSTRAINT_VIOLATION = ISSUE_CODE_PREFIX + "ConstraintViolation";

	@Override
	public IQualifiedNameProvider getQNP() {
		return this.qnp;
	}

	@Override
	public ClassLoader getClzLoader() {
		return FaceValidator.classLoader;
	}

	@Override
	public EPackage getPackage() {
		return FacePackage.eINSTANCE;
	}

	@Override
	public String getPluginID() {
		return com.epistimis.face.FaceRuntimeModule.PLUGIN_ID;
	}

	@Override
	protected void augmentRegistry(EPackage.Registry registry) {
		super.augmentRegistry(registry);
		registry.put(FacePackage.eNS_URI, FacePackage.eINSTANCE);
	}

	@Override
	protected EPackage.Registry createMinimalRegistry() {
		EPackage.Registry registry = super.createMinimalRegistry();
		registry.put(FacePackage.eNS_URI, FacePackage.eINSTANCE);
		return registry;
	}

	
	@Override
	public void register(EValidatorRegistrar registrar) {
		super.register(registrar);

//		loadOCLAndRegister(registrar,"src/com/epistimis/face/constraints/all-invariants.ocl",FacePackage.eINSTANCE, com.epistimis.face.FaceRuntimeModule.PLUGIN_ID);
		/**
		 * Registrations here are for OCL we ALWAYS want available. These provide
		 * foundational rules about the FACE metamodel
		 */
//		loadOCLAndRegister(registrar,"src/com/epistimis/face/constraints/face.ocl",FacePackage.eINSTANCE, com.epistimis.face.FaceRuntimeModule.PLUGIN_ID);
//		loadOCLAndRegister(registrar, "src/com/epistimis/face/constraints/uop.ocl",FacePackage.eINSTANCE, com.epistimis.face.FaceRuntimeModule.PLUGIN_ID);
//		loadOCLAndRegister(registrar, "src/com/epistimis/face/constraints/integration.ocl",FacePackage.eINSTANCE, com.epistimis.face.FaceRuntimeModule.PLUGIN_ID);

		/**
		 * These will be automatically loaded as needed by other files
		 */
//		loadOCLAndRegister(registrar, "src/com/epistimis/face/constraints/uddlquery.ocl",FacePackage.eINSTANCE, com.epistimis.face.FaceRuntimeModule.PLUGIN_ID);
//		loadOCLAndRegister(registrar,"src/com/epistimis/face/constraints/uopExtensions.ocl",FacePackage.eINSTANCE, com.epistimis.face.FaceRuntimeModule.PLUGIN_ID);
//		loadOCLAndRegister(registrar,"src/com/epistimis/face/constraints/integrationExtensions.ocl",FacePackage.eINSTANCE, com.epistimis.face.FaceRuntimeModule.PLUGIN_ID);

		logger.trace("FaceValidator::register done");
	}

	@Check
	public void checkMainMethodCount(UopTemplate templ) {
		
		QualifiedName containerName = qnp.getFullyQualifiedName(templ);
		TemplateSpecification tspec = tproc.parseTemplate(templ);
		// initialize for the current UopTemplate
		templValidator.init(containerName);
		
		long mainTemplateMethodCnt = tspec.getStructuredTemplateElementTypeDecl().stream()
											.filter(e -> { return e instanceof MainTemplateMethodDecl;} )
											.count();
		if (mainTemplateMethodCnt > 1) {
			error(MessageFormat.format(TemplValidator.TOO_MANY_MAIN_METHODS_FMT,containerName.toString(),mainTemplateMethodCnt),tspec,
					TemplPackage.eINSTANCE.getTemplateSpecification_StructuredTemplateElementTypeDecl(), 
					TemplValidator.NO_MORE_THAN_ONE_MM, containerName.toString());
		}
	}

//	/**
//	 * Load an OCL file from the specified URI and register any constraints found
//	 * therein. Note that this loads the entire file so contained operations should
//	 * be visible as well.
//	 * 
//	 * @param ocl The OCL instance associated the ResourceSet we are currently
//	 *            processing
//	 * @param uri The URI of the file to load
//	 * @return A map of constraints. The key is the class name the invariant is
//	 *         associated with.
//	 */
//	protected synchronized Map<String, Set<Constraint>> loadConstraintsFromFile(OCL ocl, URI uri) {
//
//		// parse the contents as an OCL document
//		Resource asResource = ocl.parse(uri);
//		// accumulate the document constraints in constraintMap and print all
//		// constraints
//		Map<String, Set<Constraint>> constraintMap = new HashMap<String, Set<Constraint>>();
//		for (TreeIterator<EObject> tit = asResource.getAllContents(); tit.hasNext();) {
//			EObject next = tit.next();
//			
//			// Operations are functions. Constraints are invariants
//			if (next instanceof Constraint) {
//				Constraint constraint = (Constraint) next;
//				Class container = (Class) next.eContainer();
//				String clzName = container.getName();
//				Set<Constraint> cSet = constraintMap.get(clzName);
//				if (cSet == null) {
//					cSet = new HashSet<Constraint>();
//					constraintMap.put(clzName, cSet);
//				}
//				cSet.add(constraint);
////				ExpressionInOCL expressionInOCL;
////				try {
////					expressionInOCL = ocl.getSpecification(constraint);
////					if (expressionInOCL != null) {
////						String name = constraint.getName();
////						if (name != null) {
////							constraintMap.put(name, expressionInOCL);
////							debugPrintf("%s: %s%n\n", name, expressionInOCL.getOwnedBody());
////						}
////					}
////				} catch (ParserException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//			}
//		}
//		return constraintMap;
//	}

}
