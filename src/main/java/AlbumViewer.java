import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AlbumViewer {

private final ArrayList<byte[]>images1;
    public AlbumViewer(ArrayList<byte[]>images, ArrayList<String>info) {

        JList list = new JList(info.toArray());
        list.setCellRenderer(new AlbumListListener());

        images1 = images;
        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(1000, 600));

        JFrame frame = new JFrame();
        frame.setTitle("Album Viewer");
        frame.add(scroll);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public class AlbumListListener extends DefaultListCellRenderer {

        Font font = new Font("Expressway", Font.BOLD, 15);

        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            label.setIcon(ResizeImage(images1.get(index)));
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setFont(font);
            return label;
        }
    }

    public ImageIcon ResizeImage(byte[] image)
    {
        ImageIcon MyImage = new ImageIcon(image);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(400,250, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

}