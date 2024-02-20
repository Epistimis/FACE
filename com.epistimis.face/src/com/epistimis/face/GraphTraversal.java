package com.epistimis.face;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

//import com.epistimis.face.face.FaceElement;
//import com.epistimis.face.face.IntegrationElement;
//import com.epistimis.face.face.IntegrationIntegrationModel;
import com.epistimis.face.face.IntegrationUoPInstance;
//import com.epistimis.face.face.IntegrationUoPOutputEndPoint;

/**
 * Basic graph traversal algorithms. These return nodes and/or edges,  or apply graph algorithms as 
 * the graph is walked. 
 * See https://wiki.eclipse.org/EMF_Search---Developer_Guide
 * https://stackoverflow.com/questions/2750717/how-can-i-traverse-the-emf-object-tree-generated-by-xtext
 * 
 * While there are already visitor tools for EMF, that is low level. Because of the way FACE models
 * are constructed, I create my own graph traversal code because it will know what is an edge and what is 
 * a node
 */
public class GraphTraversal {

	public GraphTraversal() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Check to see if o2 is downstream of o2 in the graph. Downstream is based
	 * on the direction of IntegrationTSNodeConnection source/destination.
	 * 
	 * Objects o1 and o2 can be any FACE Integration model elements.
	 * 
	 * @param o1 potentially upstream object. 
	 * @param o2 potentially downstream object
	 * @return returns true if o2 is downstream of o1 or if they are they same object.
	 */
	public static boolean isDownstream (EObject o1, EObject o2) {
		
		return false;
	}

	/**
	 * Return a set of all the IntegrationModel objects downstream of the root that we care 
	 * about. Note that this stops the depth first traversal when it detects cycles.
	 * 
	 * @param root the starting point of the graph.
	 * @param filter A list of classes. If specified, the results are filtered to include only 
	 * instances of those classes.
	 * @return
	 */
	public static Set<EObject> downstreamSubgraph(EObject root, @SuppressWarnings("rawtypes") List<Class> filter) {
		Set<EObject> result = new HashSet<>();
		
		return result;
	}
	/**
	 * May also need 
	 */
	
	public static Set<IntegrationUoPInstance> downstreamUoPInstances(IntegrationUoPInstance start) {
		Set<IntegrationUoPInstance> result = new HashSet<>();
		
		// Simplest approach is to find the containing IntegrationModel, then search all the 
		// IntegrationContexts in that model for TSNodeConnections that reference any of the 
		// output endpoints of this instance. Once we find those, we just follow the connections
		// until we find input endpoints on each path.
		
//		IntegrationIntegrationModel model = (IntegrationIntegrationModel) start.eContainer();
//		for (IntegrationElement elem: model.getElement()) {
//			
//		}
		
//		for (IntegrationUoPOutputEndPoint endPt: start.getOutput()) {
//			// Must traverse links until we get to an IntegrationUoPInputEndPoint
//			endPt.
//		}
		
		return result;
		
	}
}
