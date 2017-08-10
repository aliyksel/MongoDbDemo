# MongoDbDemo
Spring Boot and MongoDB example

This example includes MongoDb samples.
Data structure is look like this,
{
        "_id" : ObjectId("5983954bbb13061a88ca2925"),
        "_class" : "com.example.demo.models.Customer",
        "firstName" : "Alice",
        "lastName" : "Smith",
        "totalSpend" : 470,
        "bills" : [
                {
                        "billId" : NumberLong(2),
                        "totalSpend" : 470,
                        "items" : [
                                {
                                        "barcode" : NumberLong(113),
                                        "name" : "bread 2",
                                        "price" : 120
                                },
                                {
                                        "barcode" : NumberLong(112),
                                        "name" : "pice 2",
                                        "price" : 350
                                }
                        ]
                }
        ]
}
{
        "_id" : ObjectId("598cb22b8f81d71e2487900d"),
        "_class" : "com.example.demo.models.Customer",
        "firstName" : "Bob",
        "lastName" : "Smith",
        "totalSpend" : 0
}

Data model includes two level array. The example shows that how two level array updates.  

Test Cases;
create customers.
add bill to customer.
remove bill from customer.
remove item from bills.
update item from bills.
