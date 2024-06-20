package com.zubayer.zpos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Currency;
import com.zubayer.zpos.entity.pk.CurrencyPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 15, 2024
 */
@Repository
public interface CurrencyRepo extends JpaRepository<Currency, CurrencyPK> {

}
