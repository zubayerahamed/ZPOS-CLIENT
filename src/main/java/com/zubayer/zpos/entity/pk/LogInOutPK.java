package com.zubayer.zpos.entity.pk;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since May 30, 2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogInOutPK implements Serializable {

	private static final long serialVersionUID = 9109185959364010355L;

	private Integer zid;
	private Integer xoutlet;
	private Integer xshop;
	private Integer xterminal;
	private Integer xshift;
	private Date xgdate;
}
