- `src/main/java/bg/conquerors/wardrobe`: The main package of the application contains all Java files.
	- [`config`](config.md): Contains Java classes for configuring various aspects of the application, including security.
	- [`controller`](controller.md): Defines Spring MVC controllers that handle incoming HTTP requests and responses.
	- [`exceptions`](exeptions.md): Specialized classes for handling exceptions that may be thrown by the application.
	- [`model`](models.md): Describes the domain models representing the data structures with which the application operates.
	- [`repository`](repository.md): Packages that contain interfaces for Spring Data JPA repositories to interact with the database.
	- [`service`](service.md): A layer for business logic where the core functionality of the application is processed.
	- `WardrobeApplication`: The startup class for the Spring Boot application, which is executed every time the website is ran.
- `src/main/resources`: Resources such as properties files, Bootstrap, CSS, JavaScript, HTML files, as well as images and logos.
