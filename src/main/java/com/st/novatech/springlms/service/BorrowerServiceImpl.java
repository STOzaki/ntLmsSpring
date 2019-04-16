package com.st.novatech.springlms.service;

import java.sql.Date;
//import java.io.IOException;
//import java.sql.Connection;
import java.sql.SQLException;
//import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
//import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.st.novatech.springlms.dao.BookDao;
import com.st.novatech.springlms.dao.BookLoansDao;
import com.st.novatech.springlms.dao.BorrowerDao;
import com.st.novatech.springlms.dao.CopiesDao;
//import com.st.novatech.springlms.dao.DBConnectionFactory;
import com.st.novatech.springlms.dao.LibraryBranchDao;
import com.st.novatech.springlms.exception.DeleteException;
import com.st.novatech.springlms.exception.InsertException;
import com.st.novatech.springlms.exception.RetrieveException;
//import com.st.novatech.springlms.exception.RetrieveException;
import com.st.novatech.springlms.exception.TransactionException;
import com.st.novatech.springlms.exception.UnknownSQLException;
import com.st.novatech.springlms.model.Book;
import com.st.novatech.springlms.model.Borrower;
import com.st.novatech.springlms.model.Branch;
import com.st.novatech.springlms.model.Loan;
import com.st.novatech.springlms.util.ThrowingRunnable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The "service" class to help UIs for borrowers.
 *
 * @author Jonathan Lovelace
 */
@Service("BorrowerService")
public final class BorrowerServiceImpl implements BorrowerService {
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
//	/**
//	 * The clock to get "the current time" from.
//	 */
//	private final Clock clock;
//	/**
//	 * Logger for handling errors in the DAO layer.
//	 */
	private static final Logger LOGGER = Logger.getLogger(BorrowerService.class.getName());
//	/**
//	 * Method to use to commit a transaction, if the DAO backend supports transactions.
//	 */
//	private final ThrowingRunnable<SQLException> commitHandle;
//	/**
//	 * Method to use to roll back a transaction, if the DAO backend supports transactions.
//	 */
//	private final ThrowingRunnable<SQLException> rollbackHandle;
	
	/**
	 * The DAO for the "books" table.
	 */
	@Autowired
	private BookDao bookDao;

//	/**
//	 * To construct this service class, the caller must supply instances of each DAO
//	 * it uses, a clock to get "the current date," and method references to commit
//	 * and roll back transactions.
//	 *
//	 * @param branchDao   the library-branch DAO.
//	 * @param loanDao     the loan DAO
//	 * @param copiesDao   the copies DAO
//	 * @param borrowerDao the borrower DAO
//	 * @param commit      the method handle to commit a transaction, if the backend
//	 *                    supports that
//	 * @param rollback    the method handle to roll back a transaction, if the
//	 *                    backend supports that
//	 */
//	public BorrowerServiceImpl(final LibraryBranchDao branchDao, final BookLoansDao loanDao, final CopiesDao copiesDao,
//			final BorrowerDao borrowerDao, final BookDao bookDao, final Clock clock,
//			final ThrowingRunnable<SQLException> commit, final ThrowingRunnable<SQLException> rollback) {
//		this.branchDao = branchDao;
//		this.loanDao = loanDao;
//		this.copiesDao = copiesDao;
//		this.borrowerDao = borrowerDao;
//		this.clock = clock;
//		this.bookDao = bookDao;
//		commitHandle = commit;
//		rollbackHandle = rollback;
//	}
//
//	/**
//	 * To construct this service class using this constructor, the caller must
//	 * merely supply a connection to the database.
//	 * 
//	 * @param db the connection to the database
//	 * @throws SQLException on error setting up DAOs.
//	 */
//	public BorrowerServiceImpl(final Connection db) throws SQLException {
//		this(new LibraryBranchDaoImpl(db), new BookLoansDaoImpl(db), new CopiesDaoImpl(db), new BorrowerDaoImpl(db),
//				new BookDaoImpl(db), Clock.systemDefaultZone(), db::commit, db::rollback);
//	}
//
//	/**
//	 * Constructor that uses the default DB connection factory to supply the
//	 * database connection and uses the default DAO implementations.
//	 * 
//	 * @throws IOException  on I/O error reading DB configuration
//	 * @throws SQLException on error setting up the database or DAOs
//	 */
//	public BorrowerServiceImpl() throws IOException, SQLException {
//		this(DBConnectionFactory.getDatabaseConnection());
//	}
//
//	@Override
//	public List<Branch> getAllBranches() throws TransactionException {
//		try {
//			return branchDao.getAll();
//		} catch (final SQLException except) {
//			LOGGER.log(Level.SEVERE,  "SQL error while getting all branches", except);
//			throw rollback(new UnknownSQLException("Getting all branches failed", except));
//		}
//	}

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
		} catch (final TransactionException except) {
			LOGGER.log(Level.SEVERE, "SQL error while creating a loan record", except);
			throw new UnknownSQLException("Creating a loan failed", except);
//			throw rollback(new InsertException("Creating a loan failed", except));
		}
	}
