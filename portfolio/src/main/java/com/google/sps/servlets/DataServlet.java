// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import java.util.ArrayList;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  private ArrayList<String> arrayList = new ArrayList<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    //ArrayList<String> arrayList = new ArrayList<>();
    //arrayList.add("Dog");
    //arrayList.add("Cat");
    //arrayList.add("Bird");

    // Convert the server stats to JSON
    String json = convertToJsonUsingGson(arrayList);

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
  /**
  * When the user clicks the Submit button, the form sends a POST request to the URL specified in the form's action attribute. 
  * The server looks for a servlet that maps to that URL, and then runs its doPost() function.
  */
    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String value = request.getParameter("comment");
    arrayList.add(value);

    Entity taskEntity = new Entity("Task");
    taskEntity.setProperty("comment", value);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }


    /**
   * Converts a ArrayList instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(ArrayList<String> arr) {
    Gson gson = new Gson();
    String json = gson.toJson(arr);
    return json;
  }
}
