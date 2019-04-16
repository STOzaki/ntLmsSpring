package com.st.novatech.springlms.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Copies of books.
 *
 * @author Salem Ozaki
 */
@Entity
@Table(name = "tbl_book_copies")
public class Copies {
	
	/**
	 * Primary key composed of branchId and bookId
	 */
	@EmbeddedId
	private CopiesCompositeKey compositeKey;
	
    @Column(name = "noOfCopies")
    private int noOfCopies;

	/**
	 * Get the book associated with the number of copies.
	 * @return the book associated with the number of copies.
	 */
	public Book getBook() {
		return compositeKey.getBook();
	}

	/**
	 * Get the branch associated with the number of copies.
	 * @return the branch associated with the number of copies.
	 */
	public Branch getBranch() {
		return compositeKey.getBranch();
	}

	/**
	 * Get number of copies of book for this branch
	 * @return	number of copies of book for this branch
	 */
	public int getNoOfCopies() {
		return noOfCopies;
	}

	/**
	 * Set number of copies of book for this branch
	 * @param noOfCopies	to set for this book for this branch
	 */
	public void setNoOfCopies(int noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

	/**
	 * We use the ID from book and branch for this object's hash-code.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getBook(), getBranch());
	}

	/**
	 * An object is equal to this one iff it is a Copies with the
	 * same primary keys, and noOfCopies
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Copies) {
			return Objects.equals(getBook(), ((Copies) obj).getBook())
					&& Objects.equals(getBranch(), ((Copies) obj).getBranch())
					&& Objects.equals(noOfCopies, ((Copies) obj).getNoOfCopies());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return getBranch().getName() + " has " + noOfCopies + " copies of " + getBook().getTitle() + ".";
	}
}
