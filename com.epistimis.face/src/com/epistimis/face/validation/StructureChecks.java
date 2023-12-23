package com.epistimis.face.validation;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;

import com.epistimis.face.face.IntegrationTSNodeConnection;
import com.epistimis.face.face.IntegrationUoPInputEndPoint;
import com.epistimis.face.face.IntegrationUoPOutputEndPoint;
import com.epistimis.face.face.UopConnection;
import com.epistimis.face.face.UopMessageType;

/**
 * Generalized validation methods to be used by both XText and Sirius. Placing this in the XText project because XText is the serialization 
 * for everything.
 */
public class StructureChecks {

	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	public StructureChecks() {
		// TODO Auto-generated constructor stub
	}

	public static boolean doConnectionsMatch(IntegrationTSNodeConnection conn) {
		logger.info("doConnectionsMatch called for connection " + conn.toString());

		IntegrationUoPOutputEndPoint source = (IntegrationUoPOutputEndPoint) conn.getSource();
		IntegrationUoPInputEndPoint dest = (IntegrationUoPInputEndPoint) conn.getDestination();

		UopConnection sourceConn = source.getConnection();
		UopConnection destConn = dest.getConnection();

		if (sourceConn.getClass() != destConn.getClass()) {
			// If these don't match, they can't possibly be the same.
			logger.error(" .... doConnectionsMatch - mismatched class ");
			return false;
		}
		if (sourceConn instanceof com.epistimis.face.face.UopClientServerConnection) {
			com.epistimis.face.face.UopClientServerConnection scu = (com.epistimis.face.face.UopClientServerConnection) sourceConn;
			com.epistimis.face.face.UopClientServerConnection dcu = (com.epistimis.face.face.UopClientServerConnection) destConn;
			if (scu.getRole() == dcu.getRole()) {
				// They should be opposites
				logger.error(" .... doConnectionsMatch - mismatched role (CS) ");
				return false;
			}
			// If the UopMessageTypes are not the same, they can't possibly be the same.
			UopMessageType scReqMsgType = scu.getRequestType();
			UopMessageType dcReqMsgType = dcu.getRequestType();
			UopMessageType scRespMsgType = scu.getResponseType();
			UopMessageType dcRespMsgType = dcu.getResponseType();
			if (((scReqMsgType != null) || (dcReqMsgType != null))
					&& ((scRespMsgType != null) || (dcRespMsgType != null))
					&& (!scReqMsgType.equals(dcReqMsgType) || !scRespMsgType.equals(dcRespMsgType))) {
				logger.error(" .... doConnectionsMatch - mismatched MsgType (CS) ");
				return false;
			}

			// If we get here, they should be the same.
			logger.info(" .... doConnectionsMatch -  good (CS) ");
			return true;
		}
		if (sourceConn instanceof com.epistimis.face.face.UopPubSubConnection) {
			// If the UopMessageTypes are not the same, they can't possibly be the same.
			UopMessageType scMsgType = ((com.epistimis.face.face.UopPubSubConnection) sourceConn).getMessageType();
			UopMessageType dcMsgType = ((com.epistimis.face.face.UopPubSubConnection) destConn).getMessageType();
			if (((scMsgType != null) || (dcMsgType != null)) && (!scMsgType.equals(dcMsgType))) {
				logger.error(" .... doConnectionsMatch - mismatched MsgType (PS) ");
				return false;
			}

			// If we get here, they should be the same.
			logger.info(" .... doConnectionsMatch -  good (PS) ");
			return true;

		}
		logger.info(" .... doConnectionsMatch - all good ");

		// If we get here, they should be the same.
		return true;
	}

}
