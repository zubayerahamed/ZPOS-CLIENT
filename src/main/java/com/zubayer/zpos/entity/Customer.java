package com.zubayer.zpos.entity;

import com.zubayer.zpos.entity.pk.CustomerPK;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jun 12, 2024
 */
@Data
@Entity
@Table(name = "customer")
@IdClass(CustomerPK.class)
@EqualsAndHashCode(callSuper = true)
public class Customer extends AbstractModel<Integer> {

	private static final long serialVersionUID = -7837504511822802289L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xmobile")
	private String xmobile;

	@Column(name = "xname", length = 100)
	private String xname;
}
