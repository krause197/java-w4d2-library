import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.sql2o.*;
import java.sql.Timestamp;


public class Book extends Item {

  private String author;
  public static final int DATABASE_TYPE = 1;

  public Book(String title, String author, int itemtype) {
    this.title = title;
    this.author = author;
    this.itemtype = DATABASE_TYPE;
    this.newestrecord = -1;
  }

  public String getAuthor(){
    return this.author;
  }

  public static List<Book> all() {
    String sql = "SELECT * FROM items WHERE itemtype=:itemtype";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .addParameter("itemtype", DATABASE_TYPE)
      .throwOnMappingFailure(false)
      .executeAndFetch(Book.class);
    }
  }

  public static Book find(int itemId) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM items WHERE id=:itemid";
      return con.createQuery(sql)
        .addParameter("itemid", itemId)
        .throwOnMappingFailure(false)
        .executeAndFetchFirst(Book.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO items (title, author, itemtype, newestrecord) VALUES (:title, :author, :itemtype, :newestrecord)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", this.title)
        .addParameter("author", this.author)
        .addParameter("itemtype", this.itemtype)
        .addParameter("newestrecord", this.newestrecord)
        .executeUpdate()
        .getKey();
    }
  }

}
