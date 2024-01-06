package org.eclipse.om2m.ipe.semantic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;

public class sm_Func {

	private static Log LOGGER = LogFactory.getLog(sm_Func.class);

	public static void createResources(String appId, String poa) {
		// TODO Auto-generated method stub
		// create the application resource

		AE ae = new AE();
		ae.setRequestReachability(true);
		ae.getPointOfAccess().add(poa);
		ae.setAppID(appId);
		ae.setName(appId);

		ResponsePrimitive response = RequestSender.createAE(ae);
		// Create Application sub-resources only if application not yet created
		if (response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)) {
			Container cnt = new Container();
			cnt.getLabels().add("semantic");
			cnt.setMaxNrOfInstances(BigInteger.valueOf(0));
			cnt = new Container();
			cnt.setMaxNrOfInstances(BigInteger.valueOf(10));
			// Create STATE container sub-resource
			cnt.setName(sm_Constants.DATA);
			LOGGER.info(RequestSender.createContainer(response.getLocation(), cnt));
		}
	}

	public static void handleCI_read(String con) {
		InputStream is = new ByteArrayInputStream(con.getBytes());
		sm_Constants.model.read(is, null, "TURTLE");
		sm_Constants.model.write(System.out, "TURTLE");
	}

	public static void handleCI_query(String con) {
		Query query = QueryFactory.create(con);
		QueryExecution qexec = QueryExecutionFactory.create(query, sm_Constants.model);
		try {
			ResultSetRewindable results = ResultSetFactory.makeRewindable(qexec.execSelect());
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				System.out.println(soln);
				Literal name = soln.getLiteral("result");
				System.out.println(name);
				try {
					URL url = new URL(name.toString());
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//	    	        conn.setDoOutput(false);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("X-M2M-Origin", "admin:admin");
					System.out.println(conn.getResponseCode());
					conn.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			qexec.close();
		}
	}

	public static void handleCI_update(String con) {
		UpdateRequest updateRequest = UpdateFactory.create(con);
		UpdateAction.execute(updateRequest, sm_Constants.model);
		sm_Constants.model.write(System.out, "TURTLE");
	}
}