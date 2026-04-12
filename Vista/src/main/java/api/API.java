package api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Usuariorecepcionista;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Path("/app")
public class API{
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@QueryParam("filter") String filter) {
        // Returns a 200 OK with a JSON body
        HashMap<String, Object> map = new HashMap<>();
        if(filter != null) {
            if(filter.equals("ur")) {
                map.put("usuario_recepcionista", ServiceFacadeLocator.getInstanceURFacade().listarUsuarioR());
            }
            else if(filter.equals("ua")){
                map.put("usuario_administrador", ServiceFacadeLocator.getInstanceAAFacade().listarUA());
            }
        }
        else{
            map.put("usuario_recepcionista", ServiceFacadeLocator.getInstanceURFacade().listarUsuarioR());
            map.put("usuario_administrador", ServiceFacadeLocator.getInstanceAAFacade().listarUA());
        }
        return Response.ok(map).build();
    }

    @GET
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserID(@PathParam("id") String value) {
        // Returns a 200 OK with a JSON body
        if(ServiceFacadeLocator.getInstanceURFacade().obtenerUsuarioRPorId(value) != null) {
            return Response.ok(ServiceFacadeLocator.getInstanceURFacade().obtenerUsuarioRPorId(value)).build();
        }
        else if(ServiceFacadeLocator.getInstanceAAFacade().obtenerUsuarioAPorId(value) != null) {
            return Response.ok(ServiceFacadeLocator.getInstanceAAFacade().obtenerUsuarioAPorId(value)).build();
        }
        else{
            return Response.status(404).entity(new ArrayList<Usuariorecepcionista>()).build();
        }
    }
    @GET
    @Path("/product")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducto() {
        // Returns a 200 OK with a JSON body
        return Response.ok(ServiceFacadeLocator.getInstanceProductoFacade().listarProductos()).build();
    }
}

