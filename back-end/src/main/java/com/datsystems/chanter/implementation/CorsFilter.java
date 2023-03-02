package com.datsystems.chanter.implementation;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.osgi.service.component.annotations.Component;

@Component(service = ContainerResponseFilter.class,
    property = { 
        "osgi.jaxrs.extension=true",
    }
)
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
    }
}
