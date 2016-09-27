import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.sql2o.*;
import java.sql.Timestamp;


public class Magazine extends Item {

  private String issue;
  public static final int DATABASE_TYPE = 2;

  public Magazine(String title, String issue, int itemtype) {
    this.title = title;
    this.issue = issue;
    this.itemtype = DATABASE_TYPE;
    this.newestrecord = -1;
  }

  public String getIssue(){
    return this.issue;
  }

  public static List<Magazine> all() {
    String sql = "SELECT * FROM items WHERE itemtype=:itemtype";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .addParameter("itemtype", DATABASE_TYPE)
      .throwOnMappingFailure(false)
      .executeAndFetch(Magazine.class);
    }
  }

  public static Magazine find(int itemId) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM items WHERE id=:itemid";
      return con.createQuery(sql)
        .addParameter("itemid", itemId)
        .throwOnMappingFailure(false)
        .executeAndFetchFirst(Magazine.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO items (title, issue, itemtype, newestrecord) VALUES (:title, :issue, :itemtype, :newestrecord)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", this.title)
        .addParameter("issue", this.issue)
        .addParameter("itemtype", this.itemtype)
        .addParameter("newestrecord", this.newestrecord)
        .executeUpdate()
        .getKey();
    }
  }

}
