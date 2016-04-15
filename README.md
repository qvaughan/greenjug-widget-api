1. Start by creating the widget-api project in Activator.
    * activator new
1. Open IntelliJ
1. Update project/plugins.sbt, uncomment
    * addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "1.0.0")
1. Change the plugin version to 3.0.0.
1. Update build.sbt with
    * `.enablePlugins(PlayJava, PlayEbean)`
    * `"mysql"                           % "mysql-connector-java"                % "5.1.36",`

1. Create the widgets schema in MySQL
1. Update application.conf with database configurations — Point out you can also make settings inside of application.conf as environment variables or override in separate files via import.
    ```
   play.evolutions {
      autoApply="true"
   }
   db.default {
      driver="com.mysql.jdbc.Driver"
      url="jdbc:mysql://127.0.0.1:3306/widgets"
      username="root"
      password="password"
   }
   ```

1. Create the evolutions file for creating the Widget table.
   * Create conf/evolutions/default directory
   * Create 1.sql
   ```
   # --- !Ups

   CREATE TABLE Widget (
      id varbinary(16) primary key,
      make varchar(255) not null,
      model varchar(255) not null
   );

   # --- !Downs

   DROP TABLE Widget;
   ```

1. Create the model class for Widget.
   * Create models.db package
   * Create Model class, with id, make, model as properties and a constructor with no args, all props, and (make, model)
   
   ```
   package models.db;

   import com.avaje.ebean.Model;
   import java.util.UUID;
   import javax.persistence.Entity;
   import javax.persistence.Id;
   /**
    * @author Michael Vaughan
    */
   @Entity
   public class Widget extends Model {

      @Id
      private UUID id;
      private String make;
      private String model;
    
      public Widget(UUID id, String make, String model) {
         this.id = id;
         this.make = make;
         this.model = model;
      }
    
      public Widget(String make, String model) {
         this.make = make;
         this.model = model;
      }
    
      public Widget() {}


      public UUID getId() {
         return id;
      }

      public void setId(UUID id) {
         this.id = id;
      }

      public String getMake() {
         return make;
      }

      public void setMake(String make) {
         this.make = make;
      }

      public String getModel() {
         return model;
      }

      public void setModel(String model) {
         this.model = model;
      }

   }
   ```
   
   * update application.conf with ebean directory.
      `ebean.default=["models.db.*"]`

1. Add ServerConfigStartup to use binary UUIDs
   ```
   package models.db;

   import com.avaje.ebean.config.ServerConfig;
   import com.avaje.ebean.config.ServerConfig.DbUuid;
   import com.avaje.ebean.event.ServerConfigStartup;

   /**
    * @author Michael Vaughan
    */
   public class WidgetsServerConfigStartup implements ServerConfigStartup {

      @Override
      public void onStart(ServerConfig serverConfig) {
         serverConfig.setDbUuid(DbUuid.BINARY);
      }
   }
   ```

1. Create models.dto package and create WidgetRequest.
   ```
   package models.dto;

   import models.db.Widget;

   /**
    * @author Michael Vaughan
    */
   public class WidgetRequest {

      private String make;
      private String model;

      public String getMake() {
         return make;
      }

      public void setMake(String make) {
         this.make = make;
      }

      public String getModel() {
         return model;
      }

      public void setModel(String model) {
         this.model = model;
      }

      public Widget toWidget() {
         return new Widget(make, model);
      }
   }
   ```

1. Create Widget Service interface, add methods to create a Widget, list Widgets, get a Widget, update a widget.

   ```
   package services;

   import models.db.Widget;
   import models.dto.WidgetRequest;
   import java.util.List;
   import java.util.UUID;
   
   /**
    * @author Michael Vaughan
    */
   public interface WidgetService {

      Widget create(WidgetRequest request);

      List<Widget> list(String make, String model);

      Widget get(UUID id);
   }
   ```

