import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase cbd = mongoClient.getDatabase("cbd");
            testConnection(cbd);
            MongoCollection<Document> restaurants = cbd.getCollection("restaurants");
            addTestRestaurant(restaurants);
            add2Restaurants(restaurants);
            updateRestaurant(restaurants);
            findRestaurants(restaurants);
        }
    }

    private static void addTestRestaurant(MongoCollection<Document> restaurants) {
        try {
            restaurants.insertOne(
                    new Document("_id", new ObjectId())
                            .append("address", Arrays.asList(
                                    new Document("building", "1234"),
                                    new Document("coord", Arrays.asList(0, 0)),
                                    new Document("rua", "Test Street"),
                                    new Document("zipcode", "12345")
                            ))
                            .append("localidade", "Test Location")
                            .append("gastronomia", "Test Gastronomy")
                            .append("grades", Arrays.asList(
                                    new Document("date", new Date())
                                            .append("grade", "A")
                                            .append("score", 1),
                                    new Document("date", new Date())
                                            .append("grade", "A")
                                            .append("score", 1),
                                    new Document("date", new Date())
                                            .append("grade", "A")
                                            .append("score", 1),
                                    new Document("date", new Date())
                                            .append("grade", "A")
                                            .append("score", 1),
                                    new Document("date", new Date())
                                            .append("grade", "A")
                                            .append("score", 1)
                            ))
                            .append("nome", "Test Name")
                            .append("restaurant_id", "0")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void add2Restaurants(MongoCollection<Document> restaurants) {
        try {
            restaurants.insertMany(
                    Arrays.asList(
                            new Document("_id", new ObjectId())
                                    .append("address", Arrays.asList(
                                            new Document("building", "1234"),
                                            new Document("coord", Arrays.asList(0, 0)),
                                            new Document("rua", "Test Street"),
                                            new Document("zipcode", "12345")
                                    ))
                                    .append("localidade", "Test Location")
                                    .append("gastronomia", "Test Gastronomy")
                                    .append("grades", Arrays.asList(
                                            new Document("date", new Date())
                                                    .append("grade", "A")
                                                    .append("score", 1),
                                            new Document("date", new Date())
                                                    .append("grade", "A")
                                                    .append("score", 1),
                                            new Document("date", new Date())
                                                    .append("grade", "A")
                                                    .append("score", 1),
                                            new Document("date", new Date())
                                                    .append("grade", "A")
                                                    .append("score", 1),
                                            new Document("date", new Date())
                                                    .append("grade", "A")
                                                    .append("score", 1)
                                    ))
                                    .append("nome", "Insert Many 1")
                                    .append("restaurant_id", "1"),
                            new Document("_id", new ObjectId())
                                    .append("address", Arrays.asList(
                                            new Document("building", "1234"),
                                            new Document("coord", Arrays.asList(0, 0)),
                                            new Document("rua", "Test Street"),
                                            new Document("zipcode", "12345")
                                    ))
                                    .append("localidade", "Test Location")
                                    .append("gastronomia", "Test Gastronomy")
                                    .append("grades", Arrays.asList(
                                            new Document("date", new Date())
                                                    .append("grade", "A")
                                                    .append("score", 1),
                                            new Document("date", new Date())
                                                    .append("grade", "A")
                                                    .append("score", 1),
                                            new Document("date", new Date())
                                                    .append("grade", "A")
                                                    .append("score", 1),
                                            new Document("date", new Date())
                                                    .append("grade", "A")
                                                    .append("score", 1),
                                            new Document("date", new Date())
                                                    .append("grade", "A")
                                                    .append("score", 1)
                                    ))
                                    .append("nome", "Insert Many 2")
                                    .append("restaurant_id", "2")
                    )
            );
        } catch (MongoException me) {
            me.printStackTrace();
        }
    }

    private static void updateRestaurant(MongoCollection<Document> restaurants) {
        Document query = new Document().append("nome", "Test Name");
        Bson updates = Updates.combine(Updates.set("nome", "Updated Test Name"));
        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            UpdateResult result = restaurants.updateOne(query, updates, options);
            System.out.println("Modified document count: " + result.getModifiedCount());
            // only contains a value when an upsert is performed
            System.out.println("Upserted id: " + result.getUpsertedId());
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
        }
    }

    private static void findRestaurants(MongoCollection<Document> restaurants) {
        FindIterable<Document> docsIterable = restaurants.find(Filters.eq("localidade", "Test Location"));
        for (Document d : docsIterable)
            System.out.println(d.toJson());
    }

    private static void createIndexes(MongoCollection<Document> restaurants) {
        restaurants.createIndex(Indexes.text("localidade"));
    }

    private static boolean testConnection(MongoDatabase database) {
        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = database.runCommand(command);
            System.out.println("Connected successfully to server.");
            return true;
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            return false;
        }
    }
}
