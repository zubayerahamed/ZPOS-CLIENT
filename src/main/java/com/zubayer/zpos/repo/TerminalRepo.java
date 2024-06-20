package com.zubayer.zpos.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Terminal;
import com.zubayer.zpos.entity.pk.TerminalPK;

/**
 * @author Zubayer Ahamed
 * @since Apr 8, 2024
 * CSE202101068
 */
@Repository
public interface TerminalRepo extends JpaRepository<Terminal, TerminalPK> {

	List<Terminal> findAllByZid(Integer zid);
}
