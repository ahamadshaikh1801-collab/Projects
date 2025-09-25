package GuiProject;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

final class BgPanel extends JPanel {
    private Image image;

    public BgPanel(String imgPath) {
        image = new ImageIcon(imgPath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