//
//	@Override
//	public Map<Book, Integer> getAllBranchCopies(final Branch branch)
//			throws TransactionException {
//		try {
//			return copiesDao.getAllBranchCopies(branch);
//		} catch (final SQLException except) {
//			LOGGER.log(Level.SEVERE, "SQL error while getting branch copies", except);
//			throw rollback(new UnknownSQLException("Getting branch copy records failed", except));
//		}
//	}

	@Override
	public Boolean returnBook(final Borrower borrower, final Book book,
			final Branch branch, final LocalDate dueDate)
					throws TransactionException {
		final Loan loan;
//		try {
			loan = loanDao.getLoanByIds(book.getId(), branch.getId(), borrower.getCardNo());
//		} catch (final SQLException except) {
//			LOGGER.log(Level.SEVERE, "SQL error while getting loan details", except);
//			throw new InsertException("Getting loan details failed", except);
////			throw rollback(new UnknownSQLException("Getting loan details failed", except));
//		}
		
		if (loan != null) {
			if (LocalDate.now().isAfter(loan.getDueDate())) {
				return false;
			} else {
//				try {
					final int copies = getCopies(branch, book);
					setCopies(branch, book, copies + 1);
//				} catch (final SQLException except) {
//					LOGGER.log(Level.SEVERE, "SQL error while incrementing copies on return", except);
//					throw new UnknownSQLException("Incrementing copies on return failed", except);
////					throw rollback(new UnknownSQLException("Incrementing copies on return failed", except));
//				}
//				try {
					loanDao.delete(loan);
//				} catch (final SQLException except) {
//					LOGGER.log(Level.SEVERE, "SQL error while removing a loan record", except);
//					throw new DeleteException("Removing loan record failed", except);
////					throw rollback(new DeleteException("Removing loan record failed", except));
//				}
				return true;
			}
		} else {
			return null;
		}
	}

//	@Override
//	public List<Branch> getAllBranchesWithLoan(final Borrower borrower) {
////			throws TransactionException {
//		return getAllBorrowedBooks(borrower).parallelStream().map(b -> b.getBranch())
//				.collect(Collectors.toList());
//	}

	@Override
	public List<Loan> getAllBorrowedBooks(final Borrower borrower) {
		return loanDao.findAll().parallelStream()
				.filter(loan -> borrower.equals(loan.getBorrower()))
				.collect(Collectors.toList());
//			throws TransactionException {
//		try {
//			return loanDao.getAll().parallelStream()
//					.filter(loan -> borrower.equals(loan.getBorrower()))
//					.collect(Collectors.toList());
//		} catch (final SQLException except) {
//			LOGGER.log(Level.SEVERE, "SQL error while getting loan records", except);
//			throw rollback(new UnknownSQLException("Getting loan records failed", except));
//		}
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
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while retrieving a borrower", e);
			throw new RetrieveException("We were unable to find the requested borrower due to errors.");
		}
//		try {
//			return borrowerDao.get(cardNo);
//		} catch (final SQLException except) {
//			LOGGER.log(Level.SEVERE, "SQL error while getting borrower details", except);
//			throw rollback(new UnknownSQLException("Getting borrower record failed", except));
//		}
	}

//	@Override
//	public void commit() throws TransactionException {
//		try {
//			commitHandle.run();
//		} catch (final SQLException except) {
//			LOGGER.log(Level.SEVERE, "Error of some kind while committing transaction", except);
//			throw new UnknownSQLException("Committing the transaction failed", except);
//		}
//	}
//	private <E extends Exception> E rollback(final E pending) {
//		try {
//			rollbackHandle.run();
//		} catch (final SQLException except) {
//			LOGGER.log(Level.SEVERE, "Further error while rolling back transaction", except);
//			pending.addSuppressed(except);
//		}
//		return pending;
//	}

	@Override
	public Branch getbranch(int branchId) throws TransactionException {
		try {
			Optional<Branch> foundbranch = branchDao.findById(branchId);
			if(foundbranch.isPresent()) {
				return foundbranch.get();
			} else {
				return null;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while retrieving a branch", e);
			throw new RetrieveException("We were unable to find the requested branch due to errors.");
		}
		
//		try {
//			foundbranch = branchDao.get(branchId);
//		} catch (final SQLException except) {
//			LOGGER.log(Level.SEVERE, "SQL error while getting a branch", except);
//			throw rollback(new RetrieveException("Getting a branch failed", except));
//		}
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
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while retrieving a book", e);
			throw new RetrieveException("We were unable to find the requested book due to errors.");
		}
		
//		try {
//			foundbook = bookDao.get(bookId);
//		} catch (final SQLException except) {
//			LOGGER.log(Level.SEVERE, "SQL error while getting a book", except);
//			throw rollback(new RetrieveException("Getting a book failed", except));
//		}
	}

	@Override
	public Loan getLoan(int cardNo, int branchId, int bookId) throws TransactionException {
		try {
			return loanDao.getLoanByIds(bookId, branchId, cardNo);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while retrieving loans", e);
			throw new RetrieveException("We were unable to find the requested loan due to errors.");
		}
//		try {
//			foundLoan = loanDao.get(bookDao.get(bookId), borrowerDao.get(cardNo), branchDao.get(branchId));
//		} catch (final SQLException except) {
//			LOGGER.log(Level.SEVERE, "SQL error while getting a Loan record", except);
//			throw rollback(new RetrieveException("Getting a Loan failed", except));
//		}
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