import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryManagementSystem extends JFrame {
    private JTabbedPane tabbedPane;
    private BookPanel bookPanel;
    private SearchPanel searchPanel;
    private CategoryPanel categoryPanel;
    private List<Book> allBooks;

    // Custom colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Blue
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);  // Light Blue
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);      // Red
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray
    private static final Color TEXT_COLOR = new Color(44, 62, 80);         // Dark Blue
    private static final Color HOVER_COLOR = new Color(52, 73, 94);        // Darker Blue

    public LibraryManagementSystem() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        // Set custom colors
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("Button.background", PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", PRIMARY_COLOR);
        UIManager.put("TabbedPane.background", BACKGROUND_COLOR);
        UIManager.put("TabbedPane.selected", PRIMARY_COLOR);
        UIManager.put("TabbedPane.foreground", TEXT_COLOR);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", TEXT_COLOR);
        UIManager.put("TableHeader.background", PRIMARY_COLOR);
        UIManager.put("TableHeader.foreground", Color.WHITE);
        
        // Initialize components
        allBooks = new ArrayList<>();
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        bookPanel = new BookPanel(allBooks);
        searchPanel = new SearchPanel();
        categoryPanel = new CategoryPanel();
        
        // Add tabs with custom icons
        tabbedPane.addTab("Books", createIcon("📚"), bookPanel);
        tabbedPane.addTab("Search", createIcon("🔍"), searchPanel);
        tabbedPane.addTab("Categories", createIcon("📑"), categoryPanel);
        
        // Connect panels
        searchPanel.setBooks(allBooks);
        categoryPanel.setBooks(allBooks);
        
        // Add tabbed pane with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
        
        // Add window listener to update other panels when books change
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                searchPanel.setBooks(allBooks);
                categoryPanel.setBooks(allBooks);
            }
        });
    }

    private ImageIcon createIcon(String emoji) {
        JLabel label = new JLabel(emoji);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        return new ImageIcon(createImageFromComponent(label));
    }

    private java.awt.Image createImageFromComponent(JComponent component) {
        component.setSize(32, 32);
        component.setPreferredSize(new Dimension(32, 32));
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
            32, 32, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        component.paint(image.getGraphics());
        return image;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LibraryManagementSystem().setVisible(true);
        });
    }
} 