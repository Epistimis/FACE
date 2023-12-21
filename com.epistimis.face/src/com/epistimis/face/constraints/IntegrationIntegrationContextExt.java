/**
 * 
 */
package com.epistimis.face.constraints;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;

import com.epistimis.face.face.IntegrationIntegrationContext;
import com.epistimis.face.face.IntegrationTSNodeConnection;
import com.epistimis.face.face.IntegrationUoPInputEndPoint;
import com.epistimis.face.face.IntegrationUoPOutputEndPoint;
import com.epistimis.face.face.UopPlatformSpecificComponent;
import com.epistimis.face.face.UopPortableComponent;
import com.epistimis.face.face.UopUnitOfPortability;

/**
 * 
 */
public class IntegrationIntegrationContextExt {

//	def: allReferencedComponents(): Set(UopUnitOfPortability) =
//			let dests = self.connection->collect(destination)->select(oclIsKindOf(face::IntegrationUoPInputEndPoint))->collect(oclAsType(face::IntegrationUoPInputEndPoint))  in
//			let sources = self.connection->collect(source)->select(oclIsKindOf(face::IntegrationUoPOutputEndPoint))->collect(oclAsType(face::IntegrationUoPOutputEndPoint)) in
//			let dcomps = dests->collect(connection)->collect(oclContainer) in
//			let scomps = sources->collect(connection)->collect(oclContainer) in 		
//			dcomps->asSet()->union(scomps->asSet())->collect(oclAsType(face::UopUnitOfPortability))->asSet()
	public static Set<UopUnitOfPortability> allReferencedComponents(IntegrationIntegrationContext self) {
		Set<UopUnitOfPortability> result = new HashSet<UopUnitOfPortability>();
		Set<UopUnitOfPortability> dcomps = 
				self.getConnection().stream()
									.map(IntegrationTSNodeConnection::getDestination)
									.filter(IntegrationUoPInputEndPoint.class::isInstance)
									.map(IntegrationUoPInputEndPoint.class::cast)
									.map(IntegrationUoPInputEndPoint::getConnection)
									.map(EObject::eContainer)
									.map(UopUnitOfPortability.class::cast)
									.collect(Collectors.toSet());

		Set<UopUnitOfPortability> scomps = 
				self.getConnection().stream()
									.map(IntegrationTSNodeConnection::getSource)
									.filter(IntegrationUoPOutputEndPoint.class::isInstance)
									.map(IntegrationUoPOutputEndPoint.class::cast)
									.map(IntegrationUoPOutputEndPoint::getConnection)
									.map(EObject::eContainer)
									.map(UopUnitOfPortability.class::cast)
									.collect(Collectors.toSet());

		result.addAll(dcomps);
		result.addAll(scomps);
		return result;
	}
//	
//		def: allReferencedPCs(): Set(UopPortableComponent) =
//			self.allReferencedComponents()->selectByKind(UopPortableComponent)->collect(oclAsType(UopPortableComponent))->asSet()
		public static Set<UopPortableComponent> allReferencedPCs(IntegrationIntegrationContext self) {
			return allReferencedComponents(self).stream()
					.filter(UopPortableComponent.class::isInstance)
					.map(UopPortableComponent.class::cast)
					.collect(Collectors.toSet());
		}
	
//	
//		def: allReferencedPSCs(): Set(UopPlatformSpecificComponent) =
//			self.allReferencedComponents()->selectByKind(UopPlatformSpecificComponent)->collect(oclAsType(UopPlatformSpecificComponent))->asSet()
		public static Set<UopPlatformSpecificComponent> allReferencedPSCs(IntegrationIntegrationContext self) {
			return allReferencedComponents(self).stream()
					.filter(UopPlatformSpecificComponent.class::isInstance)
					.map(UopPlatformSpecificComponent.class::cast)
					.collect(Collectors.toSet());
		}

}
