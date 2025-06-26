import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryPanel extends JPanel {
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField categoryField;
    private JButton addButton, removeButton;
    private List<String> categories;
    private List<Book> allBooks;

    public CategoryPanel() {
        setLayout(new BorderLayout());
        categories = new ArrayList<>();
        allBooks = new ArrayList<>();
        initializeComponents();
        loadDefaultCategories();
    }

    private void initializeComponents() {
        // Create input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryField = new JTextField(20);
        addButton = new JButton("Add Category");
        removeButton = new JButton("Remove Category");

        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(addButton);
        inputPanel.add(removeButton);

        // Create table
        String[] columns = {"Category", "Number of Books"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(categoryTable);

        // Add components
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addCategory());
        removeButton.addActionListener(e -> removeCategory());
    }

    private void loadDefaultCategories() {
        addCategoryToList("Fiction");
        addCategoryToList("Non-Fiction");
        addCategoryToList("Science Fiction");
        addCategoryToList("Fantasy");
        addCategoryToList("Romance");
        addCategoryToList("Mystery");
        addCategoryToList("Biography");
        addCategoryToList("History");
    }

    public void setBooks(List<Book> books) {
        this.allBooks = books;
        updateCategoryCounts();
    }

    private void addCategory() {
        String category = categoryField.getText().trim();
        if (category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a category name!");
            return;
        }

        if (categories.contains(category)) {
            JOptionPane.showMessageDialog(this, "Category already exists!");
            return;
        }

        addCategoryToList(category);
        categoryField.setText("");
    }

    private void addCategoryToList(String category) {
        categories.add(category);
        updateCategoryCounts();
    }

    private void removeCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to remove!");
            return;
        }

        String category = (String) tableModel.getValueAt(selectedRow, 0);
        categories.remove(category);
        updateCategoryCounts();
    }

    private void updateCategoryCounts() {
        tableModel.setRowCount(0);
        for (String category : categories) {
            long count = allBooks.stream()
                .filter(book -> book.getCategory().equals(category))
                .count();
            tableModel.addRow(new Object[]{category, count});
        }
    }
} 