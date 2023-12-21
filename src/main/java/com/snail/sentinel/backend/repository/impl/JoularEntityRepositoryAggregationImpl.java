package com.snail.sentinel.backend.repository.impl;

import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.repository.JoularEntityRepositoryAggregation;
import com.snail.sentinel.backend.service.dto.joular.JoularAggregateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class JoularEntityRepositoryAggregationImpl implements JoularEntityRepositoryAggregation {
    private final Logger log = LoggerFactory.getLogger(JoularEntityRepositoryAggregationImpl.class);
    private final MongoTemplate mongoTemplate;

    public JoularEntityRepositoryAggregationImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<JoularAggregateDTO> aggregateAll() {
        MatchOperation matchOperation = getAllValues();
        GroupOperation groupOperation = groupAllValues();
        MatchOperation matchOperation30Values = getAllMethodsHaving30Values();
        ProjectionOperation projectionOperation = addFields();

        Aggregation aggregation = newAggregation(matchOperation, groupOperation, matchOperation30Values, projectionOperation);
        AggregationResults<JoularAggregateDTO> output = mongoTemplate.aggregate(aggregation, JoularEntity.class, JoularAggregateDTO.class);
        return output.getMappedResults();
    }

    @Override
    public List<JoularAggregateDTO> aggregateAllByCommit(String sha) {
        MatchOperation matchOperation = getAllValues();
        GroupOperation groupOperation = groupAllValues();
        MatchOperation matchOperation30Values = getAllMethodsHaving30Values();
        ProjectionOperation projectionOperation = addFields();
        MatchOperation matchCommitSha = matchCommitSha(sha);

        Aggregation aggregation = newAggregation(matchOperation, groupOperation, matchOperation30Values, projectionOperation, matchCommitSha);
        AggregationResults<JoularAggregateDTO> output = mongoTemplate.aggregate(aggregation, JoularEntity.class, JoularAggregateDTO.class);
        return output.getMappedResults();
    }

    private MatchOperation getAllValues() {
        Criteria valueCriteria = where("value").gt(0);
        return Aggregation.match(valueCriteria);
    }

    private GroupOperation groupAllValues() {
        Fields idGroup = Fields.from(
            Fields.field("repoName", "$commit.repository.name"),
            Fields.field("sha", "$commit.sha"),
            Fields.field("className", "$measurableElement.className"),
            Fields.field("methodSignature", "$measurableElement.methodSignature"),
            Fields.field("owner", "$commit.repository.owner"),
            Fields.field("astElem", "$measurableElement.astElem"),
            Fields.field("filePath", "$measurableElement.filePath")
        );
        return group(idGroup)
            .push("value").as("allValues")
            .count().as("size");
    }

    private MatchOperation getAllMethodsHaving30Values() {
        Criteria criteria = where("size").is(30);
        return Aggregation.match(criteria);
    }

    private ProjectionOperation addFields() {
        return Aggregation.project("allValues", "size")
            .andExpression("$_id.sha").as("commit.sha")
            .andExpression("$_id.repoName").as("commit.repository.name")
            .andExpression("$_id.owner").as("commit.repository.owner")
            .andExpression("$_id.astElem").as("measurableElement.astElem")
            .andExpression("$_id.filePath").as("measurableElement.filePath")
            .andExpression("$_id.className").as("measurableElement.className")
            .andExpression("$_id.methodSignature").as("measurableElement.methodSignature");
    }

    private MatchOperation matchCommitSha(String sha) {
        Criteria criteria = where("commit.sha").is(sha);
        return Aggregation.match(criteria);
    }
}
