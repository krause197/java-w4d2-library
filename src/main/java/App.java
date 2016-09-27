import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class App {

  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      model.put("itemtypes", ItemType.all());
      model.put("patrons", Patron.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/admin", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/admin.vtl");
      model.put("ITEM", Item.class);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/itemtypes/new", (request, response) -> {
      String typeName = request.queryParams("new-item-type-name");
      ItemType newItemType = new ItemType(typeName);
      newItemType.save();
      response.redirect("/admin");
      return "New Item Type Added";
    });

    get("/books", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/books.vtl");
      model.put("books", Book.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/books/:itemid", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int itemId = Integer.parseInt(request.params(":itemid"));
      Book book = Book.find(itemId);
      model.put("book", book);
      model.put("patrons", Patron.all());
      model.put("patronclass", Patron.class);
      model.put("template", "templates/book.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/books/new", (request, response) -> {
      String itemTitle = request.queryParams("new-item-title");
      String itemAuthor = request.queryParams("new-item-author");
      int itemTypeId = Integer.parseInt(request.queryParams("new-item-type"));
      Book newBook = new Book(itemTitle, itemAuthor, itemTypeId);
      newBook.save();
      response.redirect("/books");
      return "New Book Added";
    });

    post("/books/checkout", (request, response) -> {
      int patronId = Integer.parseInt(request.queryParams("item-checkout-patronid"));
      int itemId = Integer.parseInt(request.queryParams("item-checkout-itemid"));
      ItemRecord newItemRecord = new ItemRecord(patronId, itemId);
      newItemRecord.save();
      response.redirect("/books/" + itemId);
      return "Item Checked Out";
    });

    post("/books/renew", (request, response) -> {
      int itemId = Integer.parseInt(request.queryParams("item-renew-itemid"));
      ItemRecord newItemRecord = Book.find(itemId).getCurrentRecord();
      newItemRecord.renewBook();
      response.redirect("/books/" + itemId);
      return "Item Renewed";
    });

    post("/books/checkin", (request, response) -> {
      int itemId = Integer.parseInt(request.queryParams("item-checkin-itemid"));
      int itemRecordId = Integer.parseInt(request.queryParams("item-checkin-recordid"));
      ItemRecord newItemRecord = ItemRecord.find(itemRecordId);
      newItemRecord.checkIn();
      response.redirect("/books/" + itemId);
      return "Item has been checked in";
    });

    post("/books/delete", (request, response) -> {
      int itemId = Integer.parseInt(request.queryParams("item-delete-itemid"));
      Book newBook =  Book.find(itemId);
      newBook.deleteItem();
      response.redirect("/books");
      return "Book Deleted";
    });

    post("/cds/delete", (request, response) -> {
      int itemId = Integer.parseInt(request.queryParams("item-delete-itemid"));
      CD newCD =  CD.find(itemId);
      newCD.deleteItem();
      response.redirect("/cds");
      return "CD Deleted";
    });

    post("/magazines/delete", (request, response) -> {
      int itemId = Integer.parseInt(request.queryParams("item-delete-itemid"));
      Magazine newMagazine =  Magazine.find(itemId);
      newMagazine.deleteItem();
      response.redirect("/magazines");
      return "Magazine Deleted";
    });



    get("/magazines", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/magazines.vtl");
      model.put("magazines", Magazine.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/magazines/:itemid", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int itemId = Integer.parseInt(request.params(":itemid"));
      Magazine magazine = Magazine.find(itemId);
      model.put("magazine", magazine);
      model.put("patrons", Patron.all());
      model.put("patronclass", Patron.class);
      model.put("template", "templates/magazine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/magazines/new", (request, response) -> {
      String itemTitle = request.queryParams("new-item-title");
      String itemIssue = request.queryParams("new-item-issue");
      int itemTypeId = Integer.parseInt(request.queryParams("new-item-type"));
      Magazine newMagazine = new Magazine(itemTitle, itemIssue, itemTypeId);
      newMagazine.save();
      response.redirect("/magazines");
      return "New Magazine Added";
    });

    post("/magazines/checkout", (request, response) -> {
      int patronId = Integer.parseInt(request.queryParams("item-checkout-patronid"));
      int itemId = Integer.parseInt(request.queryParams("item-checkout-itemid"));
      ItemRecord newItemRecord = new ItemRecord(patronId, itemId);
      newItemRecord.save();
      response.redirect("/magazines/" + itemId);
      return "Item Checked Out";
    });

    post("/magazines/checkin", (request, response) -> {
      int itemId = Integer.parseInt(request.queryParams("item-checkin-itemid"));
      int itemRecordId = Integer.parseInt(request.queryParams("item-checkin-recordid"));
      ItemRecord newItemRecord = ItemRecord.find(itemRecordId);
      newItemRecord.checkIn();
      response.redirect("/magazines/" + itemId);
      return "Item has been checked in";
    });

    get("/cds", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/cds.vtl");
      model.put("cds", CD.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cds/:itemid", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int itemId = Integer.parseInt(request.params(":itemid"));
      CD cd = CD.find(itemId);
      model.put("cd", cd);
      model.put("patrons", Patron.all());
      model.put("patronclass", Patron.class);
      model.put("template", "templates/cd.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cds/new", (request, response) -> {
      String itemTitle = request.queryParams("new-item-title");
      String itemArtist = request.queryParams("new-item-artist");
      int itemTypeId = Integer.parseInt(request.queryParams("new-item-type"));
      CD newCD = new CD(itemTitle, itemArtist, itemTypeId);
      newCD.save();
      response.redirect("/cds");
      return "New CD Added";
    });

    post("/cds/checkout", (request, response) -> {
      int patronId = Integer.parseInt(request.queryParams("item-checkout-patronid"));
      int itemId = Integer.parseInt(request.queryParams("item-checkout-itemid"));
      ItemRecord newItemRecord = new ItemRecord(patronId, itemId);
      newItemRecord.save();
      response.redirect("/cds/" + itemId);
      return "Item Checked Out";
    });

    post("/cds/checkin", (request, response) -> {
      int itemId = Integer.parseInt(request.queryParams("item-checkin-itemid"));
      int itemRecordId = Integer.parseInt(request.queryParams("item-checkin-recordid"));
      ItemRecord newItemRecord = ItemRecord.find(itemRecordId);
      newItemRecord.checkIn();
      response.redirect("/cds/" + itemId);
      return "Item has been checked in";
    });

    // get("/collections/:itemtype", (request, response) -> {
    //   Map<String, Object> model = new HashMap<String, Object>();
    //   ItemType itemType = ItemType.find(request.params(":itemtype"));
    //   List<Item> items = Item.findAll(itemType.getId());
    //   model.put("items", items);
    //   model.put("itemtype", itemType);
    //   model.put("template", "templates/collection.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());



    post("/patrons/new", (request, response) -> {
      String patronName = request.queryParams("new-patron-name");
      String patronEmail = request.queryParams("new-patron-email");
      Patron newPatron = new Patron(patronName, patronEmail);
      newPatron.save();
      response.redirect("/");
      return "New Patron Added";
    });

    get("/patrons/:patronid", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int patronId = Integer.parseInt(request.params(":patronid"));
      Patron patron = Patron.find(patronId);
      model.put("patron", patron);
      //model.put("patronclass", Patron.class);
      //model.put("records", Patron.findPatronRecords(patronId));
      model.put("template", "templates/patron.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
