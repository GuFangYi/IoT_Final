package org.eclipse.om2m.ipe.semantic;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.interworking.service.InterworkingService;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.MimeMediaType;

public class sm_Router implements InterworkingService {
	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();

	@Override
	public ResponsePrimitive doExecute(RequestPrimitive request) {
		// TODO Auto-generated method stub
		ResponsePrimitive response = new ResponsePrimitive(request);
		System.out.println("sm_Router1: " + request);
		DocumentBuilder builder;
		InputSource src;
		Document doc = null;
		String ty = null;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			src = new InputSource();
			src.setCharacterStream(new StringReader(request.getContent().toString()));

			doc = builder.parse(src);
			ty = doc.getElementsByTagName("ty").item(0).getTextContent();

			response.setResponseStatusCode(ResponseStatusCode.OK);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ty.equals("16")) {
			System.out.println("sm_Router2: " + ty);
			String csi = doc.getElementsByTagName("csi").item(0).getTextContent();
			System.out.println("sm_Router2_1: " + csi);
			RequestSender.createSubscribe("MONITOR_SUB_MN", csi, false);
//			RequestSender.createSubscribe("MONITOR_SUB_MN_NR", csi, true);
		} else if (ty.equals("2")) {
			System.out.println("sm_Router3: " + ty);
			String lbl = doc.getElementsByTagName("lbl").item(0).getTextContent();
			System.out.println("sm_Router3_1: " + lbl);
			if (lbl.contains("monitor") == true) {
				System.out.println("monitor");
				String ri = doc.getElementsByTagName("ri").item(0).getTextContent();
				RequestSender.createSubscribe("MONITOR_SUB_AE", ri, false);
//				RequestSender.createSubscribe("MONITOR_SUB_AE_NR", ri, true);
			}
		} else if (ty.equals("3")) {
			System.out.println("sm_Router4: " + ty);
			String ol = doc.getElementsByTagName("ol").item(0).getTextContent();
			System.out.println("sm_Router4_1: " + ol);
			if (ol.contains("monitor_cnt") == true) {
				System.out.println("monitor_cnt");
				String ri = doc.getElementsByTagName("ri").item(0).getTextContent();
				RequestSender.createSubscribe("MONITOR_SUB_CNT", ri, false);
//				RequestSender.createSubscribe("MONITOR_SUB_CNT_NR", ri, true);
			}
		} else if (ty.equals("4")) {
			System.out.println("sm_Router5: " + ty);
			String cnf = doc.getElementsByTagName("cnf").item(0).getTextContent();
			System.out.println("sm_Router5_1_test_1: " + cnf);
			String name = doc.getElementsByTagName("con").item(0).getTextContent();
			
			System.out.println("sm_Router5_1: " + name);
			switch (cnf) {
			case "post_ae":
				AE ae = new AE();
				ae.setRequestReachability(true);
				ae.setAppID("attack");
				ae.setName(name);
				RequestSender.createAE(ae);
				break;
			case "post_cnt":
				Container cnt = new Container();
				int index=name.lastIndexOf('/');
				String cnt_name = name.substring(index+1,name.length());
				cnt.setName(cnt_name);
				System.out.println("sm_Router5_1_post_cnt: " + cnt_name);
				RequestSender.createContainer(name.substring(0,index), cnt); 
				break;
			case "post_cin":
				ContentInstance cin = new ContentInstance();
				int index_cin=name.lastIndexOf('/');
				String cin_name = name.substring(index_cin+1,name.length());
				cin.setName(cin_name);
				double random = -10 + Math.random() * (50 - (-10));
				String con = "&lt;obj>&lt;str name=&quot;Temperature &quot; val=&quot;"+String.valueOf(random)+"&quot;/>&lt;/obj>";
				cin.setContent(con);
				cin.setContentInfo(MimeMediaType.XML);
				System.out.println("sm_Router5_1 post_cin: " + cin_name);
				System.out.println("sm_Router5_2: " + con);
				RequestSender.createContentInstance(name.substring(0,index_cin), cin); 
				break;
			case "post_sub":
				int index_sub=name.lastIndexOf('/');
				String sub_name = name.substring(index_sub+1,name.length());
				RequestSender.createSubscribe(sub_name,name.substring(0,index_sub), false); 
				break;
			case "get_ae":
				ResponsePrimitive response_getae = RequestSender.getRequest("/" + Constants.CSE_ID + "/" +name);
				System.out.println("sm_Router5_3: " + "/" + Constants.CSE_ID + "/" + name);
				System.out.println("sm_Router5_3: get ae\n" + response_getae.getContent());
//				System.out.println("sm_Router5_3: get ae" + response_getae.getResponseStatusCode());
//				System.out.println("sm_Router5_3: get ae" + String.valueOf(response_getae.getContent() instanceof AE));
				break;
			case "get_cnt":
				ResponsePrimitive response_getcnt = RequestSender.getRequest("/" + Constants.CSE_ID + "/" +name);
				System.out.println("sm_Router5_3: " + "/" + Constants.CSE_ID + "/" + name);
				System.out.println("sm_Router5_3: get cnt\n" + response_getcnt.getContent());
//				System.out.println("sm_Router5_3: get cnt" + response_getcnt.getResponseStatusCode());
//				System.out.println("sm_Router5_3: get cnt" + String.valueOf(response_getcnt.getContent() instanceof Container));
				break;
			case "get_cin":
				ResponsePrimitive response_getcin = RequestSender.getRequest("/" + Constants.CSE_ID + "/" +name);
				System.out.println("sm_Router5_4: " + "/" + Constants.CSE_ID + "/" + name);
				System.out.println("sm_Router5_4: get cin\n" + response_getcin.getContent());
//				System.out.println("sm_Router5_4: " + response_getcin.getResponseStatusCode());
//				System.out.println("sm_Router5_3: get cin" + String.valueOf(response_getcin.getContent() instanceof ContentInstance));
				break;
			case "get_sub":
				ResponsePrimitive response_getsub = RequestSender.getRequest("/" + Constants.CSE_ID + "/" +name);
				System.out.println("sm_Router5_4: " + "/" + Constants.CSE_ID + "/" + name);
				System.out.println("sm_Router5_4: get sub\n" + response_getsub.getContent());
				
			default:
				response.setContent("This cnf is not supported");
				break;
			}
		} else {
			System.out.println("sm_Router6: " + ty);
		}
		return response;
	}

	@Override
	public String getAPOCPath() {
		// TODO Auto-generated method stub
		return sm_Constants.POA;
	}
}
