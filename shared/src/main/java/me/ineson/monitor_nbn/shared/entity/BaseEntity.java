package me.ineson.monitor_nbn.shared.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * @author peter
 *
 */
public abstract class BaseEntity {

	@Id
	private ObjectId id;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}
