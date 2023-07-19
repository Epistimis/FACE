package com.epistimis.face.scoping;

import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.xtext.scoping.impl.ImportUriResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

import com.epistimis.face.face.IntegrationTSNodeConnection;
//import com.google.inject.Singleton;
//import com.epistimis.face.face.ModelFile;
import com.google.inject.Inject;

public class FaceResourceDescriptionsStrategy extends DefaultResourceDescriptionStrategy {
	public static final String INCLUDES = "includes";
	@Inject
	ImportUriResolver uriResolver;

	public FaceResourceDescriptionsStrategy() {
		// TODO Auto-generated constructor stub
	}
	@Override 
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
//		if(eObject instanceof ModelFile) {
//			createEObjectDescriptionForModel((ModelFile)eObject, acceptor);
//			return true;
//		} else 
		if ((eObject instanceof IntegrationTSNodeConnection) 
				)
		{
			// don't index contents of a block
			return false;
		} else
			return super.createEObjectDescriptions(eObject, acceptor);
	}

//	protected void createEObjectDescriptionForModel(ModelFile model, IAcceptor<IEObjectDescription> acceptor) {
//		List<String> uris = new ArrayList<>();
//		model.getIncludes().stream().forEach( it -> uris.add(uriResolver.apply(it)));
//		Map<String,String> userData = new HashMap<>();
//	    userData.put(INCLUDES, IterableExtensions.join(uris, ","));
//		acceptor.accept(EObjectDescription.create(QualifiedName.create(model.eResource().getURI().toString()), model, userData));
//	}

}
