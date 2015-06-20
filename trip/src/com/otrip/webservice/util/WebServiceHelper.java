
package com.otrip.webservice.util;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;



import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

/**
 * Contains helper methods for web services
 * @author luis
 */
public class WebServiceHelper {

    private static final String CODE_PREFIX = "otrip";

    /**
     * Returns a SOAP fault
     */
    public static SoapFault fault(final Throwable exception) {
        WebServiceFault fault;
        if ((exception instanceof Exception) || (exception instanceof IllegalArgumentException)) {
            fault = WebServiceFaultsEnum.INVALID_PARAMETERS;
        }else if (exception instanceof Exception) {
            fault = WebServiceFaultsEnum.QUERY_PARSE_ERROR;
        } else if (exception instanceof Exception) {
            fault = WebServiceFaultsEnum.INVALID_CREDENTIALS;
        } else if (exception instanceof Exception) {
            fault = WebServiceFaultsEnum.BLOCKED_CREDENTIALS;
        } else if (exception instanceof AccessDeniedException || exception instanceof Exception) {
            fault = WebServiceFaultsEnum.UNAUTHORIZED_ACCESS;
        } else  if (exception instanceof Exception) {
            fault = WebServiceFaultsEnum.WS_NO_SESSION_ISNULL;
        } else  if (exception instanceof Exception) {
            fault = WebServiceFaultsEnum.WS_NO_LOGIN_ISNULL;
        } else {
            fault = WebServiceFaultsEnum.UNEXPECTED_ERROR;
        }

        return fault(fault, exception);
    }

    public static SoapFault fault(final WebServiceFault fault) {
        return fault(fault.code(), null);
    }

    public static SoapFault fault(final WebServiceFault fault, final String serverDetailsMessage) {
        return fault(fault, new Exception(serverDetailsMessage));
    }

    /**
     * Throw a SoapFault with the specified fault code and the specified Throwable as the cause
     */
    public static SoapFault fault(final WebServiceFault fault, final Throwable cause) {
        return fault(fault.code(), cause);
    }

    /**
     * Extract the username and password from the Authorization HTTP header, supporting both BASIC and USER authentications, or null when not informed
     */
    public static String[] getCredentials(final HttpServletRequest request) {
        final String header = request.getHeader("Authorization");
        if (StringUtils.isEmpty(header)) {
            return null;
        }
        final String[] parts = header.split("\\s");
        // There should be 2 parts separated by a blank space: the method and the username:password section
        if (parts.length != 2) {
            return null;
        }
        final String method = parts[0];
        String credentials = parts[1];
        if (method.equalsIgnoreCase("basic")) {
            credentials = new String(Base64.decodeBase64(credentials.getBytes()));
        } else if (!method.equalsIgnoreCase("user")) {
            // Unsupported method
            return null;
        }
        // Return the credentials parts
        return credentials.split(":", 2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T getParameter(final SoapMessage message) {
        final List parameterValues = message.getContent(List.class);
        if (CollectionUtils.isNotEmpty(parameterValues)) {
            return (T) parameterValues.iterator().next();
        } else {
            return (T) message.getContent(Object.class);
        }
    }

   

    /**
     * Checks whether the given fault was generated by Cyclos
     */
    public static boolean isFromCyclos(final Fault fault) {
        return CODE_PREFIX.equals(fault.getFaultCode().getNamespaceURI());
    }

    /**
     * Returns the HttpServletRequest instance for the given SOAP message
     */
    public static HttpServletRequest requestOf(final SoapMessage message) {
        return (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
    }

    /**
     * Returns the HttpServletRequest instance for the given SOAP message
     */
    public static ServletContext servletContextOf(final SoapMessage message) {
        return (ServletContext) message.get(AbstractHTTPDestination.HTTP_CONTEXT);
    }

    /**
     * Returns a SOAP fault
     */
    private static SoapFault fault(final String code, final Throwable th) throws SoapFault {
        return new SoapFault("Server error: " + code, th, faultCode(code));
    }

    /**
     * Returns a qualified name for a fault code
     */
    private static QName faultCode(final String code) {
        return new QName(CODE_PREFIX, code);
    }

    /*********************************************************************
     * @see login session key 
     *********************************************************************/
    private static final String ws_login_session_info_to_session_key="ws_login_session_info_to_session_key";
    private static final String ws_login_session_to_content_key="ws_login_session_to_content_key";

    public static final String getWsLoginSessionInfoToSessionKey(){
    	return ws_login_session_info_to_session_key;	//session保存key
    }
    public static String getWsLoginSessionToContentKey(){
    	return ws_login_session_to_content_key;	//登入session的key
    }


}
