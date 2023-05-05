import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MassUpload {
    public void massUpload(ArrayList<String> filePaths, String title){
        ImageUploader image1 = Main.newImage;

        DatabaseImageSave dbSave=new DatabaseImageSave();
        for(String filePath:filePaths){
            Path path= Paths.get(filePath);
            String name=path.getFileName().toString();
            String imageTitle=title;
            String people="";
            String location="";
            String date = " ";
            DatabaseImageSave.SaveImage(path.toString(),name,imageTitle,location,people,date);

        }
    }
}