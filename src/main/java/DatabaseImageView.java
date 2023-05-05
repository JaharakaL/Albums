import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseImageView {
    ArrayList<byte[]>images = new ArrayList<>();
    ArrayList<String>info = new ArrayList<>();
    boolean flag = false;
    public DatabaseImageView(String name){

        ImageUploader image1 = Main.newImage;
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mariadb://127.0.0.1/Images","root","159357a");

//            PreparedStatement ps=con.prepareStatement(
//                    "select * from image_table where date like ?");
            PreparedStatement ps=con.prepareStatement(
                    "SELECT * from image_table WHERE (date LIKE'%"+ name +
                            "%' OR name LIKE'%"+ name +
                            "%' OR imageTitle LIKE'%"+ name +
                            "%' OR location LIKE'%"+ name +"%')");
//            ps.setString(1,"%" + name + "%");
            ResultSet rs=ps.executeQuery();

            while(rs.next()){
                String temp = "";
                temp = temp + "Image name: ";
                temp = temp + " ";
                temp = temp + " ";
                temp = temp + rs.getString("name");
                temp = temp + " ";
                temp = temp + " ";
                temp = temp + " ";
                temp = temp + " ";
//                temp = temp + rs.getString("location");
                temp = temp + " ";
                temp = temp + " ";
                temp = temp + " ";
                temp = temp + "Image date: ";
                temp = temp + " ";
                temp = temp + " ";
                temp = temp + rs.getString("date");
                info.add(temp);
                byte[] img = rs.getBytes("image");
                images.add(rs.getBytes("image"));
                flag = true;
            }
            ps.close();
            con.close();
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
        flag = false;
    }
}
