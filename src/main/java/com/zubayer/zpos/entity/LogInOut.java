package com.zubayer.zpos.entity;

import java.util.Date;

import com.zubayer.zpos.entity.pk.LogInOutPK;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@IdClass(LogInOutPK.class)
@Table(name = "log_in_out")
@EqualsAndHashCode(callSuper = true)
public class LogInOut extends AbstractModel<Integer> {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xoutlet")
	private Integer xoutlet;

	@Id
	@Basic(optional = false)
	@Column(name = "xshop")
	private Integer xshop;

	@Id
	@Basic(optional = false)
	@Column(name = "xterminal")
	private Integer xterminal;

	@Id
	@Basic(optional = false)
	@Column(name = "xshift")
	private Integer xshift;

	@Id
	@Basic(optional = false)
	@Column(name = "xgdate")
	@Temporal(TemporalType.DATE)
	private Date xgdate;

	@Column(name = "xusername")
	private Integer xusername;

	@Column(name = "xseqn")
	private Integer xseqn;

	@Column(name = "xintime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xintime;

	@Column(name = "xouttime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xouttime;

	@Column(name = "xstatus")
	private String xstatus;

	@Transient
	private String signInUser;
}
