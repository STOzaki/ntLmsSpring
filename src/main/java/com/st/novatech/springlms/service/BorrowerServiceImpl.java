package com.st.novatech.springlms.service;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.st.novatech.springlms.dao.BookDao;
import com.st.novatech.springlms.dao.BookLoansDao;
import com.st.novatech.springlms.dao.BorrowerDao;
import com.st.novatech.springlms.dao.CopiesDao;
import com.st.novatech.springlms.dao.LibraryBranchDao;
import com.st.novatech.springlms.exception.CriticalSQLException;
import com.st.novatech.springlms.exception.RetrieveException;
import com.st.novatech.springlms.exception.TransactionException;
import com.st.novatech.springlms.exception.UnknownSQLException;
import com.st.novatech.springlms.model.Book;
import com.st.novatech.springlms.model.Borrower;
import com.st.novatech.springlms.model.Branch;
import com.st.novatech.springlms.model.Copies;
import com.st.novatech.springlms.model.Loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The "service" class to help UIs for borrowers.
 *
 * @author Jonathan Lovelace
 */
@Service("BorrowerService")
public class BorrowerServiceImpl implements BorrowerService {
	/**
	 * The DAO for the "branches" table.
	 */
	@Autowired
	private LibraryBranchDao branchDao;
	/**
	 * The DAO for the "loans" table.
	 */
	@Autowired
	private BookLoansDao loanDao;
	/**
	 * The DAO for the "copies" table.
	 */
	@Autowired
	private CopiesDao copiesDao;
	/**
	 * The DAO for the "borrowers" table.
	 */
	@Autowired
	private BorrowerDao borrowerDao;
	/**
	 * Logger for handling errors in the DAO layer.
	 */
	private static final Logger LOGGER = Logger.getLogger(BorrowerService.class.getName());
	
	/**
	 * The DAO for the "books" table.
	 */
	@Autowired
	private BookDao bookDao;

	@Override
	public List<Branch> getAllBranches() throws TransactionException {
		try {
		return branchDao.findAll();
		} catch (final Exception except) {
			LOGGER.log(Level.SEVERE,  "Error while getting all branches", except);
			throw new CriticalSQLException("Error occured while getting all branches failed", except);
		}
	}

	@Transactional
	@Override
	public Loan borrowBook(final Borrower borrower, final Book book,
			final Branch branch, final LocalDateTime dateOut,
			final LocalDate dueDate) throws TransactionException {
		try {
			if (loanDao.getLoanByIds(book.getId(), borrower.getCardNo(),
					branch.getId()) == null) {
				
				final int copies = getCopies(branch, book);
				if (copies > 0) {
					setCopies(branch, book, copies - 1);
					loanDao.createLoanWithIds(book.getId(), branch.getId(),
				borrower.getCardNo(), Date.valueOf(dateOut.toLocalDate()),
				Date.valueOf(dueDate));

					return loanDao.getLoanByIds(book.getId(), borrower.getCardNo(),
						branch.getId());
				} else {
					return null;
				}
			} else {
				return null; // TODO: Add getLoan() method to interface
			}
		} catch (final Exception except) {
			LOGGER.log(Level.SEVERE, "SQL error while creating a loan record", except);
			throw new UnknownSQLException("Creating a loan failed", except);
		}
	}

	@Override
	public List<Copies> getAllBranchCopies(final Branch branch)
			throws TransactionException {
		try {
			return copiesDao.getAllBranchCopies(branch.getId());
		} catch (final Exception except) {
			LOGGER.log(Level.SEVERE, "Error while getting branch copies", except);
			throw new UnknownSQLException("Getting branch copy records failed", except);
		}
	}

	@Transactional
	@Override
	public Boolean returnBook(final Borrower borrower, final Book book,
			final Branch branch, final LocalDate dueDate)
					throws TransactionException {
		final Loan loan;
		try {
			loan = loanDao.getLoanByIds(book.getId(), branch.getId(), borrower.getCardNo());
			
			if (loan != null) {
				if (LocalDate.now().isAfter(loan.getDueDate())) {
					return false;
				} else {
					final int copies = getCopies(branch, book);
					setCopies(branch, book, copies + 1);
					loanDao.delete(loan);
					return true;
				}
			} else {
				return null;
			}
		} catch (final Exception exception) {
			throw new UnknownSQLException("Committing the transaction failed", exception);
		}
	}

