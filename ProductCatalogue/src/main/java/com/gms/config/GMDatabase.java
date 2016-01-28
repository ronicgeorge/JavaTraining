package com.gms.config;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class GMDatabase extends Configuration {
	
	@JsonProperty("couchbase.nodes")
	private String[] couchbaseNodes;

	@JsonProperty("couchbase.bucket")
	private String couchbaseBucket;

	@JsonProperty("couchbase.password")
	private String couchbasePassword;

	public String[] getCouchbaseNodes() {
		return couchbaseNodes;
	}

	public String getCouchbaseBucket() {
		return couchbaseBucket;
	}

	public String getCouchbasePassword() {
		return couchbasePassword;
	}
}
