package com.example.fromscratch;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Scanner;
import java.util.stream.Stream;

@Path("/food")
public class HelloResource {

    //Prep for reading/writing to json file via gson
    Gson gson = new Gson();
    File myFile = new File(System.getProperty("user.dir") + "/file.json");
    //String jsonString = fileToString(myFile);
    //JsonArray jsonArray = gson.fromJson(jsonString, JsonArray.class);
    FileReader fileReader = new FileReader(myFile);
    JsonReader jsonReader = new JsonReader(fileReader);
    JsonArray jsonArray = gson.fromJson(jsonReader,JsonArray.class);

    public HelloResource() throws FileNotFoundException {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listMostRecentRecourse() {
        Response.Status ok = Response.Status.OK;
        String mostRecentElement = jsonArray.get(jsonArray.size() - 1).toString();

        return Response.status(ok).entity(mostRecentElement).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addToDatabase
            (String user_id, String business_id, String text, String date, int compliment_count) throws IOException {
        //populating my javaObject
        MyObject data = new MyObject();
            data.setUser_id(user_id);
            data.setBusiness_id(business_id);
            data.setText(text);
            data.setDate(date);
            data.setCompliment_count(compliment_count);

        //turning my javaObject into a JsonElement and adding it to my JsonArray
        JsonElement jsonElement = gson.toJsonTree(data, MyObject.class);
        jsonArray.add(jsonElement);

        //Create FileWriter & JsonWriter(using FileWriter) and use gson to write the JsonArray to the file.
        FileWriter file = new FileWriter(System.getProperty("user.dir") + "/file.json",false);
        JsonWriter jsonWriter = new JsonWriter(file);
        gson.toJson(jsonArray,JsonArray.class,jsonWriter);
//                   //ALTERNATIVE\\
//        jsonWriter.jsonValue(jsonArray.toString());
//                      \\ END //
        //file.flush();
        //jsonWriter.flush();

        Response.Status created = Response.Status.CREATED;
        return Response.status(created).build();
    }

    public static String fileToString(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        StringBuilder fileContents = new StringBuilder();
        while (scanner.hasNext()){
            fileContents.append(scanner.next());
        }
        return fileContents.toString();
    }
}