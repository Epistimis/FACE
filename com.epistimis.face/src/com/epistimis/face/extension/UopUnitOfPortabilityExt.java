/**
 * 
 */
package com.epistimis.face.extension;

import java.util.Map;
import java.util.HashMap;

import com.epistimis.face.face.UopUnitOfPortability;

/**
 * 
 */
public class UopUnitOfPortabilityExt {

	/**
	 * Helper method to collect info needed 
	 */
//	def: nameDescriptionLogic(): Tuple(name:String, description: String, logic:String ) =
//		let logicDesc =  self?.implementedBy?.description in
//		Tuple{ name: String = self.name, description: String = self.description, logic: String = logicDesc }  

	public static Map<String,String> nameDescriptionLogic(UopUnitOfPortability self) {
		Map<String,String> result = new HashMap<String,String>();
		result.put("name",self.getName());
		result.put("description",self.getDescription());
		if (self.getImplementedBy() != null) {
			result.put("logic",self.getImplementedBy().getDescription());
		}
		return result;
		
	}
}
