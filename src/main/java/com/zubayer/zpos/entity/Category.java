package com.zubayer.zpos.entity;

import com.zubayer.zpos.entity.pk.CategoryPK;
import com.zubayer.zpos.enums.SubmitFor;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Apr 26, 2024 
 * CSE202401068
 */
@Data
@Entity
@Table(name = "category")
@IdClass(CategoryPK.class)
@EqualsAndHashCode(callSuper = true)
public class Category extends AbstractModel<Integer> {

	private static final long serialVersionUID = -2212434269045686076L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcode")
	private Integer xcode;

	@NotBlank(message = "Category name required")
	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xseqn")
	private Integer xseqn = Integer.valueOf(0);

	@Column(name = "xicon", length = 50)
	private String xicon;

	@Column(name = "xthumbnail", length = 250)
	private String xthumbnail;

	@Column(name = "xpcode", nullable = true)
	private Integer xpcode;

	@Column(name = "zactive", length = 1)
	private Boolean zactive = Boolean.TRUE;

	@Transient
	private String parentCategory;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Category getDefaultInstance() {
		Category obj = new Category();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXseqn(0);
		obj.setZactive(true);
		return obj;
	}
}
