package com.zubayer.zpos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Xfloor;
import com.zubayer.zpos.entity.pk.XfloorPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 12, 2024
 */
@Repository
public interface XfloorRepo extends JpaRepository<Xfloor, XfloorPK> {

}
