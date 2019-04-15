package com.st.novatech.springlms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.novatech.springlms.model.Borrower;

/**
 * A Data Access Object interface to access the table of borrowers.
 *
 * @author Salem Ozaki
 * @author Jonathan Lovelace
 */
@Repository
public interface BorrowerDao extends Dao<Borrower>, JpaRepository<Borrower, Integer> {
}
