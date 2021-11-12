import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
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
//            addTestRestaurant(restaurants);
//            add2Restaurants(restaurants);
//            updateRestaurant(restaurants);
//            findRestaurants(restaurants);
//            query1(restaurants);
//            query2(restaurants);
            query3(restaurants);
//            query4(restaurants);
//            query5(restaurants);
        }
    }

    private static void addTestRestaurant(MongoCollection<Document> restaurants) {
        try {
            restaurants.insertOne(
                    new Document("_id", new ObjectId())
                            .append("address",
                                    new Document("building", "1234")
                                            .append("coord", Arrays.asList(0, 0))
                                            .append("rua", "Test Street")
                                            .append("zipcode", "12345")
                            )
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
                                    .append("address",
                                            new Document("building", "1234")
                                                    .append("coord", Arrays.asList(0, 0))
                                                    .append("rua", "Test Street")
                                                    .append("zipcode", "12345")
                                    )
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
                                    .append("address",
                                            new Document("building", "1234")
                                                    .append("coord", Arrays.asList(0, 0))
                                                    .append("rua", "Test Street")
                                                    .append("zipcode", "12345")
                                    )
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
            UpdateResult result = restaurants.updateMany(query, updates, options);
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
        restaurants.createIndex(Indexes.ascending("localidade"));
        restaurants.createIndex(Indexes.ascending("gastronomia"));
        restaurants.createIndex(Indexes.text("nome"));
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

    // c)

    // 1. Liste todos os documentos da coleção.
    private static void query1(MongoCollection<Document> restaurants) {
        FindIterable<Document> docsIterable = restaurants.find();
        System.out.println("Query 1");
        for (Document doc : docsIterable)
            System.out.println(doc.toJson());
    }

    // 2. Apresente os campos restaurant_id, nome, localidade e gastronomia para todos os
    //    documentos da coleção.
    private static void query2(MongoCollection<Document> restaurants) {
        FindIterable<Document> docsIterable = restaurants.find().projection(
                Projections.fields(
                        Projections.include("restaurant_id", "nome", "localidade", "gastronomia")
                )
        );
        System.out.println("Query 2");
        for (Document doc : docsIterable)
            System.out.println(doc.toJson());
    }

    // 3. Apresente os campos restaurant_id, nome, localidade e código postal (zipcode), mas
    // exclua o campo _id de todos os documentos da coleção.
    private static void query3(MongoCollection<Document> restaurants) {
        FindIterable<Document> docsIterable = restaurants.find().projection(
                Projections.fields(
                        Projections.include("restaurant_id", "nome", "localidade", "gastronomia", "address.zipcode"),
                        Projections.exclude("_id")
                )
        );
        System.out.println("Query 3");
        for (Document doc : docsIterable)
            System.out.println(doc.toJson());
    }

    // 4. Indique o total de restaurantes localizados no Bronx
    private static void query4(MongoCollection<Document> restaurants) {
        System.out.println("Query 4\nNumber of restaurants located in the Bronx: " +
                restaurants.countDocuments((Filters.eq("localidade", "Bronx"))));
    }

    // 5. Apresente os primeiros 15 restaurantes localizados no Bronx, ordenados por
    //    ordem crescente de nome.
    private static void query5(MongoCollection<Document> restaurants) {
        FindIterable<Document> docsIterable = restaurants.find(
                Filters.eq("localidade", "Bronx")
        ).limit(15).sort(Sorts.ascending("nome"));
    }
}
