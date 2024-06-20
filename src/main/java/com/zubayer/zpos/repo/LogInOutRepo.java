package com.zubayer.zpos.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.LogInOut;
import com.zubayer.zpos.entity.pk.LogInOutPK;

/**
 * @author Zubayer Ahamed
 * @since May 30, 2024
 */
@Repository
public interface LogInOutRepo extends JpaRepository<LogInOut, LogInOutPK>{

	Optional<LogInOut> findTop1ByZidAndXoutletAndXshopAndXterminalOrderByXgdateDescXshiftDesc(Integer zid, Integer xoutlet, Integer xshop, Integer xterminal);
}
