package de.mhus.osgi.dev.cxf.impl;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

public class WriterInterceptorExtension implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        System.out.println("JaxRsExtension:aroundWriteTo");
        context.proceed();
    }
}
