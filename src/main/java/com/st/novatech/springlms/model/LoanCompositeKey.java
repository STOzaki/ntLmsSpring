package com.st.novatech.springlms.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LoanCompositeKey implements Serializable {

    @Column(name = "bookId")
    private int bookId;
 
    @Column(name = "branchId")
    private int branchId;
    
    @Column(name = "cardNo")
    private int cardNo;
 
    public LoanCompositeKey() {
    }
 
    public LoanCompositeKey(int bookId, int branchId, int cardNo) {
        this.bookId = bookId;
        this.branchId = branchId;
        this.cardNo = cardNo;
    }
 
    /**
     * Get bookId
     * @return	bookId
     */
    public int getBookId() {
		return bookId;
	}
    
    /**
     * Get branchId
     * @return	branchId
     */
	public int getBranchId() {
		return branchId;
	}

	/**
     * Get cardNo
     * @return	cardNo
     */
	public int getCardNo() {
		return cardNo;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof LoanCompositeKey) {
        	return Objects.equals(getBookId(), ((LoanCompositeKey) o).getBookId())
        			&& Objects.equals(getBranchId(), ((LoanCompositeKey) o).getBranchId())
        			&& Objects.equals(getCardNo(), ((LoanCompositeKey) o).getCardNo());
        } else {
        	return false;
        }
    }
 
    @Override
    public int hashCode() {
    	return Objects.hash(bookId, cardNo, branchId);
    }

}
