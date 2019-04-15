package com.st.novatech.springlms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.novatech.springlms.model.Branch;

/**
 * A Data Access Object class to access the table of library branches.
 *
 * @author Salem Ozaki
 * @author Jonathan Lovelace
 */
@Repository
public interface LibraryBranchDao extends Dao<Branch>, JpaRepository<Branch, Integer> {
}
