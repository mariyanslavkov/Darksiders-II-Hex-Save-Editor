import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UI app = new UI();
            app.setVisible(true);
        });
    }
}