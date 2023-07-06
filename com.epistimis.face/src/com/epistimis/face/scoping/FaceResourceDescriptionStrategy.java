package com.epistimis.face.scoping;

import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.util.IAcceptor;

import com.epistimis.face.face.IntegrationTSNodeConnection;
//import com.google.inject.Singleton;

public class FaceResourceDescriptionStrategy extends DefaultResourceDescriptionStrategy {

	public FaceResourceDescriptionStrategy() {
		// TODO Auto-generated constructor stub
	}
	@Override 
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		if ((eObject instanceof IntegrationTSNodeConnection) 
				)
		{
			// don't index contents of a block
			return false;
		} else
			return super.createEObjectDescriptions(eObject, acceptor);
	}

}
