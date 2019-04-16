package com.st.novatech.springlms.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Embeddable
public class CopiesCompositeKey implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The book that is associated with the number of copies.
	 */
	@JsonBackReference
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne()
    @JoinColumn(name="bookId")
	private Book book;
	
    /**
	 * The branch that is associated with the number of copies.
	 */
	@JsonBackReference
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne()
    @JoinColumn(name="branchId", insertable = false, updatable = false)
	private Branch branch;

	protected CopiesCompositeKey() {
	}
	
    protected CopiesCompositeKey(Book book, Branch branch) {
        this.book = book;
        this.branch = branch;
    }

    /**
     * get book for this copies entity
     * 
     * @return	book associated to this entity
     */
	public Book getBook() {
		return book;
	}

	/**
     * get branch for this copies entity
     * 
     * @return	branch associated to this entity
     */
	public Branch getBranch() {
		return branch;
	}


	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof LoanCompositeKey) {
        	return Objects.equals(getBook(), ((LoanCompositeKey) o).getBook())
        			&& Objects.equals(getBranch(), ((LoanCompositeKey) o).getBranch());
        } else {
        	return false;
        }
    }
 
    @Override
    public int hashCode() {
    	return Objects.hash(book.getId(), branch.getId());
    }
}
