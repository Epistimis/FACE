/**
 * 
 */
package com.epistimis.face.constraints;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.epistimis.face.face.IntegrationElement;
import com.epistimis.face.face.IntegrationIntegrationContext;
import com.epistimis.face.face.IntegrationIntegrationModel;

/**
 * 
 */
public class IntegrationIntegrationModelExt {

	/**
	 * Returns a flattened list of all the IntegrationModels nested in this one
	 */
//	def: containedIMs():Set(IntegrationIntegrationModel) =
//		self->closure(im)->asSet()
	public static Set<IntegrationIntegrationModel> containedIMs(IntegrationIntegrationModel self) {
		Set<IntegrationIntegrationModel> result = new HashSet<IntegrationIntegrationModel>();
		for (IntegrationIntegrationModel im: self.getIm()) {
			result.add(im);
			result.addAll(containedIMs(im));
		}
		return result;
	}
	
	
	/**
	 * Returns a set of all the IntegrationElements in all IMs (this one and all its nested IMs)
	 */
//	def: containedElements(): Set(IntegrationElement) =
//		self.containedIMs()->collect(element)->asSet()

	public static Set<IntegrationElement> containedElements(IntegrationIntegrationModel self) {		
		return containedIMs(self).stream().map(IntegrationIntegrationModel::getElement)
										.flatMap(list->list.stream())
										.collect(Collectors.toSet());
		
	}
	
//	def: allIntegrationModels(): Set(IntegrationIntegrationModel) =
//			self->closure(im)
	public static Set<IntegrationIntegrationModel> allIntegrationModels(IntegrationIntegrationModel self) {
		Set<IntegrationIntegrationModel> result = containedIMs(self);
		result.add(self);
		return result;
	}
	
	
	/**
	 * All the IntegrationContexts (local and nested)
	 */
//	def: allIntegrationContexts(): Set(IntegrationIntegrationContext) =
//		let models = self.allIntegrationModels() in
//			models->collect(element->selectByKind(IntegrationIntegrationContext))->asSet()

	public static Set<IntegrationIntegrationContext> allIntegrationContexts(IntegrationIntegrationModel self) {
		return allIntegrationModels(self).stream()
					.map(IntegrationIntegrationModel::getElement)
					.flatMap(list ->list.stream())
					.filter(IntegrationIntegrationContext.class::isInstance)
					.map(IntegrationIntegrationContext.class::cast)
					.collect(Collectors.toSet());
	}
	
}
