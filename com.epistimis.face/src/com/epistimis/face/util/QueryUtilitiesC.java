/**
 * 
 */
package com.epistimis.face.util;

import org.eclipse.emf.common.util.EList;

import com.epistimis.face.face.UopTemplate;
import com.epistimis.uddl.ConceptualQueryProcessor;
import com.epistimis.uddl.uddl.ConceptualCharacteristic;
import com.epistimis.uddl.uddl.ConceptualCompositeQuery;
import com.epistimis.uddl.uddl.ConceptualComposition;
import com.epistimis.uddl.uddl.ConceptualEntity;
import com.epistimis.uddl.uddl.ConceptualParticipant;
import com.epistimis.uddl.uddl.ConceptualQuery;
import com.epistimis.uddl.uddl.ConceptualQueryComposition;
import com.epistimis.uddl.uddl.ConceptualView;

/**
 * 
 */

public class QueryUtilitiesC extends
		QueryUtilities<ConceptualCharacteristic, ConceptualEntity, ConceptualComposition, ConceptualParticipant, ConceptualView, ConceptualQuery, ConceptualCompositeQuery, ConceptualQueryComposition, ConceptualQueryProcessor
//		, ConnectionProcessorC
		> {

	// @Override
	public ConceptualQuery getQuery(UopTemplate templ) {
		// This may throw an NPE since realizes is option
		return templ.getBoundQuery().getRealizes().getRealizes();
	}

	// @Override
	public boolean isCompositeQuery(ConceptualView query) {
		return (query instanceof ConceptualCompositeQuery);
	}

	// @Override
	public EList<ConceptualQueryComposition> getCompositions(ConceptualView query) {
		assert(query instanceof ConceptualCompositeQuery);
		return ((ConceptualCompositeQuery) query).getComposition();
	}

	// @Override
	public ConceptualView getCompositionType(ConceptualQueryComposition qc) {
		return qc.getType();
	}

	@Override
	public boolean isQuery(ConceptualView v) {
		// TODO Auto-generated method stub
		return (v instanceof ConceptualView);
	}

	@Override
	public ConceptualQuery conv2Query(ConceptualView v) {
		// TODO Auto-generated method stub
		return (ConceptualQuery)v;
	}

	@Override
	public ConceptualCompositeQuery conv2CompositeQuery(ConceptualView v) {
		// TODO Auto-generated method stub
		return (ConceptualCompositeQuery)v;
	}

}
