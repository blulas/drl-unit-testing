# IBM Digital Business Automation - Decision Manager Open Edition - DRL Testing Quick Start

This repository contains various tools, scripts, and product extensions useful for managing `IBM Decision Manager Open Edition` projects.

## Overview

The purpose of this repository is to guide the developer through the setup and use of Cucumber for embedded DRL rule unit testing, without having to deploy the soluiton to a runtime environment.  The `first step` is to make sure you local development environment or workstation is configured with the proper developer tooling, frameworks, and runtimes.  Once your development environment is configured, you can proceed to the remaining steps to create the project, add assets and unit tests, and deploy your solution.

## Tool & Environment Requirements

The following tools and frameworks are required to be installed on the developer workstation.  Please follow the link in order to download and install the various tools:

1. [**Maven**](https://maven.apache.org) - Required in order to perform Maven builds on the desktop.  
2. [**Git Client**](https://git-scm.com) - Required in order to clone and make changes to the project GIT repositories
3. [**JDK 11**](https://www.oracle.com/java/technologies/downloads/) - Requires Java 11 at this time
5. [**VS Code IDE**](https://code.visualstudio.com/download) - An IDE is required, VS Code IDE is the default.  While you can use any IDE you wish, the embeddable editors are only available for VS Code.

*_Once you have installed VS Code, you can go to the Extensions Marketplace and install the `Kogito Bundle` for BPMN, DMN, and Test Scenarios_*.

## How To Build

Once you have configured your local development environment, you need to perform a `Maven Build` of the repository.  This repository is built using `mvn clean install` by either the CI/CD pipeline or on a local developer workstation.  If deploying artifacts to an enterprise Maven repository, please use `mvn clean deploy`, which requires configuration of the `distributionManagement` section of your project's parent pom.xml.

*_Once you have performed a build of the repository, you will have the following modules available to you in order to start creating and building decision services..._*.

## Repository Modules

This repository is organized in a series of modules:  

1.  [**Embedded Rule Engine Adaptor**](./embedded-engine-adaptor/readme.md) - An embedded engine adaptor for DRL rules.  
2.  [**Cucumber Adaptor**](./cucumber-adaptor/readme.md) - An adaptor for the Cucumbrer framework, which uses the embedded rule engine adaptor to execute DRL rules from Cucumber features.
3.  [**Sample KJAR**](./sample-kjar/readme.md) - A sample KJAR with DRL rules and Cucumber features, used for demonstration purposes.

## Additional Information (*Appendicies*)
This repository is focused on business automation using the **IBM Decison Manager Open Edition** product, which in turn relies on various open source tools and technology. The following online documentation is available in order to learn various aspects of these tools:

- [**Apache Maven**](https://maven.apache.org/) is a free and open source software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project’s build, reporting and documentation from a central piece of  information. A **getting started guide** is available [here](http://maven.apache.org/guides/getting-started/).
- [**Git**](https://git-scm.com//) is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency. There is a **handbook** available [here](https://guides.github.com/introduction/git-handbook/), as well as various **guides** for learning and working with Git available [here](https://guides.github.com/
business logic lives these days. By taking advantage of the latest technologies (Quarkus, knative, etc.), you get amazingly fast boot times and instant scaling on orchestration platforms like Kubernetes.
- [**IBM Business Automation Manager Open Edition**](https://www.ibm.com/docs/en/ibamoe) `IBM Business Automation Manager Open Editions`, which consists of `IBM Process Automation Manager Open Edition` and `IBM Decision Manager Open Edition`, offer developers the ability to build cloud-native applications that automate business operations. `IBM Process Automation Manager Open Edition` is a platform for developing containerized microservices and applications that automate processes and business decisions. `IBM Decision Manager Open Edition` is a separately available platform for developing containerized microservices and applications that automate business decisions, business rules, and complex event processing.
