package com.home.wrm.server;

import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.home.wrm.server.logging.SmartLogger;

public class RemoteServiceServletInterceptor extends RemoteServiceServlet {
    private static final long serialVersionUID = 1L;

    private static final SmartLogger logger = SmartLogger.getLogger(RemoteServiceServletInterceptor.class);

    public RemoteServiceServletInterceptor() {
    }

    @Override
    public void init() throws ServletException {
        logger.debug("Spring GWT service exporter deployed");
    }

    @Override
    public String processCall(String payload) throws SerializationException {
        try {
            Object rpcSerlvetHandler = getBean(getThreadLocalRequest());
            RPCRequest rpcRequest = RPC.decodeRequest(payload, rpcSerlvetHandler.getClass(), this);
            onAfterRequestDeserialized(rpcRequest);
            
            Method rpcMethod = rpcRequest.getMethod();
            Object[] methodArguments = rpcRequest.getParameters();
            SerializationPolicy serializationPolicy = rpcRequest.getSerializationPolicy();
            
            logger.debug("Invoking " + rpcSerlvetHandler.getClass().getName() + "." + rpcMethod.getName());
            return RPC.invokeAndEncodeResponse(rpcSerlvetHandler, rpcMethod, methodArguments, serializationPolicy);
        } catch (IncompatibleRemoteServiceException ex) {
            log("An IncompatibleRemoteServiceException was thrown while processing this call.", ex);
            return RPC.encodeResponseForFailure(null, ex);
        }
    }

    protected Object getBean(HttpServletRequest request) {
        String service = getService(request);
        Object bean = getBean(service);
        if (!(bean instanceof RemoteService)) {
            throw new IllegalArgumentException("Spring bean is not a GWT RemoteService: " + service + " (" + bean + ")");
        }
        logger.debug("Bean for service " + service + " is " + bean);
        return bean;
    }

    protected String getService(HttpServletRequest request) {
        String url = request.getRequestURI();
        String service = url.substring(url.lastIndexOf("/") + 1);
        logger.debug("Service for URL " + url + " is " + service);
        return service;
    }

    protected Object getBean(String name) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        
        if (applicationContext == null) {
            throw new IllegalStateException("No Spring web application context found");
        }
        if (!applicationContext.containsBean(name)) {
            throw new IllegalArgumentException("Spring bean not found: " + name);
        } else {
            return applicationContext.getBean(name);
        }
    }

}
