package com.epistimis.face

import com.epistimis.face.face.UoPClientServerRole
import com.epistimis.face.face.UopClientServerConnection
import com.epistimis.face.face.UopCompositeTemplate
import com.epistimis.face.face.UopQueuingConnection
import com.epistimis.face.face.UopSingleInstanceMessageConnection
import com.epistimis.face.face.UopTemplate
import com.epistimis.face.face.UopTemplateComposition
import java.util.ArrayList
import java.util.List

/**
 * General methods for extracting info from various FACE derived classes - so we don't have to worry about the exact derived class we're dealing with.
 */
class FaceExtractor {
	
	def dispatch List<UopTemplate> inputTemplList(UopTemplate templ) {
			var List<UopTemplate> result = new ArrayList<UopTemplate>();
			result.add(templ);
			return result;
	}
	
	def dispatch List<UopTemplate> inputTemplList(UopCompositeTemplate templ) {
			var List<UopTemplate> result = new ArrayList<UopTemplate>();
			for (UopTemplateComposition comp : templ.composition) {
				val List<UopTemplate> compRes = inputTemplList(comp.type);
				result.addAll(compRes);
			}
			
			return result;		
	}


	def dispatch List<UopTemplate> inputTemplList(UopClientServerConnection conn){
		if (conn.role == UoPClientServerRole.CLIENT) {
			// This is the client in a C/S relationship, so this connection outputs the request and inputs the response
			return conn.requestType.inputTemplList;
		} else {
			// this is the server in a C/S relationship, so this connection inputs the request and outputs the response
			return conn.responseType.inputTemplList;
		}
	}
	/**
	 * QueueingConnections
	 */
	
	def dispatch List<UopTemplate> inputTemplList(UopQueuingConnection conn){
		return conn.messageType.inputTemplList;
	}
	def dispatch List<UopTemplate> inputTemplList(UopSingleInstanceMessageConnection conn){
		return conn.messageType.inputTemplList;
	}
	
	
}