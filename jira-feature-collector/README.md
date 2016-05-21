#Jira Feature Collector
Retrieves feature content data from the source system APIs and places it in a MongoDB for later retrieval and use by the DevOps Dashboard

This project uses Spring Boot to package the collector as an executable JAR with dependencies.

Building and Deploying
--------------------------------------

Run
```
mvn install
```
to package the collector into an executable JAR file. Copy this file to your server and launch it using :
```
java -JAR jira-feature-collector.jar
```
You will need to provide an **application.properties** file that contains information about how
to connect to the Dashboard MongoDB database instance, as well as properties the Jira feature collector requires. See
the Spring Boot [documentation](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-application-property-files)
for information about sourcing this properties file.

###Sample application.properties file
--------------------------------------

    #Database Name - default is test
    spring.data.mongodb.database=dashboard

    #Database HostName - default is localhost
    spring.data.mongodb.host=10.0.1.1

    #Database Port - default is 27017
    spring.data.mongodb.port=9999

    #Database Username - default is blank
    spring.data.mongodb.username=db

    #Database Password - default is blank
    spring.data.mongodb.password=dbpass

    #Collector schedule (required)
    feature.cron=0 * * * * *

    #Page size for data calls (Jira maxes at 1000)
    feature.pageSize=1000

    #In-built folder housing prepared REST queries (required)
    feature.queryFolder=jiraapi-queries

    #Jira API Query file names (String template requires the files to have .st extension) (required)
    feature.storyQuery=story
    feature.epicQuery=epic
    feature.projectQuery=projectinfo
    feature.memberQuery=memberinfo
    feature.sprintQuery=sprintinfo
    feature.teamQuery=teaminfo
    feature.trendingQuery=trendinginfo

    #Jira Connection Details
    feature.jiraProxyUrl=http://proxy.com
    feature.jiraProxyPort=9999

    # Trending Query:  Number of days in a sprint (not-required)
    feature.sprintDays=60
    # Trending Query:  Length of sprint week (not-required)
    feature.sprintEndPrior=7

    #Scheduled Job prior minutes to recover data created during execution time (usually, 2 minutes is enough)
    feature.scheduledPriorMin=2

    #Delta change date that modulates the collector item task - should be about as far back as possible, in ISO format (required)
    feature.deltaCollectorItemStartDate=2008-01-01T00:00:00.000000

    #Jira Connection Details
    feature.jiraBaseUrl=http://jira-instance.com/
    #64-bit encoded credentials with the pattern username:password
    feature.jiraCredentials=YWxsIHlvdXIgYmFzZSBhcmUgYmVsb25nIHRvIHVzOiB5b3UgYXJlIG9uIHRoZSB3YXkgdG8gZGVzdHJ1Y3Rpb246IG1ha2UgeW91ciB0aW1l
    #OAuth2.0 token credentials (currently not supported in this version)
    feature.jiraOauthAuthtoken=sdfghjkl==
    feature.jiraOauthRefreshtoken=sdfagheh==
    feature.jiraOauthRedirecturi=uri.this.is.test:uri
    feature.jiraOauthExpiretime=234567890987
    # In Jira, general IssueType IDs are associated to various "issue"
	# attributes. However, there is one attribute which this collector's
	# queries rely on that change between different instantiations of Jira.
	# Please provide the name value reference to your instance's IssueType for
	# the lowest level of Issues (e.g., "user story") specific to your Jira
	# instance.  Note:  You can retrieve your instance's IssueType Name
	# listings via the following URI:
	# https://[your-jira-domain-name]/rest/api/2/issuetype/.  It is listed as
	# attribute "name" in the JSON response.
    feature.jiraIssueTypeId=Story
    # In Jira, your instance will have its own custom field created for "sprint" or "timebox" details,
	# which includes a list of information.  This field allows you to specify that data field for your
	# instance of Jira. Note: You can retrieve your instance's sprint data field name
	# via the following URI, and look for a package name <em>com.atlassian.greenhopper.service.sprint.Sprint</em>;
	# your custom field name describes the values in this field:
	# https://[your-jira-domain-name]/rest/api/2/issue/[some-issue-name]
	feature.jiraSprintDataFieldName=customfield_10007
	# In Jira, your instance will have its own custom field created for "super story" or "epic" back-end ID,
	# which includes a list of information.  This field allows you to specify that data field for your instance
	# of Jira.  Note:  You can retrieve your instance's epic ID field name via the following URI where your
	# queried user story issue has a super issue (e.g., epic) tied to it; your custom field name describes the
	# epic value you expect to see, and is the only field that does this for a given issue:
	# https://[your-jira-domain-name]/rest/api/2/issue/[some-issue-name]
	feature.jiraEpicIdFieldName=customfield_10400

    #Start dates from which to begin collector data, if no other data is present - usually, a month back is appropriate (required)
    feature.deltaStartDate=2015-03-01T00:00:00.000000
    feature.masterStartDate=2008-01-01T00:00:00.000000
    
    #Core Artifacts
	# These arrays of status mappings must be completed in order for custom source system statuses to be providable by
	# Hygieia.  Currently, Hygieia only maps to the following 3 status mappings:  to do, doing, and done.  You can find
	# all available issue status mappings by going to your instance's version of the following API call:
	# http://fake.jira.com/rest/api/2/status/.  Your statuses are listed in the JSON response as "name."
	feature.todoStatuses[0]=
	feature.doingStatuses[0]=
	feature.doneStatuses[0]=
