import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.sql2o.*;
import java.sql.Timestamp;

public abstract class Item {

  public int id;
  public String title;
  public int itemtype;
  public int newestrecord;

  public int getId(){
    return this.id;
  }

  public String getTitle(){
    return this.title;
  }

  public int getItemType(){
    return this.itemtype;
  }

  public String getItemTypeName(){
    return ItemType.getName(this.itemtype);
  }

  public int getnewestrecord(){
       return this.newestrecord;
   }

  public ItemRecord getCurrentRecord(){
      return ItemRecord.find(this.newestrecord);
  }

  // public String getPatron(){
  //   ItemRecord.find(this.newestrecord);
  // }

  public List<ItemRecord> getRecords() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM itemrecords WHERE itemid=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(ItemRecord.class);
    }
  }

  // public void updateRecord(int crid){
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "UPDATE items SET newestrecord = :crid WHERE id = :id";
  //     con.createQuery(sql)
  //       .addParameter("crid", crid)
  //       .addParameter("id", this.id)
  //       .executeUpdate();
  //     }
  // }

  public void deleteItem(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM items WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();

      sql = "DELETE FROM itemrecords WHERE itemid = :id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
      }
  }

  @Override
  public boolean equals(Object otherItem) {
    if (!(otherItem instanceof Item)) {
      return false;
    } else {
      Item newItem = (Item) otherItem;
      return this.getTitle().equals(newItem.getTitle()) &&
             this.getId() == newItem.getId();
    }
  }

}
