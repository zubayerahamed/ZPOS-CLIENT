package com.zubayer.zpos.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Zbusiness;

/**
 * @author Zubayer Ahamed
 * @since Mar 23, 2024
 * CSE202101068
 */
@Repository
public interface ZbusinessRepo extends JpaRepository<Zbusiness, Integer> {

	Optional<Zbusiness> findTop1ByZidOrderByUpdatedOnDesc(Integer zid);

	Optional<Zbusiness> findTop1ByZidOrderByCreatedOnDesc(Integer zid);
}
