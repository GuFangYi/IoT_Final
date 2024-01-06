package org.eclipse.om2m.ipe.semantic;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.core.service.CseService;
import org.eclipse.om2m.interworking.service.InterworkingService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
	/** Logger */
	private static Log logger = LogFactory.getLog(Activator.class);
	/** SCL service tracker */
	private ServiceTracker<Object, Object> cseServiceTracker;

	public void start(BundleContext bundleContext) throws Exception {
		logger.info("Register IpeService..");
		bundleContext.registerService(InterworkingService.class.getName(), new sm_Router(), null);
		logger.info("IpeService is registered.");
		cseServiceTracker = new ServiceTracker<Object, Object>(bundleContext, CseService.class.getName(), null) {
			public void removedService(ServiceReference<Object> reference, Object service) {
				logger.info("CseService removed");
			}

			public Object addingService(ServiceReference<Object> reference) {
				logger.info("CseService discovered");
				CseService cseService = (CseService) this.context.getService(reference);
				sm_Controller.setCse(cseService);
				// create Resource in in-cse
				sm_Func.createResources(sm_Constants.AE_NAME, sm_Constants.POA);
				RequestSender.createSubscribe("MONITOR_SUB", "/in-cse/in-name", false);
//				RequestSender.createSubscribe("MONITOR_SUB_NR", "/in-cse/in-name", true);
				return cseService;
			}
		};
		cseServiceTracker.open();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("Stopping Sample Ipe");
	}
}
