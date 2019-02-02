# Chanter
Chanter is a requirements management system.

If you have had to live with Rational DOORS and it's proprietary database and how difficult it is to connect to your open source project, then this project is for you.

### Goals:
* allow users to create, modify and delete modules
* use a NoSQL database to manage modules (each module in a separate collection)
* deliver containers for the back-end and front-end
* provide Kubernetes yaml definitions
* ability to import into a new module from MS Excel, MS Word, OpenOffice Calc, CSV, JSON, XML and others
* ability to export modules to MS Excel, MS Word, OpenOffice Calc, CSV, JSON, XML and others
* ability to manage requirements and all the edits, auditing of changes
* ability to baseline the requirements 
* ability to compare baselines for a selected module
* perform database backup and restore
* provide connectors to integrate into JIRA, Redmine and other tools
* generate VCRMs given that requirements are linked to tests
* connect to external test suites (Cucumber, Karate, etc)

### Build Instructions
Currently there are three projects.
To run the web interface, cd to "web" folder and type "mvn meecrowave:run".
To run the back-end, cd to "back-end" folder and type "mvn meecrowave:run".

To produce a self-contained tar ball, type "mvn meecrowave:bundle".

The system is still under construction, so it has not been stress tested.
The enforcement of users and roles will come in later versions, if ever.

### Why the Name?
Wikipedia says "The chanter is the part of the bagpipe upon which the player creates the melody."

The analogy here is "the requirements are the part of a system which define the final solution."

I've worked multiple projects where the delivery of the system always ended up going back to 
the requirements and their interpretation.

While IBM Rational DOORS is a great tool, it is also very proprietary and all the plugins
are extremely expensive and difficult to integrate, at least according to the IBM consultants...
 