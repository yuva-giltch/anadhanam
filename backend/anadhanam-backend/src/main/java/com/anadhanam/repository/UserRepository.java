package com.anadhanam.repository;

import com.anadhanam.model.User;

import org.springframework.stereotype.Repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

@Repository
public class UserRepository {

    private final DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table(
                "AnadhanamUsers",
                TableSchema.fromBean(User.class)
        );
    }

    public void saveUser(User user) {

        PutItemEnhancedRequest<User> request =
                PutItemEnhancedRequest.builder(User.class)
                .item(user)
                .conditionExpression(
                        Expression.builder()
                        .expression("attribute_not_exists(email)")
                        .build()
                )
                .build();

        try {
            userTable.putItem(request);
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException(
                    "Email already registered"
            );
        }
    }

    public User findByEmail(String email) {

        Key key = Key.builder()
                .partitionValue(email)
                .build();

        return userTable.getItem(key);
    }
}
