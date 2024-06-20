package com.zubayer.zpos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Customer;
import com.zubayer.zpos.entity.pk.CustomerPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 12, 2024
 */
@Repository
public interface CustomerRepo extends JpaRepository<Customer, CustomerPK> {

}
