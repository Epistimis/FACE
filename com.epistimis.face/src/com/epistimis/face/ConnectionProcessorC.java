/**
 * 
 */
package com.epistimis.face;

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
public class ConnectionProcessorC extends
		ConnectionProcessor<ConceptualCharacteristic, ConceptualEntity, ConceptualComposition, ConceptualParticipant, ConceptualView, ConceptualQuery, ConceptualCompositeQuery, ConceptualQueryComposition, ConceptualQueryProcessor> {

	@Override
	protected ConceptualQuery getQuery(UopTemplate templ) {
		return templ.getBoundQuery().getRealizes().getRealizes();
	}

	@Override
	protected boolean isQuery(ConceptualView v) {
		return (v instanceof ConceptualQuery);
	}

	@Override
	protected boolean isCompositeQuery(ConceptualView v) {
		return (v instanceof ConceptualCompositeQuery);
	}

	@Override
	protected ConceptualQuery conv2Query(ConceptualView v) {
		return (ConceptualQuery)v;
	}

	@Override
	protected ConceptualCompositeQuery conv2CompositeQuery(ConceptualView v) {
		return (ConceptualCompositeQuery)v;
	}

}
