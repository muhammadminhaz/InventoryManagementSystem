package com.muhammadminhaz.inventorymanagementsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice_item")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;
    @Column(name = "is_customized")
    private Boolean isCustomized = false;
    @Column(name = "print_type")
    private String printType;
    @Column(name = "adjusted_price")
    private Double adjustedPrice;
    @Column(name = "printed_side")
    private Integer printedSide;

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public Boolean getCustomized() {
        return isCustomized;
    }

    public void setCustomized(Boolean customized) {
        isCustomized = customized;
    }

    public Double getAdjustedPrice() {
        return adjustedPrice;
    }

    public void setAdjustedPrice(Double adjustedPrice) {
        this.adjustedPrice = adjustedPrice;
    }

    public Integer getPrintedSide() {
        return printedSide;
    }

    public void setPrintedSide(Integer printedSide) {
        this.printedSide = printedSide;
    }

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

}
