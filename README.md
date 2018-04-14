# chanter
Chanter is a requirements management system.  

If you have had to live with Rational DOORS and it's proprietary database and how difficult it is to connect to your open source project, then this project is for you.

Goals:
* allow users to create, modify and delete modules
* use a NoSQL database to manage modules (each module in a separate collection)
* deliver containers for the back-end and front-end
* provide Kubernetes yaml definitions
* ability to import into a new module from MS Excel, MS Word, OpenOffice Calc, CSV, JSON, XML and others
* ability to manage requirements and all the edits, auditing of changes
* ability to baseline the requirements 
* ability to compare baselines for a selected module
* ability to export modules to MS Excel, MS Word, OpenOffice Calc, CSV, JSON, XML and others
* perform database backup and restore
* provide connectors to integrate into JIRA, Redmine and other tools
* generate VCRMs given that requirements are linked to tests
* connect to external test suites (Cucumber, Karate, etc)
