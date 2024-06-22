package com.zubayer.zpos.entity;

import java.math.BigDecimal;

import com.zubayer.zpos.entity.pk.ItemAddonsPK;
import com.zubayer.zpos.enums.SubmitFor;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jun 16, 2024
 */
@Data
@Entity
@Table(name = "item_addons")
@IdClass(ItemAddonsPK.class)
@EqualsAndHashCode(callSuper = true)
public class ItemAddons extends AbstractModel<Integer> {

	private static final long serialVersionUID = -1284100415450552808L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xitem")
	private Integer xitem;

	@Id
	@Basic(optional = false)
	@Column(name = "xaddons")
	private Integer xaddons;

	@Transient
	private String addonsName;

	@Transient
	private BigDecimal addonsPrice;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static ItemAddons getDefaultInstance(Integer xitem) {
		ItemAddons obj = new ItemAddons();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXitem(xitem);
		return obj;
	}
}
