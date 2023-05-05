

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseImageUpdate {
    public static boolean UpdateImage(String path,String name,String imageTitle,String location,String people,String newName,String date){
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mariadb://127.0.0.1/Images", "root","159357a");

            PreparedStatement ps1=con.prepareStatement(
                    "select * from image_table where name=?");
            ps1.setString(1,newName);
            ResultSet rs=ps1.executeQuery();



            if(rs.next() && !name.equalsIgnoreCase(newName)){
                return false;
            }
            PreparedStatement ps=con.prepareStatement("update image_table set imageTitle=?,location=?,people=?,name=?,date=? where name=?");
            ps.setString(6,name);
            ps.setString(1,imageTitle);
            ps.setString(2,location);
            ps.setString(3,people);
            ps.setString(4,newName);
            ps.setString(5,date);

            Main.newImage.setName(newName);
            ps.executeUpdate();
            ps.close();
            con.close();


        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
