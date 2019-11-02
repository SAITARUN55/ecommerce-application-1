# eCommerce Application Project

This project is intended to work on the security and DevOps part of an eCommerce application that provides a REST API. As any typical eCommerce application, a user has a cart, and a cart has a number of items that will encompass an order. The order is submitted, and as a result becomes associated to the user. The application has been built using Java, Spring Boot, Maven, Hibernate ORM, and H2 as database.

## Table of Contents

* [Description of the Project](#description-of-the-project)
* [Testing coverage](#testing-coverage)
* [Getting Started](#getting-started)
* [References](#references)
* [Contributing](#contributing)

## Description of the Project

As has already been mentioned, this project revolves around an eCommerce application that provides a REST API. [JWT](https://auth0.com/docs/jwt) authentication is implemented in the form of username and password, and authorization is properly handled. Tests are written to a high code coverage rate. The system is monitored, once the metrics for logging are identified, and the metrics are indexed to [Splunk](https://www.splunk.com/). Finally, the CI/CD pipeline is configured and automated. The work that has been done is best described by explaining its main steps:

* Git is used for repository management.
* Authentication is applied to users in order to decide whether they can access the application. In addition, JWT headers are used. Furthermore, passwords have length requirements, and there is a confirmation field to make sure passwords do not have typos.
* A number of tests have been written against an in-memory database, and to a very high code coverage level.
* The code traces a number of events, such as: create user request successes, create user request failures, exceptions, order requests successes, and order requests failures, amongst others.
* Logs are indexed to Splunk, in such a way that Splunk monitors them continuously and in real time.
* In Splunk, search queries can be run, and a dashbord and an alert are set up.
* Successful Jenkins built and project setup are accomplished.

## Testing coverage

In this section, the tests covered are listed:

* SareetaApplicationTests:
	* contextLoads: tests context loading.
* UserControllerTest:
	* create_user_happy_path: tests that a user is successfully created when no exceptions are found.
	* create_user_password_invalid: tests that a user creation is unsuccessful when the password provided is invalid.
	* find_user_by_id_happy_path: tests that a user is successfully found by id when no exceptions are found.
	* find_user_by_username_happy_path: tests that a user is successfully found by username when no exceptions are found.
* CartControllerTest:
	* add_to_cart_happy_path: tests that a cart request is successfully added to a cart when no exceptions are found.
	* add_to_cart_user_not_found: tests that a cart request addition is unsuccessful when the user is not found.
	* add_to_cart_item_not_found: tests that a cart request addition is unsuccessful when the item is not found.
	* remove_from_cart_happy_path: tests that a cart request is successfully removed from a cart when no exceptions are found.
	* remove_from_cart_user_not_found: tests that a cart request removal is unsuccessful when the user is not found.
	* remove_from_cart_item_not_found: tests that a cart request removal is unsuccessful when the item is not found.
* OrderControllerTest:
	* submit_order_username_happy_path: tests that an order is successfully submitted when no exceptions are found.
	* submit_order_username_not_found: tests that an order submission is unsuccessful when the user is not found.
	* get_orders_for_user_happy_path: tests that orders are successfully retrieved for a user when no exceptions are found.
	* get_orders_for_user_username_not_found: tests that the retrieval of orders for a user is unsuccessful when the user is not found.
* ItemControllerTest:
	* get_items_happy_path: tests that items are successfully retrieved when no exceptions are found.
	* get_item_by_id_happy_path: tests that an item is successfully retrieved by id when no exceptions are found.
	* get_items_by_name_happy_path: tests that items are successfully retrieved by name when no exceptions are found.
	* get_items_by_name_empty: tests that items retrieval by name is unsuccessful when no items are found.
* TestSuite: a test suite is defined for the CartControllerTest, ItemControllerTest, OrderControllerTest, and UserControllerTest test classes.

## Getting Started

The procedure to obtain a functional a copy of the project on your local machine so that you can further develop and/or test it is explained in this section. These are the steps to be followed:

* Firstly, you have to download/clone the project files from this repository onto your local machine. Then, cd into the root folder where the project files are located.
* For your information, this is the result of the execution of the packaging step for the *auth-course* application:
![ecommercepackage](/ScreenShots/ecommercepackage.png)
* This step creates this *jar* file: *auth-course-0.0.1-SNAPSHOT.jar*.
* Now, secondly, you can execute the packaged application. Just run the *jar* file on a terminal shell window by typing `java -jar target/auth-course-0.0.1-SNAPSHOT.jar`:
	* The eCommerce API server is started on port 8080:
	![jar](/ScreenShots/jar.png)
* Thirdly, to verify that correct handling of authorization is performed with proper security using JWT, the eCommerce API can be manually tested executing a number of POSTMAN requests:
	* Using JWT, to create a user, it is not enough providing just the username:
	![postman1](/ScreenShots/postman1.png)
	* Password and password confirmation are also required:
	![postman2](/ScreenShots/postman2.png)
	* Now it is possible to log in. Note how a JWT token is obtained in the response in the authorization header:
	![postman3](/ScreenShots/postman3.png)
	* Once we have logged in successfully, and have been given a JWT token, if we do not provide the token when accessing resources, the access is not granted. Let's see that trying to add to cart:
	![postman4](/ScreenShots/postman4.png)
	* If the authorization header is added to the request, adding to cart is allowed by the server, as the user is an authorized one now:
	![postman5](/ScreenShots/postman5.png)
	* The same happens if we try to submit an order without and with the token:
	![postman6](/ScreenShots/postman6.png)
	![postman7](/ScreenShots/postman7.png)
	* In addition, it can be seen how the minimum length of the password (7 characters) is checked:
	![postman8](/ScreenShots/postman8.png)
* In the fourth place, to verify that proper tests have been written and meet an acceptable code coverage, you can run the supporting tests yourself. All you have to do is	:
	* Make sure the application is running. If you have followed along, this should be the case now.
	* Open a new terminal shell window, cd to the root folder of this project, and type, for instance, `mvn test`. Please, note how all tests pass:
	![tests1](/ScreenShots/tests1.png)
	![tests2](/ScreenShots/tests2.png)
* In the fifth place, to verify that the code successfully traces such events as user creation and order requests and exceptions all you have to do is:
	* Change directory to to the *logs* folder, and type `cat spring-boot-logger-log4j2.log`. This command shows the contents of the *spring-boot-logger-log4j2.log* file, where the code traces events:
	![logs](/ScreenShots/logs.png)
* In the sixth place, to verify the usage of Splunk. The logs have been indexed to it:
	* In the terminal shell window, type `/Applications/Splunk/bin/splunk start`. This command starts the splunk server daemon at port 8000:
	![splunk1](/ScreenShots/splunk1.png)
	* Within Splunk itself, queries can be run. For instance, the first one below shows the events the events corresponding to successful user creation, and the second one below shows the events corresponding to unsuccessful user creation:
	![splunk2](/ScreenShots/splunk2.png)
	![splunk3](/ScreenShots/splunk3.png)
	* One alert has been set up named *Unsuccessful user creation threshold exceeded*. This alert is triggered in real time when there are more than five unsuccessful attempts to create a user. Below, its definition can be seen, together with one example where it is triggered after six unsuccessful attempts to create a user with a password shorter than the minimum length allowed:
	![splunk6](/ScreenShots/splunk6.png)
	![splunk4](/ScreenShots/splunk4.png)
	![splunk5](/ScreenShots/splunk5.png)
	* A dashboard has been created for user creation success rate per minute within one week, and order submission success rate per minute within one day. Below, the resulting dashboard, and the definition of its charts is shown.
	![splunk7](/ScreenShots/splunk7.png)
	![splunk8](/ScreenShots/splunk8.png)
	![splunk9](/ScreenShots/splunk9.png)
* Finally, let's show successful Jenkins build and project setup. With this aim in view, the entire CI/CD pipeline is created.
	* Firstly, we log into our AWS account, where we can observe how our already created instance is running. It is noteworthy that this instance includes Docker. In addition, a custom TCP rule for port 8080 has been added in the security group configuration, because Jenkins needs that. A new key pair called *esteve* has also been created, and, as a result, a *esteve.pem* file generated. Permissions are assigned to this file typing `chmod 400 esteve.pem` on the terminal shell window.
	![cicd1](/ScreenShots/cicd1.png)
	* Secondly, in order to connect to the instance, we type `ssh -i "esteve.pem" ec2-user@ec2-3-9-139-217.eu-west-2.compute.amazonaws.com`:
	![cicd2](/ScreenShots/cicd2.png)
	* Thirdly, assuming Docker has already been installed into the instance by typing `sudo yum install docker`, and a user group added to it by typing `sudo usermod -a -G docker $USER`, we start Docker by typing `sudo service docker start`. After that, we run the Docker image by typing `docker run --rm -u root -d --name jenkins -p 8080:8080 -v jenkins-data:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v "$HOME":/home jenkinsci/blueocean` which provides us the container ID for the Docker image, *2ce53aad3eb1ff58d8f8776f206e517feeac473ef4fe228b67a5277aab5db297*:
	![cicd3](/ScreenShots/cicd3.png)
	* In the fourth place, a shell can be opened into Jenkins by typing `docker exec -it jenkins bash`, where we assume *maven* has already been installed by typing `apk add maven`. It is also assumed that SSH keys have been generated, entering no passphrase, by typing `ssh-keygen -t rsa`. This would have created the public and private keys in the *.ssh* folder.
	* In the fifth place, we go to the AWS instance, to 8080 port where we have jenkins running. We assume *jenkins* has already been configured, and global credentials set up (SSH username with private key):
	![jenkins1](/ScreenShots/jenkins1.png)
	![jenkins2](/ScreenShots/jenkins2.png)
	* It the sixth place, it is assumed the public key has been added into the Git repository. The eCommerce application that we have built is the one that we want to deploy to cloud. It can be seen that we have one key successfully deployed. As a result, now the entire pipeline is set up:
	![git1](/ScreenShots/git1.png)
	* In the seventh place, the project is configured in Jenkins. A new job is created for Jenkins. We name the freestyle project *ecommerce-application*, and associate the Git repository URL to it, and the credentials. We choose the *master* branch, so that the Jenkins pipeline is set up for this particular branch. Next, the *pom* location is added at the *build* step, and *package* is added as *goal*. Now, we trigger a build, which successfully goes to the entire process of building.
	![jenkins3](/ScreenShots/jenkins3.png)
	![jenkins4](/ScreenShots/jenkins4.png)
	* From now on, every time I make a commit, it will trigger an automatic build that is going to trigger this Jenkins pipeline.

# References

Please, consider these resources for further information:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [JWT](https://auth0.com/docs/jwt)
* [Splunk](https://www.splunk.com/)

## Contributing

This repository contains all the work that makes up the project. Individuals and I myself are encouraged to further improve this project. As a result, I will be more than happy to consider any pull requests.