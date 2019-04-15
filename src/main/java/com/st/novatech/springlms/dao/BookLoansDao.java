package com.st.novatech.springlms.dao;

import com.st.novatech.springlms.model.Loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * A Data Access Object interface to access the table of outstanding loans.
 *
 * @author Salem Ozaki
 * @author Jonathan Lovelace
 */
public interface BookLoansDao extends JpaRepository<Loan, Integer> {
	@Query(value = "SELECT * FROM tbl_book_loans l WHERE l.bookId = ? AND l.branchId = ? AND l.cardNo = ?", nativeQuery = true)
	public Loan getLoanByIds(int bookId, int branchId, int cardNo);
}
