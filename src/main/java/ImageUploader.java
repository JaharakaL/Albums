import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class ImageUploader extends JFrame{
    JButton buttonUpload, buttonRemove,buttonSaver,buttonRetrieve,buttonDelete,buttonUpdate,buttonViewImages,buttonGeoLocation,buttonMassUpload;
    JLabel imageLabel,titleLabel,nameLabel,peopleWithLabel,locationLabel,dateTakenLabel;
    JTextField txtTitle,txtName,txtPeopleWith,txtLocation,txtDateTaken;
    private String path;
    private String name;
    private String[] tokensLat;
    private String[] tempLat;
    private String[] tokensLong;
    private String[] tempLong;
    String Lat;
    String Long;
    public ImageUploader(){
        super("Picture Viewer");
        buttonUpload = new JButton("Browse");
        buttonUpload.setBounds(170,300,150,40);
        imageLabel = new JLabel();
        imageLabel.setBounds(50,5,480,280);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        imageLabel.setBorder(border);
        add(imageLabel);
        add(buttonUpload);
//        String textDeleteButton = "Delete\nImage";
//        buttonDelete = new JButton("<html>" + textDeleteButton.replaceAll("\\n", "<br>") + "</html>");
        buttonDelete = new JButton("<html>Delete<br/>Image</html>");
        buttonDelete.setBounds(50,325,85,40);
        add(buttonDelete);
        buttonDelete.setEnabled(false);
        buttonRemove = new JButton("Remove Image");
        buttonRemove.setBounds(380,300,150,40);
        add(buttonRemove);
        buttonRemove.setEnabled(false);
        buttonSaver = new JButton("Save Image");
        buttonSaver.setBounds(170,350,150,40);
        add(buttonSaver);
        buttonRetrieve = new JButton("Retrieve Image");
        buttonRetrieve.setBounds(380,350,150,40);
        add(buttonRetrieve);
        buttonUpdate = new JButton("<html>Image<br/>Update</html>");
        buttonUpdate.setBounds(650,300,100,40);
        add(buttonUpdate);
        buttonViewImages = new JButton("<html>Image<br/>View</html>");
        buttonViewImages.setBounds(650,350,100,40);
        add(buttonViewImages);
        buttonGeoLocation = new JButton("<html>View<br/>Location</html>");
        buttonGeoLocation.setBounds(547,330,85,40);
        add(buttonGeoLocation);
        buttonGeoLocation.setEnabled(false);
        buttonMassUpload = new JButton("<html>Mass<br/>Upload</html>");
        buttonMassUpload.setBounds(760,330,85,40);
        add(buttonMassUpload);
        txtTitle = new JTextField("Title");
        txtTitle.setBounds(660,35,210,40);
        add(txtTitle);
        txtName = new JTextField("Name");
        txtName.setBounds(660,80,210,40);
        add(txtName);
        txtPeopleWith = new JTextField("People");
        txtPeopleWith.setBounds(660,125,210,40);
        add(txtPeopleWith);
        txtLocation = new JTextField("Location");
        txtLocation.setBounds(660,170,210,40);
        add(txtLocation);
        txtDateTaken = new JTextField("Date/Time");
        txtDateTaken.setBounds(660,215,210,40);
        add(txtDateTaken);
        titleLabel = new JLabel("Image Title");
        titleLabel.setBounds(550,35,210,40);
        titleLabel.setFont(new Font("Expressway",Font.BOLD, 16));
        add(titleLabel);
        nameLabel = new JLabel("Image Name");
        nameLabel.setBounds(550,85,125,25);
        nameLabel.setFont(new Font("Expressway",Font.BOLD, 16));
        add(nameLabel);
        peopleWithLabel = new JLabel("Tag People");
        peopleWithLabel.setBounds(550,130,125,25);
        peopleWithLabel.setFont(new Font("Expressway",Font.BOLD, 16));
        add(peopleWithLabel);
        locationLabel = new JLabel("Location");
        locationLabel.setBounds(550,180,125,25);
        locationLabel.setFont(new Font("Expressway",Font.BOLD, 16));
        add(locationLabel);
        dateTakenLabel = new JLabel("Date/Time");
        dateTakenLabel.setBounds(550,225,125,25);
        dateTakenLabel.setFont(new Font("Expressway",Font.BOLD, 16));
        add(dateTakenLabel);
        buttonUpdate.setEnabled(false);
        buttonUpdate.setEnabled(false);



        buttonMassUpload.addActionListener(e -> {

            UIManager.put("FileChooser.saveButtonText", "Open");
            UIManager.put("FileChooser.cancelButtonText", "Cancel");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = fileChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFolder = fileChooser.getSelectedFile();
                String folderPath = selectedFolder.getAbsolutePath();
                DatabaseImageSave ts = new DatabaseImageSave();

                ArrayList<String> filePaths = new ArrayList();
                String keyWord = "";
                String[] extensions = new String[]{"gif", "png", "bmp","jpg"};
                for (final File fileEntry : selectedFolder.listFiles()) {
                    if (!fileEntry.isDirectory()) {
                        for(String text : extensions) {
                            if (fileEntry.getName().endsWith("." + text))
                                filePaths.add(fileEntry.getAbsolutePath());
                        }
                    }
                }
                if(filePaths.size() > 0){
                    keyWord = JOptionPane.showInputDialog(getParent(),"Type word for Image Title","Enter word",JOptionPane.INFORMATION_MESSAGE);
                    if (keyWord == null) {
                        return;
                    }
                }else
                {
                    JOptionPane.showMessageDialog(getParent(), "No Images in the Folder", "", JOptionPane.INFORMATION_MESSAGE);

                }
                ts.massUpload(filePaths,keyWord);

                buttonRemove.setEnabled(true);
                buttonUpdate.setEnabled(true);
            }
          else if (result == JFileChooser.CANCEL_OPTION) {
                    System.out.println("No Folder Selected");
                    JOptionPane.showMessageDialog(getParent(), "No Image Selected", "", JOptionPane.INFORMATION_MESSAGE);
                }
        });
        buttonViewImages.addActionListener(e -> {
            String keyWord = JOptionPane.showInputDialog(getParent(),"Type word for search","Enter word",JOptionPane.INFORMATION_MESSAGE);
            if (keyWord == null) {
                return;
            }
            if (keyWord.equals("")){
                JOptionPane.showMessageDialog(getParent(), "You can't view the album if you leave it blank", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DatabaseImageView newImage = new DatabaseImageView(keyWord);
            if(!newImage.flag){
                JOptionPane.showMessageDialog(getParent(),"No images found with keyword " + keyWord,"Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            new AlbumViewer(newImage.images,newImage.info);
        });

        buttonUpload.addActionListener(e -> {

            UIManager.put("FileChooser.saveButtonText","Open");
            UIManager.put("FileChooser.cancelButtonText","Cancel");
            JFileChooser file = new JFileChooser();
            file.setCurrentDirectory(new File(System.getProperty("user.home")+ "/Desktop"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files",
                    "jpg","png","bmp","tiff","exif");
//            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            file.setAcceptAllFileFilterUsed(false);
            file.addChoosableFileFilter(filter);
            int result = file.showSaveDialog(null);

            if(result == JFileChooser.APPROVE_OPTION){
                File selectedFile = file.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                txtLocation.setText("Location");
                txtPeopleWith.setText("People");
                txtTitle.setText("Title");
                txtName.setText("Name");
                txtDateTaken.setText("Date/Time");
                this.path = path;
                imageLabel.setIcon(ResizeImage(path));
                buttonRemove.setEnabled(true);
                buttonGeoLocation.setEnabled(true);
                String date;
                String comment;
                String name;

                try {
                    Metadata metadata = ImageMetadataReader.readMetadata(new File(path));

                    for (Directory directory : metadata.getDirectories()) {
                        for (Tag tag : directory.getTags()) {
                            System.out.format("\n[%s] - %s = %s",
                                    directory.getName(), tag.getTagName(), tag.getDescription());
                            if (directory.getName().equals("Exif IFD0") && tag.getTagName().equals("Date/Time")){
                                txtDateTaken.setText(date = tag.getDescription());
                            }
                            if (directory.getName().equals("Exif SubIFD") && tag.getTagName().equals("User Comment")){
                                txtLocation.setText(comment = tag.getDescription());
                            }
                            if (directory.getName().equals("File") && tag.getTagName().equals("File Name")){
                                txtName.setText(removeExtension(name = tag.getDescription()));
                            }
                            if(directory.getName().equals("GPS") && tag.getTagName().equals("GPS Latitude")){
                                Lat = tag.getDescription();
                                System.out.println(Lat);
                                tokensLat = Lat.split(" ");
                                tokensLat[0] = tokensLat[0].substring(0,tokensLat[0].length()-1);
                                tokensLat[1] = tokensLat[1].substring(0,tokensLat[1].length()-1);
                                tokensLat[2] = tokensLat[2].substring(0,tokensLat[2].length()-1);
                                tempLat = tokensLat[2].split(",");
                                System.out.println(tempLat[0]);
                                System.out.println(tempLat[1]);
                            }
                            if(directory.getName().equals("GPS") && tag.getTagName().equals("GPS Longitude")){
                                Long = tag.getDescription();
                                System.out.println(Long);
                                tokensLong = Long.split(" ");
                                tokensLong[0] = tokensLong[0].substring(0,tokensLong[0].length()-1);
                                System.out.println(tokensLong[0]);
                                tokensLong[1] = tokensLong[1].substring(0,tokensLong[1].length()-1);
                                System.out.println(tokensLong[1]);
                                tokensLong[2] = tokensLong[2].substring(0,tokensLong[2].length()-1);
                                tempLong = tokensLong[2].split(",");
                                System.out.println(tempLong[0]);
                                System.out.println(tempLong[1]);
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
            }
            else if(result == JFileChooser.CANCEL_OPTION){
                JOptionPane.showMessageDialog(getParent(),"No Image Selected","",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonGeoLocation.addActionListener(e ->{
            try {
                Desktop.getDesktop().browse(new URL("https://maps.google.com/maps?q=%2B"+tokensLat[0]+"%C2%B0+"+tokensLat[1]+"'+"+tokensLat[2].replace(",",".")+"%22,+"+tokensLong[0]+"%C2%B0+"+tokensLong[1]+"'+"+tokensLong[2].replace(",",".")+"&ie=UTF-8").toURI());
            } catch (Exception d) {}
        });

        buttonDelete.addActionListener(e -> {
            if(DatabaseImageDelete.ImageDelete(name)){
                imageLabel.setIcon(ResizeImage(this.path = null));
                buttonRemove.setEnabled(false);
                buttonDelete.setEnabled(false);
                this.name = null;
                JOptionPane.showMessageDialog(getParent(),"You have successfully deleted the image from the database","Success",JOptionPane.INFORMATION_MESSAGE);
                txtTitle.setText("Title");
                txtLocation.setText("Location");
                txtPeopleWith.setText("People");
                txtName.setText("Name");
                txtDateTaken.setText("Date/Time");
            }
        });

        buttonRemove.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(null, "Do you want to remove it?", "Remove",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                imageLabel.setIcon(ResizeImage(this.path = null));
                buttonRemove.setEnabled(false);
                this.name = null;
                buttonDelete.setEnabled(false);
                buttonUpdate.setEnabled(false);
                txtTitle.setText("Title");
                txtLocation.setText("Location");
                txtPeopleWith.setText("People");
                txtName.setText("Name");
                txtDateTaken.setText("Date/Time");
            }

        });
        buttonSaver.addActionListener(e -> {
            System.out.println(Lat+"\n");
            System.out.println(Long);
            if (path == null) {
                JOptionPane.showMessageDialog(getParent(),"Υou have to upload an image first","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtName.getText() == null){
                return;
            }
            if (txtName.getText().equals("")) {
                UIManager.put("OptionPane.okButtonText", "OK");
                JOptionPane.showMessageDialog(getParent(),"Please type a name before saving","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!DatabaseImageSave.SaveImage(path,txtName.getText(),txtTitle.getText(),txtLocation.getText(),txtPeopleWith.getText(),txtDateTaken.getText())){
                JOptionPane.showMessageDialog(getParent(),"This name already exists");
                return;
            }
            imageLabel.setIcon(ResizeImage(null));
            path = null;
            txtTitle.setText("Title");
            txtLocation.setText("Location");
            txtPeopleWith.setText("People");
            txtName.setText("Name");
            txtDateTaken.setText("Date/Time");
            buttonRemove.setEnabled(false);
            buttonUpdate.setEnabled(false);

        });
        buttonRetrieve.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(getParent(),"Type name for the Image you wanna retrieve","Enter name",JOptionPane.INFORMATION_MESSAGE);
            if (name == null) {
                return;
            }
            if(name.equals("")) {
                JOptionPane.showMessageDialog(getParent(), "You can't retrieve if you leave it blank", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!DatabaseImageRetrieve.ImageRetrieve(name)) {
                JOptionPane.showMessageDialog(getParent(),"This name does not exist in the database","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            this.name = name;
            txtName.setText(name);
            buttonRemove.setEnabled(true);
            buttonDelete.setEnabled(true);
            buttonUpdate.setEnabled(true);
            buttonGeoLocation.setEnabled(true);
        });

        buttonUpdate.addActionListener(e -> {
            if(!DatabaseImageUpdate.UpdateImage(path,name,txtTitle.getText(),txtLocation.getText(),txtPeopleWith.getText(),txtName.getText(),txtDateTaken.getText())){
                JOptionPane.showMessageDialog(getParent(),"This name already exists");
            }else
                JOptionPane.showMessageDialog(getParent(),"You have successfully updated the image information","Success",JOptionPane.INFORMATION_MESSAGE);
//            DatabaseImageUpdate.UpdateImage(path,name,txtTitle.getText(),txtLocation.getText(),txtPeopleWith.getText(),txtName.getText());
//            if(txtName.getText().equals("")) {
//                JOptionPane.showMessageDialog(getParent(), "You can't update if you leave it blank", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
        });
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900,450);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public ImageIcon ResizeImage(String ImagePath)
    {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }
    public static String removeExtension(String s) {

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path up to the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }
        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;
        return filename.substring(0, extensionIndex);
    }
}