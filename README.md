Pivotal CF Workshop - Spring Boot
================================

Introduction
------------

This is the Spring Boot sample application for the Pivotal CF Workshop.
It is intended to demonstrate some of the basic functionality of Pivotal
CF:

 * Pivotal CF target, login, and push
 * Pivotal CF environment variables
 * Pivotal CF service variables
 * Scaling, router and load balancing
 * Health manager and application restart
 * RDBMS services and application auto-configuration

Building, Packaging, and Deploying
--------------------------------

###To get the source code and build the WAR file


    git clone https://github.com/pivotal-cf-workshop/cf-workshop-spring-boot

    mvn clean package

###To run the application

The application is set to use an embedded H2 database in non-PaaS environments,
and to take advantage of Pivotal CF's auto-configuration for services.  To use
a MySQL Dev service in PCF, simply create and bind a service to the app and 
restart the app.  No additional configuration is necessary when running locally 
or in Pivotal CF.

In Pivotal CF, it is assumed that a Pivotal MySQL service will be used.

###For Docker

There is a Dockerfile in the top level directory.  You can build your own Docker Image from this and push it to Docker Hub, or use mine(https://hub.docker.com/u/dbeauregard/) and just push it to Cloud Foundry: cf push app-name -o dbeauregard/boot-docker

###For Concourse

There is a Concourse pipeline in the concourse directory.  It will watch GitHub for pushes, build the project (Maven) and run the unit tests, version the jar, save the jar in S3, bump the version # and push the app to a PCF staging env.  There is then a manual step/click to do a blue/green deployment (cf push) to 'production'.
 1. In the concourse directory copy credentials.yml.sample to credentials.yml (same directory) and fill in your credentials for PCF and Amazon S3.
 2. From the concourse directory run the following command (after you have targeted your concourse deployment with fly): fly -t lite set-pipeline -p boot-app -c pipeline.yml -l credentials.yml
