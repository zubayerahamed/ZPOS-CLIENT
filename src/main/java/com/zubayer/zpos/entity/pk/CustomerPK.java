package com.zubayer.zpos.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jun 12, 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPK implements Serializable {

	private static final long serialVersionUID = -39207130762521663L;

	private Integer zid;
	private String xmobile;
}
