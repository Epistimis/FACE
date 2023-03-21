package com.epistimis.face.generator

import com.epistimis.face.face.UopClientServerConnection
import com.epistimis.face.face.UopCompositeTemplate
import com.epistimis.face.face.UopConnection
import com.epistimis.face.face.UopQueuingConnection
import com.epistimis.face.face.UopSingleInstanceMessageConnection
import com.epistimis.face.face.UopTemplate
import com.epistimis.face.face.UopTemplateComposition
import com.epistimis.face.face.UopUnitOfPortability
import com.epistimis.uddl.generator.QueryProcessor
import com.epistimis.uddl.query.query.QuerySpecification
import com.epistimis.uddl.uddl.PlatformCompositeQuery
import com.epistimis.uddl.uddl.PlatformEntity
import com.epistimis.uddl.uddl.PlatformQuery
import com.epistimis.uddl.uddl.PlatformQueryComposition
import com.google.inject.Inject
import java.util.ArrayList
import java.util.List
import java.util.Map
import java.util.SortedMap
import java.util.TreeMap
import org.eclipse.xtext.naming.IQualifiedNameProvider
import com.epistimis.uddl.uddl.ConceptualQuery
import com.epistimis.uddl.uddl.ConceptualQueryComposition
import com.epistimis.uddl.uddl.ConceptualCompositeQuery

/**
 * These are utilities that are used to handle the transition between Face -> Query -> Uddl
 */
class QueryUtilities {

	@Inject extension IQualifiedNameProvider qnp;

	@Inject extension QueryProcessor qp; 

	def dispatch List<PlatformEntity> getReferencedPlatformEntities(UopUnitOfPortability comp) {
		var List<PlatformEntity> entities = new ArrayList<PlatformEntity>();
		for (conn : comp.connection) {
			entities.addAll(conn.getReferencedPlatformEntities);
		}
//		// Figure out which Entities are referenced by this component
//		var referencedQueries = new TreeMap<String, PlatformQuery>();
//		for (conn : comp.connection) {
//			referencedQueries.putAll(conn.platformQueriesMap);
//		}
//		/**
//		 * Now get all the QuerySpecifications from these queries and, from those, get all the referenced Entities
//		 */
//		for (Map.Entry<String,PlatformQuery> entry : referencedQueries.entrySet) {
//			val PlatformQuery query = entry.value
//			val QuerySpecification spec = qp.processQuery(query);
//			entities.addAll(qp.matchQuerytoUDDL(query, spec));
//		}

		return entities;
	}

	def dispatch List<PlatformEntity> getReferencedPlatformEntities(UopConnection conn) {
		var List<PlatformEntity> entities = new ArrayList<PlatformEntity>();
		// Figure out which Entities are referenced by this component
		var referencedQueries = conn.platformQueriesMap;

		/**
		 * Now get all the QuerySpecifications from these queries and, from those, get all the referenced Entities
		 */
		for (Map.Entry<String,PlatformQuery> entry : referencedQueries.entrySet) {
			val PlatformQuery query = entry.value
			val QuerySpecification spec = qp.processQuery(query);
			entities.addAll(qp.matchQuerytoUDDL(query, spec));
		}

		return entities;
	}

	/**
	 * Get all the queries referenced by this Template/Connection. Recurses down through Composites to find everything, keeping only
	 * a single reference, ordered by the FQN of the query. Note that this returns only PlatformQuery, not PlatformCompositeQuery - 
	 * effectively flattening the list.
	 */
	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(UopTemplate templ) {
		var result = new TreeMap<String, PlatformQuery>();
		result.put(qnp.getFullyQualifiedName(templ.boundQuery).toString, templ.boundQuery);
		return result;
	}

	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(UopCompositeTemplate templ) {
		var result = new TreeMap<String, PlatformQuery>();
		for (UopTemplateComposition comp : templ.composition) {
			result.putAll(comp.type.platformQueriesMap);
		}
		return result;
	}

	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(UopQueuingConnection conn) {
		val result = conn.messageType?.platformQueriesMap;
		if (result !== null) {
			return result;
		} else {
			new TreeMap<String,PlatformQuery>();
		}
	}

	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(UopSingleInstanceMessageConnection conn) {
		val result = conn.messageType?.platformQueriesMap;
		if (result !== null) {
			return result;
		} else {
			return new TreeMap<String,PlatformQuery>();
		}
	}

	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(UopClientServerConnection conn) {
		var result = new TreeMap<String,PlatformQuery>();
		val reqs = conn.requestType?.platformQueriesMap;
		if (reqs !== null) {
			result.putAll(reqs);
		}
		val resp = conn.responseType?.platformQueriesMap;
		if (resp !== null) {
			result.putAll(resp);		
		}
		
		return result;
	}

	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(PlatformQuery query) {
		var result = new TreeMap<String, PlatformQuery>();
		result.put(qnp.getFullyQualifiedName(query).toString, query);
		return result;
	}

	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(PlatformCompositeQuery query) {
		var result = new TreeMap<String, PlatformQuery>();
		for (PlatformQueryComposition comp : query.composition) {
			result.putAll(comp.type.platformQueriesMap);
		}
		return result;
	}

	/**
	 * Get all the queries referenced by this Template/Connection. Recurses down through Composites to find everything, keeping only
	 * a single reference, ordered by the FQN of the query. Note that this returns only ConceptualQuery, not ConceptualCompositeQuery - 
	 * effectively flattening the list.
	 */
	def dispatch SortedMap<String, ConceptualQuery> conceptualQueriesMap(UopTemplate templ) {
		var result = new TreeMap<String, ConceptualQuery>();
		result.put(qnp.getFullyQualifiedName(templ.boundQuery?.realizes?.realizes).toString, templ.boundQuery?.realizes?.realizes);
		return result;
	}

	def dispatch SortedMap<String, ConceptualQuery> conceptualQueriesMap(UopCompositeTemplate templ) {
		var result = new TreeMap<String, ConceptualQuery>();
		for (UopTemplateComposition comp : templ.composition) {
			result.putAll(comp.type.conceptualQueriesMap);
		}
		return result;
	}

	def dispatch SortedMap<String, ConceptualQuery> conceptualQueriesMap(UopQueuingConnection conn) {
		return conn.messageType.conceptualQueriesMap;
	}

	def dispatch SortedMap<String, ConceptualQuery> conceptualQueriesMap(UopSingleInstanceMessageConnection conn) {
		return conn.messageType.conceptualQueriesMap;
	}

	def dispatch SortedMap<String, ConceptualQuery> conceptualQueriesMap(UopClientServerConnection conn) {
		var req = conn.requestType.conceptualQueriesMap;
		var resp = conn.responseType.conceptualQueriesMap;

		req.putAll(resp);
		return req;
	}

	def dispatch SortedMap<String, ConceptualQuery> conceptualQueriesMap(ConceptualQuery query) {
		var result = new TreeMap<String, ConceptualQuery>();
		result.put(qnp.getFullyQualifiedName(query).toString, query);
		return result;
	}

	def dispatch SortedMap<String, ConceptualQuery> conceptualQueriesMap(ConceptualCompositeQuery query) {
		var result = new TreeMap<String, ConceptualQuery>();
		for (ConceptualQueryComposition comp : query.composition) {
			result.putAll(comp.type.conceptualQueriesMap);
		}
		return result;
	}

}
