package com.aims.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Product {
    private int productId;
    private String title;
    private String category; // BOOK, CD, LP_RECORD, DVD
    private double value; // Giá trị không bao gồm VAT
    private double price; // Giá hiện tại không bao gồm VAT
    private int stockQuantity;
    private boolean isRushEligible;
    private double weight;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Book-specific fields
    private String authors;
    private String coverType; // PAPERBACK, HARDCOVER
    private String publisher;
    private LocalDate publicationDate;
    private Integer numPages; // Tùy chọn
    private String bookLanguage; // Tùy chọn
    private String bookGenre; // Tùy chọn
    // CD and LP Record-specific fields
    private String artists;
    private String recordLabel;
    private String tracklist;
    private String musicGenre;
    private LocalDate releaseDate; // Tùy chọn
    // DVD-specific fields
    private String discType; // BLU_RAY, HD_DVD
    private String director;
    private Integer runtime; // Thời lượng (phút)
    private String studio;
    private String dvdLanguage;
    private String subtitles;

    // Constructor
    public Product() {}

    public Product(int productId, String title, String category, double value, double price, 
                   int stockQuantity, boolean isRushEligible, double weight, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.title = title;
        this.category = category;
        this.value = value;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.isRushEligible = isRushEligible;
        this.weight = weight;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public boolean isRushEligible() { return isRushEligible; }
    public void setRushEligible(boolean isRushEligible) { this.isRushEligible = isRushEligible; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    // Book-specific getters and setters
    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }
    public String getCoverType() { return coverType; }
    public void setCoverType(String coverType) { this.coverType = coverType; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public LocalDate getPublicationDate() { return publicationDate; }
    public void setPublicationDate(LocalDate publicationDate) { this.publicationDate = publicationDate; }
    public Integer getNumPages() { return numPages; }
    public void setNumPages(Integer numPages) { this.numPages = numPages; }
    public String getBookLanguage() { return bookLanguage; }
    public void setBookLanguage(String bookLanguage) { this.bookLanguage = bookLanguage; }
    public String getBookGenre() { return bookGenre; }
    public void setBookGenre(String bookGenre) { this.bookGenre = bookGenre; }
    // CD and LP Record-specific getters and setters
    public String getArtists() { return artists; }
    public void setArtists(String artists) { this.artists = artists; }
    public String getRecordLabel() { return recordLabel; }
    public void setRecordLabel(String recordLabel) { this.recordLabel = recordLabel; }
    public String getTracklist() { return tracklist; }
    public void setTracklist(String tracklist) { this.tracklist = tracklist; }
    public String getMusicGenre() { return musicGenre; }
    public void setMusicGenre(String musicGenre) { this.musicGenre = musicGenre; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    // DVD-specific getters and setters
    public String getDiscType() { return discType; }
    public void setDiscType(String discType) { this.discType = discType; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public Integer getRuntime() { return runtime; }
    public void setRuntime(Integer runtime) { this.runtime = runtime; }
    public String getStudio() { return studio; }
    public void setStudio(String studio) { this.studio = studio; }
    public String getDvdLanguage() { return dvdLanguage; }
    public void setDvdLanguage(String dvdLanguage) { this.dvdLanguage = dvdLanguage; }
    public String getSubtitles() { return subtitles; }
    public void setSubtitles(String subtitles) { this.subtitles = subtitles; }
}