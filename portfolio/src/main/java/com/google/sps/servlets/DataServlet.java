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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("CommentSubmission").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<String> tasks = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      // long id = entity.getKey().getId();
      String name = (String) entity.getProperty("name");
      String comment = (String) entity.getProperty("comment");
      long timestamp = (long) entity.getProperty("timestamp");
      String userEmail = (String) entity.getProperty("email");
      
      tasks.add('"' + comment + '"' + " - " + userEmail);
    }

    Gson gson = new Gson();

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(tasks));

  }
  /**
  * When the user clicks the Submit button, the form sends a POST request to the URL specified in the form's action attribute. 
  * The server looks for a servlet that maps to that URL, and then runs its doPost() function. Data send via POST is stored in DataService.
  * In Order to send this POST request to /data URL, user must be logged in.
  */
    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = request.getParameter("name");
    String value = request.getParameter("comment");
    long timestamp = System.currentTimeMillis();

    UserService userService = UserServiceFactory.getUserService();
    String userEmail = userService.getCurrentUser().getEmail();

    Entity taskEntity = new Entity("CommentSubmission");
    taskEntity.setProperty("name", name);
    taskEntity.setProperty("comment", value);
    taskEntity.setProperty("timestamp", timestamp);
    taskEntity.setProperty("email", userEmail);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    // Redirect client back to the HTML page (i.e. Home page)
    response.sendRedirect("/");
  }


    /**
   * Converts an ArrayList instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(ArrayList<String> arr) {
    Gson gson = new Gson();
    String json = gson.toJson(arr);
    return json;
  }
}
