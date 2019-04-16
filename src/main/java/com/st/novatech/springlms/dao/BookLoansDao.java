package com.st.novatech.springlms.dao;

import java.sql.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.st.novatech.springlms.model.Loan;


/**
 * A Data Access Object interface to access the table of outstanding loans.
 *
 * @author Salem Ozaki
 * @author Jonathan Lovelace
 */
@Repository
public interface BookLoansDao extends JpaRepository<Loan, Integer> {
	@Query(value = "SELECT * FROM tbl_book_loans l WHERE l.bookId = ? AND l.branchId = ? AND l.cardNo = ?", nativeQuery = true)
	public Loan getLoanByIds(int bookId, int branchId, int cardNo);
	
	@Modifying
	@Query(value = "INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) VALUES (?, ?, ?, ?, ?)", nativeQuery = true)
	@Transactional
	public void createLoanWithIds(int bookId, int branchId, int cardNo, Date dateOut, Date dueDate);
}
