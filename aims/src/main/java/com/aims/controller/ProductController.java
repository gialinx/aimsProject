package com.aims.controller;

import com.aims.dao.ProductDAO;
import com.aims.entity.Product;
import com.aims.view.MainFrame;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductController {
    private MainFrame view;
    private ProductDAO productDAO;

    public ProductController(MainFrame view, ProductDAO productDAO) {
        this.view = view;
        this.productDAO = productDAO;
    }

    public ResultSet getProducts(String searchText, String category, Boolean isRushEligible, String sortColumn, String sortOrder, int page) {
        return productDAO.getProducts(searchText, category, isRushEligible, sortColumn, sortOrder, page);
    }

    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }

    public void addProduct(Product product, int userId) {
    	int productID = productDAO.addProduct(product);
        productDAO.logProductHistory(productID, "ADD", "Added new product: " + product.getTitle(), userId);
    }

    public void updateProduct(Product product, int userId) {
        productDAO.updateProduct(product);
        productDAO.logProductHistory(product.getProductId(), "EDIT", "Updated product: " + product.getTitle(), userId);
    }

    public void deleteProduct(int productId, int userId) {
        Product product = getProductById(productId);
        productDAO.deleteProduct(productId);
        productDAO.logProductHistory(productId, "DELETE", "Deleted product: " + product.getTitle(), userId);
    }
}