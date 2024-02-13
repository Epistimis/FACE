/**
 * 
 */
package com.epistimis.face;

import java.io.ByteArrayInputStream;
import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.naming.IQualifiedNameProvider;

import com.epistimis.face.face.UopTemplate;
import com.epistimis.face.templ.templ.TemplateSpecification;
import com.google.inject.Inject;

/***
* Processing Queries requires the following: 1) Identify all the entities
* referenced in the query (matchQuerytoUDDL) 2) Identify all the
* characteristics referenced in the query (selectCharacteristicsFromUDDL) 3)
* Process the joins (which will determine cardinality of the result)
* 
* 
* @author stevehickman
*
 * Process FACE Templates. Uses Template grammar to determine what has been referenced, then finds it
 * using boundQueries.
 * 
 * 1) Parse the template
 * 2) Find other referenced templates
 * 3) Find all the referenced queries
 * 
 */
public class TemplProcessor {
	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Inject
	IQualifiedNameProvider qnp;

	// Set up to process with correct parser
	/**
	 * Parse a Template and return a TemplateSpecification object
	 * 
	 * @param template The FACE Template containing the specification string.
	 * @return A TemplateSpecification object
	 */
	public TemplateSpecification parseTemplate(UopTemplate template) {
		String templateText = template.getSpecification();
		TemplateSpecification tspec = null;
		try {
			// See https://www.eclipse.org/forums/index.php?t=msg&th=173070/ for explanation
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource resource = resourceSet.createResource(URI.createURI("temp.templ"));
			ByteArrayInputStream bais = new ByteArrayInputStream(templateText.getBytes());
			resource.load(bais, null);
			tspec = (TemplateSpecification) resource.getContents().get(0);

		} catch (Exception e) {
			// TODO: This should also check for Parse errors - like unit tests do - and
			// print out something.
			logger.error("Template " + qnp.getFullyQualifiedName(template).toString() + " contains a malformed template: '"
					+ templateText + "'",e);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return tspec;
	}

}
