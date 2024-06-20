package com.zubayer.zpos.entity;

import java.math.BigDecimal;

import com.zubayer.zpos.entity.pk.CurrencyPK;

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
 * @since Jun 15, 2024
 */
@Data
@Entity
@Table(name = "currency")
@IdClass(CurrencyPK.class)
@EqualsAndHashCode(callSuper = true)
public class Currency extends AbstractModel<Integer> {

	private static final long serialVersionUID = 2253798612486247362L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcode", length = 3)
	private String xcode;

	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xsign", length = 10)
	private String xsign;

	@Column(name = "xconvf")
	private BigDecimal xconvf;

	@Column(name = "xsignposition")
	private String xsignposition;
}
