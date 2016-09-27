import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class ItemRecord {

  private int id;
  private int patronid;
  private int itemid;
  private int timesrenewed;
  private Timestamp datecheckedout;
  private Timestamp datecheckedin;

  public static final int MAX_WEEKS_LOANED = 2;
  public static final int MAX_TIME_RENEWED = 2;

  public ItemRecord(int patronid, int itemid) {
    this.patronid = patronid;
    this.itemid = itemid;
    this.timesrenewed = 0;
  }

  public int getId(){
    return this.id;
  }

  public int getItemId(){
    return this.itemid;
  }

  public int getPatronId(){
    return this.patronid;
  }

  public long getTimesRenewed(){
    long numWeeks = MAX_WEEKS_LOANED * (1+timesrenewed);
    return numWeeks;
  }

  public String getPatronName(){
    return Patron.find(this.patronid).getName();
  }

  public String getDueDate(){

    long numWeeks = MAX_WEEKS_LOANED * (1+timesrenewed);
    Timestamp time = new Timestamp(datecheckedout.getTime() + (numWeeks * 604800000));
    return time.toString();
  }

  public String getDateCheckedOut(){
    return datecheckedout.toString();
  }

  public String getDateCheckedIn(){
    return datecheckedin.toString();
  }

  public String renewBook(){
    if(this.timesrenewed <= MAX_TIME_RENEWED){
      timesrenewed++;
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE itemrecords SET timesrenewed = :timesrenewed WHERE id = :id";
          con.createQuery(sql)
            .addParameter("timesrenewed", this.timesrenewed)
            .addParameter("id", this.id)
            .executeUpdate();

      }
      return "Item has been renewed";
    } else
      return "Item cannot be renewed";
  }

  public String getItemTitle(){

    if(Book.find(this.itemid).getItemType() == 1){
      return Book.find(this.itemid).getTitle();
    }
    else if(CD.find(this.itemid).getItemType() == 3){
      return CD.find(this.itemid).getTitle();
    }
    else if(Magazine.find(this.itemid).getItemType() == 2){
      return Magazine.find(this.itemid).getTitle();
    }
    else{
      return "No Item Found";
    }
  }

  public static ItemRecord find(int id) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "SELECT * FROM itemrecords WHERE id=:id";
        ItemRecord itemrecord = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchFirst(ItemRecord.class);
        return itemrecord;
      }
    }

  // public static List<ItemRecord> findItemRecords(int itemId) {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "SELECT * FROM itemrecords WHERE itemid=:itemid";
  //     List<ItemRecord> itemrecords = con.createQuery(sql)
  //       .addParameter("itemid", itemId)
  //       .executeAndFetch(ItemRecord.class);
  //     return itemrecords;
  //   }
  // }

  // public static List<ItemRecord> findPatronRecords(int patronId) {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "SELECT * FROM itemrecords WHERE patronid=:patronid";
  //     List<ItemRecord> itemrecords = con.createQuery(sql)
  //       .addParameter("patronid", patronId)
  //       .executeAndFetch(ItemRecord.class);
  //     return itemrecords;
  //   }
  // }


  public void checkIn(){
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE itemrecords SET datecheckedin = now() WHERE id = :id";
        con.createQuery(sql).addParameter("id", this.id).executeUpdate();

        sql = "UPDATE items SET newestrecord = :crid WHERE id = :itemid";
          con.createQuery(sql)
            .addParameter("crid", -1)
            .addParameter("itemid", this.itemid)
            .executeUpdate();

      }
    }

  public void save() {
   try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO itemrecords (patronid, itemid, datecheckedout, timesrenewed) VALUES (:patronid, :itemid, now(), :timesrenewed)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("patronid", this.patronid)
      .addParameter("itemid", this.itemid)
      .addParameter("timesrenewed", this.timesrenewed)
      .executeUpdate()
      .getKey();

    sql = "UPDATE items SET newestrecord = :crid WHERE id = :itemid";
    con.createQuery(sql)
      .addParameter("crid", this.id)
      .addParameter("itemid", this.itemid)
      .executeUpdate();
   }
 }

}
