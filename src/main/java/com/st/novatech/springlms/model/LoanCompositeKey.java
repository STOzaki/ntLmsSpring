package com.st.novatech.springlms.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Embeddable
public class LoanCompositeKey implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * The book that is associated with the number of copies.
	 */
//	@JsonBackReference
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne()
    @JoinColumn(name="bookId")
	private Book book;

    
    /**
	 * The branch from which the book was checked out.
	 */
//	@JsonBackReference
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne()
    @JoinColumn(name="branchId")
	private Branch branch;
	/**
	 * The borrower who checked out the book.
	 */
//	@JsonBackReference
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne()
    @JoinColumn(name="cardNo")
	private Borrower borrower;
    
    protected LoanCompositeKey() {
    }
    
    protected LoanCompositeKey(Book book, Branch branch, Borrower borrower) {
        this.book = book;
        this.branch = branch;
        this.borrower = borrower;
    }
    
    /**
	 * Get the book that is involved in this loan.
	 * @return the book that was checked out
	 */
    public Book getBook() {
		return book;
	}

    /**
	 * Get the branch involved in this loan.
	 * @return the branch from which the book was borrowed.
	 */
	public Branch getBranch() {
		return branch;
	}

	/**
	 * Get the borrower involved in this loan.
	 * @return the borrower who checked the book out.
	 */
	public Borrower getBorrower() {
		return borrower;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof LoanCompositeKey) {
        	return Objects.equals(getBook(), ((LoanCompositeKey) o).getBook())
        			&& Objects.equals(getBranch(), ((LoanCompositeKey) o).getBranch())
        			&& Objects.equals(getBorrower(), ((LoanCompositeKey) o).getBorrower());
        } else {
        	return false;
        }
    }
 
    @Override
    public int hashCode() {
    	return Objects.hash(book.getId(), borrower.getCardNo(), branch.getId());
    }

}
