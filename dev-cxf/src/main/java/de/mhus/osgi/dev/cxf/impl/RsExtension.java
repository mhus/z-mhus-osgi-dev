package de.mhus.osgi.dev.cxf.impl;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

public class RsExtension implements ReaderInterceptor, WriterInterceptor, ContainerResponseFilter, ContainerRequestFilter {

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        System.out.println("RsExtension:aroundReadFrom");
        return context.proceed();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("RsExtension:ContainerRequestFilter");
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        System.out.println("RsExtension:ContainerResponseFilter");
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        System.out.println("RsExtension:aroundWriteTo");
        context.proceed();
    }

}
