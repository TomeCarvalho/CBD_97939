// NMEC: 97939
// 1. Liste todos os documentos da coleção.
db.restaurants.find()
// 3772

// 2. Apresente os campos restaurant_id, nome, localidade e gastronomia para todos os documentos da coleção.
db.restaurants.find({},{"restaurant_id":1,"nome":1,"localidade":1,"gastronomia":1})
// {
//     _id: ObjectId("617a6f9bc02beedac108cadd"),
//     localidade: 'Manhattan',
//     gastronomia: 'Turkish',
//     nome: 'The Country Cafe',
//     restaurant_id: '40362715'
// }

// 3. Apresente os campos restaurant_id, nome, localidade e código postal (zipcode), mas exclua o campo _id de todos os documentos da coleção.
db.restaurants.find({},{"restaurant_id":1,"nome":1,"localidade":1,"address.zipcode":1,"_id":0})
// {
//     address: { zipcode: '10005' },
//     localidade: 'Manhattan',
//     nome: 'The Country Cafe',
//     restaurant_id: '40362715'
// }

// 4. Indique o total de restaurantes localizados no Bronx
db.restaurants.find({"localidade":"Bronx"}).count()
// 309

// 5. Apresente os primeiros 15 restaurantes localizados no Bronx, ordenados por ordem crescente de nome.
db.restaurants.find({"localidade":"Bronx"}).limit(15).sort("nome")

// 6. Liste todos os restaurantes que tenham pelo menos um score superior a 85
db.restaurants.find({"grades.score":{$gt:85}})

// 7. Encontre os restaurantes que obtiveram uma ou mais pontuações (score) entre [80 e 100].
db.restaurants.find({ "grades": { $elemMatch: { "score": { $gte: 80, $lte: 100 } } } })
// 4

// 8. Indique os restaurantes com latitude inferior a -95,7.
db.restaurants.find({"address.coord.0": {$lt: -95.7}})
// 3

// 9. Indique os restaurantes que não têm gastronomia "American", tiveram uma (ou mais) pontuação superior a 70 e estão numa latitude inferior a -65.
db.restaurants.find({"gastronomia":{$ne:"American"}, "grades.score":{$gt:70}, "address.coord.0":{$lt:-65}})
// 5

// 10.Liste o restaurant_id, o nome, a localidade e gastronomia dos restaurantes cujo nome começam por "Wil"
db.restaurants.find({"nome":/^Wil.*/}, {"nome":1, "localidade":1, "gastronomia":1})
// 3
// note: /pattern/ is regex in JavaScript, we could alternatively write it as:
// db.restaurants.find({"nome":{$regex: "^Wil.*"}}, {"nome":1, "restaurand_id":1, "localidade":1, "gastronomia":1})

// 11. Liste o nome, a localidade e a gastronomia dos restaurantes que pertencem ao Bronx e cuja gastronomia é do tipo "American" ou "Chinese"
db.restaurants.find({ "localidade": "Bronx", "gastronomia": { $in: ["American", "Chinese"] } }, { "nome": 1, "localidade": 1, "gastronomia": 1 })
// 91

// 12. Liste o restaurant_id, o nome, a localidade e a gastronomia dos restaurantes localizados em "Staten Island", "Queens", ou "Brooklyn".
db.restaurants.find({"localidade": {$in: ["Staten Island", "Queens", "Brooklyn"]}}, {"restaurant_id":1, "localidade":1, "gastronomia":1})

// 13. Liste o nome, a localidade, o score e gastronomia dos restaurantes que alcançaram sempre pontuações inferiores ou igual a 3.
db.restaurants.find({ "grades.score": { $not: { $gt: 3 } } }, {"nome":1, "localidade":1, "grades.score":1, "gastronomia":1})
// 7

// 14. Liste o nome e as avaliações dos restaurantes que obtiveram uma avaliação com um grade "A", um score 10 na data "2014-08-11T00: 00: 00Z" (ISODATE).
db.restaurants.find({ "grades": {$elemMatch: {"grade":"A", "score":10, "date": ISODate("2014-08-11T00:00:00Z")} } }, {"nome":1, "grades":1})
// 6

// 15. Liste o restaurant_id, o nome e os score dos restaurantes nos quais a segunda avaliação foi grade "A" e ocorreu em ISODATE "2014-08-11T00: 00: 00Z"
db.restaurants.find({"grades.1.grade":"A", "grades.1.date": ISODate("2014-08-11T00:00:00Z")},{"restaurant_id": 1, "nome": 1, "grades.score": 1})
// 2

