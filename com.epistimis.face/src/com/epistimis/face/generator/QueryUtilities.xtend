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

/**
 * These are utilities that are used to handle the transition between Face -> Query -> Uddl
 */
class QueryUtilities {

	@Inject extension IQualifiedNameProvider qnp;

	@Inject extension QueryProcessor qp; 

	def dispatch List<PlatformEntity> getReferencedEntities(UopUnitOfPortability comp) {
		// Figure out which Entities are referenced by this component
		var referencedQueries = new TreeMap<String, PlatformQuery>();
		for (conn : comp.connection) {
			referencedQueries.putAll(conn.queriesMap);
		}
		/**
		 * Now get all the QuerySpecifications from these queries and, from those, get all the referenced Entities
		 */
		var List<PlatformEntity> entities = new ArrayList<PlatformEntity>();
		for (Map.Entry<String,PlatformQuery> entry : referencedQueries.entrySet) {
			val PlatformQuery query = entry.value
			val QuerySpecification spec = qp.processQuery(query);
			entities.addAll(qp.matchQuerytoUDDL(query, spec));
		}

		return entities;
	}

	def dispatch List<PlatformEntity> getReferencedEntities(UopConnection conn) {
		// Figure out which Entities are referenced by this component
		var referencedQueries = conn.queriesMap;

		/**
		 * Now get all the QuerySpecifications from these queries and, from those, get all the referenced Entities
		 */
		var List<PlatformEntity> entities = new ArrayList<PlatformEntity>();
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
	def dispatch SortedMap<String, PlatformQuery> queriesMap(UopTemplate templ) {
		var result = new TreeMap<String, PlatformQuery>();
		result.put(qnp.getFullyQualifiedName(templ.boundQuery).toString, templ.boundQuery);
		return result;
	}

	def dispatch SortedMap<String, PlatformQuery> queriesMap(UopCompositeTemplate templ) {
		var result = new TreeMap<String, PlatformQuery>();
		for (UopTemplateComposition comp : templ.composition) {
			result.putAll(comp.type.queriesMap);
		}
		return result;
	}

	def dispatch SortedMap<String, PlatformQuery> queriesMap(UopQueuingConnection conn) {
		return conn.messageType.queriesMap;
	}

	def dispatch SortedMap<String, PlatformQuery> queriesMap(UopSingleInstanceMessageConnection conn) {
		return conn.messageType.queriesMap;
	}

	def dispatch SortedMap<String, PlatformQuery> queriesMap(UopClientServerConnection conn) {
		var req = conn.requestType.queriesMap;
		var resp = conn.responseType.queriesMap;

		req.putAll(resp);
		return req;
	}

	def dispatch SortedMap<String, PlatformQuery> queriesMap(PlatformQuery query) {
		var result = new TreeMap<String, PlatformQuery>();
		result.put(qnp.getFullyQualifiedName(query).toString, query);
		return result;
	}

	def dispatch SortedMap<String, PlatformQuery> queriesMap(PlatformCompositeQuery query) {
		var result = new TreeMap<String, PlatformQuery>();
		for (PlatformQueryComposition comp : query.composition) {
			result.putAll(comp.type.queriesMap);
		}
		return result;
	}

}