1. Create Widget Service Implementation.
   * Create services.impl package.
   * Create WidgetServiceImpl
   
   ```
   package services.impl;

   import com.avaje.ebean.Ebean;
   import com.avaje.ebean.ExpressionList;
   import models.db.Widget;
   import models.dto.WidgetRequest;
   import services.WidgetService;
   import java.util.List;
   import java.util.UUID;
   /**
    * @author Michael Vaughan
    */
   public class WidgetServiceImpl implements WidgetService {

      @Override
      public Widget create(WidgetRequest request) {
         Widget widget = request.toWidget();
         widget.save();
         return widget;
      }

      @Override
      public List<Widget> list(String make, String model) {
         ExpressionList<Widget> el = Ebean.find(Widget.class).where();
         if (make != null) {
            el = el.eq("make", make);
         }
         if (model != null) {
            el = el.eq("model", model);
         }
         return el.findList();
      }
    
      @Override
      public Widget get(UUID id) {
         return Ebean.find(Widget.class).where().idEq(id).findUnique();
      }

   }
   ```

1. Wire up WidgetService to WidgetServiceImpl in Module.
   `bind(WidgetService.class).to(WidgetServiceImpl.class);`

1. Create Widget controller to create a widget.
   ```
   package controllers;

   import models.db.Widget;
   import models.dto.WidgetRequest;
   import play.libs.Json;
   import play.mvc.Controller;
   import play.mvc.Result;
   import services.WidgetService;
   import javax.inject.Inject;
   /**
    * @author Michael Vaughan
    */
   public class WidgetController extends Controller {

      @Inject
      private WidgetService widgetService;

      public Result create() {
      WidgetRequest widgetRequest = Json.fromJson(request().body().asJson(), WidgetRequest.class);
         Widget widget = widgetService.create(widgetRequest);
         return ok(Json.toJson(widget));
      }

   }
   ```

1. Add route to to top of routes for creating a widget.
`POST    /widgets                        controllers.WidgetController.create`

1. Use Postman to create a widget. ( 1. Create Widget )

1. Add 2.sql evolution to add color column to the Widget table.
   ```
   # --- !Ups

   ALTER TABLE Widget ADD COLUMN color VARCHAR(255);

   # --- !Downs

   ALTER TABLE Widget DROP COLUMN color;
   ```

1. Add color property, getter/setter, and update non-id constructor to models.db.Widget.

1. Add color property and getter/setter, and update toWidget() in dto.WidgetRequest.

1. Create a widget in Postman with color property specified.

1. Update Widget Controller to list Widgets.
   ```
   public Result list(String make, String model) {
      List<Widget> widgets = widgetService.list(make, model, color);
      return ok(Json.toJson(widgets));
   }
   ```

1. Update WidgetService and WidgetServiceImpl to allow color on list method.

1. Update routes to list widgets, allowing filtering with query string.
`GET         /widgets             controllers.WidgetController.list(make: String ?= null, model: String ?= null, color: String ?= null)`

1. Demonstrate listing widgets with Postman, including filtering by make and model. (2. List Widgets)

1. Update Widget Controller to retrieve a specific widget.
   ```
   public Result get(UUID id) {
      return ok(Json.toJson(widgetService.get(id)));
   }
   ```
   
1. Update routes to update a specific widget.
   `GET         /widgets/:id            controllers.WidgetController.get(id: java.util.UUID)`

1. Demonstrate getting a specific widget with Postman.

1. Add annotation for action to authenticate a secret bearer token.
   * Create actions package.
   * Add class MagicWordAuth
      ```
      package actions;

      import play.mvc.Action;
      import play.mvc.Http.Context;
      import play.mvc.Http.Request;
      import play.mvc.Result;

      import java.util.concurrent.CompletableFuture;
      import java.util.concurrent.CompletionStage;

      /**
       * @author Michael Vaughan
       */
      public class MagicWordAuth extends Action.Simple {

         @Override
         public CompletionStage<Result> call(Context context) {
            Request request = context.request();
            if ("please".equalsIgnoreCase(request.getHeader("Authorization"))) {
               return delegate.call(context);
            }
            return CompletableFuture.completedFuture(unauthorized(“You didn’t say the magic word."));
         }
      }
      ```

1. Update the create/update widget paths to require the secret bearer token annotation.
   `@With(MagicWordAuth.class)`

1. Attempt to Create a widget in Postman with and without the “please” Authorization header.
