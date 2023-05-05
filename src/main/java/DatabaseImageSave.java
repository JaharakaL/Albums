import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseImageSave {
    public static boolean SaveImage(String path,String name,String imageTitle,String location,String people,String date){
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mariadb://127.0.0.1/Images", "root","159357a");

            File file=new File(path);
            FileInputStream fis=new FileInputStream(file);
            PreparedStatement ps1=con.prepareStatement(
                    "select image from image_table where name=?");
            ps1.setString(1,name);
            ResultSet rs=ps1.executeQuery();

            if(rs.next()){
                return false;
            }
            PreparedStatement ps=con.prepareStatement("insert into image_table (name,image,imageTitle,location,people,date) values(?,?,?,?,?,?)");
            ps.setString(1,name);
            ps.setBinaryStream(2,fis,(int)file.length());
            ps.setString(3,imageTitle);
            ps.setString(4,location);
            ps.setString(5,people);
            ps.setString(6,date);
            ps.executeUpdate();
            ps.close();
            fis.close();
            con.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public void massUpload(ArrayList<String> filePaths, String title){

        DatabaseImageSave dbSave=new DatabaseImageSave();
        String imageTitle=title;
        String people="";
        String location="";
        String date = "";

        for(String filePath:filePaths){
            Path path= Paths.get(filePath);
            String name=path.getFileName().toString();
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(new File(filePath));

                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        System.out.format("\n[%s] - %s = %s",
                                directory.getName(), tag.getTagName(), tag.getDescription());
                        if (directory.getName().equals("Exif IFD0") && tag.getTagName().equals("Date/Time")){
                            date = tag.getDescription();
                        }
                    }
                    if (directory.hasErrors()) {
                        for (String error : directory.getErrors()) {
                            System.err.format("ERROR: %s", error);
                        }
                    }
                }
            } catch (ImageProcessingException | IOException imageProcessingException) {
                imageProcessingException.printStackTrace();
            }
            DatabaseImageSave.SaveImage(path.toString(),name,imageTitle,location,people,date);
        }
    }
}

