package org.eclipse.om2m.ipe.semantic;

import org.eclipse.om2m.core.service.CseService;

public class sm_Controller {

	public static void setCse(CseService cseService) {
		// TODO Auto-generated method stub
		System.out.println("setCSE: " + sm_Constants.CSE);
		sm_Constants.CSE = cseService;
	}

}