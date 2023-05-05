
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseImageDelete {

    public static boolean ImageDelete(String name){
        ImageUploader image1 = Main.newImage;

        try{
            Class.forName("org.mariadb.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mariadb://127.0.0.1/Images","root","159357a");

            PreparedStatement ps=con.prepareStatement(
                    "delete from image_table where name=?");
            ps.setString(1,name);
            ResultSet rs=ps.executeQuery();

            ps.close();
            con.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
