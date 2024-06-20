package com.zubayer.zpos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Xtable;
import com.zubayer.zpos.entity.pk.XtablePK;

/**
 * @author Zubayer Ahamed
 * @since Jun 12, 2024
 */
@Repository
public interface XtableRepo extends JpaRepository<Xtable, XtablePK> {

}
