package com.gms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gms.config.GMDatabase;
import com.gms.controller.CatalogueController;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class GMApplication extends Application<GMDatabase> {
	private static final Logger LOGGER = LoggerFactory.getLogger(GMApplication.class);

	@Override
	public void initialize(Bootstrap<GMDatabase	> b) {
	}

	@Override
	public void run(GMDatabase c, Environment e)
			throws Exception {
		LOGGER.info("Method App#run() called");
		System.out.println("Hello world, by Dropwizard!");
		System.out.println("Coucbase Bucket : " + c.getCouchbaseBucket());
		e.jersey().register(new CatalogueController(c));
	}

	public static void main(String[] args) throws Exception {
		new GMApplication().run(args);
	}
}