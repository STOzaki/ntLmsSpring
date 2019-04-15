package com.st.novatech.springlms.dao;

import java.sql.SQLException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.st.novatech.springlms.model.Copies;

/**
 * A Data Access Object interface to access the number of copies of books in
 * branches.
 *
 * @author Salem Ozaki
 * @author Jonathan Lovelace
 */
@Repository
public interface CopiesDao extends JpaRepository<Copies, Integer> {
	/**
	 * Get the copies of a book held by a particular branch.
	 *
	 * @param branchId The branchId in question.
	 * @param bookId   The bookId in question.
	 * @return the copies entity
	 * @throws SQLException on unexpected error in dealing with the database.
	 */
	@Query(value = "SELECT * FROM tbl_book_copies c WHERE c.branchId = ? AND c.bookId = ?", nativeQuery = true)
	public Copies getCopiesByIds(int branchId, int bookId) throws SQLException;

	/**
	 * create Copies entry
	 *
	 * @param branchId     the branchId in question
	 * @param bookId       the bookId in question
	 * @param noOfCopies the number of copies held by that branch; must not be
	 *                   negative.
	 * @throws SQLException on unexpected error in dealing with the database.
	 */
	@Modifying
	@Query(value = "INSERT INTO tbl_book_copies (branchId, bookId, noOfCopies) VALUES (?, ?, ?)", nativeQuery = true)
	@Transactional
	public void createCopies(int branchId, int bookId, int noOfCopies) throws SQLException;
	
	/**
	 * Update copies entry with new noOfCopies
	 * 
	 * @param noOfCopies	the number of copies held by that branch; must not be
	 *                   negative.
	 * @param branchId		the branchId in question
	 * @param bookId		the bookId in question
	 * @throws SQLException
	 */
	@Modifying
	@Query(value = "UPDATE tbl_book_copies SET noOfCopies = ? WHERE branchId = ? AND bookId = ?", nativeQuery = true)
	@Transactional
	public void updateCopies(int noOfCopies, int branchId, int bookId) throws SQLException;
	
	/**
	 * Delete copies entry
	 * 
	 * @param branchId	branchId in question
	 * @param bookId	bookId in question
	 * @throws SQLException	If something goes wrong when executing the query
	 */
	@Modifying
	@Query(value = "DELETE FROM tbl_book_copies WHERE branchId = ? AND bookId = ?", nativeQuery = true)
	@Transactional
	public void deleteCopies(int branchId, int bookId) throws SQLException;
//
//	/**
//	 * Retrieve all copies held by the given branch, as a mapping from books to the
//	 * number held.
//	 *
//	 * @param branch the branch in question
//	 * @return the number of copies of all books the branch holds.
//	 * @throws SQLException on unexpected error in dealing with the database.
//	 */
//	Map<Book, Integer> getAllBranchCopies(Branch branch) throws SQLException;
//
//	/**
//	 * Retrieve all copies of the given book held by any branch, as a mapping from
//	 * branches to the number of copies of the book they hold.
//	 *
//	 * @param book the book in question
//	 * @return the number of copies of that book in each branch that holds it.
//	 * @throws SQLException on unexpected error in dealing with the database.
//	 */
//	Map<Branch, Integer> getAllBookCopies(Book book) throws SQLException;
//
//	/**
//	 * Retrieve a list of all copies of all books held by all branches.
//	 *
//	 * @return the number of copies of all books in all branches.
//	 * @throws SQLException on unexpected error in dealing with the database.
//	 */
//	List<Copies> getAllCopies() throws SQLException;
}
