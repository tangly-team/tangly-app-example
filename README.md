== How To Build and Run

The whole application can be build and executed with:

[source,bash]
----
./gradlew run
----

The application is available under \http://localhost:8080.

=== Entities, Identifiers, Tags, Comments

The demonstration application shows the user interface for

Simple Entities:: with internal object identifier, external identifier, name, validity time range and text.
Complex Entities:: simple entities with tags and comments.
Comments View:: An entity can have a set of human-readable comments.
Tags View:: An entity can have a set of tags with optional or mandatory values.
Codes:: Enumeration values similar to reference tables are supported.
One-to-One Relation:: relation between two entities.
One-to-Many Relation:: collection between an owning entity and a set of owned entities
CRUD Operations:: are provided for simple and complex entitites.
The available operations are either for read-only entities or modifiable entities.
