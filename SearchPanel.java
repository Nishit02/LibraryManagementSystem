import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchPanel extends JPanel {
    private JTable searchTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private List<Book> allBooks;
    private JLabel resultCountLabel;

    public SearchPanel() {
        setLayout(new BorderLayout());
        allBooks = new ArrayList<>();
        initializeComponents();
    }

    private void initializeComponents() {
        // Create search panel with better styling
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create styled search field
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Create styled combo box
        String[] searchTypes = {"Title", "Author", "Category", "ISBN"};
        searchTypeCombo = new JComboBox<>(searchTypes);
        searchTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Create styled search button
        JButton searchButton = createStyledButton("Search", "🔍");
        
        // Create result count label
        resultCountLabel = new JLabel("No results");
        resultCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resultCountLabel.setForeground(new Color(100, 100, 100));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("By:"));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchButton);
        searchPanel.add(resultCountLabel);

        // Create table with custom renderer
        String[] columns = {"Title", "Author", "ISBN", "Category", "Available"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        searchTable = new JTable(tableModel);
        searchTable.setRowHeight(30);
        searchTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Add hover effect
        searchTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                searchTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        JScrollPane scrollPane = new JScrollPane(searchTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add search functionality
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
    }

    private JButton createStyledButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(60, 120, 170));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
        
        return button;
    }

    public void setBooks(List<Book> books) {
        this.allBooks = books;
        updateTable(books);
    }

    private void performSearch() {
        String searchText = searchField.getText().toLowerCase();
        String searchType = (String) searchTypeCombo.getSelectedItem();

        List<Book> filteredBooks = allBooks.stream()
            .filter(book -> {
                switch (searchType) {
                    case "Title":
                        return book.getTitle().toLowerCase().contains(searchText);
                    case "Author":
                        return book.getAuthor().toLowerCase().contains(searchText);
                    case "Category":
                        return book.getCategory().toLowerCase().contains(searchText);
                    case "ISBN":
                        return book.getIsbn().toLowerCase().contains(searchText);
                    default:
                        return false;
                }
            })
            .collect(Collectors.toList());

        updateTable(filteredBooks);
        updateResultCount(filteredBooks.size());
    }

    private void updateTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCategory(),
                book.isAvailable() ? "Yes" : "No"
            });
        }
    }

    private void updateResultCount(int count) {
        resultCountLabel.setText(count + " result" + (count != 1 ? "s" : "") + " found");
    }
} 