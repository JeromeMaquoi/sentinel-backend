package com.snail.sentinel.backend.repository.impl;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepositoryAggregation;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;


import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class CkEntityRepositoryAggregationImpl implements CkEntityRepositoryAggregation {
    private static final Logger log = LoggerFactory.getLogger(CkEntityRepositoryAggregationImpl.class);
    private final MongoTemplate mongoTemplate;

    public CkEntityRepositoryAggregationImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CkAggregateLineHashMapDTO aggregate(String repoName) {
        MatchOperation matchOperation = getRepoNameMatchOperation(repoName);
        AddFieldsOperation addFieldsOperation = addLineLocFieldsOperation();
        GroupOperation groupOperation = groupOperation();

        Aggregation aggregation = newAggregation(matchOperation, addFieldsOperation, groupOperation);
        AggregationResults<CkAggregateLineDTO> output = mongoTemplate.aggregate(aggregation, CkEntity.class, CkAggregateLineDTO.class);
        CkAggregateLineHashMapDTO result = new CkAggregateLineHashMapDTO();
        result.setCkAggregateLineHashMapDTO(output.getMappedResults());
        return result;

    }

    private MatchOperation getRepoNameMatchOperation(String repoName) {
        Criteria lineCriteria = where("name").is("line");
        Criteria locCriteria = where("name").is("loc");
        Criteria repoNameCriteria = where("commit.repository.name").is(repoName).and("measurableElement.astElem").is("method").orOperator(lineCriteria, locCriteria);
        return Aggregation.match(repoNameCriteria);
    }

    private AddFieldsOperation addLineLocFieldsOperation() {
        ConditionalOperators.Cond lineCond = ConditionalOperators.Cond.when(Criteria.where("name").is("line")).thenValueOf("value").otherwise("$$REMOVE");
        ConditionalOperators.Cond locCond = ConditionalOperators.Cond.when(Criteria.where("name").is("loc")).thenValueOf("value").otherwise("$$REMOVE");

        return Aggregation.addFields().addFieldWithValue("line", lineCond).addFieldWithValue("loc", locCond).build();
    }

    private GroupOperation groupOperation() {
        Fields idGroup = Fields.from(
            Fields.field("className", "$measurableElement.className"),
            Fields.field("methodName", "$measurableElement.methodSignature"));

        return group(idGroup)
            .first("measurableElement.className").as("className")
            .first("measurableElement.filePath").as("filePath")
            .first("measurableElement.methodSignature").as("methodSignature")
            .push("line").as("line")
            .push("loc").as("loc");
    }
}