	@Override
	public List<Branch> getAllBranchesWithLoan(final Borrower borrower)
			throws TransactionException {
		try {
			return getAllBorrowedBooks(borrower).parallelStream().map(b -> b.getBranch())
					.collect(Collectors.toList());
		} catch (final Exception exception) {
			throw new RetrieveException("Committing the transaction failed", exception);
		}
	}

	@Override
	public List<Loan> getAllBorrowedBooks(final Borrower borrower) throws TransactionException {
		try {
			return loanDao.findAll().parallelStream()
					.filter(loan -> borrower.equals(loan.getBorrower()))
					.collect(Collectors.toList());
		} catch (final Exception exception) {
			throw new RetrieveException("Committing the transaction failed", exception);
		}
	}

	@Override
	public Borrower getBorrower(final int cardNo) throws TransactionException {
		try {
			Optional<Borrower> foundBorrower = borrowerDao.findById(cardNo);
			if(foundBorrower.isPresent()) {
				return foundBorrower.get();
			} else {
				return null;
			}
		} catch (final Exception e) {
			LOGGER.log(Level.SEVERE, "Error while retrieving a borrower", e);
			throw new RetrieveException("We were unable to find the requested borrower due to errors.");
		}
	}

	@Override
	public Branch getbranch(int branchId) throws TransactionException {
		try {
			Optional<Branch> foundbranch = branchDao.findById(branchId);
			if(foundbranch.isPresent()) {
				return foundbranch.get();
			} else {
				return null;
			}
		} catch (final Exception e) {
			LOGGER.log(Level.SEVERE, "Error while retrieving a branch", e);
			throw new RetrieveException("We were unable to find the requested branch due to errors.");
		}
	}

	@Override
	public Book getBook(int bookId) throws TransactionException {
		try {
			Optional<Book> foundbook = bookDao.findById(bookId);
			if(foundbook.isPresent()) {
				return foundbook.get();
			} else {
				return null;
			}
		} catch (final Exception e) {
			LOGGER.log(Level.SEVERE, "Error while retrieving a book", e);
			throw new RetrieveException("We were unable to find the requested book due to errors.");
		}
	}

	@Override
	public Loan getLoan(int cardNo, int branchId, int bookId) throws TransactionException {
		try {
			return loanDao.getLoanByIds(bookId, branchId, cardNo);
		} catch (final Exception e) {
			LOGGER.log(Level.SEVERE, "Error while retrieving loans", e);
			throw new RetrieveException("We were unable to find the requested loan due to errors.");
		}
	}
	
	/**
	 * Set the number of copies of a book held by a particular branch. If the number
	 * is set to 0, the row is deleted from the database.
	 *
	 * @param branch     the branch in question
	 * @param book       the book in question
	 * @param noOfCopies the number of copies held by that branch; MUST not be
	 *                   negative.
	 * @throws TransationException on unexpected error in dealing with the database. WARNING (NEED to prevent user from passing negative numbers)
	 */
	protected void setCopies(Branch branch, Book book, int noOfCopies) throws TransactionException {
		try {
			if (noOfCopies < 0) {
				throw new IllegalArgumentException(
						"Number of copies must be nonnegative");
			} else if (book == null || branch == null) {
				// TODO: throw IllegalArgumentException?
			} else if (noOfCopies == 0) {
				copiesDao.deleteCopies(branch.getId(), book.getId());
			} else if (getCopies(branch, book) == 0) {
				// TODO: Use INSERT ... ON DUPLICATE KEY UPDATE
				copiesDao.createCopies(branch.getId(), book.getId(), noOfCopies);
			} else {
				copiesDao.updateCopies(noOfCopies, branch.getId(), book.getId());
			}
		} catch (SQLException e) {
			throw new UnknownSQLException("Error with setting copies", e);
		}
	}
	
	/**
	 * Get the copies entity for a given branch and book
	 * 
	 * @param branch	branch in question
	 * @param book		book in question
	 * @return			number of copies for a given branch and book
	 * @throws TransactionException
	 */
	protected int getCopies(final Branch branch, final Book book) throws TransactionException {
		try {
			if (branch == null || book == null) {
				return 0; // TODO: Throw IllegalArgumentException instead?
			} else {
				return copiesDao.getCopiesByIds(branch.getId(), book.getId()).getNoOfCopies();
			}
		} catch (SQLException e) {
			throw new RetrieveException("Error with getting copies");
		}
	}
}