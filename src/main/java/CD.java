import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.sql2o.*;
import java.sql.Timestamp;


public class CD extends Item {

  private String artist;
  public static final int DATABASE_TYPE = 3;

  public CD(String title, String artist, int itemtype) {
    this.title = title;
    this.artist = artist;
    this.itemtype = DATABASE_TYPE;
    this.newestrecord = -1;
  }

  public String getArtist(){
    return this.artist;
  }

  public static List<CD> all() {
    String sql = "SELECT * FROM items WHERE itemtype=:itemtype";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .addParameter("itemtype", DATABASE_TYPE)
      .throwOnMappingFailure(false)
      .executeAndFetch(CD.class);
    }
  }

  public static CD find(int itemId) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM items WHERE id=:itemid";
      return con.createQuery(sql)
        .addParameter("itemid", itemId)
        .throwOnMappingFailure(false)
        .executeAndFetchFirst(CD.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO items (title, artist, itemtype, newestrecord) VALUES (:title, :artist, :itemtype, :newestrecord)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", this.title)
        .addParameter("artist", this.artist)
        .addParameter("itemtype", this.itemtype)
        .addParameter("newestrecord", this.newestrecord)
        .executeUpdate()
        .getKey();
    }
  }

}
