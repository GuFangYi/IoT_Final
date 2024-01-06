package org.eclipse.om2m.ipe.semantic;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.eclipse.om2m.core.service.CseService;

public class sm_Constants {
	public static final String POA = "ipe_monitor";
	protected static final String AE_NAME = "MONITOR";
	public static final String DATA = null;
	public static CseService CSE = null;
	public static Model model = ModelFactory.createDefaultModel();
}
