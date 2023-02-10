package com.epistimis.face.generator

import com.epistimis.face.face.FaceElement
import com.epistimis.face.face.UopUnitOfPortability
import com.epistimis.uddl.generator.RDBMSDataStructureGenerator
import com.epistimis.uddl.uddl.PlatformDataModel
import com.epistimis.uddl.uddl.PlatformEntity
import java.util.ArrayList
import java.util.List

class RDBMSFunctionGenerator extends CommonFunctionGenerator implements IFaceLangGenerator {


	/**
	 * Eventually this code generator can look at the version of language (look up SupportingComponents for runtimes and see
	 * which version of the runtime is being used. The assumption is that the compiler must be at that version, so we can use
	 * language features from that version if desired)
	 */
	
	new(QueryUtilities qu) {
		super(qu,new RDBMSDataStructureGenerator());
	}
	
	override getSrcExtension() {
		return ".cpp";
	}
			
	/**
	 * Only UopUnitOfPortability will have CPP files
	 */
	override String generateFileName(FaceElement obj) {
		return generateDirectories(obj)+ getDirDelimiter() + obj.name + getSrcExtension();
	}
	
	/**
	 * Do common code generation for both Portable and PlatformSpecific Components.
	 * 
	 * Commonalities:
	 * 1) All the input ports define input parameters to the generated function.
	 * 2) The output port(s) define outputs from the generated function. If there is more than one output port
	 * then we must create either a Tuple or have in/out parameters (References in CPP)
	 * 3) Every function starts with a call to standard code to do dynamic checks (for consent) followed by
	 * an assertion that whatever makes it past that point is OK.
	 * 
	 * For each port, we need to make sure the data structure has been generated and the header for that structure 
	 * included in this file.
	 * 
	 * TODO: parameter list doesn't properly address the possibility of multiple entities matching a connection
	 */
	override compileUopCommon(UopUnitOfPortability uop,List<PlatformEntity> entities){
	'''
//	/** Include all needed headers */
//	«var entityIncludes = new ArrayList<PlatformEntity>»
//	«var List<PlatformDataModel> pdmIncludes = new ArrayList<PlatformDataModel>»
//	«FOR ent: entities»
//		«ent.generateInclude(pdmIncludes, entityIncludes)»
//	«ENDFOR»
//	public void «uop.name»(«FOR conn: uop.connection  SEPARATOR ','» «qu.getReferencedEntities(conn).get(0).typeString» «conn.name»«ENDFOR»)
//	{
//		/** The first step in this function must be a check for runtime privacy issues (e.g. where individual choices matter like Consent).
//		 *  This might be a null function
//		 */
//		bool hasConsent = checkConsents(«FOR conn: uop.connection  SEPARATOR ','» «conn.name»«ENDFOR»);	
//		
//		/**
//		* The remainder of this function body should be manually filled in
//		*/
//	}
	'''
	}

	

	
}