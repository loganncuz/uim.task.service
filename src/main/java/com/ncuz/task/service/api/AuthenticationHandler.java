package com.ncuz.task.service.api;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Node;

public class AuthenticationHandler implements SOAPHandler<SOAPMessageContext>{
	private String authenticationToken;
	 static final QName authenticationHeaderName =
		      new QName("urn:api.ecm.opentext.com", "OTAuthentication");
	 static final QName authenticationTokenName =
		      new QName("urn:api.ecm.opentext.com", "AuthenticationToken");
	 @SuppressWarnings("unused")
	private static final Set<QName> headers =
		      Collections.singleton(AuthenticationHandler.authenticationHeaderName);
	public AuthenticationHandler(String authenticationToken) {
	    this.authenticationToken = authenticationToken;
	  }

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		SOAPMessage message = context.getMessage();
	    if (message == null) { 
	      return true;
	    }

	    if ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
	      try {
	        SOAPHeader header = message.getSOAPHeader();
	        if (header == null) {
	          SOAPPart part = message.getSOAPPart();
	          SOAPEnvelope envelope = part.getEnvelope();
	          header = envelope.addHeader();
	        }
	        SOAPHeaderElement authenticationHeaderElement =
	            header.addHeaderElement(
	                AuthenticationHandler.authenticationHeaderName);
	        authenticationHeaderElement.setPrefix("");
	        SOAPElement authenticationTokenElement =
	            authenticationHeaderElement.addChildElement(
	                AuthenticationHandler.authenticationTokenName);
	        authenticationTokenElement.setPrefix("");
	        authenticationTokenElement.addTextNode(this.authenticationToken);
	      } catch (SOAPException soapException) { 
	        return false;
	      }
	    } else {
	      try {
	        SOAPHeader header = message.getSOAPHeader();
	        if (header != null) {
	          Iterator<?> headerElements = header.getChildElements(
	              AuthenticationHandler.authenticationHeaderName);
	          if (headerElements.hasNext()) {
	            SOAPHeaderElement headerElement =
	                (SOAPHeaderElement) headerElements.next();
	            Iterator<?> childElements = headerElement.getChildElements(
	                AuthenticationHandler.authenticationTokenName);
	            if (childElements.hasNext()) {
	              SOAPElement child = (SOAPElement) childElements.next();
	              Node tokenNode = child.getFirstChild();
	              if (tokenNode != null) {
	                this.authenticationToken = tokenNode.getNodeValue();;
	              }
	            }
	          }
	        }
	      } catch (SOAPException soapException) {
	        
	      }
	    }
	    return true;
	}
	
	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

}
