package server.controllers;

import server.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Path("/client")
public class ClientController {

    @GET
    @Path("images/{path}")
    @Produces({"image/jpeg,image/png"})
    public byte[] getImageFile(@PathParam("path") String path) {
        return getFile("client/images/" + path);
    }

    @GET
    @Path("js/{path}")
    @Produces({"text/javascript"})
    public byte[] getJavaScriptFile(@PathParam("path") String path) {
        return getFile("client/js/" + path);
    }

    @GET
    @Path("css/{path}")
    @Produces({"text/css"})
    public byte[] getCSSFile(@PathParam("path") String path) {
        return getFile("client/css/" + path);
    }

    @GET
    @Path("html/{path}")
    @Produces({"text/html"})
    public byte[] getIHTMLFile(@PathParam("path") String path) {
        return getFile("client/html/" + path);
    }

    @GET
    @Path("flagicon.ico")
    @Produces({"image/x-icon"})
    public byte[] getFlagicon() {
        return getFile("client/flagicon.ico");
    }


    private byte[] getFile(String filename) {
        try {

            File file = new File("resources/" + filename);
            byte[] fileData = new byte[(int) file.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileData);
            dis.close();
            Logger.log("Sending: " + filename);
            return fileData;
        } catch (IOException ioe) {
            Logger.log("File IO error: " + ioe.getMessage());
        }
        return null;
    }

}