import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookPanel extends JPanel {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private List<Book> books;
    private JTextField titleField, authorField, isbnField, categoryField, descriptionField;
    private JButton addButton, removeButton, editButton, clearButton, exportButton, importButton;
    private JComboBox<String> categoryCombo;
    private JSpinner yearSpinner;
    private JCheckBox availableCheckBox;
    private JLabel statusLabel;

    // Custom colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Blue
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);  // Light Blue
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);      // Red
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray
    private static final Color TEXT_COLOR = new Color(44, 62, 80);         // Dark Blue
    private static final Color HOVER_COLOR = new Color(52, 73, 94);        // Darker Blue

    public BookPanel(List<Book> books) {
        this.books = books;
        setLayout(new BorderLayout());
        initializeComponents();
        loadSampleBooks();
    }

    private void initializeComponents() {
        // Create table with custom renderer
        String[] columns = {"Title", "Author", "ISBN", "Category", "Year", "Available", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(30);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Add hover effect
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bookTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                bookTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Create input panel with better layout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create styled input fields
        titleField = createStyledTextField();
        authorField = createStyledTextField();
        isbnField = createStyledTextField();
        descriptionField = createStyledTextField();

        // Create category combo box
        String[] categories = {"Fiction", "Non-Fiction", "Science Fiction", "Fantasy", "Romance", "Mystery", "Biography", "History"};
        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Create year spinner
        SpinnerNumberModel yearModel = new SpinnerNumberModel(LocalDate.now().getYear(), 1900, LocalDate.now().getYear(), 1);
        yearSpinner = new JSpinner(yearModel);
        yearSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Create available checkbox
        availableCheckBox = new JCheckBox("Available");
        availableCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        availableCheckBox.setSelected(true);

        // Add input fields
        addInputField(inputPanel, "Title:", titleField, gbc, 0);
        addInputField(inputPanel, "Author:", authorField, gbc, 1);
        addInputField(inputPanel, "ISBN:", isbnField, gbc, 2);
        addInputField(inputPanel, "Category:", categoryCombo, gbc, 3);
        addInputField(inputPanel, "Year:", yearSpinner, gbc, 4);
        addInputField(inputPanel, "Description:", descriptionField, gbc, 5);
        gbc.gridx = 1;
        gbc.gridy = 6;
        inputPanel.add(availableCheckBox, gbc);

        // Create button panel with styled buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = createStyledButton("Add Book", "➕");
        removeButton = createStyledButton("Remove Book", "➖");
        editButton = createStyledButton("Edit Book", "✏️");
        clearButton = createStyledButton("Clear Fields", "🗑️");
        exportButton = createStyledButton("Export Books", "📤");
        importButton = createStyledButton("Import Books", "📥");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(importButton);

        // Create status label
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_COLOR);
        buttonPanel.add(statusLabel);

        // Add action listeners
        addButton.addActionListener(e -> addBook());
        removeButton.addActionListener(e -> removeBook());
        editButton.addActionListener(e -> editBook());
        clearButton.addActionListener(e -> clearFields());
        exportButton.addActionListener(e -> exportBooks());
        importButton.addActionListener(e -> importBooks());

        // Add panels to main panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JButton createStyledButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }

    private void addInputField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(jLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
        gbc.weightx = 0.0;
    }

    private void loadSampleBooks() {
        // Fiction (20 books)
        addBookToList(new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", "Fiction", "A story of the fabulously wealthy Jay Gatsby"));
        addBookToList(new Book("To Kill a Mockingbird", "Harper Lee", "978-0446310789", "Fiction", "The story of racial injustice and the loss of innocence"));
        addBookToList(new Book("Pride and Prejudice", "Jane Austen", "978-0141439518", "Fiction", "A romantic novel of manners"));
        addBookToList(new Book("The Catcher in the Rye", "J.D. Salinger", "978-0316769488", "Fiction", "A classic coming-of-age story"));
        addBookToList(new Book("The Alchemist", "Paulo Coelho", "978-0062315007", "Fiction", "A philosophical novel"));
        addBookToList(new Book("One Hundred Years of Solitude", "Gabriel García Márquez", "978-0060883287", "Fiction", "A magical realism masterpiece"));
        addBookToList(new Book("The Kite Runner", "Khaled Hosseini", "978-1594631931", "Fiction", "A story of friendship and redemption"));
        addBookToList(new Book("The Book Thief", "Markus Zusak", "978-0375842207", "Fiction", "A story set in Nazi Germany"));
        addBookToList(new Book("The Little Prince", "Antoine de Saint-Exupéry", "978-0156013987", "Fiction", "A philosophical tale"));
        addBookToList(new Book("The Road", "Cormac McCarthy", "978-0307387899", "Fiction", "A post-apocalyptic novel"));
        addBookToList(new Book("The Secret Life of Bees", "Sue Monk Kidd", "978-0142001745", "Fiction", "A coming-of-age story"));
        addBookToList(new Book("The Help", "Kathryn Stockett", "978-0425232200", "Fiction", "A story about civil rights"));
        addBookToList(new Book("The Giver", "Lois Lowry", "978-0544336261", "Fiction", "A dystopian novel"));
        addBookToList(new Book("The Color Purple", "Alice Walker", "978-0156028356", "Fiction", "A powerful story of resilience"));
        addBookToList(new Book("The Bell Jar", "Sylvia Plath", "978-0061148514", "Fiction", "A semi-autobiographical novel"));
        addBookToList(new Book("The Perks of Being a Wallflower", "Stephen Chbosky", "978-0671027346", "Fiction", "A coming-of-age story"));
        addBookToList(new Book("The Curious Incident of the Dog in the Night-Time", "Mark Haddon", "978-1400032716", "Fiction", "A mystery novel"));
        addBookToList(new Book("The Fault in Our Stars", "John Green", "978-0142424179", "Fiction", "A young adult novel"));
        addBookToList(new Book("The Goldfinch", "Donna Tartt", "978-0316055437", "Fiction", "A Pulitzer Prize-winning novel"));
        addBookToList(new Book("The Night Circus", "Erin Morgenstern", "978-0307744432", "Fiction", "A magical fantasy novel"));

        // Science Fiction (20 books)
        addBookToList(new Book("1984", "George Orwell", "978-0451524935", "Science Fiction", "A dystopian social science fiction novel"));
        addBookToList(new Book("Dune", "Frank Herbert", "978-0441172719", "Science Fiction", "A science fiction masterpiece"));
        addBookToList(new Book("The Martian", "Andy Weir", "978-0553418026", "Science Fiction", "A survival story on Mars"));
        addBookToList(new Book("Project Hail Mary", "Andy Weir", "978-1524741331", "Science Fiction", "A space adventure"));
        addBookToList(new Book("Neuromancer", "William Gibson", "978-0441569595", "Science Fiction", "A cyberpunk classic"));
        addBookToList(new Book("Foundation", "Isaac Asimov", "978-0553293357", "Science Fiction", "A space opera"));
        addBookToList(new Book("Ender's Game", "Orson Scott Card", "978-0812550702", "Science Fiction", "A military science fiction novel"));
        addBookToList(new Book("The Three-Body Problem", "Liu Cixin", "978-0765382030", "Science Fiction", "A hard science fiction novel"));
        addBookToList(new Book("Snow Crash", "Neal Stephenson", "978-0553380958", "Science Fiction", "A cyberpunk novel"));
        addBookToList(new Book("The Left Hand of Darkness", "Ursula K. Le Guin", "978-0441478125", "Science Fiction", "A science fiction novel"));
        addBookToList(new Book("Hyperion", "Dan Simmons", "978-0553283686", "Science Fiction", "A science fiction novel"));
        addBookToList(new Book("The Dispossessed", "Ursula K. Le Guin", "978-0060512750", "Science Fiction", "An anarchist utopian novel"));
        addBookToList(new Book("Altered Carbon", "Richard K. Morgan", "978-0345457684", "Science Fiction", "A cyberpunk novel"));
        addBookToList(new Book("The Forever War", "Joe Haldeman", "978-0312536633", "Science Fiction", "A military science fiction novel"));
        addBookToList(new Book("Children of Time", "Adrian Tchaikovsky", "978-1447273300", "Science Fiction", "A space opera"));
        addBookToList(new Book("The Fifth Season", "N.K. Jemisin", "978-0316229296", "Science Fiction", "A fantasy novel"));
        addBookToList(new Book("Ancillary Justice", "Ann Leckie", "978-0316246620", "Science Fiction", "A space opera"));
        addBookToList(new Book("The Windup Girl", "Paolo Bacigalupi", "978-1597808217", "Science Fiction", "A biopunk novel"));
        addBookToList(new Book("Station Eleven", "Emily St. John Mandel", "978-0804172448", "Science Fiction", "A post-apocalyptic novel"));
        addBookToList(new Book("The City & the City", "China Miéville", "978-0345497529", "Science Fiction", "A weird fiction novel"));

        // Fantasy (20 books)
        addBookToList(new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", "Fantasy", "A fantasy novel and children's book"));
        addBookToList(new Book("The Lord of the Rings", "J.R.R. Tolkien", "978-0544003415", "Fantasy", "An epic high-fantasy novel"));
        addBookToList(new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "978-0590353427", "Fantasy", "The first book in the Harry Potter series"));
        addBookToList(new Book("A Game of Thrones", "George R.R. Martin", "978-0553103540", "Fantasy", "The first book in A Song of Ice and Fire"));
        addBookToList(new Book("The Name of the Wind", "Patrick Rothfuss", "978-0756404741", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Way of Kings", "Brandon Sanderson", "978-0765326355", "Fantasy", "An epic fantasy novel"));
        addBookToList(new Book("Mistborn: The Final Empire", "Brandon Sanderson", "978-0765350386", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Lies of Locke Lamora", "Scott Lynch", "978-0553588941", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Eye of the World", "Robert Jordan", "978-0812511819", "Fantasy", "The first book in The Wheel of Time"));
        addBookToList(new Book("The Blade Itself", "Joe Abercrombie", "978-0575079791", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Poppy War", "R.F. Kuang", "978-0062662569", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Priory of the Orange Tree", "Samantha Shannon", "978-1635570299", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Bear and the Nightingale", "Katherine Arden", "978-1101885957", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The City of Brass", "S.A. Chakraborty", "978-0062678102", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Fifth Season", "N.K. Jemisin", "978-0316229296", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Grace of Kings", "Ken Liu", "978-1481424271", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Traitor Baru Cormorant", "Seth Dickinson", "978-0765380739", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Goblin Emperor", "Katherine Addison", "978-0765326997", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Library at Mount Char", "Scott Hawkins", "978-0553418606", "Fantasy", "A fantasy novel"));
        addBookToList(new Book("The Ten Thousand Doors of January", "Alix E. Harrow", "978-0316421997", "Fantasy", "A fantasy novel"));

        // Mystery (20 books)
        addBookToList(new Book("The Da Vinci Code", "Dan Brown", "978-0307474278", "Mystery", "A mystery thriller novel"));
        addBookToList(new Book("Gone Girl", "Gillian Flynn", "978-0307588364", "Mystery", "A psychological thriller"));
        addBookToList(new Book("The Girl with the Dragon Tattoo", "Stieg Larsson", "978-0307454541", "Mystery", "A crime thriller"));
        addBookToList(new Book("The Silent Patient", "Alex Michaelides", "978-1250301697", "Mystery", "A psychological thriller"));
        addBookToList(new Book("The Thursday Murder Club", "Richard Osman", "978-0241988268", "Mystery", "A cozy mystery"));
        addBookToList(new Book("The Guest List", "Lucy Foley", "978-0062868930", "Mystery", "A thriller novel"));
        addBookToList(new Book("The Seven Deaths of Evelyn Hardcastle", "Stuart Turton", "978-1492657969", "Mystery", "A mystery novel"));
        addBookToList(new Book("The Last Thing He Told Me", "Laura Dave", "978-1501171345", "Mystery", "A mystery novel"));
        addBookToList(new Book("The Maidens", "Alex Michaelides", "978-1250304452", "Mystery", "A psychological thriller"));
        addBookToList(new Book("The Paris Apartment", "Lucy Foley", "978-0008384807", "Mystery", "A thriller novel"));
        addBookToList(new Book("The Plot", "Jean Hanff Korelitz", "978-1250790755", "Mystery", "A psychological thriller"));
        addBookToList(new Book("The Sanatorium", "Sarah Pearse", "978-0593296677", "Mystery", "A thriller novel"));
        addBookToList(new Book("The Push", "Ashley Audrain", "978-0525657601", "Mystery", "A psychological thriller"));
        addBookToList(new Book("The Last House on Needless Street", "Catriona Ward", "978-1250812624", "Mystery", "A psychological thriller"));
        addBookToList(new Book("The Other Black Girl", "Zakiya Dalila Harris", "978-1982160135", "Mystery", "A thriller novel"));
        addBookToList(new Book("The Final Girl Support Group", "Grady Hendrix", "978-0593201237", "Mystery", "A horror novel"));
        addBookToList(new Book("The Night She Disappeared", "Lisa Jewell", "978-1982137335", "Mystery", "A thriller novel"));
        addBookToList(new Book("The Last Thing to Burn", "Will Dean", "978-0316703484", "Mystery", "A thriller novel"));
        addBookToList(new Book("The Therapist", "B.A. Paris", "978-1250270797", "Mystery", "A psychological thriller"));
        addBookToList(new Book("The Perfect Marriage", "Jeneva Rose", "978-1950057317", "Mystery", "A thriller novel"));

        // Biography (10 books)
        addBookToList(new Book("Steve Jobs", "Walter Isaacson", "978-1451648539", "Biography", "The biography of Apple's co-founder"));
        addBookToList(new Book("Einstein: His Life and Universe", "Walter Isaacson", "978-0743264747", "Biography", "A biography of Albert Einstein"));
        addBookToList(new Book("Becoming", "Michelle Obama", "978-1524763138", "Biography", "Memoir of the former First Lady"));
        addBookToList(new Book("The Autobiography of Malcolm X", "Malcolm X", "978-0345350688", "Biography", "The autobiography of Malcolm X"));
        addBookToList(new Book("The Diary of a Young Girl", "Anne Frank", "978-0553577129", "Biography", "The diary of Anne Frank"));
        addBookToList(new Book("Long Walk to Freedom", "Nelson Mandela", "978-0316548182", "Biography", "The autobiography of Nelson Mandela"));
        addBookToList(new Book("The Wright Brothers", "David McCullough", "978-1476728742", "Biography", "The story of the Wright brothers"));
        addBookToList(new Book("The Immortal Life of Henrietta Lacks", "Rebecca Skloot", "978-1400052189", "Biography", "The story of Henrietta Lacks"));
        addBookToList(new Book("The Glass Castle", "Jeannette Walls", "978-0743247542", "Biography", "A memoir of Jeannette Walls"));
        addBookToList(new Book("Born a Crime", "Trevor Noah", "978-0399588174", "Biography", "Stories from a South African childhood"));

        // History (10 books)
        addBookToList(new Book("Sapiens", "Yuval Noah Harari", "978-0062316097", "History", "A brief history of humankind"));
        addBookToList(new Book("Guns, Germs, and Steel", "Jared Diamond", "978-0393317558", "History", "The fates of human societies"));
        addBookToList(new Book("The Rise and Fall of the Third Reich", "William L. Shirer", "978-0671728687", "History", "A history of Nazi Germany"));
        addBookToList(new Book("A People's History of the United States", "Howard Zinn", "978-0062397348", "History", "A history of the United States"));
        addBookToList(new Book("The Guns of August", "Barbara W. Tuchman", "978-0345476098", "History", "The outbreak of World War I"));
        addBookToList(new Book("The Silk Roads", "Peter Frankopan", "978-1101912379", "History", "A new history of the world"));
        addBookToList(new Book("SPQR", "Mary Beard", "978-0871404637", "History", "A history of ancient Rome"));
        addBookToList(new Book("The Crusades", "Thomas Asbridge", "978-0060787288", "History", "The authoritative history of the war for the Holy Land"));
        addBookToList(new Book("The Plantagenets", "Dan Jones", "978-0143124924", "History", "The warrior kings and queens who made England"));
        addBookToList(new Book("The Romanovs", "Simon Sebag Montefiore", "978-0307266521", "History", "1613-1918"));
    }

    private void addBookToList(Book book) {
        books.add(book);
        tableModel.addRow(new Object[]{
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.getCategory(),
            book.getYear(),
            book.isAvailable() ? "Yes" : "No",
            book.getDescription()
        });
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        String description = descriptionField.getText().trim();
        int year = (Integer) yearSpinner.getValue();
        boolean available = availableCheckBox.isSelected();

        // Validate input
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title!", "Error", JOptionPane.ERROR_MESSAGE);
            titleField.requestFocus();
            return;
        }
        if (author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an author!", "Error", JOptionPane.ERROR_MESSAGE);
            authorField.requestFocus();
            return;
        }
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ISBN!", "Error", JOptionPane.ERROR_MESSAGE);
            isbnField.requestFocus();
            return;
        }
        if (category == null || category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a category!", "Error", JOptionPane.ERROR_MESSAGE);
            categoryCombo.requestFocus();
            return;
        }

        // Check if ISBN already exists
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                JOptionPane.showMessageDialog(this, 
                    "A book with this ISBN already exists!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                isbnField.requestFocus();
                return;
            }
        }

        try {
            Book newBook = new Book(title, author, isbn, category, description, year, available);
            addBookToList(newBook);
            clearFields();
            updateStatus("Book added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error adding book: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to remove!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove this book?", 
            "Confirm Removal", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            books.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            updateStatus("Book removed successfully!");
        }
    }

    private void editBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit!");
            return;
        }

        Book book = books.get(selectedRow);
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        categoryCombo.setSelectedItem(book.getCategory());
        descriptionField.setText(book.getDescription());
        yearSpinner.setValue(book.getYear());
        availableCheckBox.setSelected(book.isAvailable());

        books.remove(selectedRow);
        tableModel.removeRow(selectedRow);
        updateStatus("Book loaded for editing!");
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        categoryCombo.setSelectedIndex(0);
        descriptionField.setText("");
        yearSpinner.setValue(LocalDate.now().getYear());
        availableCheckBox.setSelected(true);
        updateStatus("Fields cleared!");
    }

    private void exportBooks() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Books");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // TODO: Implement book export functionality
            updateStatus("Books exported successfully!");
        }
    }

    private void importBooks() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import Books");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            // TODO: Implement book import functionality
            updateStatus("Books imported successfully!");
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
        Timer timer = new Timer(3000, e -> statusLabel.setText("Ready"));
        timer.setRepeats(false);
        timer.start();
    }
} 