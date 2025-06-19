package com.aims.view;

import com.aims.controller.ProductController;
import com.aims.entity.Product;
import com.aims.util.Session;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddProductFrame extends JDialog {
    private MainFrame parentFrame;
    private Product product;
    private ProductController productController;
    private JTextField titleField, priceField, valueField, stockField, weightField;
    private JComboBox<String> categoryCombo;
    private JCheckBox rushEligibleCheck;
    // Trường cho Book
    private JTextField authorsField, coverTypeField, publisherField, publicationDateField, numPagesField, bookLanguageField, bookGenreField;
    // Trường cho CD và LP_Record
    private JTextField artistsField, recordLabelField, tracklistField, musicGenreField, releaseDateField;
    // Trường cho DVD
    private JTextField directorField, runtimeField, studioField, dvdLanguageField, subtitlesField, discTypeField;
    private JPanel bookPanel, cdPanel, dvdPanel, dynamicPanel;

    public AddProductFrame(MainFrame parentFrame) {
        this(parentFrame, null);
    }

    public AddProductFrame(MainFrame parentFrame, Product product) {
        super(parentFrame, product == null ? "Add Product" : "Edit Product", true);
        this.parentFrame = parentFrame;
        this.product = product;
        this.productController = parentFrame.getProductController();
        initUI();
        if (product != null) {
            populateFields();
        }
    }

    private void initUI() {
        setSize(600, 700);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new BorderLayout());
        JPanel commonPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        commonPanel.setBorder(BorderFactory.createTitledBorder("Common Information"));

        commonPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        commonPanel.add(titleField);

        commonPanel.add(new JLabel("Category:"));
        categoryCombo = new JComboBox<>(new String[]{"Book", "CD", "LP_Record", "DVD"});
        categoryCombo.addActionListener(e -> updateSpecificFields());
        commonPanel.add(categoryCombo);

        commonPanel.add(new JLabel("Price (VND):"));
        priceField = new JTextField();
        commonPanel.add(priceField);

        commonPanel.add(new JLabel("Value (VND):"));
        valueField = new JTextField();
        commonPanel.add(valueField);

        commonPanel.add(new JLabel("Stock Quantity:"));
        stockField = new JTextField();
        commonPanel.add(stockField);

        commonPanel.add(new JLabel("Weight (kg):"));
        weightField = new JTextField();
        commonPanel.add(weightField);

        commonPanel.add(new JLabel("Rush Eligible:"));
        rushEligibleCheck = new JCheckBox();
        commonPanel.add(rushEligibleCheck);

        bookPanel = createBookPanel();
        cdPanel = createCDPanel();
        dvdPanel = createDVDPanel();

        dynamicPanel = new JPanel(new BorderLayout());
        dynamicPanel.add(bookPanel, BorderLayout.CENTER);

        formPanel.add(commonPanel, BorderLayout.NORTH);
        formPanel.add(dynamicPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(e -> saveProduct());
        cancelButton.addActionListener(e -> dispose());

        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Book Information"));

        panel.add(new JLabel("Authors:"));
        authorsField = new JTextField();
        panel.add(authorsField);

        panel.add(new JLabel("Cover Type:"));
        coverTypeField = new JTextField();
        panel.add(coverTypeField);

        panel.add(new JLabel("Publisher:"));
        publisherField = new JTextField();
        panel.add(publisherField);

        panel.add(new JLabel("Publication Date (yyyy-MM-dd):"));
        publicationDateField = new JTextField();
        panel.add(publicationDateField);

        panel.add(new JLabel("Number of Pages:"));
        numPagesField = new JTextField();
        panel.add(numPagesField);

        panel.add(new JLabel("Language:"));
        bookLanguageField = new JTextField();
        panel.add(bookLanguageField);

        panel.add(new JLabel("Genre:"));
        bookGenreField = new JTextField();
        panel.add(bookGenreField);

        return panel;
    }

    private JPanel createCDPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("CD / LP_Record Information"));

        panel.add(new JLabel("Artists:"));
        artistsField = new JTextField();
        panel.add(artistsField);

        panel.add(new JLabel("Record Label:"));
        recordLabelField = new JTextField();
        panel.add(recordLabelField);

        panel.add(new JLabel("Tracklist:"));
        tracklistField = new JTextField();
        panel.add(tracklistField);

        panel.add(new JLabel("Music Genre:"));
        musicGenreField = new JTextField();
        panel.add(musicGenreField);

        panel.add(new JLabel("Release Date (yyyy-MM-dd):"));
        releaseDateField = new JTextField();
        panel.add(releaseDateField);

        return panel;
    }

    private JPanel createDVDPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("DVD Information"));

        panel.add(new JLabel("Director:"));
        directorField = new JTextField();
        panel.add(directorField);

        panel.add(new JLabel("Runtime (minutes):"));
        runtimeField = new JTextField();
        panel.add(runtimeField);

        panel.add(new JLabel("Studio:"));
        studioField = new JTextField();
        panel.add(studioField);

        panel.add(new JLabel("Language:"));
        dvdLanguageField = new JTextField();
        panel.add(dvdLanguageField);

        panel.add(new JLabel("Subtitles:"));
        subtitlesField = new JTextField();
        panel.add(subtitlesField);

        panel.add(new JLabel("Disc Type:"));
        discTypeField = new JTextField();
        panel.add(discTypeField);

        return panel;
    }

    private void updateSpecificFields() {
        String category = (String) categoryCombo.getSelectedItem();
        dynamicPanel.removeAll();
        switch (category) {
            case "Book":
                dynamicPanel.add(bookPanel, BorderLayout.CENTER);
                break;
            case "CD":
            case "LP_Record":
                dynamicPanel.add(cdPanel, BorderLayout.CENTER);
                break;
            case "DVD":
                dynamicPanel.add(dvdPanel, BorderLayout.CENTER);
                break;
        }
        dynamicPanel.revalidate();
        dynamicPanel.repaint();
    }

    private void populateFields() {
        titleField.setText(product.getTitle());
        categoryCombo.setSelectedItem(product.getCategory());
        priceField.setText(String.valueOf(product.getPrice()));
        valueField.setText(String.valueOf(product.getValue()));
        stockField.setText(String.valueOf(product.getStockQuantity()));
        weightField.setText(String.valueOf(product.getWeight()));
        rushEligibleCheck.setSelected(product.isRushEligible());

        switch (product.getCategory()) {
            case "Book":
                authorsField.setText(product.getAuthors());
                coverTypeField.setText(product.getCoverType());
                publisherField.setText(product.getPublisher());
                if (product.getPublicationDate() != null) {
                    publicationDateField.setText(product.getPublicationDate().toString());
                }
                if (product.getNumPages() != null) {
                    numPagesField.setText(String.valueOf(product.getNumPages()));
                }
                bookLanguageField.setText(product.getBookLanguage());
                bookGenreField.setText(product.getBookGenre());
                break;
            case "CD":
            case "LP_Record":
                artistsField.setText(product.getArtists());
                recordLabelField.setText(product.getRecordLabel());
                tracklistField.setText(product.getTracklist());
                musicGenreField.setText(product.getMusicGenre());
                if (product.getReleaseDate() != null) {
                    releaseDateField.setText(product.getReleaseDate().toString());
                }
                break;
            case "DVD":
                directorField.setText(product.getDirector());
                if (product.getRuntime() != null) {
                    runtimeField.setText(String.valueOf(product.getRuntime()));
                }
                studioField.setText(product.getStudio());
                dvdLanguageField.setText(product.getDvdLanguage());
                subtitlesField.setText(product.getSubtitles());
                discTypeField.setText(product.getDiscType());
                break;
        }
    }

    private void saveProduct() {
        try {
            Product newProduct = product != null ? product : new Product();
            newProduct.setTitle(titleField.getText().trim());
            newProduct.setCategory((String) categoryCombo.getSelectedItem());
            newProduct.setPrice(Double.parseDouble(priceField.getText().trim()));
            newProduct.setValue(Double.parseDouble(valueField.getText().trim()));
            newProduct.setStockQuantity(Integer.parseInt(stockField.getText().trim()));
            newProduct.setWeight(Double.parseDouble(weightField.getText().trim()));
            newProduct.setRushEligible(rushEligibleCheck.isSelected());

            switch (newProduct.getCategory()) {
                case "Book":
                    newProduct.setAuthors(authorsField.getText().trim());
                    newProduct.setCoverType(coverTypeField.getText().trim());
                    newProduct.setPublisher(publisherField.getText().trim());
                    String pubDate = publicationDateField.getText().trim();
                    if (!pubDate.isEmpty()) {
                        newProduct.setPublicationDate(LocalDate.parse(pubDate, DateTimeFormatter.ISO_LOCAL_DATE));
                    }
                    String numPages = numPagesField.getText().trim();
                    if (!numPages.isEmpty()) {
                        newProduct.setNumPages(Integer.parseInt(numPages));
                    }
                    newProduct.setBookLanguage(bookLanguageField.getText().trim());
                    newProduct.setBookGenre(bookGenreField.getText().trim());
                    break;
                case "CD":
                case "LP_Record":
                    newProduct.setArtists(artistsField.getText().trim());
                    newProduct.setRecordLabel(recordLabelField.getText().trim());
                    newProduct.setTracklist(tracklistField.getText().trim());
                    newProduct.setMusicGenre(musicGenreField.getText().trim());
                    String relDate = releaseDateField.getText().trim();
                    if (!relDate.isEmpty()) {
                        newProduct.setReleaseDate(LocalDate.parse(relDate, DateTimeFormatter.ISO_LOCAL_DATE));
                    }
                    break;
                case "DVD":
                    newProduct.setDirector(directorField.getText().trim());
                    String runtime = runtimeField.getText().trim();
                    if (!runtime.isEmpty()) {
                        newProduct.setRuntime(Integer.parseInt(runtime));
                    }
                    newProduct.setStudio(studioField.getText().trim());
                    newProduct.setDvdLanguage(dvdLanguageField.getText().trim());
                    newProduct.setSubtitles(subtitlesField.getText().trim());
                    newProduct.setDiscType(discTypeField.getText().trim());
                    break;
            }

            if (product == null) {
                newProduct.setCreatedAt(LocalDateTime.now());
                newProduct.setUpdatedAt(LocalDateTime.now());
                productController.addProduct(newProduct, Session.getCurrentUser().getUserId());
            } else {
                newProduct.setUpdatedAt(LocalDateTime.now());
                productController.updateProduct(newProduct, Session.getCurrentUser().getUserId());
            }

            JOptionPane.showMessageDialog(this, "Product saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parentFrame.loadProductList();
            dispose();
        } catch (NumberFormatException | DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid input format. Please check numbers and dates.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
