package com.st.novatech.springlms.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class LoanCompositeKey implements Serializable {
    
    /**
	 * current version of this implementation
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * The book that is associated with the number of copies.
	 */
    @ManyToOne()
    @JoinColumn(name="bookId")
	private Book book;

    
    /**
	 * The branch from which the book was checked out.
	 */
    @ManyToOne()
    @JoinColumn(name="branchId")
	private Branch branch;
	/**
	 * The borrower who checked out the book.
	 */
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

	/**
	 * An object is equal to this one iff it is a Borrower with the same card
	 * number, branch with the same id, and book with the same id.
	 */
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
 
	/**
	 * We use the ID from book, borrower, and branch for this object's hash-code.
	 */
    @Override
    public int hashCode() {
    	return Objects.hash(book.getId(), borrower.getCardNo(), branch.getId());
    }

}
