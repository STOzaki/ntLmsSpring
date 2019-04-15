package com.st.novatech.springlms.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A user of the library who is able to check out books.
 *
 * @author Salem Ozaki
 * @author Jonathan Lovelace
 */
@Entity
@Table(name = "tbl_borrower")
public class Borrower implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The borrower's card number, used as this object's identity.
	 */
	@Id
	@Column(name = "cardNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cardNo;
	/**
	 * The borrower's name.
	 */
	@Column(name = "name")
	private String name;
	/**
	 * The borrower's address.
	 */
	@Column(name = "address")
	private String address;
	/**
	 * The borrower's phone number.
	 */
	@Column(name = "phone")
	private String phone;

	@JsonBackReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "compositeKey.borrower")
	private List<Loan> loans;
	
	/**
	 * Get list of loans belonging to this borrower
	 * 
	 * @return	get list of loans belonging to this borrower
	 */
	public List<Loan> getLoans() {
		return loans;
	}

	/**
	 * Set new list of loans
	 * 
	 * @param loans	set new list of loans for this borrower
	 */
	public void setLoans(List<Loan> loans) {
		this.loans = loans;
	}

	/**
	 * Get the borrower's name, which will not be null.
	 *
	 * @return the borrower's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Change the borrower's name.
	 *
	 * @param name the borrower's new name, which should not be null.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Get the borrower's address, which will not be null.
	 *
	 * @return the borrower's address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Change the borrower's address.
	 *
	 * @param address the borrower's new address, which should not be null.
	 */
	public void setAddress(final String address) {
		this.address = address;
	}

	/**
	 * Get the borrower's phone number as a non-null string.
	 *
	 * @return the borrower's phone number.
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Change the borrower's phone number.
	 *
	 * @param phone the borrower's new phone number, which should not be null.
	 */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
	 * Get the borrower's card number.
	 *
	 * @return the borrower's card number
	 */
	public int getCardNo() {
		return cardNo;
	}

	/**
	 * Change cardNo for the borrower
	 * 
	 * @param cardNo the new cardNo
	 */
	public void setCardNo(int cardNo) {
		this.cardNo = cardNo;
	}

	/**
	 * We use only the ID for this object's hash-code.
	 */
	@Override
	public int hashCode() {
		return cardNo;
	}

	/**
	 * An object is equal to this one iff it is a Borrower with the same card
	 * number, name, address, and phone number.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Borrower) {
			return cardNo == ((Borrower) obj).getCardNo()
					&& Objects.equals(name, ((Borrower) obj).getName())
					&& Objects.equals(address, ((Borrower) obj).getAddress())
					&& Objects.equals(phone, ((Borrower) obj).getPhone());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Borrower " + name + "(" + cardNo + ") at " + address + " with phone: " + phone;
	}
}
