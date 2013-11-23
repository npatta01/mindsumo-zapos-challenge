## Zapos Challenge ##
========================

My submissions for mind sumo challenges.
The goal was to create a Java application, where the user can enter a Zappos product id, and product name to be registered for when the product is on sale.

### General ###
- The UI is created using JavaFx 2 (JDK 7)
- The backend notification [service](http://fakezapposnotificationservice.npatta01.cloudbees.net/)  is written using the Play framework and is deployed on cloudbees
- Applications is composed of several maven modules
- Its best to download the release binary from github

### Running the program
Please follow the build instructions if you would like to build the project, else download the release binary from github.
The main app is the jar file 'UI-1.0-SNAPSHOT-jfx.jar'



### Screens in the UI ###
**Login Screen:**
Enter email. If you are an existing user, your subscription list will be shown, else you will automatically register as a new user
![](/images/main_screen.png)

**Search Results Screen:**
If product query, has multiple style the below window will be shown asking to select specific styles.
![](//images/login_screen.png)
**Main Screen**
Click on subscribe, to update the subscription list
![](//images/results.png)


###Notification Service###
A backend service that I wrote to wrap around the ZapposApi. Users can ask the api for product information given a styleid, productid, product name. User’s can register/unregister to be notified for certain items .
The server is deployed [here](http://fakezapposnotificationservice.npatta01.cloudbees.net/) 

Every 12 hours, a job is fired to check the price of registered items.
Emails are send using SendGrid: from “notification@npatta01-zappos.com “


Below are possible options to get info from the service

- Get info on product id 115328
[/api/Product?product_id=115328](http://fakezapposnotificationservice.npatta01.cloudbees.net/api/Product?product_id=115328)

- Get info on product with name “classic tall black”
[/api/Product?product_name=classic%20tall%20black](http://fakezapposnotificationservice.npatta01.cloudbees.net/api/Product?product_name=classic%20tall%20black)

- Get info on product with style id “1788226”
[/api/Product?style_id=1788226](http://fakezapposnotificationservice.npatta01.cloudbees.net/api/Product?style_id=1788226)

- Unregister user “npatta01@gmail.com”
[/api/User/unregister?email=npatta01@gmail.com](http://fakezapposnotificationservice.npatta01.cloudbees.net/api/User/unregister?email=npatta01@gmail.com)

- Register user
[/api/User/register?email=npatta01@gmail.com](http://fakezapposnotificationservice.npatta01.cloudbees.net/api/User/register?email=npatta01@gmail.com)

- Verify User with email npatta01@gmail.com and verification code 9708a56b-9566-40c9-8e6a-604a3b24274f
[/api/User/verify?email=npatta01@gmail.com&verificationCode=9708a56b-9566-40c9-8e6a-604a3b24274f](http://fakezapposnotificationservice.npatta01.cloudbees.net/api/User/verify?email=npatta01@gmail.com&verificationCode=9708a56b-9566-40c9-8e6a-604a3b24274f)

- Notify to be subscribed for email npatta01@gmail.com and items [2123605,1788226\
Post a json request to /api/User/notify
With a body such as 
{"emailAddress":"npatta01@gmail.com", "products":[2123605, 1788226]} 

- Force Notification Job
To force a price check, submit a post request to

[http://fakezapposnotificationservice.npatta01.cloudbees.net/startJob](http://fakezapposnotificationservice.npatta01.cloudbees.net/startJob)

### Building###
- Requires JavaFx/JDK7 to be installed and in the path
- Requires maven to be installed and in path
- Optional: running NotificationService requires play 2.1+ to be installed
- cd into the 'ZappposNotification' directory (main maven module)
- Run ‘mvn install’
- the program is composed of 4 moduels, if the 'Services' module that is obkay

**To create an executable jar**

- navigate to the UI module
- run mvn clean jfx:jar
- result will be in target/jfx/app
- click " UI-1.0-SNAPSHOT-jfx.jar"





