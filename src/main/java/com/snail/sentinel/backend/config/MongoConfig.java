package com.snail.sentinel.backend.config;

import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return null;
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
