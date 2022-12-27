package com.epistimis.face;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

import com.epistimis.face.face.IntegrationTSNodeInputPort;
import com.epistimis.face.face.IntegrationTSNodeOutputPort;
import com.epistimis.face.face.IntegrationTransportNode;
import com.epistimis.face.face.IntegrationUoPInputEndPoint;
import com.epistimis.face.face.IntegrationUoPInstance;
import com.epistimis.face.face.IntegrationUoPOutputEndPoint;
import com.epistimis.face.face.UopCompositeTemplate;
import com.epistimis.face.face.UopTemplateComposition;
import com.epistimis.uddl.UddlQNP;
import com.epistimis.uddl.uddl.LogicalCharacteristic;
import com.epistimis.uddl.uddl.LogicalEntity;


public class FaceQNP extends UddlQNP {

	// UoP
	public static QualifiedName qualifiedName(UopTemplateComposition obj) {
		UopCompositeTemplate ce = (UopCompositeTemplate) obj.eContainer();
		return QualifiedName.create(ce.getName(),obj.getRolename());
	}


	// Integration
	/**
	 * The name here should use the direction and index into the containing feature
	 * since it has no name field.
	 * @param obj
	 * @return
	 */
	public static QualifiedName qualifiedName(IntegrationUoPInputEndPoint obj) {
		IntegrationUoPInstance ce = (IntegrationUoPInstance) obj.eContainer();
		EStructuralFeature instanceInput = obj.eContainingFeature();

		return null;
//		return QualifiedName.create(obj.getName(),"input");
	}

	public static QualifiedName qualifiedName(IntegrationUoPOutputEndPoint obj) {
		IntegrationUoPInstance ce = (IntegrationUoPInstance) obj.eContainer();
		EStructuralFeature instanceInput = obj.eContainingFeature();

		return null;
//		return QualifiedName.create(obj.getName(),"output");
	}


	public static QualifiedName qualifiedName(IntegrationTSNodeInputPort obj) {
		IntegrationTransportNode ce = (IntegrationTransportNode) obj.eContainer();
		EStructuralFeature instanceInput = obj.eContainingFeature();

		return null;
//		return QualifiedName.create(obj.getName(),"input");
	}

	public static QualifiedName qualifiedName(IntegrationTSNodeOutputPort obj) {
		IntegrationTransportNode ce = (IntegrationTransportNode) obj.eContainer();
		EStructuralFeature instanceInput = obj.eContainingFeature();

		return null;
//		return QualifiedName.create(obj.getName(),"output");
	}

}