// 16. Liste o restaurant_id, o nome, o endereço (address) e as coordenadas geográficas (coord) dos restaurantes onde o 2º elemento da matriz de coordenadas tem um valor superior a 42 e inferior ou igual a 52.
db.restaurants.find({"address.coord.1": {$gt: 42, $lte: 52}}, {"restaurant_id":1, "nome":1, "address":1, "coord":1})
// 7

// 17. Liste nome, gastronomia e localidade de todos os restaurantes ordenando por ordem crescente da gastronomia e, em segundo, por ordem decrescente de localidade.
db.restaurants.find({}, {"nome":1, "gastronomia":1, "localidade":1}).sort({"gastronomia":1, "localidade":-1})
// {
//     _id: ObjectId("617a6f9bc02beedac108d1b6"),
//     localidade: 'Manhattan',
//     gastronomia: 'Afghan',
//     nome: 'Afghan Kebab House'
//   },
//   {
//     _id: ObjectId("617a6f9bc02beedac108d2e1"),
//     localidade: 'Manhattan',
//     gastronomia: 'Afghan',
//     nome: 'Khyber Pass'
//   },
//   {
//     _id: ObjectId("617a6f9bc02beedac108d399"),
//     localidade: 'Manhattan',
//     gastronomia: 'Afghan',
//     nome: 'Afghan Kebab House #1'
//   },
//   {
//     _id: ObjectId("617a6f9bc02beedac108d8b9"),
//     localidade: 'Manhattan',
//     gastronomia: 'Afghan',
//     nome: 'Ariana Kebab House'
//   },
//   {
//     _id: ObjectId("617a6f9bc02beedac108d735"),
//     localidade: 'Queens',
//     gastronomia: 'African',
//     nome: 'Africana Restaurant'
//   },
//   {
//     _id: ObjectId("617a6f9bc02beedac108d4ba"),
//     localidade: 'Brooklyn',
//     gastronomia: 'African',
//     nome: 'Madiba'
//   }

// 18. Liste nome, localidade, grade e gastronomia de todos os restaurantes localizados em Brooklyn que não incluem gastronomia "American" e obtiveram uma classificação (grade) "A". Deve apresentá-los por ordem decrescente de gastronomia.
db.restaurants.find({"localidade":"Brooklyn", "gastronomia":{$ne:"American"}, "grades.grade":"A"}, {"nome":1, "localidade":1, "grade":1, "gastronomia":1}).sort("gastronomia":-1)
// 493

// 19. Conte o total de restaurante existentes em cada localidade.
db.restaurants.aggregate([{$group : { "_id" : "$localidade", "num_restaurants" : {$sum : 1}}}])
// [
//   { _id: 'Queens', num_restaurants: 738 },
//   { _id: 'Staten Island', num_restaurants: 158 },
//   { _id: 'Bronx', num_restaurants: 309 },
//   { _id: 'Manhattan', num_restaurants: 1883 },
//   { _id: 'Brooklyn', num_restaurants: 684 }
// ]

// 20. Liste todos os restaurantes cuja média dos score é superior a 30
db.restaurants.aggregate([{$addFields : { "avg_score" :  {$avg : "$grades.score"}}}, {$match : {"avg_score": {$gt: 30}}}])

// 21. Indique os restaurantes que têm gastronomia "Portuguese", o somatório de score é superior a 50 e estão numa latitude inferior a -60.
db.restaurants.aggregate([{$addFields : { "sum_score" : {$sum : "$grades.score"}}}, {$match : {"gastronomia": "Portuguese", "sum_score": {$gt: 50}, "address.coord.0": {$lt: -60}}}])

// 22. Apresente o nome e o score dos 3 restaurantes com score médio mais elevado
db.restaurants.aggregate([{$addFields : { "avg_score" :  {$avg : "$grades.score"}}}, {$sort: {"avg_score":-1}}, {$limit: 3}])

// 23. Apresente o número de gastronomias diferentes na rua "Fifth Avenue"
db.restaurants.aggregate([{$match:{"address.rua":"Fifth Avenue"}}, {$group:{"_id":"$gastronomia"}}, {$count:"num_gastronomias"}])

// 24. Conte quantos restaurantes existem por rua e ordene por ordem decrescente
db.restaurants.aggregate([{$group:{"_id":"$address.rua", "num_restaurants":{$sum:1}}}, {$sort:{"num_restaurants":-1}}])

// 25

// 26. Para cada gastronomia, mostrar o máximo dos scores médios dos restaurantes que a têm, ordenadas por ordem decrescente do mesmo
db.restaurants.aggregate([{$addFields:{"avg_score":{$avg:"$grades.score"}}}, {$group:{"_id":"$gastronomia", "max_score":{$max:"$avg_score"}}}, {$sort:{"max_score":-1}}])

// 27. 