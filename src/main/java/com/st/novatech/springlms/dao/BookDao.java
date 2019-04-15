package com.st.novatech.springlms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import com.st.novatech.springlms.model.Author;
import com.st.novatech.springlms.model.Book;
//import com.st.novatech.springlms.model.Borrower;
//import com.st.novatech.springlms.model.Publisher;

/**
 * A Data Access Object interface to access the table of books.
 *
 * @author Salem Ozaki
 * @author Jonathan Lovelace
 */
@Repository
public interface BookDao extends Dao<Book>, JpaRepository<Book, Integer> {
}
