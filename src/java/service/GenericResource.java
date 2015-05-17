/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import business.*;
import components.data.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

/**
 * REST Web Service
 *
 * @author Tushar
 */
@Path("Services")
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     * Retrieves representation of an instance of data.GenericResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/xml")
    //@Produces("text/plain")
    public String getServices() {
        //TODO return proper representation object
        return new XMLConvert(this.context.getBaseUri().toString()).introXml();
    }

    @Produces("text/xml")
    @Path("Appointments")
    @GET
    public String getAppointments() {
        Business b = new Business();
        String xml = new XMLConvert(this.context.getAbsolutePath().toString()).createAppointmentListXML(b.getallappointments());
        return xml;
    }

    @Produces("text/xml")
    @Path("Appointments/{appointmentid}")
    @GET
    public String getAppointment(@PathParam("appointmentid") String appointmentid) {
        String path = this.context.getBaseUri().toString() + "Services/Appointments/";
        XMLConvert xmlc = new XMLConvert(path);
        Business b = new Business();
        try {
            return xmlc.createAppointmentListXML(b.getappointment(appointmentid));
        } catch (Exception ex) {
            return xmlc.errorXml(ex.getMessage());
        }

    }

    /**
     * POST method for updating or creating an instance of GenericResource
     *
     * @param xml representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes("text/xml")
    @Produces("text/xml")
    @Path("Appointments")
    public String postAppointment(String xml) {
        XMLConvert xmlc = new XMLConvert(this.context.getAbsolutePath().toString());
        try {
            Appointment as = xmlc.createAppointment(xml);
            Business b = new Business();
            if (b.checkAppointment(as)) {
                return xmlc.successXml(as.getId());
            } else {
                return xmlc.errorXml("Error!");
            }
        } catch (Exception ex) {
            return xmlc.errorXml(ex.getMessage());
        }

    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param xml representation for the resource
     * @param appointmentid
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/xml")
    @Produces("text/xml")
    @Path("Appointments/{appointmentid}")
    public String putAppointment(String xml, @PathParam("appointmentid") String appointmentid) {
        XMLConvert xmlc = new XMLConvert(this.context.getAbsolutePath().toString());
        try {
            Appointment as = xmlc.createAppointment(xml, appointmentid);
            Business b = new Business();
            if (b.updateAppointment(as)) {
                return xmlc.successXml("");
            } else {
                return xmlc.errorXml("Error!");
            }
            //return as.getId();
        } catch (Exception ex) {
            return xmlc.errorXml(ex.getMessage());
        }
    }
}
