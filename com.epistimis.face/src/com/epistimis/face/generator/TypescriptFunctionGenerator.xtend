package com.epistimis.face.generator

import com.epistimis.face.face.FaceElement
import com.epistimis.face.face.UopUnitOfPortability
import com.epistimis.uddl.generator.TypescriptDataStructureGenerator
import com.epistimis.uddl.uddl.PlatformDataModel
import com.epistimis.uddl.uddl.PlatformEntity
import java.util.ArrayList
import java.util.List
//import com.google.common.base.CaseFormat
import com.epistimis.face.face.UopConnection
import java.util.Collection
import java.util.Map
import org.apache.log4j.Logger
import java.lang.invoke.MethodHandles

class TypescriptFunctionGenerator extends CommonFunctionGenerator implements IFaceLangGenerator {

	static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Eventually this code generator can look at the version of language (look up SupportingComponents for runtimes and see
	 * which version of the runtime is being used. The assumption is that the compiler must be at that version, so we can use
	 * language features from that version if desired)
	 */
	
	new(QueryUtilities qu) {
		super(qu,new TypescriptDataStructureGenerator());
	}
	
	override getSrcExtension() {
		return ".ts";
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
	override compileUopCommon(UopUnitOfPortability uop,Collection<PlatformEntity> entities){
	'''
	/** Include all needed headers */
	«var entityIncludes = new ArrayList<PlatformEntity>»
	«var List<PlatformDataModel> pdmIncludes = new ArrayList<PlatformDataModel>»
	«FOR ent: entities»
		«ent.generateInclude(uop,pdmIncludes, entityIncludes)»
	«ENDFOR»
	export default function «uop.name»(«FOR conn: uop.connection  SEPARATOR ','» «conn.genParameter» «ENDFOR»)
	{
		/** The first step in this function must be a check for runtime privacy issues (e.g. where individual choices matter like Consent).
		 *  This might be a null function
		 */
		var hasConsent: boolean = true;
		«FOR conn: uop.connection» 
		hasConsent = hasConsent && check_consents(«conn.genParameterName»);
		«ENDFOR»
		
		if (!hasConsent) {
			return
		}
		
		/**
		* The remainder of this function body should be manually filled in
		*/
	}
	'''
	}

	def genFunctionName(UopUnitOfPortability uop) {
		return uop.name;
		//return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,uop.name);	
	}
	def genParameterName(UopConnection conn) {
		return conn.name;
		//return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,conn.name);
	}
	
	def genParameter(UopConnection conn) {
		val Map<String,PlatformEntity> pes = qu.getReferencedPlatformEntities(conn);
		if (pes.empty) {
			logger.error("Connection " + conn.fullyQualifiedName + " references missing type");
			return "<Missing parameter>"
		}

		return genParameterName(conn) + ": "  + pes.entrySet.get(0).value.typeString;
	}
	

	
}