package com.epistimis.face;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import com.epistimis.face.face.IntegrationTSNodeInputPort;
import com.epistimis.face.face.IntegrationTSNodeOutputPort;
import com.epistimis.face.face.IntegrationTransportNode;
import com.epistimis.face.face.IntegrationUoPInputEndPoint;
import com.epistimis.face.face.IntegrationUoPInstance;
import com.epistimis.face.face.IntegrationUoPOutputEndPoint;
import com.epistimis.face.face.UopComponentFramework;
import com.epistimis.face.face.UopCompositeTemplate;
import com.epistimis.face.face.UopLanguageRuntime;
import com.epistimis.face.face.UopTemplateComposition;
import com.epistimis.face.face.UopUoPModel;
import com.epistimis.uddl.UddlQNP;
import com.google.inject.Inject;


public class FaceQNP extends UddlQNP {

	@Inject IQualifiedNameConverter qnc;
	
	// UoP
	public  QualifiedName qualifiedName(UopTemplateComposition obj) {
		UopCompositeTemplate ce = (UopCompositeTemplate) obj.eContainer();
		return  getFullyQualifiedName(ce).append(obj.getRolename());
//		return QualifiedName.create(ce.getName(),obj.getRolename());
	}

	public  QualifiedName qualifiedName(UopLanguageRuntime obj) {
		UopUoPModel ctr = (UopUoPModel) obj.eContainer();

		return  getFullyQualifiedName(ctr).append(obj.getName()+ ":" + obj.getVersion());
//		return QualifiedName.create(ctr.getName(),obj.getName()+ ":" + obj.getVersion());		
	}
	
	public  QualifiedName qualifiedName(UopComponentFramework obj) {
		UopUoPModel ctr = (UopUoPModel) obj.eContainer();

		return  getFullyQualifiedName(ctr).append(obj.getName()+ ":" + obj.getVersion());
//		return QualifiedName.create(ctr.getName(),obj.getName()+ ":" + obj.getVersion());		

	}


	// Integration
	/**
	 * Per this (https://www.eclipse.org/forums/index.php?t=msg&th=1084491&goto=1776641&)
	 * we can't use cross references in a QNP. Instead, we can grab the actual text
	 * of the cross reference using NodeModelUtils (https://archive.eclipse.org/modeling/tmf/xtext/javadoc/2.3/org/eclipse/xtext/nodemodel/util/NodeModelUtils.html)
	 * (which is what we want anyway).
	 * 
	 * Note this is likely to be a QN - and we need the entire thing to make sure we don't have name
	 * collisions
	 */
	private QualifiedName getReferenceAsQN(EObject obj, String featureName) {
		EStructuralFeature refFeature = obj.eClass().getEStructuralFeature(featureName);
		// Should only be 1 node
		List<INode> nodes = NodeModelUtils.findNodesForFeature(obj, refFeature);
		INode node = nodes.get(0);

		// Since the string may itself be a qualified name, we need to parse it into segments
		QualifiedName refQN = qnc.toQualifiedName(node.getText());
		return refQN;
	}

	/**
	 * The name here should use the referenced messageType/ connection and index into the containing feature
	 * since it has no name field.
	 * @param obj
	 * @return
	 */
	public  QualifiedName qualifiedName(IntegrationUoPInputEndPoint obj) {
		IntegrationUoPInstance inst = (IntegrationUoPInstance) obj.eContainer();
		// TODO: Is it possible to have the same data flowing over multiple endpoints?
		// If so, we then need to using indexing into the input list as part of the name
		EStructuralFeature container = obj.eContainingFeature();
		EObjectContainmentEList<IntegrationUoPInputEndPoint> inputs = (EObjectContainmentEList<IntegrationUoPInputEndPoint>) inst.getInput();
		int ndx = inputs.basicIndexOf(obj);

		return  getFullyQualifiedName(inst).append(container.getName()+Integer.toString(ndx));

		//QualifiedName connQN = getReferenceAsQN(obj,"connection");
//		return  getFullyQualifiedName(inst).append(connQN);
//		return  getFullyQualifiedName(inst).append(obj.getConnection().getName());
	}
	

	public  QualifiedName qualifiedName(IntegrationUoPOutputEndPoint obj) {
		IntegrationUoPInstance inst = (IntegrationUoPInstance) obj.eContainer();
		
		// TODO: Is it possible to have the same data flowing over multiple endpoints?
		// If so, we then need to using indexing into the output list as part of the name
		EStructuralFeature container = obj.eContainingFeature();
		EObjectContainmentEList<IntegrationUoPOutputEndPoint> outputs = (EObjectContainmentEList<IntegrationUoPOutputEndPoint>) inst.getOutput();
		int ndx = outputs.basicIndexOf(obj);
	
		return  getFullyQualifiedName(inst).append(container.getName()+Integer.toString(ndx));

//		QualifiedName connQN = getReferenceAsQN(obj,"connection");
//
//		return  getFullyQualifiedName(inst).append(connQN);
//		return QualifiedName.create(inst.getName(),obj.getConnection().getName());
	}


	public  QualifiedName qualifiedName(IntegrationTSNodeInputPort obj) {
		IntegrationTransportNode inst = (IntegrationTransportNode) obj.eContainer();
		// TODO: Is it possible to have the same data flowing over multiple endpoints?
		// If so, we then need to using indexing into the input list as part of the name
		EStructuralFeature container = obj.eContainingFeature();
		EObjectContainmentEList<IntegrationTSNodeInputPort> inports = (EObjectContainmentEList<IntegrationTSNodeInputPort>) inst.getInPort();
		int ndx = inports.basicIndexOf(obj);

		return  getFullyQualifiedName(inst).append(container.getName()+Integer.toString(ndx));

//		QualifiedName refQN = getReferenceAsQN(obj,"messageType");
//
//		return  getFullyQualifiedName(inst).append(refQN);
//		return QualifiedName.create(inst.getName(),obj.getMessageType().getName());
	}

	public  QualifiedName qualifiedName(IntegrationTSNodeOutputPort obj) {
		IntegrationTransportNode inst = (IntegrationTransportNode) obj.eContainer();
		// TODO: Is it possible to have the same data flowing over multiple endpoints?
		// If so, we then need to using indexing into the output list as part of the name
		EStructuralFeature container = obj.eContainingFeature();
		//IntegrationTSNodeOutputPort outport =  inst.getOutPort();

		// There is only 1 output port, so the index will always be zero
		return  getFullyQualifiedName(inst).append(container.getName()+"0");

//		QualifiedName refQN = getReferenceAsQN(obj,"messageType");
//
//		return  getFullyQualifiedName(inst).append(refQN);
//		return QualifiedName.create(inst.getName(),obj.getMessageType().getName());
	}

}

