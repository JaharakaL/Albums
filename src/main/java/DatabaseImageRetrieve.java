import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DatabaseImageRetrieve {
    public static boolean ImageRetrieve(String name){

        ImageUploader image1 = Main.newImage;

        try{
            Class.forName("org.mariadb.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mariadb://127.0.0.1/Images","root","159357a");

            PreparedStatement ps=con.prepareStatement(
                    "select * from image_table where name=?");
            ps.setString(1,name);
            ResultSet rs=ps.executeQuery();


            if(rs.next()){
                byte[] img = rs.getBytes("image");
                ImageIcon image = new ImageIcon(img);
                Image im = image.getImage();
                Image myImg = im.getScaledInstance(image1.imageLabel.getWidth(),
                        image1.imageLabel.getHeight(),Image.SCALE_SMOOTH);
                ImageIcon newImage = new ImageIcon(myImg);
                image1.imageLabel.setIcon(newImage);
                image1.txtTitle.setText(rs.getString("imageTitle"));
                image1.txtPeopleWith.setText(rs.getString("people"));
                image1.txtLocation.setText(rs.getString("location"));
                image1.txtDateTaken.setText(rs.getString("date"));

                return true;
            }
            ps.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
