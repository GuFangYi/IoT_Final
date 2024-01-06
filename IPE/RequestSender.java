package org.eclipse.om2m.ipe.semantic;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.FilterUsage;
import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.constants.NotificationContentType;
import org.eclipse.om2m.commons.constants.Operation;
import org.eclipse.om2m.commons.constants.ResourceType;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.commons.resource.FilterCriteria;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.Resource;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.commons.resource.Subscription;
import org.eclipse.om2m.commons.resource.URIList;
import org.eclipse.om2m.datamapping.jaxb.Mapper;

public class RequestSender {
	private static Mapper mapper;

	public RequestPrimitive createRP(String targetId, Object content, BigInteger op, String requestContentType,
			Integer rt) {
		RequestPrimitive rp = new RequestPrimitive();
		rp.setFrom(Constants.ADMIN_REQUESTING_ENTITY);
		rp.setTo(targetId);
		rp.setContent(content);
		rp.setOperation(op);
		rp.setRequestContentType(requestContentType);
		if (rt != null) {
			rp.setResourceType(rt);
		}
		return rp;
	}

	public static ResponsePrimitive createResource(String targetId, Resource resource, int resourceType) {
		RequestPrimitive request = new RequestPrimitive();
		request.setFrom(Constants.ADMIN_REQUESTING_ENTITY);
		request.setTo(targetId);
		request.setResourceType(BigInteger.valueOf(resourceType));
		request.setRequestContentType(MimeMediaType.OBJ);
		request.setReturnContentType(MimeMediaType.XML);
		request.setContent(resource);
		request.setOperation(Operation.CREATE);
		System.out.println(request);
		return sm_Constants.CSE.doRequest(request);
	}

	public static ResponsePrimitive createAE(AE resource) {
		// TODO Auto-generated method stub
		System.out.println("create AE in "+"/" + Constants.CSE_ID);
		return createResource("/" + Constants.CSE_ID, resource, ResourceType.AE);
	}

	public static ResponsePrimitive createContainer(String targetId, Container resource) {
		System.out.println("create container in "+"/" + Constants.CSE_ID + "/" + targetId);
		return createResource("/" + Constants.CSE_ID + "/" + targetId, resource, ResourceType.CONTAINER);
	}
	
	public static ResponsePrimitive createContentInstance(String targetId, ContentInstance resource) {
		System.out.println("create contentInstance in "+ "/" + Constants.CSE_ID + "/" + targetId);
		return createResource("/" + Constants.CSE_ID + "/" + targetId, resource, ResourceType.CONTENT_INSTANCE);
	}

	public static ResponsePrimitive createSubscribe(String subName, String targetId, Boolean toNodeRed) {
		Subscription sub = new Subscription();
		sub.setName(subName);
		System.out.println("createSubscribe: " + subName + ", in " + targetId);
		if(toNodeRed)
			sub.getNotificationURI().add("http://localhost:1880/monitor");
		else
			sub.getNotificationURI().add("/in-cse/in-name/" + sm_Constants.AE_NAME);
		sub.setNotificationContentType(NotificationContentType.MODIFIED_ATTRIBUTES);
		return createResource(targetId, sub, ResourceType.SUBSCRIPTION);
	}

	public static List<String> Type_Discovery(String targetId, int ty) {
		FilterCriteria fc = new FilterCriteria();
		fc.setFilterUsage(FilterUsage.DISCOVERY_CRITERIA);
		fc.setResourceType(BigInteger.valueOf(ty));
		return do_Discovery(targetId, fc);
	}

	public static List<String> do_Discovery(String targetId, FilterCriteria fc) {
		RequestPrimitive rep = new RequestPrimitive();
		rep.setFrom(Constants.ADMIN_REQUESTING_ENTITY);
		rep.setTo(targetId);
		rep.setOperation(Operation.RETRIEVE);
		ResponsePrimitive resp = sm_Constants.CSE.doRequest(rep);
		if (resp.getResponseStatusCode().equals(ResponseStatusCode.OK)) {
			return ((URIList) mapper.stringToObj((String) resp.getContent())).getListOfUri();
		} else {
			return null;
		}
	}
	
	public static ResponsePrimitive getRequest(String targetId){
		RequestPrimitive request = new RequestPrimitive();
		request.setFrom(Constants.ADMIN_REQUESTING_ENTITY);
		request.setTo(targetId);
		request.setReturnContentType(MimeMediaType.XML);
		request.setOperation(Operation.RETRIEVE);
		request.setRequestContentType(MimeMediaType.OBJ);
		return sm_Constants.CSE.doRequest(request);
	}
}